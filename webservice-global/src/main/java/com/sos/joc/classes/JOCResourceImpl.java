package com.sos.joc.classes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;

public class JOCResourceImpl {

    protected DBItemInventoryInstance dbItemInventoryInstance;
    protected JobSchedulerUser jobschedulerUser;
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCResourceImpl.class);
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);
    private String accessToken;

    private ObjectMapper mapper = new ObjectMapper();
    private JocError jocError = new JocError();

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
        updateUserInMetaInfo();
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    protected static Logger getAuditLogger() {
        return AUDIT_LOGGER;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public JobSchedulerUser getJobschedulerUser() {
        return jobschedulerUser;
    }

    public JocError getJocError() {
        return jocError;
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
        return init(accessToken, schedulerId, permission, false);
    }

    public JOCDefaultResponse init(String accessToken, String schedulerId, boolean permission, boolean withJobSchedulerDBCheck) throws Exception {
        this.accessToken = accessToken;
        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        return init(schedulerId, permission, withJobSchedulerDBCheck);
    }

    public JOCDefaultResponse init(String accessToken) throws Exception {
        this.accessToken = accessToken;
        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        return init401And440();
    }

    public String normalizePath(String path) {
        return ("/" + path.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
    }

    public String normalizePathForDB(String path) {
        return normalizePath(path).replaceFirst("^/", "");
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
            return p.toString().replace('\\', '/');
        }
    }

    protected boolean matchesRegex(Pattern p, String path) {
        if (p != null) {
            return p.matcher(path).find();
        } else {
            return true;
        }
    }

    public void initLogging(String apiCall, Object body) {
        LOGGER.debug(apiCall);
        jocError.addMetaInfoOnTop(getMetaInfo(apiCall, body));
    }

    public String[] getMetaInfo(String apiCall, Object body) {
        String[] strings = new String[3];
        if (apiCall == null) {
            apiCall = "-";
        }
        strings[0] = "\nREQUEST: " + apiCall;
        strings[1] = "PARAMS: " + getJsonString(body);
        try {
            strings[2] = "USER: " + jobschedulerUser.getSosShiroCurrentUser().getUsername();
        } catch (Exception e) {
            strings[2] = "USER: -";
        }
        return strings;
    }

    public void logAuditMessage() {
        logAuditMessage(null);
    }

    public void logAuditMessage(Object body) {
        try {
            List<String> metaInfo = jocError.getMetaInfo();
            String params = (body == null) ? metaInfo.get(1) : "PARAMS: " + getJsonString(body);
            AUDIT_LOGGER.info(String.format("%1$s%n%2$s%n%3$s", metaInfo.get(2), metaInfo.get(0).trim(), params));
        } catch (Exception e) {
            LOGGER.error("Cannot write to audit log file", e);
        }
    }

    private String getJsonString(Object body) {
        if (body != null) {
            try {
                return mapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                return body.toString();
            }
        }
        return "-";
    }

    private JOCDefaultResponse init(String schedulerId, boolean permission, boolean withJobSchedulerDBCheck) throws JocException {
        JOCDefaultResponse jocDefaultResponse = init401And440();
        if (!permission) {
            return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
        }
        if (schedulerId == null) {
            throw new JocMissingRequiredParameterException("undefined 'jobschedulerId'");
        }
        checkConnection(Globals.sosHibernateConnection);
        if (!"".equals(schedulerId)) {
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));
            if (withJobSchedulerDBCheck) {
                checkConnection(Globals.getConnection(schedulerId));
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

    private void checkConnection(SOSHibernateConnection connection) throws DBConnectionRefusedException {
        try {
            connection.beginTransaction();
            connection.rollback();
        } catch (Exception ex) {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                connection.connect();
            } catch (Exception e) {
                throw new DBConnectionRefusedException(e);
            }
        }
    }

    private void updateUserInMetaInfo() {
        try {
            String userMetaInfo = "USER: " + jobschedulerUser.getSosShiroCurrentUser().getUsername();
            List<String> metaInfo = getJocError().getMetaInfo();
            if (metaInfo.size() > 2) {
                metaInfo.remove(2);
                metaInfo.add(2, userMetaInfo);
            }
        } catch (Exception e) {
        }
    }

}
