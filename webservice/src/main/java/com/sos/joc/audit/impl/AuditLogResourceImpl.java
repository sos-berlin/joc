package com.sos.joc.audit.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.sos.joc.model.common.Folder;
import com.sos.schema.JsonValidator;

@Path("audit_log")
public class AuditLogResourceImpl extends JOCResourceImpl implements IAuditLogResource {

    private static final String API_CALL = "./audit_log";

    @Override
    public JOCDefaultResponse postAuditLog(String accessToken, byte[] auditLogFilterBytes) {

        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(auditLogFilterBytes, AuditLogFilter.class);
            AuditLogFilter auditLogFilter = Globals.objectMapper.readValue(auditLogFilterBytes, AuditLogFilter.class);
            if (auditLogFilter.getJobschedulerId() == null) {
                auditLogFilter.setJobschedulerId("");
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, auditLogFilter, accessToken, auditLogFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(auditLogFilter.getJobschedulerId(), accessToken).getAuditLog().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AuditLogDBFilter auditLogDBFilter = new AuditLogDBFilter(auditLogFilter);

            if (SearchStringHelper.isDBWildcardSearch(auditLogFilter.getRegex())) {
                auditLogDBFilter.setReason(auditLogFilter.getRegex());
                auditLogFilter.setRegex("");
            }

            AuditLog entity = new AuditLog();

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
            List<DBItemAuditLog> auditLogs = dbLayer.getAuditLogs(auditLogDBFilter, auditLogFilter.getLimit());

            Matcher regExMatcher = null;
            if (auditLogFilter.getRegex() != null && !auditLogFilter.getRegex().isEmpty()) {
                regExMatcher = Pattern.compile(auditLogFilter.getRegex()).matcher("");
            }
            entity.setAuditLog(fillAuditLogItems(auditLogs, auditLogFilter.getJobschedulerId(), regExMatcher));

            entity.setDeliveryDate(Date.from(Instant.now()));

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

    private List<AuditLogItem> fillAuditLogItems(List<DBItemAuditLog> auditLogsFromDb, String jobschedulerId, Matcher regExMatcher) throws JocException {

        Map<String, Set<Folder>> permittedFoldersMap = folderPermissions.getListOfFoldersForInstance();
        List<AuditLogItem> audits = new ArrayList<AuditLogItem>();
        for (DBItemAuditLog auditLogFromDb : auditLogsFromDb) {
            AuditLogItem auditLogItem = new AuditLogItem();
            if (auditLogFromDb.getFolder() != null && !auditLogFromDb.getFolder().isEmpty()) {
                if (!folderPermissions.isPermittedForFolder(auditLogFromDb.getFolder(), permittedFoldersMap.get(""))) {
                    continue;
                }
                if (!folderPermissions.isPermittedForFolder(auditLogFromDb.getFolder(), permittedFoldersMap.get(auditLogFromDb.getSchedulerId()))) {
                    continue;
                }
            }
            if (jobschedulerId.isEmpty()) {
                if (!getPermissonsJocCockpit(auditLogFromDb.getSchedulerId(), getAccessToken()).getAuditLog().getView().isStatus()) {
                    continue;
                }
                auditLogItem.setJobschedulerId(auditLogFromDb.getSchedulerId());
            }
            if (regExMatcher != null && (auditLogFromDb.getComment() == null || auditLogFromDb.getComment().isEmpty())) {
                continue;
            }
            if (regExMatcher != null && !regExMatcher.reset(auditLogFromDb.getComment()).find()) {
                continue;
            }
            auditLogItem.setAccount(auditLogFromDb.getAccount());
            auditLogItem.setRequest(auditLogFromDb.getRequest());
            // sanitizing
            auditLogItem.setParameters(auditLogFromDb.getParameters().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
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

}