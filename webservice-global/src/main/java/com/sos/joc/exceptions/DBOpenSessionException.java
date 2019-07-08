package com.sos.joc.exceptions;


public class DBOpenSessionException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-421";

    public DBOpenSessionException() {
    }

    public DBOpenSessionException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public DBOpenSessionException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public DBOpenSessionException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public DBOpenSessionException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public DBOpenSessionException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public DBOpenSessionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public DBOpenSessionException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
