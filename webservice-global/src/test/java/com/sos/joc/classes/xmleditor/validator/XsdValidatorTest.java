package com.sos.joc.classes.xmleditor.validator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.xmleditor.exceptions.XsdValidatorException;

public class XsdValidatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidatorTest.class);

    @Ignore
    @Test
    public void validateYade() throws Exception {
        String xmlFile = "src/test/resources/xmleditor/yade.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));
        Path schema = Paths.get("../../jade/jade-engine/src/main/resources/YADE_configuration_v1.12.xsd");
        LOGGER.info(schema.toRealPath().toString());

        XsdValidator validator = new XsdValidator(schema);
        try {
            validator.validate(xml);
        } catch (XsdValidatorException e) {
            LOGGER.error(String.format("[errorElement=%s][errorDepth=%s]errorElementPosition=%s", e.getElementName(), e.getElementDepth(), e
                    .getElementPosition()));
            // LOGGER.error(String.format("[line=%s][column=%s]%s", e.getLineNumber(), e.getColumnNumber(), e.toString()), e);
            LOGGER.error(String.format("[line=%s][column=%s]%s", e.getLineNumber(), e.getColumnNumber(), e.getCause().getMessage()));
        }
    }

    @Ignore
    @Test
    public void validateNotification() throws Exception {
        String xmlFile = "src/test/resources/xmleditor/notification.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));
        Path schema = Paths.get("../SystemMonitorNotification_v1.0.xsd");
        LOGGER.info(schema.toFile().getCanonicalPath());

        XsdValidator validator = new XsdValidator(schema);
        try {
            validator.validate(xml);
        } catch (XsdValidatorException e) {
            LOGGER.error(String.format("[errorElement=%s][errorDepth=%s]errorElementPosition=%s", e.getElementName(), e.getElementDepth(), e
                    .getElementPosition()));
            LOGGER.error(String.format("[line=%s][column=%s]%s", e.getLineNumber(), e.getColumnNumber(), e), e);
        }
    }
}
