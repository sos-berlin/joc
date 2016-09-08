package com.sos.joc.exceptions;


public class JobSchedulerBadRequestException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-400";

    public JobSchedulerBadRequestException() {
    }

    public JobSchedulerBadRequestException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerBadRequestException(JocError error) {
        super(error);
    }

    public JobSchedulerBadRequestException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerBadRequestException(JocError error, Throwable cause) {
        super(error, cause);
    }

    public JobSchedulerBadRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerBadRequestException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(error, cause, enableSuppression, writableStackTrace);
    }

}
