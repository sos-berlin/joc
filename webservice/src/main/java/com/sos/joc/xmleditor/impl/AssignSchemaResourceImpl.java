package com.sos.joc.xmleditor.impl;

import java.nio.file.Files;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.xmleditor.assign.schema.AssignSchemaConfiguration;
import com.sos.joc.model.xmleditor.assign.schema.AssignSchemaConfigurationAnswer;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.xmleditor.resource.IAssignSchemaResource;

@Path(JocXmlEditor.APPLICATION_PATH)
public class AssignSchemaResourceImpl extends JOCResourceImpl implements IAssignSchemaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignSchemaResourceImpl.class);
    private static final boolean isDebugEnabled = true;//LOGGER.isDebugEnabled();

    @Override
    public JOCDefaultResponse assign(final String accessToken, final AssignSchemaConfiguration in) {
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                response = JOCDefaultResponse.responseStatus200(getSuccess(in));
            }
            return response;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void checkRequiredParameters(final AssignSchemaConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        if (in.getUri() == null) {
            if (in.getFileName() == null || in.getFileContent() == null) {
                throw new JocMissingRequiredParameterException("uri param is null. missing fileName or fileContent parameters.");
            }
        }
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final AssignSchemaConfiguration in) throws Exception {
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

    private AssignSchemaConfigurationAnswer getSuccess(final AssignSchemaConfiguration in) throws Exception {
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            String source = null;
            java.nio.file.Path target = null;
            if (in.getUri() == null) {
                source = in.getFileName();
                if (isDebugEnabled) {
                    LOGGER.info(String.format("[%s]create from local file", source));
                }
                target = JocXmlEditor.createOthersSchema(source, in.getFileContent());
            } else {
                source = in.getUri();
                if (JocXmlEditor.isHttp(in.getUri())) {
                    if (isDebugEnabled) {
                        LOGGER.info(String.format("[%s]copy from http(s)", source));
                    }
                    target = JocXmlEditor.downloadOthersSchema(in.getUri());
                } else {
                    if (isDebugEnabled) {
                        LOGGER.info(String.format("[%s]copy from local file", source));
                    }
                    target = JocXmlEditor.copyOthersSchema(in.getUri());
                }
            }
            if (Files.exists(target)) {
                AssignSchemaConfigurationAnswer answer = new AssignSchemaConfigurationAnswer();
                answer.setSchema(JocXmlEditor.getFileContent(target));
                answer.setSchemaIdentifier(JocXmlEditor.getOthersSchemaIdentifier(target));
                return answer;
            } else {
                throw new Exception(String.format("[%s][target=%s]target file not found", source, target));
            }
        } else {
            throw new Exception(String.format("[%s]not supported", in.getObjectType().name()));
        }
    }

}
