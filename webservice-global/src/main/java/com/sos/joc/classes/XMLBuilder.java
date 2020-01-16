package com.sos.joc.classes;

import java.io.IOException;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sos.exception.SOSDoctypeException;

public class XMLBuilder {

    private Element root;

    public XMLBuilder(String name) {
        this.root = DocumentHelper.createElement(name);
    }

    public Element getRoot() {
        return this.root;
    }

    public static Element create(String name) {
        return new XMLBuilder(name).getRoot();
    }

    public static Document parse(String xmlString) throws DocumentException, SOSDoctypeException, IOException, SAXException {
        SAXReader reader = new SAXReader();
        reader.setIncludeExternalDTDDeclarations(false);
        reader.setIncludeInternalDTDDeclarations(false);
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        reader.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        reader.setValidation(false);
        reader.setStripWhitespaceText(true);
        reader.setIgnoreComments(true);

        Document doc;
        try {
            doc = reader.read(new StringReader(xmlString));
        } catch (DocumentException e) {
            Throwable nested = e.getNestedException();
            if (nested != null && SAXParseException.class.isInstance(nested) && nested.getMessage().toUpperCase().contains("DOCTYPE")) {
                // On Apache, this should be thrown when disallowing DOCTYPE
                throw new SOSDoctypeException("A DOCTYPE was passed into the XML document", e);
            } else if (nested != null && IOException.class.isInstance(nested)) {
                // XXE that points to a file that doesn't exist
                throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
            } else {
                throw e;
            }
        }
        return doc;
    }

    public Element addElement(String name) {
        return this.root.addElement(name);
    }

    public Element addAttribute(String key, String value) {
        return this.root.addAttribute(key, value);
    }

    public void add(Element elem) {
        this.root.add(elem);
    }

    public String asXML() {
        return this.root.asXML();
    }

}
