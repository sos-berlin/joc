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
import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.audit.IAuditLog;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.NoUserWithAccessTokenException;

public class JOCResourceImpl {

    protected DBItemInventoryInstance dbItemInventoryInstance;
    protected JobSchedulerUser jobschedulerUser;
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCResourceImpl.class);
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);
    private String accessToken;
    private String jobschedulerId;
    private String request;

    private ObjectMapper mapper = new ObjectMapper();
    private JocError jocError = new JocError();

    private void initGetPermissions(String accessToken) throws NoUserWithAccessTokenException {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        if (jobschedulerUser.getSosShiroCurrentUser() == null) {
            throw new NoUserWithAccessTokenException("No user logged in with accessToken: " + accessToken);
        }
        updateUserInMetaInfo();
    }

    protected SOSPermissionJocCockpit getPermissonsJocCockpit(String accessToken) throws JocException {
        initGetPermissions(accessToken);
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit();
    }

    protected SOSPermissionCommands getPermissonsCommands(String accessToken) throws JocException {
        initGetPermissions(accessToken);
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionCommands();
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
        if (path == null) {
            return null;
        }
        return ("/" + path.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
    }

    public String normalizeFolder(String path) {
        if (path == null) {
            return null;
        }
        return ("/" + path.trim()).replaceAll("//+", "/");
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
        request = apiCall;
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

    public void logAuditMessage(IAuditLog body) {
        try {
            List<String> metaInfo = jocError.getMetaInfo();
            String params = (body == null) ? metaInfo.get(1) : "PARAMS: " + getJsonString(body);
            String comment = (body == null) ? "-" : body.getComment();
            comment = "COMMENT: " + comment;
            AUDIT_LOGGER.info(String.format("%1$s - %2$s - %3$s - %4$s", metaInfo.get(2), metaInfo.get(0).trim(), params, comment));
        } catch (Exception e) {
            LOGGER.error("Cannot write to audit log file", e);
        }
    }
    
    public void storeAuditLogEntry(IAuditLog body) {
        String auditParams = getJsonString(body);
        String auditAccount = null;
        try {
            auditAccount = jobschedulerUser.getSosShiroCurrentUser().getUsername();
        } catch (Exception e) {}
        DBItemAuditLog auditLogToDb = new DBItemAuditLog();
        auditLogToDb.setSchedulerId(jobschedulerId);
        auditLogToDb.setAccount(auditAccount);
        auditLogToDb.setRequest(request);
        auditLogToDb.setParameters(auditParams);
        auditLogToDb.setJob(body.getJob());
        auditLogToDb.setJobChain(body.getJobChain());
        auditLogToDb.setOrderId(body.getOrderId());
        auditLogToDb.setFolder(body.getFolder());
        auditLogToDb.setComment(body.getComment());
        auditLogToDb.setCreated(Date.from(Instant.now()));
        SOSHibernateConnection connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection();
            connection.connect();
            connection.save(auditLogToDb);
            connection.disconnect();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public String getJsonString(Object body) {
        if (body != null) {
            try {
                return mapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                return body.toString();
            }
        }
        return "-";
    }

    public String getUrl() {
        return dbItemInventoryInstance.getUrl();
    }
    
    public String getBasicAuthorization() {
        return dbItemInventoryInstance.getAuth();
    }

    public String retrySchedulerInstance() throws JocException {
        return retrySchedulerInstance(null);
    }

    public String retrySchedulerInstance(String schedulerId) throws JocException {
        if (schedulerId == null) {
            schedulerId = jobschedulerId;
        }
        String url = dbItemInventoryInstance.getUrl();
        if (schedulerId != null) {
            jobschedulerUser.getSosShiroCurrentUser().getMapOfSchedulerInstances().remove(jobschedulerId);
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));
        } else {
            return null;
        }
        if (!url.equals(dbItemInventoryInstance.getUrl())) {
            return dbItemInventoryInstance.getUrl();
        }
        return null;
    }

    private JOCDefaultResponse init(String schedulerId, boolean permission, boolean withJobSchedulerDBCheck) throws JocException {
        JOCDefaultResponse jocDefaultResponse = init401And440();

        if (!permission) {
            return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, "Access denied"));
        }
        if (schedulerId == null) {
            throw new JocMissingRequiredParameterException("undefined 'jobschedulerId'");
        } else {
            jobschedulerId = schedulerId;
        }
//        Globals.checkConnection();
        if (!"".equals(schedulerId)) {
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(schedulerId));
//            if (withJobSchedulerDBCheck) {
//                Globals.checkFactory(schedulerId);
//            }
        }
        return jocDefaultResponse;
    }

    private JOCDefaultResponse init401And440() {
        if (!jobschedulerUser.isAuthenticated()) {
            return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser, ""));
        }
        return null;
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
    
    public void getJobSchedulerInstanceByHostPort(String host, Integer port,String schedulerId) throws DBConnectionRefusedException, DBMissingDataException, DBInvalidDataException    {
        if (host != null && !host.isEmpty() && port != null && port > 0) {

            JobSchedulerIdentifier jobSchedulerIdentifier = new JobSchedulerIdentifier(schedulerId);
            jobSchedulerIdentifier.setHost(host);
            jobSchedulerIdentifier.setPort(port);

            SOSHibernateConnection connection = null;

            try {
                connection = Globals.createSosHibernateStatelessConnection();
            } catch (Exception e) {
                throw new DBConnectionRefusedException(e);
            }
            
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
            dbItemInventoryInstance = dbLayer.getInventoryInstanceByHostPort(jobSchedulerIdentifier);
            
            if (dbItemInventoryInstance == null) {
                String errMessage = String.format("jobscheduler with id:%1$s, host:%2$s and port:%3$s couldn't be found in table %4$s",
                        jobSchedulerIdentifier.getId(), jobSchedulerIdentifier.getHost(), jobSchedulerIdentifier.getPort(),
                        DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new DBInvalidDataException(errMessage);
            }
        }
    }

}
