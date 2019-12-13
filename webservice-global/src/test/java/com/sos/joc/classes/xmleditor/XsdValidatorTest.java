package com.sos.joc.classes.xmleditor;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import com.sos.joc.model.xmleditor.common.ObjectType;

public class XsdValidatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidatorTest.class);

    @Ignore
    @Test
    public void validate() throws Exception {
        String uri = "https://www.sos-berlin.com/schema/yade/YADE_configuration_v1.12.xsd";
        String xmlFile = "src/test/resources/xmleditor/yade.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));

        XsdValidator validator = new XsdValidator(ObjectType.YADE, new URI(uri));

        try {
            validator.validate(xml);
        } catch (SAXParseException e) {
            LOGGER.error(String.format("[errorElement=%s][errorDepth=%s]errorElementPosition=%s", validator.getHandler().getErrorElement(), validator
                    .getHandler().getErrorDepth(), validator.getHandler().getErrorElementPostion()));
            LOGGER.error(String.format("[line=%s][column=%s]%s", e.getLineNumber(), e.getColumnNumber(), e), e);
        }
    }
}
