package com.sos.joc.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.joc.Globals;
import com.sos.joc.audit.resource.IAuditLogResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.audit.AuditLog;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.audit.AuditLogItem;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.order.OrderPath;

@Path("audit_log")
public class AuditLogResourceImpl extends JOCResourceImpl implements IAuditLogResource {

    private static final String API_CALL = "./audit_log";

    public JOCDefaultResponse postAuditLog(String accessToken, AuditLogFilter auditLogFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            if (auditLogFilter.getJobschedulerId() == null) {
                auditLogFilter.setJobschedulerId("");
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, auditLogFilter, accessToken, auditLogFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(auditLogFilter.getJobschedulerId(), accessToken).getAuditLog().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String schedulerId = auditLogFilter.getJobschedulerId();
            List<DBItemAuditLog> auditLogs = new ArrayList<DBItemAuditLog>();
            // filters
            List<Folder> filterFolders = auditLogFilter.getFolders();
            List<JobPath> filterJobs = auditLogFilter.getJobs();
            Integer filterLimit = auditLogFilter.getLimit();
            List<OrderPath> filterOrders = auditLogFilter.getOrders();
            String filterTicketLink = auditLogFilter.getTicketLink();
            String filterAccount = auditLogFilter.getAccount();
            List<String> filterCalendars = auditLogFilter.getCalendars();
            if (filterOrders != null && !filterOrders.isEmpty()) {
                for (OrderPath order : filterOrders) {
                    checkRequiredParameter("jobChain", order.getJobChain());
                }
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
            Date filterFrom = JobSchedulerDate.getDateFrom(auditLogFilter.getDateFrom(), auditLogFilter.getTimeZone());
            Date filterTo = JobSchedulerDate.getDateTo(auditLogFilter.getDateTo(), auditLogFilter.getTimeZone());
            String filterRegex = auditLogFilter.getRegex();
            // processing
            if (filterOrders != null && !filterOrders.isEmpty()) {
                auditLogs = dbLayer.getAuditLogByOrders(schedulerId, filterOrders, filterLimit, filterFrom, filterTo, filterTicketLink,
                        filterAccount);
            } else if (filterJobs != null && !filterJobs.isEmpty()) {
                auditLogs = dbLayer.getAuditLogByJobs(schedulerId, filterJobs, filterLimit, filterFrom, filterTo, filterTicketLink, filterAccount);
            } else if (filterCalendars != null && !filterCalendars.isEmpty()) {
                auditLogs = dbLayer.getAuditLogByCalendars(schedulerId, filterCalendars, filterLimit, filterFrom, filterTo, filterTicketLink,
                        filterAccount);
            } else if (filterFolders != null && !filterFolders.isEmpty()) {
                Set<String> folders = new HashSet<String>();
                for (Folder folder : filterFolders) {
                    folders.add(normalizeFolder(folder.getFolder()));
                }
                auditLogs = dbLayer.getAuditLogByFolders(schedulerId, folders, filterLimit, filterFrom, filterTo, filterTicketLink, filterAccount);
            } else {
                auditLogs = dbLayer.getAllAuditLogs(schedulerId, filterLimit, filterFrom, filterTo, filterTicketLink, filterAccount);
            }
            if (filterRegex != null && !filterRegex.isEmpty() && (filterOrders == null || filterOrders.isEmpty()) && (filterJobs == null || filterJobs
                    .isEmpty())) {
                auditLogs = filterComment(auditLogs, filterRegex);
            }
            AuditLog entity = new AuditLog();
            entity.setAuditLog(fillAuditLogItems(auditLogs, auditLogFilter.getJobschedulerId()));
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
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

    private List<AuditLogItem> fillAuditLogItems(List<DBItemAuditLog> auditLogsFromDb, String jobschedulerId) throws JocException {
        List<AuditLogItem> audits = new ArrayList<AuditLogItem>();
        for (DBItemAuditLog auditLogFromDb : auditLogsFromDb) {
            AuditLogItem auditLogItem = new AuditLogItem();
            if (jobschedulerId.isEmpty()) {
                if (!getPermissonsJocCockpit(auditLogFromDb.getSchedulerId(), getAccessToken()).getAuditLog().getView().isStatus()) {
                    continue;
                }
                auditLogItem.setJobschedulerId(auditLogFromDb.getSchedulerId());
            }
            auditLogItem.setAccount(auditLogFromDb.getAccount());
            auditLogItem.setRequest(auditLogFromDb.getRequest());
            auditLogItem.setParameters(auditLogFromDb.getParameters());
            auditLogItem.setJob(auditLogFromDb.getJob());
            auditLogItem.setJobChain(auditLogFromDb.getJobChain());
            auditLogItem.setOrderId(auditLogFromDb.getOrderId());
            auditLogItem.setComment(auditLogFromDb.getComment());
            auditLogItem.setCreated(auditLogFromDb.getCreated());
            auditLogItem.setTicketLink(auditLogFromDb.getTicketLink());
            auditLogItem.setTimeSpent(auditLogFromDb.getTimeSpent());
            auditLogItem.setCalendar(auditLogFromDb.getCalendar());
            audits.add(auditLogItem);
        }
        return audits;
    }

    @Override
    public JOCDefaultResponse postAuditLog(String xAccessToken, String accessToken, AuditLogFilter auditLogFilter) throws Exception {
        return postAuditLog(getAccessToken(xAccessToken, accessToken), auditLogFilter);
    }

}