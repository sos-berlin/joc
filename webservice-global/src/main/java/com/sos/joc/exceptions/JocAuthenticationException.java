package com.sos.joc.exceptions;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;

public class JocAuthenticationException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-401";
    private SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = null;

    public JocAuthenticationException() {
    }

    public JocAuthenticationException(SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer) {
        super(new JocError(ERROR_CODE, sosShiroCurrentUserAnswer.getMessage()));
        this.sosShiroCurrentUserAnswer = sosShiroCurrentUserAnswer;
    }
    
    public JocAuthenticationException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JocAuthenticationException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocAuthenticationException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JocAuthenticationException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocAuthenticationException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JocAuthenticationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocAuthenticationException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }
    
    public SOSShiroCurrentUserAnswer getSosShiroCurrentUserAnswer() {
        return sosShiroCurrentUserAnswer;
    }

}
