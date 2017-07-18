package com.sos.joc.exceptions;


public class UnknownJobSchedulerMasterException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-404";

    public UnknownJobSchedulerMasterException() {
    }

    public UnknownJobSchedulerMasterException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public UnknownJobSchedulerMasterException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public UnknownJobSchedulerMasterException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public UnknownJobSchedulerMasterException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public UnknownJobSchedulerMasterException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public UnknownJobSchedulerMasterException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public UnknownJobSchedulerMasterException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
