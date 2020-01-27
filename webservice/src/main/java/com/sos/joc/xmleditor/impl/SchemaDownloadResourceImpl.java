package com.sos.joc.xmleditor.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.schema.SchemaDownloadConfiguration;
import com.sos.joc.xmleditor.resource.ISchemaDownloadResource;
import com.sos.schema.JsonValidator;

@Path(JocXmlEditor.APPLICATION_PATH)
public class SchemaDownloadResourceImpl extends JOCResourceImpl implements ISchemaDownloadResource {

    @Override
    public JOCDefaultResponse process(final String xAccessToken, String accessToken, String jobschedulerId, String objectType, String show,
            String schemaIdentifier) {
        try {
            accessToken = getAccessToken(xAccessToken, accessToken);

            JsonObjectBuilder builder = Json.createObjectBuilder();
            if (jobschedulerId != null) {
                builder.add("jobschedulerId", jobschedulerId);
            }
            if (objectType != null) {
                builder.add("objectType", objectType);
            }
            builder.add("show", show == null ? false : Boolean.parseBoolean(show));

            if (schemaIdentifier != null) {
                builder.add("schemaIdentifier", URLDecoder.decode(schemaIdentifier, JocXmlEditor.CHARSET));
            }
            String json = builder.build().toString();

            JsonValidator.validateFailFast(json.getBytes(), SchemaDownloadConfiguration.class);
            SchemaDownloadConfiguration in = Globals.objectMapper.readValue(json.getBytes(), SchemaDownloadConfiguration.class);

            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {

                return download(in);
            }
            return response;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void checkRequiredParameters(final SchemaDownloadConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("schemaIdentifier", in.getSchemaIdentifier());
        }
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final SchemaDownloadConfiguration in) throws Exception {
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

    private JOCDefaultResponse download(SchemaDownloadConfiguration in) throws Exception {

        java.nio.file.Path path = null;
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            path = JocXmlEditor.getOthersSchema(in.getSchemaIdentifier(), true);
        } else {
            path = JocXmlEditor.getStandardAbsoluteSchemaLocation(in.getObjectType());
        }

        if (!Files.exists(path)) {
            throw new Exception(String.format("[%s][%s]schema file not found", in.getSchemaIdentifier(), path.toString()));
        }

        final java.nio.file.Path downPath = path;
        StreamingOutput fileStream = new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException {
                InputStream in = null;
                try {
                    in = Files.newInputStream(downPath);
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                    output.flush();
                } finally {
                    try {
                        output.close();
                    } catch (Exception e) {
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };
        if (in.getShow()) {
            return JOCDefaultResponse.responsePlainStatus200(fileStream);
        } else {
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, path.getFileName().toString());
        }
    }
}
