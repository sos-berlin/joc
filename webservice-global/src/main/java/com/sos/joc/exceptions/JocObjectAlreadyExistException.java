package com.sos.joc.exceptions;

import java.util.Date;

public class JocObjectAlreadyExistException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-171";
    private Date surveyDate = null;

    public JocObjectAlreadyExistException() {
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public JocObjectAlreadyExistException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JocObjectAlreadyExistException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JocObjectAlreadyExistException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JocObjectAlreadyExistException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JocObjectAlreadyExistException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JocObjectAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JocObjectAlreadyExistException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
