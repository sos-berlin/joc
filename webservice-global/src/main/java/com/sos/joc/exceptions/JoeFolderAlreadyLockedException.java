package com.sos.joc.exceptions;


public class JoeFolderAlreadyLockedException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-430";

    public JoeFolderAlreadyLockedException() {
    }

    public JoeFolderAlreadyLockedException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JoeFolderAlreadyLockedException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JoeFolderAlreadyLockedException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JoeFolderAlreadyLockedException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JoeFolderAlreadyLockedException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JoeFolderAlreadyLockedException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JoeFolderAlreadyLockedException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
