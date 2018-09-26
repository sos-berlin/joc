package com.sos.joc.audit.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Path;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.joc.Globals;
import com.sos.joc.audit.resource.IAuditLogResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.audit.AuditLogDBFilter;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.audit.AuditLog;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.audit.AuditLogItem;
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
            
            AuditLogDBFilter auditLogDBFilter = new AuditLogDBFilter(auditLogFilter);
             
            if (auditLogFilter.getOrders() != null && !auditLogFilter.getOrders().isEmpty()) {
                for (OrderPath order : auditLogFilter.getOrders()) {
                    checkRequiredParameter("jobChain", order.getJobChain());
                }
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
            String filterRegex = auditLogFilter.getRegex();
            if (SearchStringHelper.isDBWildcardSearch(filterRegex)) {
            	auditLogDBFilter.setReason(filterRegex);
            	filterRegex = "";
            }
            List<DBItemAuditLog> auditLogs = dbLayer.getAuditLogs(auditLogDBFilter,auditLogFilter.getLimit());

            if (filterRegex != null && !filterRegex.isEmpty()) {
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