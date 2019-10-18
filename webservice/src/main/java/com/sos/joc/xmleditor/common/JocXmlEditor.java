package com.sos.joc.xmleditor.common;

import java.net.URI;

import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.xmleditor.common.ObjectType;

import sos.util.SOSString;

public class JocXmlEditor {

    public static final String APPLICATION_PATH = "xmleditor";
    public static final String AVAILABILITY_STARTING_WITH = "1.13.1";
    public static final String MESSAGE_UNSUPPORTED_WEB_SERVICE = String.format("Unsupported web service: JobScheduler needs at least version %s",
            AVAILABILITY_STARTING_WITH);

    public static final String JOC_SCHEMA_PATH_YADE = "xsd/yade/YADE_configuration_v1.12.xsd";
    public static final String JOC_SCHEMA_PATH_NOTIFICATION = "xsd/notification/SystemMonitorNotification_v1.0.xsd";

    public static final String MESSAGE_CODE_DRAFT_NOT_EXIST = "XMLEDITOR-101";
    public static final String MESSAGE_CODE_LIVE_NOT_EXIST = "XMLEDITOR-102";
    public static final String MESSAGE_CODE_LIVE_IS_NEWER = "XMLEDITOR-103";
    public static final String MESSAGE_CODE_DRAFT_IS_NEWER = "XMLEDITOR-104";
    public static final String MESSAGE_CODE_NO_CONFIGURATION_EXIST = "XMLEDITOR-105";

    public static final String ERROR_CODE_JOBSCHEDULER_NOT_CONNECTED = "XMLEDITOR-401";
    public static final String ERROR_CODE_WRONG_OBJECT_TYPE = "XMLEDITOR-402";
    public static final String ERROR_CODE_PERMISSION_DENIED = "XMLEDITOR-403";
    public static final String ERROR_CODE_CONFUGURATION_NOT_FOUND = "XMLEDITOR-404";
    public static final String ERROR_CODE_VALIDATION_ERROR = "XMLEDITOR-405";
    public static final String ERROR_CODE_DEPLOY_ERROR = "XMLEDITOR-406";
    public static final String ERROR_CODE_DEPLOY_ERROR_UNSUPPORTED_OBJECT_TYPE = "XMLEDITOR-407";

    public static String getResourceImplPath(final String path) {
        return String.format("./%s/%s", APPLICATION_PATH, path);
    }

    public static String getSchemaLocation(final ObjectType type, final String otherSchema) {
        if (type == null || SOSString.isEmpty(type.name())) {
            return null;
        }
        if (type.equals(ObjectType.YADE)) {
            return JOC_SCHEMA_PATH_YADE;
        } else if (type.equals(ObjectType.NOTIFICATION)) {
            return JOC_SCHEMA_PATH_NOTIFICATION;
        }
        return otherSchema;
    }

    public static URI getSchemaURI(final ObjectType type, final String otherSchema) throws Exception {
        String schemaLocation = getSchemaLocation(type, otherSchema);
        if (schemaLocation == null) {
            return null;
        }
        if (type.equals(ObjectType.OTHER)) {
            return new URI(schemaLocation);
        }
        return Globals.servletBaseUri.resolve(schemaLocation);
    }

    public static String getConfigurationName(final ObjectType type) {
        return getConfigurationName(type, null);
    }

    public static String getConfigurationName(final ObjectType type, final String name) {
        if (type.equals(ObjectType.OTHER)) {
            return name;
        }
        return JobSchedulerXmlEditor.getBaseName(type) + ".xml";
    }

    public static boolean checkRequiredParameter(final String paramKey, final ObjectType paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.toString().isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    public static XsdValidator validate(final ObjectType type, final String configuration) throws Exception {
        return validate(type, configuration, null);
    }

    public static XsdValidator validate(final ObjectType type, final String configuration, final String otherSchema) throws Exception {
        XsdValidator validator = null;
        try {
            validator = new XsdValidator(getSchemaURI(type, otherSchema));
            validator.validate(configuration);
        } catch (Throwable e) {
            String schema = "";
            if (validator != null) {
                schema = validator.getSchema().toString();
            }
            throw new JocException(new JocError(ERROR_CODE_VALIDATION_ERROR, String.format("[%s][%s]%s", type.name(), schema, e.toString())), e);
        }
        return validator;
    }
}
