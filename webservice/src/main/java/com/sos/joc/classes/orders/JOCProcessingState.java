package com.sos.joc.classes.orders;

import org.apache.log4j.Logger;

import com.sos.joc.model.job.ProcessingState;

public class JOCProcessingState {
    private static final Logger LOGGER = Logger.getLogger(JOCProcessingState.class);

    public ProcessingState getProcessingState() {
        return processingState;
    }

    private ProcessingState processingState;

    public JOCProcessingState() {
        super();
        processingState = new ProcessingState();
    }

    public void setSeverity(int severity) {
        processingState.setSeverity(severity);
    }

    public void setText(ProcessingState.Text text) {
        processingState.setText(text);
    }

    public void setText(String s) {
        try {
            ProcessingState.Text text = ProcessingState.Text.fromValue(s);
            setText(text);
        } catch (IllegalArgumentException e) {
            LOGGER.error("illegal argument:" + s);
        }
    }

    public ProcessingState.Text getText(String s) {
        try {
            return ProcessingState.Text.fromValue(s);
        } catch (IllegalArgumentException e) {
            LOGGER.error("illegal argument:" + s);
        }

        return null;
    }

    public boolean isSetLock(String actProcessingState) {
        return ProcessingState.Text.WAITING_FOR_LOCK.equals(getText(actProcessingState));
    }

    public boolean isSetNextStartTime(String actProcessingState) {
        return (ProcessingState.Text.PENDING.equals(getText(actProcessingState)) || ProcessingState.Text.JOB_NOT_IN_PERIOD.equals(getText(actProcessingState))
                || ProcessingState.Text.NODE_DELAY.equals(getText(actProcessingState)));
    }

    public boolean isSetStartedAt(String actProcessingState) {
        return !ProcessingState.Text.PENDING.equals(getText(actProcessingState));
    }

    public boolean isSetHistoryId(String actProcessingState) {
        return !ProcessingState.Text.PENDING.equals(getText(actProcessingState));
    }

    public boolean isSetProcessClass(String actProcessingState) {
        return ProcessingState.Text.WAITING_FOR_PROCESS.equals(getText(actProcessingState)) || ProcessingState.Text.WAITING_FOR_AGENT.equals(getText(actProcessingState));
    }

    public boolean isSetTaskId(String actProcessingState) {
        return ProcessingState.Text.RUNNING.equals(getText(actProcessingState));
    }

    public boolean isSetProcessedBy(String actProcessingState) {
        return ProcessingState.Text.RUNNING.equals(getText(actProcessingState)) || ProcessingState.Text.BLACKLIST.equals(getText(actProcessingState));
    }

    public boolean isSetSetBack(String actProcessingState) {
        return ProcessingState.Text.SETBACK.equals(getText(actProcessingState));
    }

}
