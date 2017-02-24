package com.sos.joc.exceptions;

import java.util.Date;

public class JobSchedulerObjectNotExistException extends JobSchedulerBadRequestException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-161";
    private Date surveyDate = null;

    public JobSchedulerObjectNotExistException() {
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public JobSchedulerObjectNotExistException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JobSchedulerObjectNotExistException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerObjectNotExistException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JobSchedulerObjectNotExistException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerObjectNotExistException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JobSchedulerObjectNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerObjectNotExistException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
