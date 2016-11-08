package com.sos.joc.exceptions;


public class JobSchedulerModifyObjectException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-419";

    public JobSchedulerModifyObjectException() {
    }

    public JobSchedulerModifyObjectException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JobSchedulerModifyObjectException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerModifyObjectException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JobSchedulerModifyObjectException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerModifyObjectException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JobSchedulerModifyObjectException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerModifyObjectException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
