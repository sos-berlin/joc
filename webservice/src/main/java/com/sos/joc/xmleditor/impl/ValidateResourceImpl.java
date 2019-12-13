package com.sos.joc.xmleditor.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.classes.xmleditor.XsdValidator;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.validate.ErrorMessage;
import com.sos.joc.model.xmleditor.validate.ValidateConfiguration;
import com.sos.joc.model.xmleditor.validate.ValidateConfigurationAnswer;
import com.sos.joc.xmleditor.resource.IValidateResource;

@Path(JocXmlEditor.APPLICATION_PATH)
public class ValidateResourceImpl extends JOCResourceImpl implements IValidateResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateResourceImpl.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    @Override
    public JOCDefaultResponse validate(final String accessToken, final ValidateConfiguration in) {
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);

            if (response == null) {
                XsdValidator validator = new XsdValidator(in.getObjectType(), JocXmlEditor.getSchemaURI(in.getObjectType(), in.getSchema()));
                try {
                    validator.validate(in.getConfiguration());
                } catch (SAXParseException e) {
                    LOGGER.error(String.format("[%s]%s", validator.getUsedSchema(), e.toString()), e);
                    response = JOCDefaultResponse.responseStatus200(getError(validator, e));
                }

                if (response == null) {
                    if (isDebugEnabled) {
                        LOGGER.debug(String.format("[%s][%s][%s]validated", in.getJobschedulerId(), in.getObjectType().name(), validator
                                .getUsedSchema()));
                    }
                    response = JOCDefaultResponse.responseStatus200(getSuccess());
                }
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
        checkRequiredParameter("configuration", in.getConfiguration());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("schema", in.getSchema());
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

    public static ValidateConfigurationAnswer getError(XsdValidator validator, SAXParseException e) {
        ValidateConfigurationAnswer answer = new ValidateConfigurationAnswer();
        answer.setValidated(null);
        answer.setValidationError(getErrorMessage(validator, e));
        return answer;
    }

    private static ErrorMessage getErrorMessage(XsdValidator validator, SAXParseException e) {
        ErrorMessage m = new ErrorMessage();
        m.setCode(JocXmlEditor.ERROR_CODE_VALIDATION_ERROR);
        m.setMessage(e.toString());
        m.setLine(e.getLineNumber());
        m.setColumn(e.getColumnNumber());
        m.setElementName(validator.getHandler().getErrorElement());
        m.setElementPosition(validator.getHandler().getErrorElementPostion());
        return m;
    }

    private ValidateConfigurationAnswer getSuccess() {
        ValidateConfigurationAnswer answer = new ValidateConfigurationAnswer();
        answer.setValidated(new Date());
        return answer;
    }
}
