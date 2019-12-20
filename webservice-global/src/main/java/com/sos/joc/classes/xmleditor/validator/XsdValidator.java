package com.sos.joc.classes.xmleditor.validator;

import java.io.StringReader;
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

public class XsdValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidator.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private Path schema = null;
    private XsdValidatorHandler handler = null;

    public XsdValidator(Path sourceSchema) {
        schema = sourceSchema;
    }

    public void validate(String content) throws Exception {
        handler = new XsdValidatorHandler();
        Schema validationSchema = null;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        if (schema != null) {
            if (Files.exists(schema) && Files.isReadable(schema)) {
                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[schema][use local file]%s", schema));
                }
                validationSchema = schemaFactory.newSchema(new StreamSource(Files.newInputStream(schema)));
            } else {
                throw new Exception(String.format("[%s]schema not found", schema.toString()));
            }
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setSchema(validationSchema);
        SAXParser parser = factory.newSAXParser();
        // parser.parse(new InputSource(new StringReader(content.replaceAll(">\\s+<", "><").trim())), handler);
        parser.parse(new InputSource(new StringReader(content)), handler);
    }

    public Path getSchema() {
        return schema;
    }

}
