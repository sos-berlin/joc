package com.sos.joc.classes.xmleditor;

import java.io.StringReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.model.xmleditor.common.ObjectType;

public class XsdValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidator.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private URI schema = null;
    private Path schemaLocalFile = null;
    private ObjectType type = null;

    public XsdValidator(ObjectType objectType, URI sourceSchema) {
        type = objectType;
        schema = sourceSchema;
    }

    public void validate(String content) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema validationSchema = null;
        if (!type.equals(ObjectType.OTHER)) {
            Path path = JocXmlEditor.getAbsoluteSchemaLocation(type);
            if (path != null) {
                if (Files.exists(path) && Files.isReadable(path)) {
                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("[schema][use local file]%s", path));
                    }
                    schemaLocalFile = path;
                    validationSchema = factory.newSchema(new StreamSource(Files.newInputStream(path)));
                } else {
                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("[schema][local file not found]%s", path));
                    }
                }
            }
        }

        if (validationSchema == null) {
            validationSchema = factory.newSchema(schema.toURL());
        }

        Validator validator = validationSchema.newValidator();
        // validator.setErrorHandler(new XsdErrorHandler());
        validator.validate(new StreamSource(new StringReader(content)));
    }

    public URI getSchema() {
        return schema;
    }

    public Path getSchemaLocalFile() {
        return schemaLocalFile;
    }
}
