package com.sos.joc.exceptions;


public class JocConfigurationException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-003";

    public JocConfigurationException() {
    }

    public JocConfigurationException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JocConfigurationException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocConfigurationException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JocConfigurationException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocConfigurationException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JocConfigurationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocConfigurationException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
