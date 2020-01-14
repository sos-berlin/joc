package com.sos.joc.xmleditor.impl;

import java.nio.file.Files;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.schema.assign.AssignSchemaConfiguration;
import com.sos.joc.model.xmleditor.schema.assign.AssignSchemaConfigurationAnswer;
import com.sos.joc.xmleditor.common.schema.SchemaHandler;
import com.sos.joc.xmleditor.resource.IAssignSchemaResource;

@Path(JocXmlEditor.APPLICATION_PATH)
public class AssignSchemaResourceImpl extends JOCResourceImpl implements IAssignSchemaResource {

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
            SchemaHandler h = new SchemaHandler();
            h.process(in.getUri(), in.getFileName(), in.getFileContent());
            if (Files.exists(h.getTarget())) {
                String schema = JocXmlEditor.getFileContent(h.getTarget());
                JocXmlEditor.parseXml(schema);

                AssignSchemaConfigurationAnswer answer = new AssignSchemaConfigurationAnswer();
                answer.setSchema(schema);
                answer.setSchemaIdentifier(JocXmlEditor.getOthersSchemaIdentifier(h.getSource()));
                return answer;
            } else {
                throw new Exception(String.format("[%s][target=%s]target file not found", h.getSource(), h.getTarget()));
            }
        } else {
            throw new Exception(String.format("[%s]not supported", in.getObjectType().name()));
        }
    }

}
