package com.sos.joc.exceptions;


public class DBMissingDataException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-408";

    public DBMissingDataException() {
    }

    public DBMissingDataException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public DBMissingDataException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public DBMissingDataException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public DBMissingDataException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public DBMissingDataException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public DBMissingDataException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public DBMissingDataException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
