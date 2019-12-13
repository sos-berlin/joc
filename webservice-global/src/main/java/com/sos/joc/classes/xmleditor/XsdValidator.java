package com.sos.joc.classes.xmleditor;

import java.io.StringReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.sos.joc.model.xmleditor.common.ObjectType;

public class XsdValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidator.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private URI schema = null;
    private Path schemaLocalFile = null;
    private ObjectType type = null;
    private XsdValidatorHandler handler = null;

    public XsdValidator(ObjectType objectType, URI sourceSchema) {
        type = objectType;
        schema = sourceSchema;
    }

    public void validate(String content) throws Exception {
        handler = new XsdValidatorHandler();
        Schema validationSchema = null;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        if (!type.equals(ObjectType.OTHER)) {
            Path path = JocXmlEditor.getAbsoluteSchemaLocation(type);
            if (path != null) {
                if (Files.exists(path) && Files.isReadable(path)) {
                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("[schema][use local file]%s", path));
                    }
                    schemaLocalFile = path;
                    validationSchema = schemaFactory.newSchema(new StreamSource(Files.newInputStream(path)));
                } else {
                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("[schema][local file not found]%s", path));
                    }
                }
            }
        }

        if (validationSchema == null) {
            validationSchema = schemaFactory.newSchema(schema.toURL());
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setSchema(validationSchema);
        SAXParser parser = factory.newSAXParser();
        parser.parse(new InputSource(new StringReader(content.replaceAll(">\\s+<", "><").trim())), handler);
    }

    public URI getSchema() {
        return schema;
    }

    public Path getSchemaLocalFile() {
        return schemaLocalFile;
    }

    public String getUsedSchema() {
        return schemaLocalFile == null ? schema.toString() : schemaLocalFile.toString();
    }

    public XsdValidatorHandler getHandler() {
        return handler;
    }
}
