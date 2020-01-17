package com.sos.joc.classes.xmleditor.validator;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;

import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.sos.exception.SOSDoctypeException;
import com.sos.joc.classes.xmleditor.JocXmlEditor;

public class XsdValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdValidator.class);
    private static boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private Path schema = null;

    public XsdValidator(Path sourceSchema) {
        schema = sourceSchema;
    }

    public void validate(String content) throws Exception {
        if (schema == null) {
            throw new Exception("missing schema");
        }
        if (!Files.exists(schema) || !Files.isReadable(schema)) {
            throw new Exception(String.format("[%s]schema not found or not readable", schema.toString()));
        }
        if (isDebugEnabled) {
            LOGGER.debug(String.format("[schema][use local file]%s", schema));
        }

        try {
            // check for vulnerabilities
            JocXmlEditor.parseXml(content);
        } catch (SOSDoctypeException e) {
            throw e;
        } catch (Throwable e) {
        }

        InputStream is = null;
        try {
            is = Files.newInputStream(schema);
            Document xsdDoc = JocXmlEditor.parseXml(is);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setSchema(schemaFactory.newSchema(new DocumentSource(xsdDoc)));

            SAXParser parser = factory.newSAXParser();
            // parser.parse(new InputSource(new StringReader(content.replaceAll(">\\s+<", "><").trim())), new XsdValidatorHandler());
            parser.parse(new InputSource(new StringReader(content)), new XsdValidatorHandler());
        } catch (Throwable e) {
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable ex) {
                }
            }
        }
    }

    public Path getSchema() {
        return schema;
    }

}
