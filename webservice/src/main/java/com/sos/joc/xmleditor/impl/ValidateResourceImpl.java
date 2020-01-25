package com.sos.joc.xmleditor.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.classes.xmleditor.exceptions.XsdValidatorException;
import com.sos.joc.classes.xmleditor.validator.XsdValidator;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.validate.ErrorMessage;
import com.sos.joc.model.xmleditor.validate.ValidateConfiguration;
import com.sos.joc.model.xmleditor.validate.ValidateConfigurationAnswer;
import com.sos.joc.xmleditor.resource.IValidateResource;
import com.sos.schema.JsonValidator;

@Path(JocXmlEditor.APPLICATION_PATH)
public class ValidateResourceImpl extends JOCResourceImpl implements IValidateResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateResourceImpl.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    @Override
    public JOCDefaultResponse process(final String accessToken, final byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, ValidateConfiguration.class);
            ValidateConfiguration in = Globals.objectMapper.readValue(filterBytes, ValidateConfiguration.class);
            
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);

            if (response == null) {
                java.nio.file.Path schema = null;
                if (in.getObjectType().equals(ObjectType.OTHER)) {
                    schema = JocXmlEditor.getOthersSchemaFile(in.getSchemaIdentifier(), false);
                } else {
                    schema = JocXmlEditor.getStandardAbsoluteSchemaLocation(in.getObjectType());
                }
                // check for vulnerabilities and validate
                XsdValidator validator = new XsdValidator(schema);
                try {
                    validator.validate(in.getConfiguration());
                } catch (XsdValidatorException e) {
                    LOGGER.error(String.format("[%s]%s", validator.getSchema(), e.toString()), e);
                    return JOCDefaultResponse.responseStatus200(getError(e));
                }

                if (isDebugEnabled) {
                    LOGGER.debug(String.format("[%s][%s][%s]validated", in.getJobschedulerId(), in.getObjectType().name(), validator.getSchema()));
                }
                response = JOCDefaultResponse.responseStatus200(getSuccess());
            }
            return response;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void checkRequiredParameters(final ValidateConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("schemaIdentifier", in.getSchemaIdentifier());
        } else {
            checkRequiredParameter("configuration", in.getConfiguration());
        }
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final ValidateConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    public static ValidateConfigurationAnswer getError(XsdValidatorException e) {
        ValidateConfigurationAnswer answer = new ValidateConfigurationAnswer();
        answer.setValidated(null);
        answer.setValidationError(getErrorMessage(e));
        return answer;
    }

    private static ErrorMessage getErrorMessage(XsdValidatorException e) {
        ErrorMessage m = new ErrorMessage();
        m.setCode(JocXmlEditor.ERROR_CODE_VALIDATION_ERROR);
        try {
            m.setMessage(String.format("'%s', line=%s, column=%s, %s", e.getElementName(), e.getLineNumber(), e.getColumnNumber(), e.getCause()
                    .getMessage()));
        } catch (Throwable ex) {
            m.setMessage(ex.toString());
        }
        m.setLine(e.getLineNumber());
        m.setColumn(e.getColumnNumber());
        m.setElementName(e.getElementName());
        m.setElementPosition(e.getElementPosition());
        return m;
    }

    private ValidateConfigurationAnswer getSuccess() {
        ValidateConfigurationAnswer answer = new ValidateConfigurationAnswer();
        answer.setValidated(new Date());
        return answer;
    }
}
