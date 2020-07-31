package com.sos.joc.xmleditor.impl;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.schema.SchemaReassignConfiguration;
import com.sos.joc.model.xmleditor.schema.SchemaReassignConfigurationAnswer;
import com.sos.joc.xmleditor.common.Xml2JsonConverter;
import com.sos.joc.xmleditor.common.schema.SchemaHandler;
import com.sos.joc.xmleditor.resource.ISchemaReassignResource;
import com.sos.schema.JsonValidator;

@Path(JocXmlEditor.APPLICATION_PATH)
public class SchemaReassignResourceImpl extends JOCResourceImpl implements ISchemaReassignResource {

    @Override
    public JOCDefaultResponse process(final String accessToken, final byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, SchemaReassignConfiguration.class);
            SchemaReassignConfiguration in = Globals.objectMapper.readValue(filterBytes, SchemaReassignConfiguration.class);

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

    private void checkRequiredParameters(final SchemaReassignConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        checkRequiredParameter("configuration", in.getConfiguration());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        if (in.getUri() == null) {
            if (in.getFileName() == null || in.getFileContent() == null) {
                throw new JocMissingRequiredParameterException("uri param is null. missing fileName or fileContent parameters.");
            }
        }
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final SchemaReassignConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (permission && response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private SchemaReassignConfigurationAnswer getSuccess(final SchemaReassignConfiguration in) throws Exception {
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            SchemaHandler h = new SchemaHandler();
            h.process(in.getUri(), in.getFileName(), in.getFileContent());
            if (Files.exists(h.getTargetTemp())) {
                boolean equals = h.getTargetTemp().equals(h.getTarget());
                try {
                    JocXmlEditor.parseXml(in.getConfiguration());
                    String schema = JocXmlEditor.getFileContent(h.getTargetTemp());
                    JocXmlEditor.parseXml(schema);

                    if (!equals) {
                        Files.move(h.getTargetTemp(), h.getTarget(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    Xml2JsonConverter converter = new Xml2JsonConverter();
                    String configurationJson = converter.convert(in.getObjectType(), h.getTarget(), in.getConfiguration());

                    SchemaReassignConfigurationAnswer answer = new SchemaReassignConfigurationAnswer();
                    answer.setSchema(schema);
                    answer.setSchemaIdentifier(JocXmlEditor.getOthersSchemaIdentifier(h.getSource()));
                    answer.setConfigurationJson(configurationJson);
                    answer.setRecreateJson(true);
                    return answer;
                } catch (Exception e) {
                    h.onError(!equals);
                    throw e;
                }
            } else {
                throw new Exception(String.format("[%s][target=%s]target file not found", h.getSource(), h.getTarget()));
            }
        } else {
            throw new Exception(String.format("[%s]not supported", in.getObjectType().name()));
        }
    }

}
