package com.sos.joc.classes.xmleditor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.xmleditor.common.ObjectType;

import sos.util.SOSString;

public class JocXmlEditor {

    public static final String APPLICATION_PATH = "xmleditor";
    public static final String AVAILABILITY_STARTING_WITH = "1.13.1";
    public static final String MESSAGE_UNSUPPORTED_WEB_SERVICE = String.format("Unsupported web service: JobScheduler needs at least version %s",
            AVAILABILITY_STARTING_WITH);

    public static final String CHARSET = "UTF-8";

    public static final String JOC_SCHEMA_YADE_FILE = "xsd/yade/" + JobSchedulerXmlEditor.SCHEMA_FILENAME_YADE;
    public static final String JOC_SCHEMA_NOTIFICATION_FILE = "xsd/notification/" + JobSchedulerXmlEditor.SCHEMA_FILENAME_NOTIFICATION;
    public static final String JOC_SCHEMA_OTHER_LOCATION = "xsd/other/";

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

    public static String getResourceImplPath(final String path) {
        return String.format("./%s/%s", APPLICATION_PATH, path);
    }

    public static Path getAbsoluteSchemaLocation(ObjectType type) {
        Path path = Globals.servletContextRealPath;
        if (path != null) {
            if (type.equals(ObjectType.YADE)) {
                path = path.resolve(Paths.get(JocXmlEditor.JOC_SCHEMA_YADE_FILE));
            } else {
                path = path.resolve(Paths.get(JocXmlEditor.JOC_SCHEMA_NOTIFICATION_FILE));
            }
        }
        return path;
    }

    public static String getRelativeSchemaLocation(final ObjectType type) {
        return getRelativeSchemaLocation(type, null);
    }

    public static String getRelativeSchemaLocation(final ObjectType type, final String otherSchema) {
        if (type == null || SOSString.isEmpty(type.name())) {
            return null;
        }
        if (type.equals(ObjectType.YADE)) {
            return JOC_SCHEMA_YADE_FILE;
        } else if (type.equals(ObjectType.NOTIFICATION)) {
            return JOC_SCHEMA_NOTIFICATION_FILE;
        }
        return otherSchema;
    }

    public static String getConfigurationName(final ObjectType type) {
        return getConfigurationName(type, null);
    }

    public static String getConfigurationName(final ObjectType type, final String name) {
        if (type.equals(ObjectType.OTHER)) {
            return name;
        }
        return getBaseName(type) + ".xml";
    }

    public static boolean checkRequiredParameter(final String paramKey, final ObjectType paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.toString().isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    public static String getBaseName(ObjectType type) {
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

    public static List<Path> getAbsoluteSchemaLocationsOther() throws Exception {
        Path path = Globals.servletContextRealPath == null ? Paths.get(System.getProperty("user.dir")) : Globals.servletContextRealPath;
        return getFiles(path.resolve(JOC_SCHEMA_OTHER_LOCATION), "xsd");
    }

    public static List<Path> getFiles(Path dir, String extension) throws Exception {
        return Files.walk(dir).filter(s -> s.toString().toLowerCase().endsWith("." + extension.toLowerCase())).map(Path::getFileName).sorted()
                .collect(Collectors.toList());
    }

}
