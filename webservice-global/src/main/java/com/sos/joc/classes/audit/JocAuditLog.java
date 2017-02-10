package com.sos.joc.classes.audit;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.joc.Globals;
import com.sos.joc.classes.WebserviceConstants;

public class JocAuditLog {

    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger(WebserviceConstants.AUDIT_LOGGER);
    private static final Logger LOGGER = LoggerFactory.getLogger(JocAuditLog.class);
    private String user;
    private String request;

    public JocAuditLog(String user, String request) {
        this.user = user;
        this.request = request;
    }
    
    public void setUser(String user) {
        this.user = user;
    }

    public void logAuditMessage() {
        logAuditMessage(null);
    }

    public void logAuditMessage(IAuditLog body) {
        try {
            String params = getJsonString(body);
            String comment = "-";
            String timeSpent = "-";
            String ticketLink = "-";
            if (body != null) {
                comment = body.getComment();
                if (body.getComment() != null) {
                    comment = body.getComment();
                }
                if (body.getTimeSpent() != null) {
                    timeSpent = body.getTimeSpent().toString()+"m";
                }
                if (body.getTicketLink() != null) {
                    ticketLink = body.getTicketLink();
                }
            }
            AUDIT_LOGGER.info(String.format("REQUEST: %1$s - USER: %2$s - PARAMS: %3$s - COMMENT: %4$s - TIMESPENT: %5$s - TICKET: %6$s", request, user, params, comment, timeSpent, ticketLink));
        } catch (Exception e) {
            LOGGER.error("Cannot write to audit log file", e);
        }
    }

    public void storeAuditLogEntry(IAuditLog body) {
        DBItemAuditLog auditLogToDb = new DBItemAuditLog();
        auditLogToDb.setSchedulerId(body.getJobschedulerId());
        auditLogToDb.setAccount(user);
        auditLogToDb.setRequest(request);
        auditLogToDb.setParameters(getJsonString(body));
        auditLogToDb.setJob(body.getJob());
        auditLogToDb.setJobChain(body.getJobChain());
        auditLogToDb.setOrderId(body.getOrderId());
        auditLogToDb.setFolder(body.getFolder());
        auditLogToDb.setComment(body.getComment());
        auditLogToDb.setTicketLink(body.getTicketLink());
        auditLogToDb.setTimeSpent(body.getTimeSpent());
        auditLogToDb.setCreated(Date.from(Instant.now()));
        SOSHibernateConnection connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection("storeAuditLogEntry");
            connection.save(auditLogToDb);
            connection.disconnect();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
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
}
