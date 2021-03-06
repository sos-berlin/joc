package com.sos.joc.xmleditor.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.xml2json.Xml2JsonConfiguration;
import com.sos.joc.model.xmleditor.xml2json.Xml2JsonConfigurationAnswer;
import com.sos.joc.xmleditor.common.Xml2JsonConverter;
import com.sos.joc.xmleditor.resource.IXml2JsonResource;
import com.sos.schema.JsonValidator;

@Path(JocXmlEditor.APPLICATION_PATH)
public class Xml2JsonResourceImpl extends JOCResourceImpl implements IXml2JsonResource {

    @Override
    public JOCDefaultResponse process(final String accessToken, final byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, Xml2JsonConfiguration.class);
            Xml2JsonConfiguration in = Globals.objectMapper.readValue(filterBytes, Xml2JsonConfiguration.class);

            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response != null) {
                return response;
            }

            JocXmlEditor.parseXml(in.getConfiguration());
            java.nio.file.Path schema = null;
            if (in.getObjectType().equals(ObjectType.OTHER)) {
                schema = JocXmlEditor.getOthersSchema(in.getSchemaIdentifier(), false);
            } else {
                schema = JocXmlEditor.getStandardAbsoluteSchemaLocation(in.getObjectType());
            }

            Xml2JsonConverter converter = new Xml2JsonConverter();
            return JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(getSuccess(converter.convert(in.getObjectType(),
                    schema, in.getConfiguration()))));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void checkRequiredParameters(final Xml2JsonConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        checkRequiredParameter("configuration", in.getConfiguration());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("schemaIdentifier", in.getSchemaIdentifier());
        }
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final Xml2JsonConfiguration in) throws Exception {
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

    private Xml2JsonConfigurationAnswer getSuccess(String json) {
        Xml2JsonConfigurationAnswer answer = new Xml2JsonConfigurationAnswer();
        answer.setConfigurationJson(json);
        return answer;
    }

}
