package com.sos.joc.classes.xmleditor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

public class XsdValidatorTest {

    @Ignore
    @Test
    public void validate() throws Exception {
        Path path = Paths.get("src/test/resources/xmleditor/YADE_configuration_v1.12.xsd");
        String xmlFile = "src/test/resources/xmleditor/yade.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));

        XsdValidator validator = null;
        validator = new XsdValidator(path);
        validator.validate(xml);

    }
}
