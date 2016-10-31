package com.sos.joc.classes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;

public class JOCResourceImpl {

    protected DBItemInventoryInstance dbItemInventoryInstance;
    protected JobSchedulerUser jobschedulerUser;
    protected JobSchedulerIdentifier jobSchedulerIdentifier;
    private String accessToken;
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCResourceImpl.class);
    private ObjectMapper mapper = new ObjectMapper();
    private static final String DBITEM_INVENTORY_INSTANCES = DBItemInventoryInstance.class.getSimpleName();


    protected SOSPermissionJocCockpit getPermissons(String accessToken) throws JocException {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        if (jobschedulerUser.getSosShiroCurrentUser() == null) {
            JocError jocError = new JocError();
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
            // TODO what should we do with this exception?
            // jocError = new JocError("JOC-420","Could not parse date: " +
            // dateString);
            LOGGER.warn("Could not parse date: " + dateString, e);
        }
        return date;
    }

    public Date getDateFromTimestamp(Long timeStamp) {
        Instant fromEpochMilli = Instant.ofEpochMilli(timeStamp / 1000);
        return Date.from(fromEpochMilli);
    }

    public JOCDefaultResponse init(String accessToken, String schedulerId, boolean permission) throws Exception {
        this.accessToken = accessToken;
        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        return init(schedulerId, permission);
    }

    public JOCDefaultResponse init(String accessToken) throws Exception {
        this.accessToken = accessToken;
        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        return init401And440();
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
        return checkRequiredParameter(paramKey, String.valueOf(paramVal));
    }

    protected String getParent(String path) {
        Path p = Paths.get(path).getParent();
        if (p == null) {
            return null;
        } else {
            return Paths.get(path).getParent().toString().replace('\\', '/');
        }
    }

    protected boolean matchesRegex(Pattern p, String path) {
        if (p != null) {
            return p.matcher(path).find();
        } else {
            return true;
        }
    }

    public String[] getMetaInfo(String apiCall, Object body) {
        String[] strings = new String[3];
        if (apiCall == null) {
            apiCall = "-";
        }
        strings[0] = "\nREQUEST: " + apiCall;
        if (body != null) {
            try {
                strings[1] = "PARAMS: " + mapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                strings[1] = "PARAMS: " + body.toString();
            }
        } else {
            strings[1] = "PARAMS: -";
        }
        try {
            strings[2] = "USER: " + jobschedulerUser.getSosShiroCurrentUser().getUsername();
        } catch (Exception e) {
            strings[2] = "USER: -";
        }
        return strings;
    }

    private JOCDefaultResponse init(String schedulerId, boolean permission) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init401And440();
        if (!permission) {
            return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
        }
        if (schedulerId == null) {
            return JOCDefaultResponse.responseStatusJSError(new JocMissingRequiredParameterException("undefined 'jobschedulerId'"));
        }
        
        String checkConnectionResult = checkConnection(schedulerId);
        if (!"".equals(checkConnectionResult)) {
            JocError jocError = new JocError(String.format("Error with database connection: %s", checkConnectionResult), WebserviceConstants.DB_CONNECTION_ERROR);
            throw new JocException(jocError);
        }

        if (!"".equals(schedulerId)) {
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));
            if (dbItemInventoryInstance == null) {
                String errMessage = String.format("jobschedulerId %s not found in table %s", schedulerId, DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new DBInvalidDataException(errMessage);
            }
        }
        

        return jocDefaultResponse;
    }

    private JOCDefaultResponse init401And440() {
        if (!jobschedulerUser.isAuthenticated()) {
            return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
        }
        return null;
    }
    
    private String checkConnection(SOSHibernateConnection connection) {
        try {
            connection.beginTransaction();
            connection.rollback();
            return "";
        } catch (Exception ex) {

            if (connection != null) {
                connection.disconnect();
            }
            try {
                connection.connect();
                return "";
            } catch (Exception e) {
                return (e.getMessage());
            }

        }
    }

    private String checkConnection(String schedulerId){
        String s;
        try {
            s = checkConnection(Globals.sosHibernateConnection) + checkConnection(Globals.getConnection(schedulerId));
        } catch (Exception e) {
            return e.getMessage();
        }
        if (!"".equals(s)){
            return s;
        }
        return "";
      }    
 
    
}
