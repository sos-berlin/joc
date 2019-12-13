package com.sos.joc.classes.xmleditor;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XsdValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidator.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private Path schema = null;

    public XsdValidator(Path sourceSchema) {
        schema = sourceSchema;
    }

    public void validate(String content) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema validationSchema = null;
        if (schema != null) {
            if (Files.exists(schema) && Files.isReadable(schema)) {
                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[schema][use local file]%s", schema));
                }
                validationSchema = factory.newSchema(new StreamSource(Files.newInputStream(schema)));
            } else {
                throw new Exception(String.format("[%s]schema not found", schema.toString()));
            }
        }
        Validator validator = validationSchema.newValidator();
        // validator.setErrorHandler(new XsdErrorHandler());
        validator.validate(new StreamSource(new StringReader(content.replaceAll(">\\s+<", "><").trim())));
    }

    public Path getSchema() {
        return schema;
    }

}
