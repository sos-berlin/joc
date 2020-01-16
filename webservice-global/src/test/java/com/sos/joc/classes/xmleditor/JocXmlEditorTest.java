package com.sos.joc.classes.xmleditor;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.dom4j.Document;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JocXmlEditorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JocXmlEditorTest.class);

    @Ignore
    @Test
    public void formatXml() throws Exception {
        String xmlFile = "src/test/resources/xmleditor/notification.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));
        LOGGER.info(JocXmlEditor.formatXmlX(xml, true));

    }

    @Ignore
    @Test
    public void formatDocumentXml() throws Exception {
        String xmlFile = "src/test/resources/xmleditor/notification.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));

        Document doc = JocXmlEditor.parseXml(xml);
        LOGGER.info(JocXmlEditor.formatXml(doc, false));

    }
}
