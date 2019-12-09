package com.sos.joc.xmleditor.common;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.URI;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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

import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.model.xmleditor.common.ObjectType;

public class Xml2JsonConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Xml2JsonConverter.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private static boolean isTraceEnabled = LOGGER.isTraceEnabled();

    private XPath xpathSchema;
    private XPath xpathXml;
    private Node rootSchema;
    private Node rootXml;
    private String rootElementNameXml;
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

        if (!type.equals(ObjectType.OTHER)) {
            rootElementNameXml = JobSchedulerXmlEditor.getRootElementName(type);
        }

        try {
            init(new InputSource(schema.toString()), new InputSource(new StringReader(xml)));
        } catch (ConnectException e) {
            throw new Exception(String.format("[%s][cant't get schema]%s", schema.toString(), e.toString()), e);
        }

        try {
            uuid = -1;

            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder = buildElements(builder, null, rootXml, 0, 0);
            builder.add("lastUuid", uuid);

            return builder.build().toString();
        } catch (Exception ex) {
            throw new Exception(ex.toString(), ex);
        }
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

        if (rootElementNameXml == null) {
            // TODO get first element name for OTHERS
        }

        Document xmlDoc = getXmlFileDocument(xmlSource);
        xpathXml = XPathFactory.newInstance().newXPath();
        XPathExpression xmlExpression = xpathXml.compile("/" + rootElementNameXml);
        rootXml = (Node) xmlExpression.evaluate(xmlDoc, XPathConstants.NODE);
        if (rootXml == null) {
            throw new Exception(String.format("[%s]root element \"%s\" not found", method, rootElementNameXml));
        }
    }

    private JsonObjectBuilder buildElements(JsonObjectBuilder parentBuilder, Node parent, Node current, long level, long parentId) throws Exception {
        String parentName = parent == null ? "#" : parent.getNodeName();
        boolean expanded = level < 3 ? true : false;

        uuid++;
        long currentUuid = uuid;

        if (isDebugEnabled) {
            LOGGER.debug(String.format("[%s][level=%s][parentId=%s][uuid=%s]expanded=%s", current.getNodeName(), level, parentId, uuid, expanded));
        }
        parentBuilder.add("ref", current.getNodeName());
        parentBuilder.add("parent", parentName);
        parentBuilder.add("uuid", currentUuid);

        if (parent != null) {
            parentBuilder.add("parentId", parentId);
        }
        parentBuilder = writeDoc(parentBuilder, current, null);
        parentBuilder.add("expanded", expanded);

        JsonArrayBuilder nodesBuilder = Json.createArrayBuilder();
        NodeList childs = current.getChildNodes();
        level++;
        String cdata = null;
        for (int i = 0; i < childs.getLength(); i++) {
            Node child = childs.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                JsonObjectBuilder b = buildElements(Json.createObjectBuilder(), current, child, level, currentUuid);
                nodesBuilder.add(b);
            } else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                cdata = child.getTextContent();
            }

        }
        parentBuilder.add("nodes", nodesBuilder);

        boolean show = true;
        NamedNodeMap attributes = current.getAttributes();
        if (attributes != null && attributes.getLength() > 0) {
            JsonArrayBuilder attributesBuilder = Json.createArrayBuilder();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);

                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("name", attribute.getNodeName());
                builder.add("data", attribute.getNodeValue());
                builder.add("parent", current.getNodeName());
                uuid++;
                builder.add("id", uuid);
                builder = writeDoc(builder, current, attribute);
                attributesBuilder.add(builder);
            }
            parentBuilder.add("attributes", attributesBuilder);
            show = false;
        }

        if (cdata != null) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("parent", current.getNodeName());
            builder.add("data", cdata);
            uuid++;
            builder.add("uuid", uuid);

            JsonArrayBuilder cdataBuilder = Json.createArrayBuilder();
            cdataBuilder.add(builder);
            parentBuilder.add("values", cdataBuilder);
            show = false;
        }
        parentBuilder.add("show", show);
        return parentBuilder;
    }

    private JsonObjectBuilder writeDoc(JsonObjectBuilder parentBuilder, Node parent, Node attribute) throws Exception {
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

        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("parent", parent.getNodeName());

        if (doc == null) {
            builder.add("doc", Json.createArrayBuilder());
        } else {
            builder.add("doc", doc);
        }
        parentBuilder.add("text", builder);
        return parentBuilder;
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
