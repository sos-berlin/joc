package com.sos.joc.xmleditor.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.joc.model.xmleditor.common.ObjectType;

public class Xml2JsonConverterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Xml2JsonConverterTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @Ignore
    @Test
    public void xml2json() throws Exception {
        JsonObjectBuilder ob = Json.createObjectBuilder();
        ob.add("TYPE", "Login");
        JsonObjectBuilder obc = Json.createObjectBuilder();
        obc.add("userId", "re");
        obc.add("password", "pass");
        ob.add("userAndPassword", obc);

        LOGGER.info(new ObjectMapper().writeValueAsString(ob.build()));

        // String s = ob.build().toString();
        // JsonReader r = Json.createReader(new StringReader(s));
        // JsonObject j = rdr.readObject();
        // r.close();

    }

    @Ignore
    @Test
    public void convertStandardXml2json() throws Exception {
        ObjectType type = ObjectType.YADE;
        String xmlFile = "src/test/resources/xmleditor/yade.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));
        Path schema = Paths.get("../../jade/jade-engine/src/main/resources/YADE_configuration_v1.12.xsd");
        LOGGER.info(schema.toFile().getCanonicalPath());

        Xml2JsonConverter c = new Xml2JsonConverter();
        try {
            String s = c.convert(type, schema, xml);
            LOGGER.info(s);
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }

    }

    @Ignore
    @Test
    public void convertOtherXml2json() throws Exception {
        ObjectType type = ObjectType.OTHER;
        String xmlFile = "src/test/resources/xmleditor/other.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));
        Path schema = Paths.get("../../jade/jade-engine/src/main/resources/YADE_configuration_v1.12.xsd");
        LOGGER.info(schema.toFile().getCanonicalPath());

        Xml2JsonConverter c = new Xml2JsonConverter();
        try {
            String s = c.convert(type, schema, xml);
            LOGGER.info(s);
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }

    }
}