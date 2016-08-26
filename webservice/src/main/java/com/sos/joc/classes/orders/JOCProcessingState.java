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
        }catch (IllegalArgumentException e){
            LOGGER.error("illegal argument:" + s);
        }
     }
    
    public ProcessingState.Text getText(String s) {
        try {
            return ProcessingState.Text.fromValue(s);
        }catch (IllegalArgumentException e){
            LOGGER.error("illegal argument:" + s);
        }

        return null;     }

    public boolean isSetLock(String actProcessingState) {
        return ProcessingState.Text.waiting_for_lock.equals(getText(actProcessingState));
    }

    public boolean isSetNextStartTime(String actProcessingState) {
        return (ProcessingState.Text.pending.equals(getText(actProcessingState)) || 
                ProcessingState.Text.job_not_in_period.equals(getText(actProcessingState)) || 
                ProcessingState.Text.node_delay.equals(getText(actProcessingState)));
    }

    public boolean isSetStartedAt(String actProcessingState) {
        return !ProcessingState.Text.pending.equals(getText(actProcessingState));
    }

    public boolean isSetHistoryId(String actProcessingState) {
        return !ProcessingState.Text.pending.equals(getText(actProcessingState));
    }

    public boolean isSetProcessClass(String actProcessingState) {
        return ProcessingState.Text.waiting_for_process.equals(getText(actProcessingState)) || 
               ProcessingState.Text.waiting_for_agent.equals(getText(actProcessingState));
    }

    public boolean isSetTaskId(String actProcessingState) {
        return ProcessingState.Text.running.equals(getText(actProcessingState));
    }

    public boolean isSetProcessedBy(String actProcessingState) {
        return ProcessingState.Text.running.equals(getText(actProcessingState)) || 
                ProcessingState.Text.backlist.equals(getText(actProcessingState));
    }

    public boolean isSetSetBack(String actProcessingState) {
        return ProcessingState.Text.setback.equals(getText(actProcessingState));
    }

}
