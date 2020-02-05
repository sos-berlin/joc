package com.sos.joc.xmleditor.common.schema;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.xmleditor.JocXmlEditor;

import sos.util.SOSString;

public class SchemaHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaHandler.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private static final String TEMP_EXTENSION = ".sostmp";
    private String source;
    private Path target;
    private Path targetTemp;
    private URI httpDownloadUri;

    public void process(String fileUri, String fileName, String fileContent) throws Exception {
        source = null;
        targetTemp = null;
        target = null;
        if (fileUri == null) {
            if (SOSString.isEmpty(fileName)) {
                throw new Exception("missing schema file name");
            }
            source = fileName.trim();
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s]create from local file", source));
            }
            target = JocXmlEditor.getOthersSchema(source);
            targetTemp = JocXmlEditor.createOthersSchema(source.concat(TEMP_EXTENSION), fileContent);
        } else {
            source = fileUri.trim();
            if (JocXmlEditor.isHttp(source)) {// http(s)://
                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[%s]copy from http(s)", source));
                }

                httpDownloadUri = JocXmlEditor.toURI(source);
                String name = JocXmlEditor.getFileName(httpDownloadUri);
                target = JocXmlEditor.getOthersHttpSchema(name);
                String tempName = JocXmlEditor.getFileName(JocXmlEditor.toURI(source.concat(TEMP_EXTENSION)));
                targetTemp = JocXmlEditor.downloadOthersSchema(JocXmlEditor.toURI(source), tempName);
            } else {
                Path sourcePath = Paths.get(source);
                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[%s]copy from local file", sourcePath));
                }
                if (sourcePath.isAbsolute()) {// C://Temp/xyz.xsd
                    target = JocXmlEditor.getOthersSchema(sourcePath.getFileName().toString());
                    targetTemp = JocXmlEditor.copyOthersSchema(sourcePath);
                } else {// xyz.xsd
                    target = JocXmlEditor.getOthersSchema(source);
                    targetTemp = target;
                }
            }
        }
    }

    public void onError(boolean deleteTempFile) {
        if (deleteTempFile) {
            try {
                Files.deleteIfExists(targetTemp);
            } catch (Throwable e) {
                LOGGER.warn(String.format("[%s]error on delete file: %s", targetTemp, e.toString()), e);
            }
        }
    }

    public String getSource() {
        return source;
    }

    public Path getTarget() {
        return target;
    }

    public Path getTargetTemp() {
        return targetTemp;
    }

    public URI getHttpDownloadUri() {
        return httpDownloadUri;
    }
}
