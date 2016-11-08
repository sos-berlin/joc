package com.sos.joc.exceptions;


public class DBInvalidDataException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-407";

    public DBInvalidDataException() {
    }

    public DBInvalidDataException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public DBInvalidDataException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public DBInvalidDataException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public DBInvalidDataException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public DBInvalidDataException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public DBInvalidDataException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public DBInvalidDataException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
