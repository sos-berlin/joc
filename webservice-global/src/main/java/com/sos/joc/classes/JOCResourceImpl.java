package com.sos.joc.classes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Error;
import com.sos.joc.model.jobscheduler.State;


public class JOCResourceImpl {
    private static final Logger LOGGER = Logger.getLogger(JOCResourceImpl.class);

    private static final String JOC_420 = "JOC-420";
    private static final String COULD_NOT_GET_ERROR_MESSAGE_FROM_JOB_SCHEDULER_ANSWER = "Could not get error message from JobScheduler answer";
    private static final String SPOOLER_ANSWER_ERROR = "//spooler/answer/ERROR";
    protected static final String JOBSCHEDULER_DATE_FORMAT = "yyyy-mm-dd hh:mm:ss.SSS'Z'";
    protected static final String JOBSCHEDULER_DATE_FORMAT2 = "yyyy-mm-dd'T'hh:mm:ss.SSS'Z'";
    protected DBItemInventoryInstance dbItemInventoryInstance;

    private String accessToken;
    protected JobSchedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;
    protected List<Error> listOfErrors;
    protected JocError jocError;

    protected SOSPermissionJocCockpit getPermissons(String accessToken) throws JocException {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        if (jobschedulerUser.getSosShiroCurrentUser() == null){
            jocError = new JocError();
            jocError.setCode(WebserviceConstants.NO_USER_WITH_ACCESS_TOKEN);
            jocError.setMessage("No User logged in with accessToken: " + accessToken);
            throw new JocException(jocError);
        }
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public JobSchedulerUser getJobschedulerUser() {
        return jobschedulerUser;
    }

    public Date getDateFromString(String dateString) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT);
            date = formatter.parse(dateString);
        } catch (Exception e) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT2);
                date = formatter.parse(dateString);
            } catch (Exception ee) {
                jocError = new JocError();
                jocError.setCode("JOC-420");
                jocError.setMessage("Could not parse date: " + dateString);
            }
        }

        return date;
    }

    public Date getDateFromTimestamp(Long timeStamp) {
        Long time = new Long(timeStamp / 1000);
        Timestamp stamp = new Timestamp(time);
        Date date = new Date(stamp.getTime());
        return date;
    }

    public JOCDefaultResponse init(String schedulerId, boolean permission) throws Exception {
        JOCDefaultResponse jocDefaultResponse = null;
        if (jobschedulerUser.getSosShiroCurrentUser() != null) {
            jobschedulerUser.getSosShiroCurrentUser().getCurrentSubject().getSession().touch();
        }

        try {

            if (!jobschedulerUser.isAuthenticated()) {
                return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return JOCDefaultResponse.responseStatus440(JOCDefaultResponse.getError401Schema(jobschedulerUser, e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

        if (!permission) {
            return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
        }

        if (schedulerId == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("undefined '%1$s'", "jobschedulerId"));
        }
        if (!"".equals(schedulerId)) {
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));

            if (dbItemInventoryInstance == null) {
                return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s", schedulerId, DBLayer.TABLE_INVENTORY_INSTANCES));
            }
        }

        return jocDefaultResponse;
    }

    public JOCDefaultResponse init(String accessToken, String schedulerId, boolean permission) throws Exception {
        this.accessToken = accessToken;
        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        return init(schedulerId, permission);

    }

    public String normalizePath(String path) {
        return ("/" + path).replaceAll("//+", "/");
    }

    public boolean checkRequiredParameter(String paramKey, String paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    public boolean checkRequiredParameter(String paramKey, Long paramVal) throws JocMissingRequiredParameterException {
        return checkRequiredParameter(paramKey,String.valueOf(paramVal));
    }
    
    public boolean checkRequiredParameter(String paramKey, Integer paramVal) throws JocMissingRequiredParameterException {
        return checkRequiredParameter(paramKey,String.valueOf(paramVal));
    }
    
    public List<Error> addError(List<Error> listOfErrors, JOCXmlCommand jocXmlCommand, String path) {
        if (listOfErrors == null) {
            listOfErrors = new ArrayList<Error>();
        }
        Error error = new Error();
        try {
            jocXmlCommand.executeXPath(SPOOLER_ANSWER_ERROR);
        } catch (Exception e) {
            error.setCode(JOC_420);
            error.setMessage(COULD_NOT_GET_ERROR_MESSAGE_FROM_JOB_SCHEDULER_ANSWER);
        }
        String code = jocXmlCommand.getAttribute("code");
        if (code != null) {
            if (code != null && code.length() > 0) {
                error.setCode(code);
                error.setMessage(jocXmlCommand.getAttribute("text"));
            }
            error.setPath(path);
            error.setSurveyDate(jocXmlCommand.getSurveyDate());
            listOfErrors.add(error);
        }
        return listOfErrors;
    }

    protected State.Text getText(String jobschedulerState){
        try{
           return State.Text.fromValue(jobschedulerState.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            LOGGER.error("IllegalArgumentException: " + jobschedulerState,e); 
            return null;
        }
    }
    
    protected String getParent(String path){
        Path  p = Paths.get(path).getParent();
        if (p == null){
            return null;
        }else{
            return Paths.get(path).getParent().toString().replace('\\','/');
        }
    }
    
    protected boolean matchesRegex(Pattern p, String path) {
        if (p != null) {
            Matcher m = p.matcher(path);
            return m.matches();
        } else {
            return true;
        }
    }
    
}
