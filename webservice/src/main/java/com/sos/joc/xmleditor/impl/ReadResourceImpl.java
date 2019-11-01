package com.sos.joc.xmleditor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.common.ObjectVersionState;
import com.sos.joc.model.xmleditor.read.ReadConfiguration;
import com.sos.joc.model.xmleditor.read.other.AnswerConfiguration;
import com.sos.joc.model.xmleditor.read.other.ReadOtherConfigurationAnswer;
import com.sos.joc.model.xmleditor.read.standard.ReadStandardConfigurationAnswer;
import com.sos.joc.model.xmleditor.read.standard.ReadStandardConfigurationAnswerState;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.xmleditor.resource.IReadResource;

import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class ReadResourceImpl extends JOCResourceImpl implements IReadResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadResourceImpl.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private static final boolean isTraceEnabled = LOGGER.isTraceEnabled();

    @Override
    public JOCDefaultResponse read(final String accessToken, final ReadConfiguration in) {
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                if (in.getObjectType().equals(ObjectType.OTHER)) {
                    ReadOtherConfigurationAnswer answer = handleOtherConfigurations(in);
                    response = JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(answer));
                } else {
                    ReadStandardConfigurationAnswer answer = handleStandardConfiguration(in);
                    response = JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(answer));
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

    private void checkRequiredParameters(final ReadConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final ReadConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isView();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private ReadStandardConfigurationAnswer handleStandardConfiguration(ReadConfiguration in) throws Exception {
        ReadStandardConfigurationAnswer answer = new ReadStandardConfigurationAnswer();
        answer.setState(new ReadStandardConfigurationAnswerState());
        answer.getState().setMessage(new AnswerMessage());

        JOCHotFolder hotFolder = new JOCHotFolder(this);
        byte[] liveFile = getLiveFile(hotFolder, in, answer);

        DBItemXmlEditorObject item = getItem(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in.getObjectType(),
                in.getName()));

        if (item != null && item.getConfigurationDraft() == null) {
            item = null;
        }

        String configuration = null;
        Date modified = null;
        if (item == null || (in.getForceLive() != null && in.getForceLive())) {
            if (liveFile != null) {
                configuration = new String(liveFile);
                modified = hotFolder.getLastModifiedDate();
            }
        } else {
            if (item != null) {
                configuration = item.getConfigurationDraft();
                modified = item.getModified();
            }
        }

        answer.setSchema(JocXmlEditor.getSchemaURI(in.getObjectType()).toString());
        answer.setConfiguration(configuration);
        answer.setModified(modified);

        if (configuration == null) {
            answer.getState().setDeployed(false);
            answer.getState().getMessage().setMessage("No configuration found");
            answer.getState().getMessage().setCode(JocXmlEditor.CODE_NO_CONFIGURATION_EXIST);
            answer.getState().setVersionState(ObjectVersionState.NO_CONFIGURATION_EXIST);
        } else {
            if (item == null) {
                answer.getState().setDeployed(true);
                answer.getState().getMessage().setMessage("Using version in live folder. No draft version found in database");
                answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_NOT_EXIST);
                answer.getState().setVersionState(ObjectVersionState.DRAFT_NOT_EXIST);
            } else {
                if (liveFile == null) {
                    answer.getState().setDeployed(false);
                    answer.getState().getMessage().setMessage("No live version found");
                    answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_NOT_EXIST);
                    answer.getState().setVersionState(ObjectVersionState.LIVE_NOT_EXIST);
                } else {
                    answer.getState().setDeployed(true);
                    if (hotFolder.getLastModifiedDate().after(item.getModified())) {
                        answer.getState().getMessage().setMessage("Version in live folder is newer then draft version in database");
                        answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_IS_NEWER);
                        answer.getState().setVersionState(ObjectVersionState.LIVE_IS_NEWER);
                    } else {
                        answer.getState().getMessage().setMessage("Draft version in database is newer then the version in the live folder");
                        answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_IS_NEWER);
                        answer.getState().setVersionState(ObjectVersionState.DRAFT_IS_NEWER);
                    }
                }
            }
        }
        return answer;
    }

    private byte[] getLiveFile(JOCHotFolder hotFolder, ReadConfiguration in, ReadStandardConfigurationAnswer answer) throws Exception {
        String file = JocXmlEditor.getLivePathXml(in.getObjectType());
        if (isDebugEnabled) {
            LOGGER.debug(String.format("[%s][%s]get file...", in.getJobschedulerId(), file));
        }

        byte[] result = null;
        try {
            result = hotFolder.getFile(file);
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s][%s]%s bytes", in.getJobschedulerId(), result == null ? "null" : result.length));
            }
        } catch (JobSchedulerConnectionRefusedException e) {
            LOGGER.warn(String.format("[%s]JobScheduler could't be connected", in.getJobschedulerId()), e);
            answer.setWarning(new AnswerMessage());
            answer.getWarning().setCode(JocXmlEditor.ERROR_CODE_JOBSCHEDULER_NOT_CONNECTED);
            answer.getWarning().setMessage(e.toString());
        } catch (JobSchedulerObjectNotExistException e) {
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s][%s]file not found", in.getJobschedulerId(), file));
            }
        }
        return result;
    }

    private ReadOtherConfigurationAnswer handleOtherConfigurations(ReadConfiguration in) throws Exception {

        ReadOtherConfigurationAnswer answer = new ReadOtherConfigurationAnswer();

        if (SOSString.isEmpty(in.getName())) {
            ArrayList<String> schemas = new ArrayList<String>();

            List<Map<String, String>> items = getOtherProperties(in, "name,schemaLocation");
            if (items != null && items.size() > 0) {
                ArrayList<AnswerConfiguration> configurations = new ArrayList<AnswerConfiguration>();
                for (int i = 0; i < items.size(); i++) {
                    Map<String, String> item = items.get(i);
                    AnswerConfiguration configuration = new AnswerConfiguration();
                    configuration.setName(item.get("0"));
                    configurations.add(configuration);

                    String uri = JocXmlEditor.getSchemaURI(ObjectType.OTHER, item.get("1")).toString();
                    if (!schemas.contains(uri)) {
                        schemas.add(uri);
                    }
                }
                answer.setConfigurations(configurations);
            }

            List<java.nio.file.Path> files = JocXmlEditor.getXsdFilesOther();
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    java.nio.file.Path path = files.get(i);
                    String schema = JocXmlEditor.JOC_SCHEMA_OTHER_LOCATION + path.getFileName();
                    String uri = JocXmlEditor.getSchemaURI(ObjectType.OTHER, schema).toString();
                    if (!schemas.contains(uri)) {
                        schemas.add(uri);
                    }
                }
            }

            if (schemas.size() > 0) {
                Collections.sort(schemas);
                answer.setSchemas(schemas);
            }

        } else {
            DBItemXmlEditorObject item = getItem(in.getJobschedulerId(), in.getObjectType().name(), in.getName());
            answer.setConfiguration(new AnswerConfiguration());
            if (item == null) {
                throw new JocException(new JocError(JocXmlEditor.CODE_NO_CONFIGURATION_EXIST, String.format("[%s][%s][%s]no configuration found", in
                        .getJobschedulerId(), in.getObjectType().name(), in.getName())));
            } else {
                answer.getConfiguration().setName(item.getName());
                answer.getConfiguration().setSchema(JocXmlEditor.getSchemaURI(ObjectType.OTHER, item.getSchemaLocation()).toString());
                answer.getConfiguration().setConfiguration(item.getConfigurationDraft());
                answer.getConfiguration().setModified(item.getModified());
            }
        }
        return answer;
    }

    private DBItemXmlEditorObject getItem(String schedulerId, String objectType, String name) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);

            session.beginTransaction();
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);
            DBItemXmlEditorObject item = dbLayer.getObject(schedulerId, objectType, name);
            session.commit();

            if (isTraceEnabled) {
                LOGGER.trace(String.format("[%s][%s][%s]%s", schedulerId, objectType, name, SOSString.toString(item, Arrays.asList(
                        "configuration"))));
            }

            return item;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }

    private List<Map<String, String>> getOtherProperties(ReadConfiguration in, String properties) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);

            session.beginTransaction();
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);
            List<Map<String, String>> items = dbLayer.getObjectProperties(in.getJobschedulerId(), ObjectType.OTHER.name(), properties);
            session.commit();
            return items;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }
}
