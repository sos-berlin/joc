package com.sos.joc.classes.xmleditor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.xml.XMLBuilder;

import sos.util.SOSString;

public class JocXmlEditor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JocXmlEditor.class);

    public static final String APPLICATION_PATH = "xmleditor";
    public static final String AVAILABILITY_STARTING_WITH = "1.13.1";
    public static final String MESSAGE_UNSUPPORTED_WEB_SERVICE = String.format("Unsupported web service: JobScheduler needs at least version %s",
            AVAILABILITY_STARTING_WITH);

    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String CHARSET = "UTF-8";

    public static final String JOC_SCHEMA_LOCATION = "xsd";

    public static final String MESSAGE_DRAFT_NOT_EXIST = "Using version in live folder. No draft version found in database";
    public static final String MESSAGE_LIVE_NOT_EXIST = "Using draft version, no live version found";
    public static final String MESSAGE_LIVE_IS_NEWER = "Using draft version but, version in live folder is newer then draft version";
    public static final String MESSAGE_DRAFT_IS_NEWER = "Using draft version as it is newer then the version in the live folder";
    public static final String MESSAGE_NO_CONFIGURATION_EXIST = "No configuration found";

    public static final String MESSAGE_CODE_DRAFT_NOT_EXIST = "XMLEDITOR-101";
    public static final String MESSAGE_CODE_LIVE_NOT_EXIST = "XMLEDITOR-102";
    public static final String MESSAGE_CODE_LIVE_IS_NEWER = "XMLEDITOR-103";
    public static final String MESSAGE_CODE_DRAFT_IS_NEWER = "XMLEDITOR-104";
    public static final String CODE_NO_CONFIGURATION_EXIST = "XMLEDITOR-105";

    public static final String ERROR_CODE_JOBSCHEDULER_NOT_CONNECTED = "XMLEDITOR-401";
    public static final String ERROR_CODE_WRONG_OBJECT_TYPE = "XMLEDITOR-402";
    public static final String ERROR_CODE_PERMISSION_DENIED = "XMLEDITOR-403";
    public static final String ERROR_CODE_CONFUGURATION_NOT_FOUND = "XMLEDITOR-404";
    public static final String ERROR_CODE_VALIDATION_ERROR = "XMLEDITOR-405";
    public static final String ERROR_CODE_DEPLOY_ERROR = "XMLEDITOR-406";
    public static final String ERROR_CODE_DEPLOY_ERROR_UNSUPPORTED_OBJECT_TYPE = "XMLEDITOR-407";

    public static final String NEW_LINE = "\r\n";

    private static Path realPath = null;

    public static Document parseXml(String xml) throws Exception {
        if (SOSString.isEmpty(xml)) {
            return null;
        }
        return XMLBuilder.parse(xml);
    }

    public static Document parseXml(InputStream is) throws Exception {
        if (is == null) {
            return null;
        }
        return XMLBuilder.parse(is);
    }

    public static String getResourceImplPath(final String path) {
        return String.format("./%s/%s", APPLICATION_PATH, path);
    }

    public static Path getStandardAbsoluteSchemaLocation(ObjectType type) throws Exception {
        setRealPath();
        Path path = realPath;
        if (path != null) {
            path = path.resolve(getStandardRelativeSchemaLocation(type));
        }
        return path;
    }

    public static String getStandardSchemaIdentifier(ObjectType type) throws Exception {
        if (type.equals(ObjectType.YADE)) {
            return JobSchedulerXmlEditor.SCHEMA_FILENAME_YADE;
        } else if (type.equals(ObjectType.NOTIFICATION)) {
            return JobSchedulerXmlEditor.SCHEMA_FILENAME_NOTIFICATION;
        }
        return null;
    }

    public static List<Path> getOthersAbsoluteSchemaLocations() throws Exception {
        setRealPath();
        Path path = realPath == null ? Paths.get(System.getProperty("user.dir")) : realPath;
        return getFiles(path.resolve(getOthersRelativeSchemaLocation().toString()), false, "xsd");
    }

    public static Path getOthersAbsoluteSchemaLocation(String name) throws Exception {
        setRealPath();
        Path path = realPath;
        if (path != null) {
            path = path.resolve(getOthersRelativeSchemaLocation(name));
        }
        return path;
    }

    public static Path getOthersAbsoluteHttpSchemaLocation(String name) throws Exception {
        setRealPath();
        Path path = realPath;
        if (path != null) {
            path = path.resolve(getOthersRelativeHttpSchemaLocation(name));
        }
        return path;
    }

    public static String getOthersSchemaIdentifier(String path) {
        if (isHttp(path)) {
            return path;
        }
        return getFileName(Paths.get(path));
    }

    public static String getOthersSchemaIdentifier(Path path) {
        return getFileName(path);
    }

    private static String getFileName(Path path) {
        return path.getFileName().toString();
    }

    public static String getFileName(URI uri) {
        String path = null;
        try {
            path = URLDecoder.decode(uri.toString(), CHARSET);
        } catch (Throwable e) {
            path = uri.toString().replaceAll("%20", " ");
        }
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static URI toURI(String uri) throws Exception {
        URL url;
        try {
            url = new URL(URLDecoder.decode(uri, CHARSET));
        } catch (Throwable e) {
            return new URI(uri.replaceAll(" ", "%20"));
        }
        return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
    }

    public static String getStandardRelativeSchemaLocation(final ObjectType type) {
        if (type == null || SOSString.isEmpty(type.name())) {
            return null;
        }
        if (type.equals(ObjectType.YADE)) {
            return getYadeRelativeSchemaLocation().append("/").append(JobSchedulerXmlEditor.SCHEMA_FILENAME_YADE).toString();
        } else if (type.equals(ObjectType.NOTIFICATION)) {
            return getNotificationRelativeSchemaLocation().append("/").append(JobSchedulerXmlEditor.SCHEMA_FILENAME_NOTIFICATION).toString();
        }
        return null;
    }

    public static StringBuilder getYadeRelativeSchemaLocation() {
        return new StringBuilder(JOC_SCHEMA_LOCATION).append("/yade");
    }

    public static StringBuilder getNotificationRelativeSchemaLocation() {
        return new StringBuilder(JOC_SCHEMA_LOCATION).append("/notification");
    }

    public static StringBuilder getOthersRelativeSchemaLocation() {
        return new StringBuilder(JOC_SCHEMA_LOCATION).append("/others");
    }

    public static StringBuilder getOthersRelativeHttpSchemaLocation() {
        return getOthersRelativeSchemaLocation().append("/http");
    }

    public static String getOthersRelativeSchemaLocation(final String name) {
        return getOthersRelativeSchemaLocation().append("/").append(name).toString();
    }

    public static String getOthersRelativeHttpSchemaLocation(final String name) {
        return getOthersRelativeHttpSchemaLocation().append("/").append(name).toString();
    }

    public static String getConfigurationName(final ObjectType type) {
        return getConfigurationName(type, null);
    }

    public static String getConfigurationName(final ObjectType type, final String name) {
        if (type.equals(ObjectType.OTHER)) {
            return name;
        }
        return getStandardBaseName(type) + ".xml";
    }

    public static boolean checkRequiredParameter(final String paramKey, final ObjectType paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.toString().isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    public static String getStandardBaseName(ObjectType type) {
        if (type == null) {
            return null;
        }
        if (type.equals(ObjectType.YADE)) {
            return JobSchedulerXmlEditor.CONFIGURATION_BASENAME_YADE;
        } else if (type.equals(ObjectType.NOTIFICATION)) {
            return JobSchedulerXmlEditor.CONFIGURATION_BASENAME_NOTIFICATION;
        }
        return null;
    }

    public static String getJobSchedulerLivePathXml(ObjectType type) {
        if (type == null) {
            return null;
        }
        if (type.equals(ObjectType.YADE)) {
            return "/" + JobSchedulerXmlEditor.getLivePathYadeXml();
        } else if (type.equals(ObjectType.NOTIFICATION)) {
            return "/" + JobSchedulerXmlEditor.getLivePathNotificationXml();
        }
        return null;
    }

    public static String getJobSchedulerLivePathYadeIni() {
        return "/" + JobSchedulerXmlEditor.getLivePathYadeIni();
    }

    public static List<Path> getFiles(Path dir, boolean recursiv, String extension) throws Exception {
        if (recursiv) {
            return Files.walk(dir).filter(s -> s.toString().toLowerCase().endsWith("." + extension.toLowerCase())).map(Path::getFileName).sorted()
                    .collect(Collectors.toList());
        } else {
            return Files.walk(dir, 1).filter(s -> s.toString().toLowerCase().endsWith("." + extension.toLowerCase())).map(Path::getFileName).sorted()
                    .collect(Collectors.toList());
        }
    }

    public static String bytes2string(byte[] bytes) {
        try {
            return new String(bytes, CHARSET);
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    public static String getFileContent(Path path) throws IOException {
        return bytes2string(Files.readAllBytes(path));
    }

    public static Path downloadOthersSchema(URI uri) throws Exception {
        String name = getFileName(uri);
        Path target = getOthersAbsoluteHttpSchemaLocation(name);
        try (InputStream inputStream = uri.toURL().openStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Throwable ex) {
            LOGGER.error(ex.toString(), ex);
            throw ex;
        }
        return target;
    }

    public static Path copyOthersSchema(Path source) throws Exception {
        Path target = JocXmlEditor.getOthersAbsoluteSchemaLocation(source.getFileName().toString());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    public static Path createOthersSchema(String fileName, String fileContent) throws Exception {
        Path target = JocXmlEditor.getOthersAbsoluteSchemaLocation(fileName);
        Files.write(target, fileContent.getBytes(JocXmlEditor.CHARSET), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return target;
    }

    // TODO
    public static boolean isHttp(String path) {
        path = path.toLowerCase();
        return path.startsWith("https://") || path.startsWith("http://");
    }

    public static Path getOthersSchemaFile(String path, boolean downloadIfHttp) throws Exception {
        Path file = null;
        if (isHttp(path)) {
            if (downloadIfHttp) {
                try {
                    file = downloadOthersSchema(toURI(path));
                } catch (Throwable e) {
                    LOGGER.error(String.format("[%s]can't download file, try to find in the %s location ..", path,
                            getOthersRelativeHttpSchemaLocation()));
                }
            }
            if (file == null) {
                file = JocXmlEditor.getOthersAbsoluteHttpSchemaLocation(getFileName(toURI(path)));
            }

        } else {
            file = JocXmlEditor.getOthersAbsoluteSchemaLocation(getFileName(Paths.get(path)));
        }
        return file;
    }

    public static String readOthersSchemaFile(String path) throws Exception {
        Path file = getOthersSchemaFile(path, true);
        if (Files.exists(file)) {
            return getFileContent(file);
        } else {
            throw new Exception(String.format("[%s]file not found", path));
        }
    }

    public static void setRealPath() throws Exception {
        if (realPath != null) {
            return;
        }
        if (Globals.sosShiroProperties != null) {
            realPath = Globals.sosShiroProperties.resolvePath(".");
        } else {
            throw new Exception("Globals.sosShiroProperties is null");
        }
    }

}
