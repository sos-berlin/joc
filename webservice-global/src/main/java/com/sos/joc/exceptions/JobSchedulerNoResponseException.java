package com.sos.joc.exceptions;

import java.util.Date;

public class JobSchedulerNoResponseException extends JocException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE = "JOC-401";
    private Date surveyDate = null;

    public JobSchedulerNoResponseException() {
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public JobSchedulerNoResponseException(Throwable cause) {
        super(new JocError(ERROR_CODE, cause.getMessage()), cause);
    }

    public JobSchedulerNoResponseException(String message) {
        super(new JocError(ERROR_CODE, message));
    }
    
    public JobSchedulerNoResponseException(JocError error) {
        super(updateJocErrorCode(error, ERROR_CODE));
    }

    public JobSchedulerNoResponseException(String message, Throwable cause) {
        super(new JocError(ERROR_CODE, message), cause);
    }

    public JobSchedulerNoResponseException(JocError error, Throwable cause) {
        super(updateJocErrorCode(error, ERROR_CODE), cause);
    }

    public JobSchedulerNoResponseException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(new JocError(ERROR_CODE, message), cause, enableSuppression, writableStackTrace);
    }
    
    public JobSchedulerNoResponseException(JocError error, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(updateJocErrorCode(error, ERROR_CODE), cause, enableSuppression, writableStackTrace);
    }

}
