package com.sos.joc.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.joc.Globals;
import com.sos.joc.audit.resource.IAuditLogResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
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
            // TODO use different permission .getAuditLog().getView()
            JOCDefaultResponse jocDefaultResponse = init(accessToken, auditLogFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getAuditLog().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            String schedulerId = auditLogFilter.getJobschedulerId();
            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
            List<DBItemAuditLog> auditLogs = new ArrayList<DBItemAuditLog>(); 
            
            List<Folder> filterFolders = auditLogFilter.getFolders();
            List<JobPath> filterJobs = auditLogFilter.getJobs();
            Integer filterLimit = auditLogFilter.getLimit();
            List<OrderPath> filterOrders = auditLogFilter.getOrders();
            String filterDateFrom = auditLogFilter.getDateFrom();
            String filterDateTo = auditLogFilter.getDateTo();
            String filterTimeZone = auditLogFilter.getTimeZone();
            String filterRegex = auditLogFilter.getRegex();
            if (filterOrders != null && !filterOrders.isEmpty()) {
                Set<String> orders = new HashSet<String>(); 
                for (OrderPath order : filterOrders) {
                    orders.add(order.getOrderId());
                }
                auditLogs = dbLayer.getAuditLogByOrders(schedulerId, orders);
            } else if (filterJobs != null && !filterJobs.isEmpty()) {
                Set<String> jobs = new HashSet<String>(); 
                for (JobPath job : filterJobs) {
                    jobs.add(job.getJob());
                }
                auditLogs = dbLayer.getAuditLogByJobs(schedulerId, jobs);
            } else if (filterFolders != null && !filterFolders.isEmpty()) {
                Set<String> folders = new HashSet<String>(); 
                for (Folder folder : filterFolders) {
                    folders.add(folder.getFolder());
                }
                auditLogs = dbLayer.getAuditLogByFolders(schedulerId, folders);
            }
            
            // TODO fill audits collection
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
