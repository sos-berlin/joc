package com.sos.joc.exceptions;

import java.util.Date;

public class JobSchedulerBadRequestException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-400";
    private Date surveyDate = null;

    public JobSchedulerBadRequestException() {
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public JobSchedulerBadRequestException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JobSchedulerBadRequestException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerBadRequestException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JobSchedulerBadRequestException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerBadRequestException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JobSchedulerBadRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerBadRequestException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
