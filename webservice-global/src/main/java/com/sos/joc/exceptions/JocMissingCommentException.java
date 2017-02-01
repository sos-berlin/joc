package com.sos.joc.exceptions;


public class JocMissingCommentException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-300";

    public JocMissingCommentException() {
        super(new JocError(ERROR_CODE, "missing comment"));
    }

    public JocMissingCommentException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JocMissingCommentException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocMissingCommentException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JocMissingCommentException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocMissingCommentException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JocMissingCommentException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocMissingCommentException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
