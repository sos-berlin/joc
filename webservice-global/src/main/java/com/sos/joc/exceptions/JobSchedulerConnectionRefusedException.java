package com.sos.joc.exceptions;


public class JobSchedulerConnectionRefusedException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-500";

    public JobSchedulerConnectionRefusedException() {
    }

    public JobSchedulerConnectionRefusedException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerConnectionRefusedException(JocError error) {
        super(error);
    }

    public JobSchedulerConnectionRefusedException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerConnectionRefusedException(JocError error, Throwable cause) {
        super(error, cause);
    }

    public JobSchedulerConnectionRefusedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerConnectionRefusedException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(error, cause, enableSuppression, writableStackTrace);
    }

}
