package com.sos.joc.exceptions;


public class JobSchedulerInvalidResponseDataException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-401";

    public JobSchedulerInvalidResponseDataException() {
    }

    public JobSchedulerInvalidResponseDataException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerInvalidResponseDataException(JocError error) {
        super(error);
    }

    public JobSchedulerInvalidResponseDataException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerInvalidResponseDataException(JocError error, Throwable cause) {
        super(error, cause);
    }

    public JobSchedulerInvalidResponseDataException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerInvalidResponseDataException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(error, cause, enableSuppression, writableStackTrace);
    }

}
