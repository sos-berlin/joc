package com.sos.joc.xmleditor.impl;

import javax.ws.rs.Path;

import org.dom4j.Document;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.format.FormatConfiguration;
import com.sos.joc.model.xmleditor.format.FormatConfigurationAnswer;
import com.sos.joc.xmleditor.resource.IFormatResource;

@Path(JocXmlEditor.APPLICATION_PATH)
public class FormatResourceImpl extends JOCResourceImpl implements IFormatResource {

    @Override
    public JOCDefaultResponse format(final String accessToken, final FormatConfiguration in) {
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                Document doc = JocXmlEditor.parseXml(in.getConfiguration());
                response = JOCDefaultResponse.responseStatus200(getSuccess(JocXmlEditor.formatXml(doc, true)));
            }
            return response;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void checkRequiredParameters(final FormatConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        checkRequiredParameter("configuration", in.getConfiguration());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final FormatConfiguration in) throws Exception {
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

    private FormatConfigurationAnswer getSuccess(String xml) {
        FormatConfigurationAnswer answer = new FormatConfigurationAnswer();
        answer.setConfiguration(xml);
        return answer;
    }

}
