package com.sos.joc.exceptions;


public class UnknownJobSchedulerAgentException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-403";

    public UnknownJobSchedulerAgentException() {
    }

    public UnknownJobSchedulerAgentException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public UnknownJobSchedulerAgentException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public UnknownJobSchedulerAgentException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public UnknownJobSchedulerAgentException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public UnknownJobSchedulerAgentException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public UnknownJobSchedulerAgentException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public UnknownJobSchedulerAgentException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
