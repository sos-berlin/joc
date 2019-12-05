package com.sos.joc.xmleditor.common;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.model.xmleditor.common.ObjectType;

public class Xml2JsonConverterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Xml2JsonConverterTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @Ignore
    @Test
    public void xml2json() throws Exception {
        ObjectType type = ObjectType.YADE;
        String xmlFile = "src/test/resources/xmleditor/yade.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));
        URI schema = new URI("http://localhost:4446/joc/xsd/yade/YADE_configuration_v1.12.xsd");
        Xml2JsonConverter c = new Xml2JsonConverter();
        try {
            System.out.println(c.convert(type, schema, xml));
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }

    }
}
