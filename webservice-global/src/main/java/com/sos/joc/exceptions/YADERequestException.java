package com.sos.joc.exceptions;


public class YADERequestException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-330";

    public YADERequestException() {
        super(new JocError(ERROR_CODE, "missing comment"));
    }

    public YADERequestException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public YADERequestException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public YADERequestException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public YADERequestException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public YADERequestException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public YADERequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public YADERequestException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
