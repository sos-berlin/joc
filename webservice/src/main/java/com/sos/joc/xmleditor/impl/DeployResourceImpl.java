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

import sos.util.SOSDate;
import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployResourceImpl.class);

    private static final String DEPLOY_HEADER = "Automatically generated file (%s UTC)";
    private static final String DEPLOY_HEADER_MESSAGE =
            "Do not make any changes to this file, as it has been automatically generated by the JOC XMLEditor and will be overridden when the configuration is deployed.";

    private static final String ROOT_ELEMENT_YADE = "Configurations";
    private static final String ROOT_ELEMENT_NOTIFICATION = "SystemMonitorNotification";

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

                Date deployed = putFile(in, item.getConfiguration());

                item.setConfiguration(null);
                item.setAccount(getAccount());
                item.setModified(new Date());
                session.update(item);
                session.commit();
                response = JOCDefaultResponse.responseStatus200(getSuccess(deployed));
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

    private DeployConfigurationAnswer getSuccess(Date deployed) {
        DeployConfigurationAnswer answer = new DeployConfigurationAnswer();
        answer.setDeployed(deployed);
        return answer;
    }

    private void checkConfiguration(DBItemXmlEditorObject item, DeployConfiguration in) throws Exception {
        if (item == null || SOSString.isEmpty(item.getConfiguration())) {
            throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, String.format("[%s] configuration not found", in.getObjectType()
                    .name())));
        }
    }

    private Date putFile(DeployConfiguration in, String configuration) throws Exception {

        String file = null;
        try {
            JOCHotFolder hotFolder = new JOCHotFolder(this);
            Date deployed = new Date();
            if (in.getObjectType().equals(ObjectType.YADE)) {
                file = JobSchedulerXmlEditor.getLivePathYadeXml();
                hotFolder.putFile(JobSchedulerXmlEditor.getLivePathYadeIni(), getIniFileContent(convertXml2Ini(configuration), deployed));
                hotFolder.putFile(file, getXmlFileContent(in.getObjectType(), configuration, deployed));
            } else {
                file = JobSchedulerXmlEditor.getLivePathNotificationXml();
                hotFolder.putFile(file, getXmlFileContent(in.getObjectType(), configuration, deployed));
            }
            return deployed;
        } catch (Throwable e) {
            throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, String.format("[%s][%s]could't put file", in
                    .getJobschedulerId(), file)), e);
        }
    }

    private String getXmlFileContent(ObjectType objectType, String configuration, Date date) {
        String msg = "unknown";
        try {
            msg = SOSDate.getDateAsString(date, "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
        }
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(JocXmlEditor.NEW_LINE);
        sb.append("<!-- ").append(String.format(DEPLOY_HEADER, msg)).append(" -->").append(JocXmlEditor.NEW_LINE);
        sb.append("<!-- ").append(DEPLOY_HEADER_MESSAGE).append(" -->").append(JocXmlEditor.NEW_LINE);

        int indx = -1;
        if (objectType.equals(ObjectType.YADE)) {
            indx = configuration.indexOf(ROOT_ELEMENT_YADE);
        } else {
            indx = configuration.indexOf(ROOT_ELEMENT_NOTIFICATION);
        }
        if (indx > -1) {
            configuration = configuration.substring(indx - 1);
        }
        return sb.append(configuration).toString();
    }

    private String getIniFileContent(String configuration, Date date) {
        String msg = "unknown";
        try {
            msg = SOSDate.getDateAsString(date, "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
        }
        StringBuilder sb = new StringBuilder("###########################################################").append(JocXmlEditor.NEW_LINE);
        sb.append("# ").append(String.format(DEPLOY_HEADER, msg)).append(JocXmlEditor.NEW_LINE);
        sb.append("# ").append(DEPLOY_HEADER_MESSAGE).append(JocXmlEditor.NEW_LINE);
        sb.append("###########################################################").append(JocXmlEditor.NEW_LINE);
        return sb.append(configuration).toString();
    }

    public String convertXml2Ini(String configuration) {

        // tmpIniFile = Files.createTempFile("sos.yade_settings_", ".ini");
        // JadeXml2IniConverter converter = new JadeXml2IniConverter();
        // converter.process(new InputSource(schemaStream), xmlFile, tmpIniFile.toString());

        return "converted";
    }

}
