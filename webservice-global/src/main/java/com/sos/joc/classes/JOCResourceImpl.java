package com.sos.joc.classes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;


public class JOCResourceImpl {
    private static final Logger LOGGER = Logger.getLogger(JOCResourceImpl.class);

    private static final String JOC_420 = "JOC-420";
    private static final String COULD_NOT_GET_ERROR_MESSAGE_FROM_JOB_SCHEDULER_ANSWER = "Could not get error message from JobScheduler answer";
    private static final String SPOOLER_ANSWER_ERROR = "//spooler/answer/ERROR";
    protected DBItemInventoryInstance dbItemInventoryInstance;

    private String accessToken;
    protected JobSchedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;
    protected List<Err419> listOfErrors;
    protected JocError jocError;

    protected SOSPermissionJocCockpit getPermissons(String accessToken) throws JocException {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        if (jobschedulerUser.getSosShiroCurrentUser() == null){
            jocError = new JocError();
            jocError.setCode(WebserviceConstants.NO_USER_WITH_ACCESS_TOKEN);
            jocError.setMessage("No user logged in with accessToken: " + accessToken);
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
            dateString = dateString.trim().replaceFirst("^(\\d{4}-\\d{2}-\\d{2}) ", "$1T");
            date = Date.from(Instant.parse(dateString));
        } catch (Exception e) {
            //TODO what should we do with this exception?
            //jocError = new JocError("JOC-420","Could not parse date: " + dateString);
            LOGGER.warn("Could not parse date: " + dateString, e);
        }
        return date;
    }

    public Date getDateFromTimestamp(Long timeStamp) {
        Instant fromEpochMilli = Instant.ofEpochMilli(timeStamp/1000);
        return Date.from(fromEpochMilli);
    }

    public JOCDefaultResponse init(String schedulerId, boolean permission) throws Exception {
        JOCDefaultResponse jocDefaultResponse = null;

        try {
            if (jobschedulerUser.getSosShiroCurrentUser().getAuthorization() != null) {
                Session curSession = jobschedulerUser.getSosShiroCurrentUser().getCurrentSubject().getSession(false);
                if (curSession != null) {
                    curSession.touch();
                } else {
                    throw new org.apache.shiro.session.InvalidSessionException("Session doesn't exist");
                }
            }
            
            if (!jobschedulerUser.isAuthenticated()) {
                return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
            }
        } catch (org.apache.shiro.session.InvalidSessionException e) {
            return JOCDefaultResponse.responseStatus440(JOCDefaultResponse.getError401Schema(jobschedulerUser, e.getMessage()));
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

        if (!permission) {
            return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
        }

        if (schedulerId == null) {
            return JOCDefaultResponse.responseStatusJSError(new JocMissingRequiredParameterException("undefined 'jobschedulerId'"));
        }
        if (!"".equals(schedulerId)) {
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));
            if (dbItemInventoryInstance == null) {
                String errMessage = String.format("jobschedulerId %s not found in table %s", schedulerId, DBLayer.TABLE_INVENTORY_INSTANCES);
                return JOCDefaultResponse.responseStatusJSError(new DBInvalidDataException(errMessage));
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
        if (paramVal == null) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }
    
    public boolean checkRequiredParameter(String paramKey, Integer paramVal) throws JocMissingRequiredParameterException {
        return checkRequiredParameter(paramKey,String.valueOf(paramVal));
    }
    
    public List<Err419> addError(List<Err419> listOfErrors, JOCXmlCommand jocXmlCommand, String path) {
        if (listOfErrors == null) {
            listOfErrors = new ArrayList<Err419>();
        }
        Err419 error = new Err419();
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
            return p.matcher(path).find();
        } else {
            return true;
        }
    }
    
}
