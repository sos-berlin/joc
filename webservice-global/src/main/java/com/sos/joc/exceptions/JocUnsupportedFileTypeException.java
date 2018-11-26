package com.sos.joc.exceptions;


public class JocUnsupportedFileTypeException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-415";

    public JocUnsupportedFileTypeException() {
    }

    public JocUnsupportedFileTypeException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JocUnsupportedFileTypeException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocUnsupportedFileTypeException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JocUnsupportedFileTypeException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocUnsupportedFileTypeException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JocUnsupportedFileTypeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocUnsupportedFileTypeException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
