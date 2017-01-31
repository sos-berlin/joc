package com.sos.joc.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.joc.Globals;
import com.sos.joc.audit.resource.IAuditLogResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.audit.AuditLog;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.audit.AuditLogItem;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.order.OrderPath;

@Path("audit_log")
public class AuditLogResourceImpl extends JOCResourceImpl implements IAuditLogResource {

    private static final String API_CALL = "./audit_log";

    @Override
    public JOCDefaultResponse postAuditLog(String accessToken, AuditLogFilter auditLogFilter) throws Exception {
        SOSHibernateConnection connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection();
            initLogging(API_CALL, auditLogFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, auditLogFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getAuditLog().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            String schedulerId = auditLogFilter.getJobschedulerId();
            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
            List<DBItemAuditLog> auditLogs = new ArrayList<DBItemAuditLog>(); 
            // filters
            List<Folder> filterFolders = auditLogFilter.getFolders();
            List<JobPath> filterJobs = auditLogFilter.getJobs();
            Integer filterLimit = auditLogFilter.getLimit();
            List<OrderPath> filterOrders = auditLogFilter.getOrders();
            Date filterFrom = JobSchedulerDate.getDate(auditLogFilter.getDateFrom(), auditLogFilter.getTimeZone());
            Date filterTo = JobSchedulerDate.getDate(auditLogFilter.getDateTo(), auditLogFilter.getTimeZone());
            String filterRegex = auditLogFilter.getRegex();
            // processing
            if (filterOrders != null && !filterOrders.isEmpty()) {
                auditLogs = dbLayer.getAuditLogByOrders(schedulerId, filterOrders, filterLimit, filterFrom, filterTo);
            } else if (filterJobs != null && !filterJobs.isEmpty()) {
                auditLogs = dbLayer.getAuditLogByJobs(schedulerId, filterJobs, filterLimit, filterFrom, filterTo);
            } else if (filterFolders != null && !filterFolders.isEmpty()) {
                Set<String> folders = new HashSet<String>(); 
                for (Folder folder : filterFolders) {
                    folders.add(folder.getFolder());
                }
                auditLogs = dbLayer.getAuditLogByFolders(schedulerId, folders, filterLimit, filterFrom, filterTo);
            } else {
                auditLogs = dbLayer.getAllAuditLogs(schedulerId, filterLimit, filterFrom, filterTo);
            }
            if(filterRegex != null && !filterRegex.isEmpty()) {
                auditLogs = filterComment(auditLogs, filterRegex);
            }
            AuditLog entity = new AuditLog();
            entity.setAuditLog(fillAuditLogItems(auditLogs));
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            connection.disconnect();
        }
    }

    private List<DBItemAuditLog> filterComment(List<DBItemAuditLog> auditLogsUnfiltered, String regex) {
        List<DBItemAuditLog> filteredAuditLogs = new ArrayList<DBItemAuditLog>();
        for (DBItemAuditLog auditLogUnfiltered : auditLogsUnfiltered) {
            if (auditLogUnfiltered.getComment() != null && !auditLogUnfiltered.getComment().isEmpty()) {
                Matcher regExMatcher = Pattern.compile(regex).matcher(auditLogUnfiltered.getComment());
                if (regExMatcher.find()) {
                    filteredAuditLogs.add(auditLogUnfiltered);
                }
            }
        }
        return filteredAuditLogs;
    }
    
    private List<AuditLogItem> fillAuditLogItems(List<DBItemAuditLog> auditLogsFromDb) {
        List<AuditLogItem> audits = new ArrayList<AuditLogItem>();
        for (DBItemAuditLog auditLogFromDb : auditLogsFromDb) {
            AuditLogItem auditLogItem = new AuditLogItem();
            auditLogItem.setAccount(auditLogFromDb.getAccount());
            auditLogItem.setRequest(auditLogFromDb.getRequest());
            auditLogItem.setParameters(auditLogFromDb.getParameters());
            auditLogItem.setJob(auditLogFromDb.getJob());
            auditLogItem.setJobChain(auditLogFromDb.getJobChain());
            auditLogItem.setOrderId(auditLogFromDb.getOrderId());
            auditLogItem.setComment(auditLogFromDb.getComment());
            auditLogItem.setCreated(auditLogFromDb.getCreated());
            audits.add(auditLogItem);
        }
        return audits;
    }
    
}
