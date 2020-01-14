package com.sos.joc.xmleditor.common.schema;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.xmleditor.JocXmlEditor;

public class SchemaHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaHandler.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private String source;
    private Path target;

    public void process(String fileUri, String fileName, String fileContent) throws Exception {
        source = null;
        target = null;
        if (fileUri == null) {
            source = fileName;
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s]create from local file", source));
            }
            target = JocXmlEditor.createOthersSchema(source, fileContent);
        } else {
            source = fileUri;
            URI uri = new URI(source);
            if (JocXmlEditor.isHttp(source)) {// http(s)://
                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[%s]copy from http(s)", source));
                }
                target = JocXmlEditor.downloadOthersSchema(uri);
            } else {
                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[%s]copy from local file", source));
                }
                if (uri.isAbsolute()) {// C://Temp/xyz.xsd
                    target = JocXmlEditor.copyOthersSchema(Paths.get(source));
                } else {// xyz.xsd
                    target = JocXmlEditor.getOthersAbsoluteSchemaLocation(source);
                }
            }
        }
    }

    public String getSource() {
        return source;
    }

    public Path getTarget() {
        return target;
    }
}
