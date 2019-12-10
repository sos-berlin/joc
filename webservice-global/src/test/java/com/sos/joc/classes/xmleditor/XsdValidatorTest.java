package com.sos.joc.classes.xmleditor;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import com.sos.joc.model.xmleditor.common.ObjectType;

public class XsdValidatorTest {

    @Ignore
    @Test
    public void validate() throws Exception {
        String uri = "https://www.sos-berlin.com/schema/yade/YADE_configuration_v1.12.xsd";
        String xmlFile = "src/test/resources/xmleditor/yade.xml";
        String xml = new String(Files.readAllBytes(Paths.get(xmlFile)));

        XsdValidator validator = null;
        validator = new XsdValidator(ObjectType.YADE, new URI(uri));
        validator.validate(xml);

    }
}
