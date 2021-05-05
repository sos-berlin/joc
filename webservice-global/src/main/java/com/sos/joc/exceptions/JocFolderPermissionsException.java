package com.sos.joc.exceptions;


public class JocFolderPermissionsException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-419";

    public JocFolderPermissionsException() {
    }

    public JocFolderPermissionsException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JocFolderPermissionsException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocFolderPermissionsException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JocFolderPermissionsException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocFolderPermissionsException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JocFolderPermissionsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocFolderPermissionsException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
