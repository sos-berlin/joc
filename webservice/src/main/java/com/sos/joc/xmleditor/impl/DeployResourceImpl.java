package com.sos.joc.xmleditor.impl;

import java.io.StringReader;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.sos.DataExchange.converter.JadeXml2IniConverter;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.XmlEditorAudit;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.deploy.DeployConfiguration;
import com.sos.joc.model.xmleditor.deploy.DeployConfigurationAnswer;
import com.sos.joc.xmleditor.resource.IDeployResource;

import sos.util.SOSDate;

@Path(JocXmlEditor.APPLICATION_PATH)
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployResourceImpl.class);

    private static final String DEPLOY_HEADER = "Automatically generated file (%s UTC).";
    private static final String DEPLOY_HEADER_MESSAGE =
            "Do not make any changes to this file, as it has been automatically generated by the JOC XMLEditor and will be overridden when the configuration is deployed.";

    private static final String ROOT_ELEMENT_YADE = "Configurations";
    private static final String ROOT_ELEMENT_NOTIFICATION = "SystemMonitorNotification";

    @Override
    public JOCDefaultResponse deploy(final String accessToken, final DeployConfiguration in) {
        SOSHibernateSession session = null;
        try {
            // TODO check folder permissions
            checkRequiredParameters(in);

            if (in.getObjectType().equals(ObjectType.OTHER)) {
                throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR_UNSUPPORTED_OBJECT_TYPE, String.format(
                        "[%s][%s]unsupported object type for deployment", in.getJobschedulerId(), in.getObjectType().name())));
            }

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                XmlEditorAudit audit = new XmlEditorAudit(in);
                logAuditMessage(audit);

                session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
                DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

                DBItemXmlEditorObject item = getItem(dbLayer, in);

                JocXmlEditor.validate(in.getObjectType(), item.getConfigurationDraft());
                Date deployed = putFile(in, item.getConfigurationDraft());

                audit.setStartTime(deployed);
                DBItemAuditLog auditItem = storeAuditLogEntry(audit);
                if (auditItem != null) {
                    item.setAuditLogId(auditItem.getId());
                }
                item.setConfigurationDeployed(item.getConfigurationDraft());
                item.setConfigurationDraft(null);
                item.setAccount(getAccount());
                item.setDeployed(deployed);
                item.setModified(new Date());

                session.beginTransaction();
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
        checkRequiredParameter("configuration", in.getConfiguration());
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

    private DBItemXmlEditorObject getItem(DbLayerXmlEditor dbLayer, DeployConfiguration in) throws Exception {
        dbLayer.getSession().beginTransaction();
        DBItemXmlEditorObject item = dbLayer.getObject(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in
                .getObjectType()));
        if (item == null) {
            item = new DBItemXmlEditorObject();
            item.setSchedulerId(in.getJobschedulerId());
            item.setObjectType(in.getObjectType().name());
            item.setName(JocXmlEditor.getConfigurationName(in.getObjectType()));
            item.setConfigurationDraft(in.getConfiguration());
            item.setSchemaLocation(JocXmlEditor.getSchemaLocation(in.getObjectType()));

            item.setAuditLogId(new Long(0));// TODO
            item.setAccount(getAccount());
            item.setCreated(new Date());
            item.setModified(item.getCreated());
            dbLayer.getSession().save(item);
        } else {
            item.setConfigurationDraft(in.getConfiguration());
            item.setAccount(getAccount());
            item.setModified(new Date());
            dbLayer.getSession().update(item);
        }
        dbLayer.getSession().commit();
        return item;
    }

    private DeployConfigurationAnswer getSuccess(Date deployed) {
        DeployConfigurationAnswer answer = new DeployConfigurationAnswer();
        answer.setDeployed(deployed);
        answer.setMessage(new AnswerMessage());
        answer.getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_IS_NEWER);
        answer.getMessage().setMessage(JocXmlEditor.MESSAGE_LIVE_IS_NEWER);
        return answer;
    }

    private Date putFile(DeployConfiguration in, String configuration) throws Exception {
        String file = null;// for exception
        try {
            JOCHotFolder hotFolder = new JOCHotFolder(this);
            Date deployed = new Date();
            String deployedDateTime = "unknown";
            try {
                deployedDateTime = SOSDate.getDateAsString(deployed, "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
            }

            file = JocXmlEditor.getLivePathXml(in.getObjectType());
            if (in.getObjectType().equals(ObjectType.YADE)) {
                hotFolder.putFile(file, getXmlFileContent(in.getObjectType(), configuration, deployedDateTime));

                file = JocXmlEditor.getLivePathYadeIni();
                hotFolder.putFile(file, convertXml2Ini(configuration, deployedDateTime));
            } else {
                hotFolder.putFile(file, getXmlFileContent(in.getObjectType(), configuration, deployedDateTime));
            }
            return deployed;
        } catch (Throwable e) {
            LOGGER.error(String.format("[%s][%s][%s]%s could't put configuration: %s", in.getJobschedulerId(), in.getObjectType().name(), file,
                    JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, e.toString()), e);
            throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_DEPLOY_ERROR, String.format("[%s][%s][%s]could't put configuration", in
                    .getJobschedulerId(), in.getObjectType().name(), file)));
        }
    }

    private byte[] getXmlFileContent(ObjectType objectType, String configuration, String deployedDateTime) throws Exception {
        StringBuilder sb = getXmlFileHeader(deployedDateTime);
        int indx = -1;
        if (objectType.equals(ObjectType.YADE)) {
            indx = configuration.indexOf(ROOT_ELEMENT_YADE);
        } else {
            indx = configuration.indexOf(ROOT_ELEMENT_NOTIFICATION);
        }
        if (indx > -1) {
            configuration = configuration.substring(indx - 1);
        }
        return sb.append(configuration).toString().getBytes(JocXmlEditor.CHARSET);
    }

    private byte[] convertXml2Ini(String configuration, String deployedDateTime) throws Exception {
        JadeXml2IniConverter converter = new JadeXml2IniConverter();
        InputSource schemaInputSource = new InputSource(JocXmlEditor.getSchemaURI(ObjectType.YADE).toString());
        InputSource configurationInputSource = new InputSource();
        configurationInputSource.setCharacterStream(new StringReader(configuration));
        return converter.process(schemaInputSource, configurationInputSource, getIniFileHeader(deployedDateTime));
    }

    private StringBuilder getIniFileHeader(String deployedDateTime) {
        StringBuilder sb = new StringBuilder("###########################################################").append(JocXmlEditor.NEW_LINE);
        sb.append("# ").append(String.format(DEPLOY_HEADER, deployedDateTime)).append(JocXmlEditor.NEW_LINE);
        sb.append("# ").append(DEPLOY_HEADER_MESSAGE).append(JocXmlEditor.NEW_LINE);
        sb.append("###########################################################");
        return sb;
    }

    private StringBuilder getXmlFileHeader(String deployedDateTime) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(JocXmlEditor.NEW_LINE);
        sb.append("<!-- ").append(String.format(DEPLOY_HEADER, deployedDateTime)).append(" -->").append(JocXmlEditor.NEW_LINE);
        sb.append("<!-- ").append(DEPLOY_HEADER_MESSAGE).append(" -->").append(JocXmlEditor.NEW_LINE);
        return sb;
    }

}
