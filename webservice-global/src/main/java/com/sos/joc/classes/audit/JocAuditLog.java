package com.sos.joc.classes.audit;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.joc.Globals;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.classes.WebserviceConstants;

public class JocAuditLog {
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);
    private JobSchedulerUser jobschedulerUser;
    private String jobschedulerId;
    private String request;
    private ObjectMapper mapper = new ObjectMapper();

    
    public JocAuditLog(JobSchedulerUser jobschedulerUser, String jobschedulerId) {
        super();
        this.jobschedulerUser = jobschedulerUser;
        this.jobschedulerId = jobschedulerId;
    }

    public void logAuditMessage() {
        logAuditMessage(null);
    }

    public void logAuditMessage(IAuditLog body) {
        try {
            String params = getJsonString(body);
            String comment = "-";
            if (body != null){
                comment = body.getComment();
            }
            comment = "COMMENT: " + comment;
            String user =  jobschedulerUser.getSosShiroCurrentUser().getUsername().trim();
            AUDIT_LOGGER.info(String.format("%1$s - %2$s - %3$s - %4$s", request, user, params, comment));
        } catch (Exception e) {
            AUDIT_LOGGER.error("Cannot write to audit log file", e);
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
            connection = Globals.createSosHibernateStatelessConnection("storeAuditLogEntry");
            connection.save(auditLogToDb);
            connection.disconnect();
        } catch (Exception e) {
            AUDIT_LOGGER.error(e.getMessage(), e);
        }

    }
    
    public String getJsonString(Object body) {
        if (body != null) {
            try {
                return mapper.writeValueAsString(body);
            } catch (Exception e) {
                return body.toString();
            }
        }
        return "-";
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
