package com.sos.joc.documentation.impl;

import java.sql.Date;
import java.time.Instant;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.AssignmentCalendarDocuAudit;
import com.sos.joc.classes.audit.AssignmentJobChainDocuAudit;
import com.sos.joc.classes.audit.AssignmentJobDocuAudit;
import com.sos.joc.classes.audit.AssignmentLockDocuAudit;
import com.sos.joc.classes.audit.AssignmentOrderDocuAudit;
import com.sos.joc.classes.audit.AssignmentProcessClassDocuAudit;
import com.sos.joc.classes.audit.AssignmentScheduleDocuAudit;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentation.resource.IDocumentationAssignJSObjectResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.CalendarDocuFilter;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.job.JobDocuFilter;
import com.sos.joc.model.jobChain.JobChainDocuFilter;
import com.sos.joc.model.lock.LockDocuFilter;
import com.sos.joc.model.order.OrderDocuFilter;
import com.sos.joc.model.processClass.ProcessClassDocuFilter;
import com.sos.joc.model.schedule.ScheduleDocuFilter;

public class DocumentationAssignJSObjectResourceImpl extends JOCResourceImpl implements IDocumentationAssignJSObjectResource {

    private static final String API_CALL_ASSIGN = "/documentation/assign";
    private static final String API_CALL_UNASSIGN = "/documentation/unassign";

    @Override
    public JOCDefaultResponse assignOrderDocu(String xAccessToken, OrderDocuFilter filter) throws Exception {
        try {
            String apiCall = "/order" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getOrder().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentOrderDocuAudit assignAudit = new AssignmentOrderDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getJobChain()) + "," + filter.getOrderId(), filter.getDocumentation(),
                    JobSchedulerObjectType.ORDER, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }

    @Override
    public JOCDefaultResponse unassignOrderDocu(String xAccessToken, OrderDocuFilter filter) throws Exception {
        try {
            String apiCall = "/order" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getOrder().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentOrderDocuAudit unassignAudit = new AssignmentOrderDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getJobChain()) + "," + filter.getOrderId(), JobSchedulerObjectType.ORDER, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignJobDocu(String xAccessToken, JobDocuFilter filter) throws Exception {
        try {
            String apiCall = "/job" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getJob().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentJobDocuAudit assignAudit = new AssignmentJobDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getJob()), filter.getDocumentation(), JobSchedulerObjectType.JOB, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignJobDocu(String xAccessToken, JobDocuFilter filter) throws Exception {
        try {
            String apiCall = "/job" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getJob().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentJobDocuAudit unassignAudit = new AssignmentJobDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getJob()), JobSchedulerObjectType.JOB, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignJobChainDocu(String xAccessToken, JobChainDocuFilter filter) throws Exception {
        try {
            String apiCall = "/job_chain" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getJobChain().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentJobChainDocuAudit assignAudit = new AssignmentJobChainDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getJobChain()), filter.getDocumentation(), JobSchedulerObjectType.JOBCHAIN, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignJobChainDocu(String xAccessToken, JobChainDocuFilter filter) throws Exception {
        try {
            String apiCall = "/job_chain" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getJobChain().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentJobChainDocuAudit unassignAudit = new AssignmentJobChainDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getJobChain()), JobSchedulerObjectType.JOBCHAIN, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignScheduleDocu(String xAccessToken, ScheduleDocuFilter filter) throws Exception {
        try {
            String apiCall = "/schedule" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getSchedule().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentScheduleDocuAudit assignAudit = new AssignmentScheduleDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getSchedule()), filter.getDocumentation(), JobSchedulerObjectType.SCHEDULE, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignScheduleDocu(String xAccessToken, ScheduleDocuFilter filter) throws Exception {
        try {
            String apiCall = "/schedule" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getSchedule().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentScheduleDocuAudit unassignAudit = new AssignmentScheduleDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getSchedule()), JobSchedulerObjectType.SCHEDULE, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignLockDocu(String xAccessToken, LockDocuFilter filter) throws Exception {
        try {
            String apiCall = "/lock" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getLock().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentLockDocuAudit assignAudit = new AssignmentLockDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getLock()), filter.getDocumentation(), JobSchedulerObjectType.LOCK, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignLockDocu(String xAccessToken, LockDocuFilter filter) throws Exception {
        try {
            String apiCall = "/lock" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getLock().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentLockDocuAudit unassignAudit = new AssignmentLockDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getLock()), JobSchedulerObjectType.LOCK, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignProcessClassDocu(String xAccessToken, ProcessClassDocuFilter filter) throws Exception {
        try {
            String apiCall = "/process_class" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getProcessClass().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentProcessClassDocuAudit assignAudit = new AssignmentProcessClassDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getProcessClass()), filter.getDocumentation(), JobSchedulerObjectType.PROCESSCLASS, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignProcessClassDocu(String xAccessToken, ProcessClassDocuFilter filter) throws Exception {
        try {
            String apiCall = "/process_class" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getProcessClass().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentProcessClassDocuAudit unassignAudit = new AssignmentProcessClassDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getProcessClass()), JobSchedulerObjectType.PROCESSCLASS, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignCalendarDocu(String xAccessToken, CalendarDocuFilter filter) throws Exception {
        try {
            String apiCall = "/calendar" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getCalendar().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentCalendarDocuAudit assignAudit = new AssignmentCalendarDocuAudit(filter);
            logAuditMessage(assignAudit);
            assignDocu(filter.getJobschedulerId(), normalizePath(filter.getCalendar()), filter.getDocumentation(), JobSchedulerObjectType.WORKINGDAYSCALENDAR, apiCall);
            storeAuditLogEntry(assignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignCalendarDocu(String xAccessToken, CalendarDocuFilter filter) throws Exception {
        try {
            String apiCall = "/calendar" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), getPermissonsJocCockpit(filter
                    .getJobschedulerId(), xAccessToken).getCalendar().isAssignDocumentation());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AssignmentCalendarDocuAudit unassignAudit = new AssignmentCalendarDocuAudit(filter);
            logAuditMessage(unassignAudit);
            unassignDocu(filter.getJobschedulerId(), normalizePath(filter.getCalendar()), JobSchedulerObjectType.WORKINGDAYSCALENDAR, apiCall);
            storeAuditLogEntry(unassignAudit);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void assignDocu(String jobschedulerId, String jsObjectPath, String docPath, JobSchedulerObjectType objType, String apiCall) throws Exception {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String type = objType.name();
            if (objType == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
                CalendarsDBLayer calDbLayer = new CalendarsDBLayer(connection);
                DBItemInventoryClusterCalendar dbCalendar = calDbLayer.getCalendar(jobschedulerId, jsObjectPath);
                if (!type.equals(dbCalendar.getType())) {
                    type = dbCalendar.getType();
                }
            }
            DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
            DBItemDocumentation dbDoc = dbLayer.getDocumentation(jobschedulerId, docPath);
            if (dbDocUsage != null) {
                dbDocUsage.setDocumentationId(dbDoc.getId());
                dbDocUsage.setModified(Date.from(Instant.now()));
                connection.update(dbDocUsage);
            } else {
                DBItemDocumentationUsage newUsage = new DBItemDocumentationUsage();
                newUsage.setSchedulerId(jobschedulerId);
                newUsage.setPath(jsObjectPath);
                newUsage.setObjectType(type);
                newUsage.setDocumentationId(dbDoc.getId());
                newUsage.setCreated(Date.from(Instant.now()));
                newUsage.setModified(newUsage.getCreated());
                connection.save(newUsage);
            }
        } finally {
            Globals.disconnect(connection);
        }
    }

    private void unassignDocu(String jobschedulerId, String jsObjectPath, JobSchedulerObjectType objType, String apiCall) throws Exception {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String type = objType.name();
            if (objType == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
                CalendarsDBLayer calDbLayer = new CalendarsDBLayer(connection);
                DBItemInventoryClusterCalendar dbCalendar = calDbLayer.getCalendar(jobschedulerId, jsObjectPath);
                if (!type.equals(dbCalendar.getType())) {
                    type = dbCalendar.getType();
                }
            }
            DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type);
            if (dbDocUsage != null) {
                connection.delete(dbDocUsage);
            }
        } finally {
            Globals.disconnect(connection);
        }
    }

}
