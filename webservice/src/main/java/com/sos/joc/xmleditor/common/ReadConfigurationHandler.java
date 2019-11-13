package com.sos.joc.xmleditor.common;

import java.util.Date;

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

    boolean deployed;
    JOCHotFolder hotFolder;
    ReadStandardConfigurationAnswer answer;
    String live;
    Date liveModified;
    String draft;
    Date draftModified;
    String current;
    Date currentModified;

    public ReadConfigurationHandler(JOCResourceImpl resource) {
        hotFolder = new JOCHotFolder(resource);
        answer = new ReadStandardConfigurationAnswer();
        answer.setState(new ReadStandardConfigurationAnswerState());
        answer.getState().setMessage(new AnswerMessage());
    }

    public void readCurrent(DBItemXmlEditorObject item, String jobschedulerId, ObjectType type, boolean forceLive) throws Exception {
        readLive(item, jobschedulerId, type);
        current = null;
        currentModified = null;
        if (item != null) {
            draft = item.getConfigurationDraft();
            draftModified = item.getModified();
        }
        if (forceLive || draft == null) {
            current = live;
            currentModified = liveModified;
        }
        if (current == null) {
            current = draft;
            currentModified = draftModified;
        }
    }

    public void readLive(DBItemXmlEditorObject item, String jobschedulerId, ObjectType type) throws Exception {
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

        if (liveFile == null) {
            if (deployed) {
                if (answer.getWarning() == null) { // not found
                    deployed = false;
                } else { // connection refused
                    live = item.getConfigurationDeployed();
                    liveModified = item.getDeployed();
                }
            }
        } else {
            deployed = true;
            live = new String(liveFile, JocXmlEditor.CHARSET);
            liveModified = hotFolder.getLastModifiedDate();
        }
        current = live;
        currentModified = liveModified;

        if (deployed) {
            if (item != null && item.getConfigurationDraft() != null) {
                deployed = false;
            }
        }

        answer.setSchema(JocXmlEditor.getSchemaURI(type).toString());
        answer.getState().setDeployed(deployed);
    }

    public ReadStandardConfigurationAnswer getAnswer() {
        answer.setConfiguration(current);
        answer.setModified(currentModified);

        if (answer.getConfiguration() == null) {
            answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_NO_CONFIGURATION_EXIST);
            answer.getState().getMessage().setCode(JocXmlEditor.CODE_NO_CONFIGURATION_EXIST);
            answer.getState().setVersionState(ObjectVersionState.NO_CONFIGURATION_EXIST);
        } else {
            if (draft == null) {
                answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_DRAFT_NOT_EXIST);
                answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_NOT_EXIST);
                answer.getState().setVersionState(ObjectVersionState.DRAFT_NOT_EXIST);
            } else {
                if (live == null) {
                    answer.getState().getMessage().setMessage(JocXmlEditor.MESSAGE_LIVE_NOT_EXIST);
                    answer.getState().getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_NOT_EXIST);
                    answer.getState().setVersionState(ObjectVersionState.LIVE_NOT_EXIST);
                } else {
                    if (liveModified.after(draftModified)) {
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

    public String getLive() {
        return live;
    }

    public Date getLiveModified() {
        return liveModified;
    }

    public String getDraft() {
        return draft;
    }

    public Date getDraftModified() {
        return draftModified;
    }

    public String getCurrent() {
        return current;
    }

    public Date getCurrentModified() {
        return currentModified;
    }

}
