package com.sos.joc.exceptions;


public class DBInvalidDataException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-407";

    public DBInvalidDataException() {
    }

    public DBInvalidDataException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public DBInvalidDataException(JocError error) {
        super(error);
    }

    public DBInvalidDataException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public DBInvalidDataException(JocError error, Throwable cause) {
        super(error, cause);
    }

    public DBInvalidDataException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public DBInvalidDataException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(error, cause, enableSuppression, writableStackTrace);
    }

}
