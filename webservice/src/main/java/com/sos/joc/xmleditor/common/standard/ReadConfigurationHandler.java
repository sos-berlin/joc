package com.sos.joc.xmleditor.common.standard;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.common.ObjectVersionState;
import com.sos.joc.model.xmleditor.read.standard.ReadStandardConfigurationAnswer;
import com.sos.joc.model.xmleditor.read.standard.ReadStandardConfigurationAnswerState;

public class ReadConfigurationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadConfigurationHandler.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    private boolean deployed;
    private JOCHotFolder hotFolder;
    private ReadStandardConfigurationAnswer answer;
    private ReadConfigurationItem live;
    private ReadConfigurationItem draft;
    private ReadConfigurationItem current;
    private URI schema;

    public ReadConfigurationHandler(JOCResourceImpl resource) {
        hotFolder = new JOCHotFolder(resource);
        answer = new ReadStandardConfigurationAnswer();
        answer.setState(new ReadStandardConfigurationAnswerState());
        answer.getState().setMessage(new AnswerMessage());
    }

    public void readCurrent(DBItemXmlEditorObject item, String jobschedulerId, ObjectType type, boolean forceLive) throws Exception {
        readLive(item, jobschedulerId, type);

        draft = new ReadConfigurationItem(type, schema);
        current = new ReadConfigurationItem(type, schema);

        if (item != null) {
            draft.set(item.getConfigurationDraft(), item.getConfigurationDraftJson(), item.getModified());
        }
        if (forceLive || draft.getConfiguration() == null) {
            current.set(live);
        }
        if (current.getConfiguration() == null) {
            current.set(draft);
        }
    }

    public void readLive(DBItemXmlEditorObject item, String jobschedulerId, ObjectType type) throws Exception {
        schema = JocXmlEditor.getSchemaURI(type);
        String file = JocXmlEditor.getLivePathXml(type);
        if (isDebugEnabled) {
            LOGGER.debug(String.format("[%s][%s]get file...", jobschedulerId, file));
        }

        if (item != null && item.getDeployed() != null) {
            deployed = true;
        }

        byte[] liveFile = null;
        try {
            liveFile = hotFolder.getFile(file);
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s][%s]%s bytes", jobschedulerId, liveFile == null ? "null" : liveFile.length));
            }
        } catch (JobSchedulerConnectionRefusedException e) {
            LOGGER.warn(String.format("[%s]JobScheduler could't be connected", jobschedulerId), e);
            answer.setWarning(new AnswerMessage());
            answer.getWarning().setCode(JocXmlEditor.ERROR_CODE_JOBSCHEDULER_NOT_CONNECTED);
            answer.getWarning().setMessage(e.toString());
        } catch (JobSchedulerObjectNotExistException e) {
            if (isDebugEnabled) {
                LOGGER.debug(String.format("[%s][%s]file not found", jobschedulerId, file));
            }
        } catch (JocException e) {
            throw e;
        }

        live = new ReadConfigurationItem(type, schema);
        if (liveFile == null) {
            if (deployed) {
                if (answer.getWarning() == null) { // not found
                    deployed = false;
                } else { // connection refused
                    live.set(item.getConfigurationDeployed(), item.getConfigurationDeployedJson(), item.getDeployed());
                }
            }
        } else {
            deployed = true;
            live.set(new String(liveFile, JocXmlEditor.CHARSET), null, hotFolder.getLastModifiedDate());
        }

        draft = new ReadConfigurationItem(type, schema);
        current = new ReadConfigurationItem(type, schema);
        current.set(live);

        if (deployed) {
            if (item != null && item.getConfigurationDraft() != null) {
                deployed = false;
            }
        }

        answer.setSchema(schema.toString());
        answer.getState().setDeployed(deployed);
    }

    public ReadStandardConfigurationAnswer getAnswer() {
        answer.setConfiguration(current.getConfiguration());
        answer.setConfigurationJson(current.getConfigurationJson());
        answer.setRecreateJson(current.recreateJson());
        answer.setModified(current.getModified());

        if (answer.getConfiguration() == null) {
            answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_NO_CONFIGURATION_EXIST);
            answer.getState().getMessage().setCode(JocXmlEditor.CODE_NO_CONFIGURATION_EXIST);
            answer.getState().setVersionState(ObjectVersionState.NO_CONFIGURATION_EXIST);
        } else {
            if (draft.getConfiguration() == null) {
                answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_DRAFT_NOT_EXIST);
                answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_NOT_EXIST);
                answer.getState().setVersionState(ObjectVersionState.DRAFT_NOT_EXIST);
            } else {
                if (live.getConfiguration() == null) {
                    answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_LIVE_NOT_EXIST);
                    answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_NOT_EXIST);
                    answer.getState().setVersionState(ObjectVersionState.LIVE_NOT_EXIST);
                } else {
                    if (live.getModified().after(draft.getModified())) {
                        answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_LIVE_IS_NEWER);
                        answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_IS_NEWER);
                        answer.getState().setVersionState(ObjectVersionState.LIVE_IS_NEWER);
                    } else {
                        answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_DRAFT_IS_NEWER);
                        answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_IS_NEWER);
                        answer.getState().setVersionState(ObjectVersionState.DRAFT_IS_NEWER);
                    }
                }
            }
        }
        return answer;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public JOCHotFolder getHotFolder() {
        return hotFolder;
    }
}
