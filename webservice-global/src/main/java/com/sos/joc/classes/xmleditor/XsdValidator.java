package com.sos.joc.classes.xmleditor;

import java.io.StringReader;
import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XsdValidator {

    private URI schema = null;

    public XsdValidator(URI sourceSchema) {
        schema = sourceSchema;
    }

    public void validate(String content) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema validationSchema = factory.newSchema(schema.toURL());
        Validator validator = validationSchema.newValidator();

        // validator.setErrorHandler(new XsdErrorHandler());
        validator.validate(new StreamSource(new StringReader(content)));
    }

    public URI getSchema() {
        return schema;
    }
}
