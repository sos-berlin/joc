package com.sos.joc.xmleditor.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.deploy.DeployConfiguration;
import com.sos.joc.model.xmleditor.deploy.DeployConfigurationAnswer;
import com.sos.joc.xmleditor.common.JocXmlEditor;
import com.sos.joc.xmleditor.resource.IDeployResource;

import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployResourceImpl.class);

    @Override
    public JOCDefaultResponse deploy(final String accessToken, final DeployConfiguration in) {
        SOSHibernateSession session = null;
        try {
            checkRequiredParameters(in);

            if (in.getObjectType().equals(ObjectType.OTHER)) {
                throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR_UNSUPPORTED_OBJECT_TYPE, String.format(
                        "unsupported object type for deployment: %s", in.getObjectType().name())));
            }

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                // TODO check folder permissions
                // TODO use audit log

                session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
                session.beginTransaction();

                DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);
                DBItemXmlEditorObject item = dbLayer.getObject(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in
                        .getObjectType()));

                checkConfiguration(item, in);
                JocXmlEditor.validate(in.getObjectType(), item.getConfiguration());
                putFile(in.getObjectType(), item.getConfiguration());

                session.delete(item);
                session.commit();
                response = JOCDefaultResponse.responseStatus200(getSuccess());
            }
            return response;
        } catch (JocException e) {
            Globals.rollback(session);
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            Globals.rollback(session);
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
        }
    }

    private void checkRequiredParameters(final DeployConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final DeployConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isDeploy();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private DeployConfigurationAnswer getSuccess() {
        DeployConfigurationAnswer answer = new DeployConfigurationAnswer();
        answer.setDeployed(new Date());
        return answer;
    }

    private void checkConfiguration(DBItemXmlEditorObject item, DeployConfiguration in) throws Exception {
        if (item == null) {
            throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, String.format("[%s] configuration not found", in.getObjectType()
                    .name())));
        } else if (SOSString.isEmpty(item.getConfiguration())) {
            throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, String.format("[%s][%s] configuration is empty", in
                    .getObjectType().name(), item.getId())));
        }
    }

    private void putFile(ObjectType objectType, String configuration) throws Exception {

        try {
            JOCHotFolder hotFolder = new JOCHotFolder(this);
            if (objectType.equals(ObjectType.YADE)) {
                hotFolder.putFile(JobSchedulerXmlEditor.getLivePathYadeIni(), convertXml2Ini(configuration));
                hotFolder.putFile(JobSchedulerXmlEditor.getLivePathYadeXml(), configuration);
            } else {
                hotFolder.putFile(JobSchedulerXmlEditor.getLivePathNotificationXml(), configuration);
            }
        } catch (Throwable e) {
            throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, e.toString()), e);
        }
    }

    public String convertXml2Ini(String configuration) {

        // tmpIniFile = Files.createTempFile("sos.yade_settings_", ".ini");
        // JadeXml2IniConverter converter = new JadeXml2IniConverter();
        // converter.process(new InputSource(schemaStream), xmlFile, tmpIniFile.toString());

        return "converted";
    }

}
