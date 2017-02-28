package com.sos.joc.exceptions;


public class DBConnectionRefusedException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-409";

    public DBConnectionRefusedException() {
    }

    public DBConnectionRefusedException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public DBConnectionRefusedException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public DBConnectionRefusedException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public DBConnectionRefusedException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public DBConnectionRefusedException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public DBConnectionRefusedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public DBConnectionRefusedException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
