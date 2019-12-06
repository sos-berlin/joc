package com.sos.joc.xmleditor.common;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.URI;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.model.xmleditor.common.ObjectType;

public class Xml2JsonConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Xml2JsonConverter.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private static boolean isTraceEnabled = LOGGER.isTraceEnabled();

    private Writer writer = null;
    private XPath xpathSchema;
    private XPath xpathXml;
    private Node rootSchema;
    private Node rootXml;
    private String rootElementName;
    private long uuid;

    public String convert(ObjectType type, URI schema, String xml) throws Exception {
        if (isDebugEnabled) {
            if (isTraceEnabled) {
                LOGGER.debug(String.format("[schema=%s]%s", schema.toString(), xml));
            } else {
                LOGGER.debug(String.format("schema=%s", schema.toString()));
            }
        }

        if (type.equals(ObjectType.OTHER)) {
            throw new Exception("OTHER is currently not supported");
        }
        rootElementName = JobSchedulerXmlEditor.getRootElementName(type);
        try {
            init(new InputSource(schema.toString()), new InputSource(new StringReader(xml)));
        } catch (ConnectException e) {
            throw new Exception(String.format("[%s][cant't get schema]%s", schema.toString(), e.toString()), e);
        }

        try {

            uuid = -1;
            writer = new StringWriter();
            writer.write("[{");
            writeElements(null, rootXml, 0, 0);
            writer.write("}]");
            return writer.toString();
        } catch (Exception ex) {
            throw new Exception(ex.toString(), ex);
        } finally {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        }
    }

    private void writeElements(Node parent, Node current, long level, long parentId) throws Exception {
        String parentName = parent == null ? "#" : parent.getNodeName();
        String expanded = level < 3 ? "true" : "false";

        uuid++;
        long currentUuid = uuid;

        if (isDebugEnabled) {
            LOGGER.debug(String.format("[%s][level=%s][parentId=%s][uuid=%s]expanded=%s", current.getNodeName(), level, parentId, uuid, expanded));
        }
        if (parent == null) {
            writer.write("\"webservice\": true,");
        }
        writer.write(String.format("\"ref\": \"%s\",", current.getNodeName()));
        writer.write(String.format("\"parent\": \"%s\",", parentName));
        if (parent != null) {
            // writer.write(String.format("\"import\": \"%s\",", current.getNodeName()));
        }

        writer.write(String.format("\"uuid\": %s,", currentUuid));
        if (parent != null) {
            writer.write(String.format("\"parentId\": %s,", parentId));
        }
        writeDoc(current, null);
        writer.write(String.format("\"expanded\": %s,", expanded));

        writer.write("\"nodes\":[");
        NodeList childs = current.getChildNodes();
        level++;
        String show = "true";
        String cdata = null;
        for (int i = 0; i < childs.getLength(); i++) {
            Node child = childs.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                writer.write("{");
                writeElements(current, child, level, currentUuid);
                writer.write("},");
            } else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                cdata = child.getTextContent();
            }

        }
        writer.write("],");

        NamedNodeMap attributes = current.getAttributes();
        if (attributes != null && attributes.getLength() > 0) {
            writer.write("\"attributes\":[");

            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                writer.write("{");

                writer.write(String.format("\"name\": \"%s\",", attribute.getNodeName()));
                writer.write(String.format("\"data\": %s,", new ObjectMapper().writeValueAsString(attribute.getNodeValue())));
                writer.write(String.format("\"parent\": \"%s\",", current.getNodeName()));

                uuid++;
                writer.write(String.format("\"id\": %s,", uuid));

                writeDoc(current, attribute);

                writer.write("},");
            }

            writer.write("],");
            show = "false";
        }

        if (cdata != null) {
            writer.write("\"values\":[{");
            writer.write(String.format("\"parent\": \"%s\",", current.getNodeName()));

            uuid++;
            writer.write(String.format("\"uuid\": %s,", uuid));
            writer.write(String.format("\"data\": %s,", new ObjectMapper().writeValueAsString(cdata)));
            writer.write("}],");
            show = "false";
        }

        writer.write(String.format("\"show\":%s", show));
    }

    private void writeDoc(Node parent, Node attribute) throws Exception {
        Node node = null;
        String doc = null;
        if (attribute == null) {
            String xpath = String.format("./xs:element[@name='%s']/xs:annotation/xs:documentation", parent.getNodeName());
            XPathExpression ex = xpathSchema.compile(xpath);
            node = (Node) ex.evaluate(rootSchema, XPathConstants.NODE);

        } else {
            String xpath = String.format("./xs:element[@name='%s']//xs:attribute[@name='%s']/xs:annotation/xs:documentation", parent.getNodeName(),
                    attribute.getNodeName(), attribute.getNodeName());
            XPathExpression ex = xpathSchema.compile(xpath);
            node = (Node) ex.evaluate(rootSchema, XPathConstants.NODE);
        }
        if (node != null) {
            if (node.hasChildNodes()) {
                Node currentChild = node.getFirstChild();
                while (currentChild != null) {
                    if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                        break;
                    }
                    currentChild = currentChild.getNextSibling();
                }

                String content = nodeToString(currentChild).trim();
                if (content != null) {
                    doc = content;
                }
            }
        }

        writer.write("\"text\":{");
        if (doc == null) {
            writer.write("\"doc\":[{}],");
        } else {
            writer.write(String.format("\"doc\": %s,", new ObjectMapper().writeValueAsString(doc)));
        }
        writer.write(String.format("\"parent\": \"%s\"", parent.getNodeName()));
        writer.write("},");
    }

    private static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            LOGGER.error(te.toString(), te);
        }
        return sw.toString();
    }

    private void init(InputSource schemaSource, InputSource xmlSource) throws Exception {
        String method = "init";
        xpathSchema = XPathFactory.newInstance().newXPath();
        xpathSchema.setNamespaceContext(getSchemaNamespaceContext());
        Document schemaDoc = getXmlFileDocument(schemaSource);
        XPathExpression schemaExpression = xpathSchema.compile("/xs:schema");
        rootSchema = (Node) schemaExpression.evaluate(schemaDoc, XPathConstants.NODE);
        if (rootSchema == null) {
            throw new Exception(String.format("[%s]\"xs:schema\" element not found in the schema file", method));
        }

        Document xmlDoc = getXmlFileDocument(xmlSource);
        xpathXml = XPathFactory.newInstance().newXPath();
        XPathExpression xmlExpression = xpathXml.compile("/" + rootElementName);
        rootXml = (Node) xmlExpression.evaluate(xmlDoc, XPathConstants.NODE);
        if (rootXml == null) {
            throw new Exception(String.format("[%s]root element \"%s\" not found", method, rootElementName));
        }
    }

    private Document getXmlFileDocument(InputSource xmlSource) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlSource);
    }

    private NamespaceContext getSchemaNamespaceContext() {
        return new NamespaceContext() {

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return "xs";
            }

            @Override
            public String getNamespaceURI(String prefix) {
                return "http://www.w3.org/2001/XMLSchema";
            }
        };
    }
}
