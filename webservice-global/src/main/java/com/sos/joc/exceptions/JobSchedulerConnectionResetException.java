package com.sos.joc.exceptions;


public class JobSchedulerConnectionResetException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-402";

    public JobSchedulerConnectionResetException() {
    }

    public JobSchedulerConnectionResetException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JobSchedulerConnectionResetException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerConnectionResetException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JobSchedulerConnectionResetException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerConnectionResetException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JobSchedulerConnectionResetException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerConnectionResetException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
