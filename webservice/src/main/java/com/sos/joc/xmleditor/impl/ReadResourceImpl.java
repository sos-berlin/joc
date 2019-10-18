package com.sos.joc.xmleditor.impl;

import java.util.Arrays;
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
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.common.ObjectVersionState;
import com.sos.joc.model.xmleditor.read.ReadConfiguration;
import com.sos.joc.model.xmleditor.read.ReadConfigurationAnswer;
import com.sos.joc.model.xmleditor.read.ReadConfigurationAnswerState;
import com.sos.joc.xmleditor.common.JocXmlEditor;
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
                ReadConfigurationAnswer answer = createAnswer(in);
                if (in.getObjectType().equals(ObjectType.OTHER)) {
                    handleOtherConfigurations(in, answer);
                } else {
                    handleStandardConfiguration(in, answer);
                }
                response = JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(answer));
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

    private ReadConfigurationAnswer handleStandardConfiguration(ReadConfiguration in, ReadConfigurationAnswer answer) throws Exception {
        JOCHotFolder hotFolder = new JOCHotFolder(this);
        byte[] liveFile = getLiveFile(hotFolder, in, answer);

        DBItemXmlEditorObject item = getItem(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in.getObjectType(),
                in.getName()));

        String configuration = null;
        Date modified = null;
        if (item == null || (in.getForceLive() != null && in.getForceLive())) {
            if (liveFile != null) {
                configuration = new String(liveFile);
                modified = hotFolder.getLastModifiedDate();
            }
        } else {
            if (item != null) {
                configuration = item.getConfiguration();
                modified = item.getModified();
            }
        }

        answer.setSchema(JocXmlEditor.getSchemaLocation(in.getObjectType(), null));
        answer.setConfiguration(configuration);
        answer.getState().setModified(modified);

        if (configuration == null) {
            answer.getState().setDeployed(false);
            answer.getState().getMessage().setMessage("No configuration found");
            answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_NO_CONFIGURATION_EXIST);
            answer.getState().setVersionState(ObjectVersionState.NO_CONFIGURATION_EXIST);
        } else {
            if (item == null) {
                answer.getState().setDeployed(false);
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

    private byte[] getLiveFile(JOCHotFolder hotFolder, ReadConfiguration in, ReadConfigurationAnswer answer) throws Exception {
        String file = JobSchedulerXmlEditor.getLivePathXml(in.getObjectType());
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
            answer.getState().setWarning(new AnswerMessage());
            answer.getState().getWarning().setCode(JocXmlEditor.ERROR_CODE_JOBSCHEDULER_NOT_CONNECTED);
            answer.getState().getWarning().setMessage(e.toString());
        } catch (JobSchedulerObjectNotExistException e) {
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s][%s]file not found", in.getJobschedulerId(), file));
            }
        }
        return result;
    }

    private ReadConfigurationAnswer handleOtherConfigurations(ReadConfiguration in, ReadConfigurationAnswer answer) throws Exception {

        if (SOSString.isEmpty(in.getName())) {
            answer.getState().setVersionState(ObjectVersionState.NO_CONFIGURATION_EXIST);
        } else {
            DBItemXmlEditorObject item = getItem(in.getJobschedulerId(), in.getObjectType().name(), in.getName());
            if (item == null) {
                answer.getState().setVersionState(ObjectVersionState.NO_CONFIGURATION_EXIST);
            } else {
                answer.setConfiguration(item.getConfiguration());
                answer.setSchema(item.getSchemaLocation());
                answer.getState().setModified(item.getModified());
                answer.getState().setVersionState(ObjectVersionState.LIVE_NOT_EXIST);
            }
        }
        return answer;
    }

    private ReadConfigurationAnswer createAnswer(ReadConfiguration in) {
        ReadConfigurationAnswer answer = new ReadConfigurationAnswer();
        answer.setState(new ReadConfigurationAnswerState());
        answer.getState().setMessage(new AnswerMessage());
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

}
