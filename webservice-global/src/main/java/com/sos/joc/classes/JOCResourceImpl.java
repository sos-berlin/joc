package com.sos.joc.classes;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.auth.rest.SOSPermissionsCreator;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.auth.rest.SOSShiroFolderPermissions;
import com.sos.auth.rest.SOSShiroSession;
import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.dailyplan.db.Calendar2DB;
import com.sos.jitl.dailyplan.db.DailyPlanCalender2DBFilter;
import com.sos.jitl.dailyplan.job.CreateDailyPlanOptions;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.audit.IAuditLog;
import com.sos.joc.classes.audit.JocAuditLog;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.db.configuration.JocConfigurationFilter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocAuthenticationException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingCommentException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.exceptions.UnknownJobSchedulerMasterException;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;

public class JOCResourceImpl {

    private static final String SESSION_KEY = "selectedInstance";
    protected DBItemInventoryInstance dbItemInventoryInstance;
    protected JobSchedulerUser jobschedulerUser;
    protected SOSShiroFolderPermissions folderPermissions;
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCResourceImpl.class);
    private static final String SHIRO_SESSION = "SHIRO_SESSION";
    private String accessToken;
    private String jobschedulerId;
    private JocAuditLog jocAuditLog;

    private JocError jocError = new JocError();

    protected void initGetPermissions(String accessToken) throws JocException {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(null);
        sosPermissionsCreator.loginFromAccessToken(accessToken);

        updateUserInMetaInfo();
    }

    private String getMasterId(String masterId) throws SessionNotExistException {
        if (masterId == null || masterId.isEmpty()) {
            SOSShiroSession sosShiroSession = new SOSShiroSession(jobschedulerUser.getSosShiroCurrentUser());
            masterId = sosShiroSession.getStringAttribute(SESSION_KEY);
            if (masterId == null) {
                masterId = "";
            }
        }
        return masterId;
    }

    protected SOSPermissionJocCockpit getPermissonsJocCockpit(String masterId, String accessToken) throws JocException {
        initGetPermissions(accessToken);
        masterId = getMasterId(masterId);
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionJocCockpit(masterId);
    }

    protected SOSPermissionCommands getPermissonsCommands(String masterId, String accessToken) throws JocException {
        initGetPermissions(accessToken);
        masterId = getMasterId(masterId);
        return jobschedulerUser.getSosShiroCurrentUser().getSosPermissionCommands(masterId);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessToken(String xAccessToken, String oldAccessToken) {
        if (xAccessToken != null && !xAccessToken.isEmpty()) {
            return xAccessToken;
        }
        return oldAccessToken;
    }

    public JobSchedulerUser getJobschedulerUser() {
        return jobschedulerUser;
    }

    public JobSchedulerUser getJobschedulerUser(String accessToken) {
        if (jobschedulerUser == null) {
            this.accessToken = accessToken;
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
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
    
    private boolean sessionExistInDb(String sessionIdString) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            sosHibernateSession = Globals.createSosHibernateStatelessConnection("JOCResourceImpl");

            JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(sosHibernateSession);
            JocConfigurationFilter filter = new JocConfigurationFilter();

            filter.setAccount(".");
            filter.setName(sessionIdString);
            filter.setConfigurationType(SHIRO_SESSION);
            List<JocConfigurationDbItem> listOfConfigurtions = jocConfigurationDBLayer.getJocConfigurationList(filter,0);
            sosHibernateSession.close();

            return (listOfConfigurtions.size() > 0);
        } catch (SOSHibernateException e) {
            throw new RuntimeException(e);

        } catch (JocException e) {
            throw new RuntimeException(e);
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    public JOCDefaultResponse init(String request, Object body, String accessToken, String schedulerId, boolean permission) throws JocException {
        this.accessToken = accessToken;

        SessionKey s = new DefaultSessionKey(accessToken);
        Session session = SecurityUtils.getSecurityManager().getSession(s);
        if ("true".equals(session.getAttribute("dao"))) {
            if (!sessionExistInDb(accessToken)) {
                if (Globals.jocWebserviceDataContainer.getCurrentUsersList() != null) {
                    Globals.jocWebserviceDataContainer.getCurrentUsersList().removeUser(accessToken);
                }
            }
        }

        if (Globals.jocWebserviceDataContainer == null || Globals.jocWebserviceDataContainer.getCurrentUsersList() == null) {
            SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer();
            sosShiroCurrentUserAnswer.setMessage("Session is broken and no longer valid. New login neccessary");
            throw new JocAuthenticationException(sosShiroCurrentUserAnswer);
        }

        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = Globals.jocWebserviceDataContainer.getCurrentUsersList().getUserByToken(accessToken);
        if (sosShiroCurrentUserAnswer.getSessionTimeout() == 0L) {
            sosShiroCurrentUserAnswer.setMessage("Session is broken and no longer valid. New login neccessary");
            throw new JocAuthenticationException(sosShiroCurrentUserAnswer);
        }

        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }
        SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(null);
        sosPermissionsCreator.loginFromAccessToken(accessToken);

        initLogging(request, body);
        return init(schedulerId, permission);
    }

    public JOCDefaultResponse init(String request, String accessToken) throws Exception {
        this.accessToken = accessToken;
        if (jobschedulerUser == null) {
            jobschedulerUser = new JobSchedulerUser(accessToken);
        }

        SOSPermissionsCreator sosPermissionsCreator = new SOSPermissionsCreator(null);
        sosPermissionsCreator.loginFromAccessToken(accessToken);

        initLogging(request, null);
        return init401And440();
    }

    public String normalizePath(String path) {
        return Globals.normalizePath(path);
    }

    public String normalizeFolder(String path) {
        if (path == null) {
            return null;
        }
        return ("/" + path.trim()).replaceAll("//+", "/");
    }

    public boolean checkRequiredComment(AuditParams auditParams) throws JocMissingCommentException {
        if (Globals.auditLogCommentsAreRequired) {
            String comment = null;
            if (auditParams != null) {
                comment = auditParams.getComment();
            }
            if (comment == null || comment.isEmpty()) {
                throw new JocMissingCommentException();
            }
        }
        return true;
    }

    public boolean checkRequiredComment(String comment) throws JocMissingCommentException {
        if (Globals.auditLogCommentsAreRequired) {
            if (comment == null || comment.isEmpty()) {
                throw new JocMissingCommentException();
            }
        }
        return true;
    }

    public boolean checkRequiredParameter(String paramKey, String paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    public boolean checkRequiredParameter(String paramKey, JobSchedulerObjectType paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.value().isEmpty()) {
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

    public boolean checkRequiredParameter(String paramKey, List<?> paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null || paramVal.isEmpty()) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    public boolean checkRequiredParameter(String paramKey, Integer paramVal) throws JocMissingRequiredParameterException {
        if (paramVal == null) {
            throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", paramKey));
        }
        return true;
    }

    protected boolean matchesRegex(Pattern p, String path) {
        if (p != null) {
            return p.matcher(path).find();
        } else {
            return true;
        }
    }

    public void logAuditMessage(IAuditLog body) {
        jocAuditLog.logAuditMessage(body);
    }

    public DBItemAuditLog storeAuditLogEntry(IAuditLog body) {
        try {
            jobschedulerUser.getSosShiroCurrentUser().getCurrentSubject().getSession().setAttribute(Globals.SESSION_KEY_FOR_SEND_EVENTS_IMMEDIATLY,
                    true);
        } catch (Exception e) {
        }
        return jocAuditLog.storeAuditLogEntry(body);
    }

    public String getJsonString(Object body) {
        if (body != null) {
            try {
                return new ObjectMapper().writeValueAsString(body);
            } catch (Exception e) {
                return body.toString();
            }
        }
        return "-";
    }

    public String writeXmlPoJoAsString(Object xmlPoJo) throws JsonProcessingException {
        // ObjectMapper xmlMapper = new XmlMapper().configure(Feature.WRITE_XML_DECLARATION, true).configure(SerializationFeature.INDENT_OUTPUT, true);
        // TODO generates <?xml version="1.0" encoding="UTF-8" ?> instead of ISO-8859-1
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n" + Globals.xmlMapper.writeValueAsString(xmlPoJo);
    }

    public String getUrl() {
        return dbItemInventoryInstance.getUrl();
    }

    public String getCommandUrl() {
        return dbItemInventoryInstance.getCommandUrl();
    }

    public String getVersion() {
        return dbItemInventoryInstance.getVersion();
    }

    public String getClusterMemberId() {
        return dbItemInventoryInstance.clusterMemberId();
    }

    public boolean versionIsOlderThan(String version) {
        try {
            String[] curVersions = dbItemInventoryInstance.getVersion().replaceFirst("-.*$", "").split("\\.");
            String[] versions = version.split("\\.");
            String curVersionsStr = curVersions[0];
            String versionsStr = versions[0];
            for (int i = 1; i < curVersions.length; i++) {
                curVersionsStr += curVersions[i].replaceFirst("^(\\d)$", "0$1");
            }
            for (int i = 1; i < versions.length; i++) {
                versionsStr += versions[i].replaceFirst("^(\\d)$", "0$1");
            }
            return (Integer.valueOf(curVersionsStr) < Integer.valueOf(versionsStr));
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public String getBasicAuthorization() {
        try {
            return dbItemInventoryInstance.getAuth();
        } catch (Exception e) {
            return null;
        }
    }

    public String getAccount() {
        try {
            return jobschedulerUser.getSosShiroCurrentUser().getUsername().trim();
        } catch (Exception e) {
            return null;
        }
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
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(schedulerId);
        } else {
            return null;
        }
        if (!url.equals(dbItemInventoryInstance.getUrl())) {
            return dbItemInventoryInstance.getUrl();
        }
        return null;
    }

    public JOCDefaultResponse accessDeniedResponse() {
        return accessDeniedResponse("Access denied");
    }

    public JOCDefaultResponse accessDeniedResponse(String message) {
        jocError.setMessage(message);
        return JOCDefaultResponse.responseStatus403(JOCDefaultResponse.getError401Schema(jobschedulerUser, jocError));
    }

    public void initLogging(String request, Object body) {
        String user;
        try {
            user = jobschedulerUser.getSosShiroCurrentUser().getUsername().trim();
        } catch (Exception e) {
            user = "-";
        }
        if (request == null || request.isEmpty()) {
            request = "-";
        }
        jocAuditLog = new JocAuditLog(user, request);
        LOGGER.debug("REQUEST: " + request + ", PARAMS: " + getJsonString(body));
        jocError.addMetaInfoOnTop("\nREQUEST: " + request, "PARAMS: " + getJsonString(body), "USER: " + user);
    }

    private JOCDefaultResponse init(String schedulerId, boolean permission) throws JocException {
        JOCDefaultResponse jocDefaultResponse = init401And440();

        if (!permission) {
            return accessDeniedResponse();
        }
        if (schedulerId == null) {
            throw new JocMissingRequiredParameterException("undefined 'jobschedulerId'");
        } else {
            jobschedulerId = schedulerId;
            folderPermissions = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions();
            folderPermissions.setSchedulerId(schedulerId);
        }
        if (!"".equals(schedulerId)) {
            dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(schedulerId);
        }
        return jocDefaultResponse;
    }

    private JOCDefaultResponse init401And440() {
        if (!jobschedulerUser.isAuthenticated()) {
            return JOCDefaultResponse.responseStatus401(JOCDefaultResponse.getError401Schema(jobschedulerUser));
        }
        return null;
    }

    private void updateUserInMetaInfo() {
        try {
            if (jocError != null) {
                String userMetaInfo = "USER: " + jobschedulerUser.getSosShiroCurrentUser().getUsername();
                List<String> metaInfo = jocError.getMetaInfo();
                if (metaInfo.size() > 2) {
                    metaInfo.remove(2);
                    metaInfo.add(2, userMetaInfo);
                }
            }
        } catch (Exception e) {
        }
    }

    public void getJobSchedulerInstanceByHostPort(String host, Integer port, String schedulerId) throws DBConnectionRefusedException,
            DBInvalidDataException, UnknownJobSchedulerMasterException {
        if (host != null && !host.isEmpty() && port != null && port > 0) {

            SOSHibernateSession session = null;

            try {
                try {
                    session = Globals.createSosHibernateStatelessConnection("getJobSchedulerInstanceByHostPort");
                } catch (Exception e) {
                    throw new DBConnectionRefusedException(e);
                }

                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
                dbItemInventoryInstance = dbLayer.getInventoryInstanceByHostPort(host, port, schedulerId);

                if (dbItemInventoryInstance == null) {
                    String errMessage = String.format("JobScheduler with id:%1$s, host:%2$s and port:%3$s couldn't be found in table %4$s",
                            schedulerId, host, port, DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new UnknownJobSchedulerMasterException(errMessage);
                }
            } finally {
                Globals.disconnect(session);
            }
        }
    }

    protected void updateDailyPlan(DailyPlanCalender2DBFilter dailyPlanCalender2DBFilter) throws Exception {
        HashMap<String, String> createDaysScheduleOptionsMap = new HashMap<String, String>();

        String commandUrl = dbItemInventoryInstance.getUrl() + "/jobscheduler/master/api/command";

        createDaysScheduleOptionsMap.put("command_url", commandUrl);
        String basicAuthorization = dbItemInventoryInstance.getAuth();
        if (basicAuthorization != null && !basicAuthorization.isEmpty()) {
            createDaysScheduleOptionsMap.put("basic_authorization", dbItemInventoryInstance.getAuth());
        }
        CreateDailyPlanOptions createDailyPlanOptions = new CreateDailyPlanOptions();
        createDailyPlanOptions.setAllOptions(createDaysScheduleOptionsMap);

        SOSHibernateSession sosHibernateSession = null;
        try {
            sosHibernateSession = Globals.createSosHibernateStatelessConnection("dailyplan");
            sosHibernateSession.setAutoCommit(false);

            Calendar2DB calendar2Db = new Calendar2DB(sosHibernateSession, dbItemInventoryInstance.getSchedulerId());
            calendar2Db.setOptions(createDailyPlanOptions);
            calendar2Db.addDailyplan2DBFilter(dailyPlanCalender2DBFilter, dbItemInventoryInstance.getId());
            calendar2Db.processDailyplan2DBFilter();

        } catch (SOSHibernateException ex) {
            try {
                if (sosHibernateSession != null) {
                    sosHibernateSession.rollback();
                }
            } catch (Exception e) {
            }
            throw new DBInvalidDataException(ex);
        } finally {
            Globals.disconnect(sosHibernateSession);
        }

    }

    protected void readJobSchedulerVariables() throws Exception {
        if (Globals.schedulerVariables == null) {
            Globals.schedulerVariables = new HashMap<String, String>();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            jocXmlCommand.executePostWithThrowBadRequest("<params.get/>", accessToken);
            org.w3c.dom.NodeList schedulerParameters = jocXmlCommand.selectNodelist("//param");
            for (int i = 0; i < schedulerParameters.getLength(); i++) {
                org.w3c.dom.Node param = schedulerParameters.item(i);

                org.w3c.dom.NamedNodeMap map = param.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    String paramName = map.item(j).getNodeValue();
                    String paramValue = "";
                    if (map.getLength() > 1) {
                        j++;
                        paramValue = map.item(j).getNodeValue();
                    }
                    LOGGER.debug(".. parameter: " + paramName + "=" + paramValue);
                    Globals.schedulerVariables.put(paramName, paramValue);
                }
            }
        }
    }

    protected String getParent(String path) {
        return Globals.getParent(path);
    }

    protected boolean canAdd(String path, Set<Folder> listOfFolders) {
        return folderPermissions.isPermittedForFolder(getParent(path), listOfFolders);
    }

    protected List<Folder> addPermittedFolder(List<Folder> folders) {
        return folderPermissions.getPermittedFolders(folders);
    }
}
