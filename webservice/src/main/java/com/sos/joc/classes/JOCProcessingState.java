package com.sos.joc.classes;

import com.sos.joc.model.job.ProcessingState;

public class JOCProcessingState {
    public ProcessingState getProcessingState() {
        return processingState;
    }

    private ProcessingState processingState;

    public JOCProcessingState() {
        super();
        processingState = new ProcessingState();
    }

    public void setDescription(String description) {
        processingState.setDescription(description);
    }

    public void setSeverity(ProcessingState.Severity severity) {
        processingState.setSeverity(severity);
    }

    public void setText(ProcessingState.Text text) {
        processingState.setText(text);
    }

    public boolean isSetLock(String actProcessingState) {
        return ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.WAITING_FOR_LOCK);
    }

    public boolean isSetNextStartTime(String actProcessingState) {
        return (ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.PENDING) || ProcessingState.Text.fromValue(actProcessingState).equals(
                ProcessingState.Text.JOB_NOT_IN_PERIOD) || ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.NODE_DELAY));
    }

    public boolean isSetStartedAt(String actProcessingState) {
        return !ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.PENDING);
    }

    public boolean isSetHistoryId(String actProcessingState) {
        return !ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.PENDING);
    }

    public boolean isSetProcessClass(String actProcessingState) {
        return ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.WAITING_FOR_PROCESS) || ProcessingState.Text.fromValue(actProcessingState).equals(
                ProcessingState.Text.WAITING_FOR_AGENT);
    }

    public boolean isSetTaskId(String actProcessingState) {
        return ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.RUNNING);
    }

    public boolean isSetProcessedBy(String actProcessingState) {
        return ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.RUNNING) || ProcessingState.Text.fromValue(actProcessingState).equals(
                ProcessingState.Text.BACKLIST);
    }

    public boolean isSetSetBack(String actProcessingState) {
        return ProcessingState.Text.fromValue(actProcessingState).equals(ProcessingState.Text.SETBACK);
    }

}
