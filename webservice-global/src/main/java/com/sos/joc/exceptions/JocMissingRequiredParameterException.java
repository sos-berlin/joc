package com.sos.joc.exceptions;


public class JocMissingRequiredParameterException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-406";

    public JocMissingRequiredParameterException() {
    }

    public JocMissingRequiredParameterException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocMissingRequiredParameterException(JocError error) {
        super(error);
    }

    public JocMissingRequiredParameterException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocMissingRequiredParameterException(JocError error, Throwable cause) {
        super(error, cause);
    }

    public JocMissingRequiredParameterException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocMissingRequiredParameterException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(error, cause, enableSuppression, writableStackTrace);
    }

}
