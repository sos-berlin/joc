package com.sos.joc.xmleditor.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.classes.xmleditor.exceptions.XmlNotMatchSchemaException;
import com.sos.joc.model.xmleditor.common.ObjectType;

import sos.util.SOSString;

public class Xml2JsonConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Xml2JsonConverter.class);

    private XPath xpathSchema;
    private XPath xpathXml;
    private Node rootSchema;
    private Node rootXml;
    private String rootElementNameXml;
    private Map<String, String> xsdDocs;
    private List<String> elements;
    private long uuid;

    public String convert(ObjectType type, Path schema, String xml) throws Exception {
        if (Files.exists(schema) && Files.isReadable(schema)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("[schema][use local file]%s", schema));
            }
        } else {
            throw new Exception(String.format("[%s]schema not found", schema.toString()));
        }

        if (!type.equals(ObjectType.OTHER)) {
            rootElementNameXml = JobSchedulerXmlEditor.getRootElementName(type);
        }

        try {
            // init(new InputSource(Files.newInputStream(schema)), new InputSource(new StringReader(xml)));
            // read UTF-8 BOM too
            init(new InputSource(Files.newInputStream(schema)), new InputSource(new ByteArrayInputStream(xml.getBytes(JocXmlEditor.CHARSET))));
        } catch (ConnectException e) {
            throw new Exception(String.format("[%s][cant't get schema]%s", schema.toString(), e.toString()), e);
        } catch (XmlNotMatchSchemaException e) {
            LOGGER.error(String.format("[%s][%s]%s", schema.toString(), xml, e.toString()), e);
            throw new Exception(String.format("XML does not match xsd schema: %s", e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(String.format("[%s][%s]%s", schema.toString(), xml, e.toString()), e);
            throw new Exception(String.format("XML can't be loaded: %s", e.getMessage()));
        }

        ByteArrayOutputStream baos = null;
        JsonGenerator gen = null;
        try {
            uuid = -1;
            xsdDocs = new HashMap<String, String>();
            elements = new ArrayList<String>();
            elements.add(rootElementNameXml);

            baos = new ByteArrayOutputStream();
            gen = new JsonFactory().createGenerator(baos, JsonEncoding.UTF8);
            writeElements(gen, null, rootXml, 0, 0);
            gen.writeNumberField("lastUuid", uuid);
            gen.writeEndObject();
            gen.close();
            gen = null;

            return new String(baos.toByteArray(), "UTF-8");
        } catch (Exception ex) {
            throw new Exception(ex.toString(), ex);
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (Throwable e) {
                    LOGGER.warn(e.toString(), e);
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Throwable e) {
                    LOGGER.warn(e.toString(), e);
                }
            }
        }
    }

    private void init(InputSource schemaSource, InputSource xmlSource) throws Exception {
        schemaSource.setEncoding(JocXmlEditor.CHARSET);
        Document schemaDoc = getXmlFileDocument(schemaSource);

        xpathSchema = XPathFactory.newInstance().newXPath();
        xpathSchema.setNamespaceContext(getSchemaNamespaceContext());
        XPathExpression schemaExpression = xpathSchema.compile("/xs:schema");
        rootSchema = (Node) schemaExpression.evaluate(schemaDoc, XPathConstants.NODE);
        if (rootSchema == null) {
            throw new Exception("\"xs:schema\" element not found in the schema file");
        }

        if (rootElementNameXml == null) {
            XPathExpression rootExpression = xpathSchema.compile("./xs:element[1]/@name");
            Node root = (Node) rootExpression.evaluate(rootSchema, XPathConstants.NODE);
            rootElementNameXml = root.getNodeValue();
        }

        xmlSource.setEncoding(JocXmlEditor.CHARSET);
        Document xmlDoc = getXmlFileDocument(xmlSource);
        xpathXml = XPathFactory.newInstance().newXPath();
        XPathExpression xmlExpression = xpathXml.compile("/" + rootElementNameXml);
        rootXml = (Node) xmlExpression.evaluate(xmlDoc, XPathConstants.NODE);
        if (rootXml == null) {
            throw new XmlNotMatchSchemaException(String.format("Root element \"%s\" not found", rootElementNameXml));
        }
    }

    private boolean fromXsd(String elementName) {
        if (elements.contains(elementName)) {
            return true;
        }
        try {
            String xpath = String.format("//xs:element[@name='%s']", elementName);
            XPathExpression ex = xpathSchema.compile(xpath);
            Node node = (Node) ex.evaluate(rootSchema, XPathConstants.NODE);
            if (node != null) {
                elements.add(elementName);
                return true;
            }
        } catch (Throwable e) {
            LOGGER.error(String.format("[%s]%s", elementName, e.toString()), e);
        }
        return false;
    }

    private void writeElements(JsonGenerator gen, Node parent, Node current, long level, long parentId) throws Exception {
        String parentName = parent == null ? "#" : parent.getNodeName();
        boolean expanded = level < 2 ? true : false;
        boolean writeEndObject = level == 0 ? false : true;

        uuid++;
        long currentUuid = uuid;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("[%s][level=%s][parentId=%s][uuid=%s]expanded=%s", current.getNodeName(), level, parentId, uuid, expanded));
        }
        gen.writeStartObject();
        gen.writeStringField("ref", current.getNodeName());
        gen.writeBooleanField("xsd", fromXsd(current.getNodeName()));
        gen.writeStringField("parent", parentName);
        gen.writeNumberField("uuid", currentUuid);
        gen.writeBooleanField("expanded", expanded);
        if (parent != null) {
            gen.writeNumberField("parentId", parentId);
        }
        writeDoc(gen, current, null);

        NodeList childs = current.getChildNodes();
        level++;
        String cdata = null;
        String textValue = null;

        gen.writeFieldName("nodes");
        gen.writeStartArray();
        for (int i = 0; i < childs.getLength(); i++) {
            Node child = childs.item(i);
            switch (child.getNodeType()) {
            case Node.ELEMENT_NODE:
                writeElements(gen, current, child, level, currentUuid);
                break;
            case Node.CDATA_SECTION_NODE:
                cdata = child.getTextContent();
                break;
            case Node.TEXT_NODE:
                textValue = child.getNodeValue();
                break;
            }
        }
        gen.writeEndArray();

        boolean show = true;
        NamedNodeMap attributes = current.getAttributes();
        if (attributes != null && attributes.getLength() > 0) {
            gen.writeFieldName("attributes");
            gen.writeStartArray();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);

                gen.writeStartObject();
                gen.writeStringField("name", attribute.getNodeName());
                gen.writeStringField("data", attribute.getNodeValue());
                gen.writeStringField("parent", current.getNodeName());
                uuid++;
                gen.writeNumberField("id", uuid);
                writeDoc(gen, current, attribute);
                gen.writeEndObject();
            }
            gen.writeEndArray();
            show = false;
        }

        if (cdata == null) {
            if (textValue != null && textValue.trim().length() > 0) {
                cdata = textValue;
            }
        }
        if (cdata != null) {
            gen.writeFieldName("values");
            gen.writeStartArray();
            gen.writeStartObject();
            gen.writeStringField("parent", current.getNodeName());
            gen.writeStringField("data", cdata);
            uuid++;
            gen.writeNumberField("uuid", uuid);
            gen.writeEndObject();
            gen.writeEndArray();

            show = false;
        }
        gen.writeBooleanField("show", show);
        if (writeEndObject) {
            gen.writeEndObject();
        }
    }

    private void writeDoc(JsonGenerator gen, Node parent, Node attribute) throws Exception {
        Node node = null;
        String doc = null;
        String attrDefaultValue = null;
        String mapKey = null;
        if (attribute == null) {
            mapKey = new StringBuilder("sosel_").append(parent.getNodeName()).toString();
            doc = xsdDocs.get(mapKey);
            if (doc == null) {
                String xpath = String.format("./xs:element[@name='%s']/xs:annotation/xs:documentation", parent.getNodeName());
                XPathExpression ex = xpathSchema.compile(xpath);
                node = (Node) ex.evaluate(rootSchema, XPathConstants.NODE);
            }
        } else {
            mapKey = new StringBuilder("sosattr_").append(parent.getNodeName()).append("_").append(attribute.getNodeName()).toString();
            doc = xsdDocs.get(mapKey);
            if (doc == null) {
                String xpath = String.format("./xs:element[@name='%s']//xs:attribute[@name='%s']/xs:annotation/xs:documentation", parent
                        .getNodeName(), attribute.getNodeName());
                XPathExpression ex = xpathSchema.compile(xpath);
                node = (Node) ex.evaluate(rootSchema, XPathConstants.NODE);
                try {
                    Node xsdAttr = node.getParentNode().getParentNode();
                    NamedNodeMap attrs = xsdAttr.getAttributes();
                    if (attrs != null && attrs.getNamedItem("default") != null) {
                        attrDefaultValue = attrs.getNamedItem("default").getNodeValue();
                    }
                } catch (Throwable e) {

                }
            }
        }
        if (doc == null) {
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
                if (attrDefaultValue != null) {
                    doc += "<br />Default: " + attrDefaultValue;
                }
            }
            xsdDocs.put(mapKey, SOSString.isEmpty(doc) ? "" : doc);
        }
        gen.writeFieldName("text");

        gen.writeStartObject();
        gen.writeStringField("parent", parent.getNodeName());
        if (SOSString.isEmpty(doc)) {
            gen.writeFieldName("doc");
            gen.writeStartArray();
            gen.writeEndArray();
        } else {
            gen.writeStringField("doc", doc);
        }
        gen.writeEndObject();
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
