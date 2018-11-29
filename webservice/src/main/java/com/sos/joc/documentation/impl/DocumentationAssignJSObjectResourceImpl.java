package com.sos.joc.documentation.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemDocumentation;
import com.sos.jitl.reporting.db.DBItemDocumentationUsage;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
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
        // TODO Permission
        try {
            String apiCall = "/order" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getJobChain() + "," + filter.getOrderId(), filter.getDocumentation(),
                    JobSchedulerObjectType.ORDER, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/order" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getJobChain() + "," + filter.getOrderId(), JobSchedulerObjectType.ORDER, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/job" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getJob(), filter.getDocumentation(), JobSchedulerObjectType.JOB, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/job" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getJob(), JobSchedulerObjectType.JOB, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/job_chain" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getJobChain(), filter.getDocumentation(), JobSchedulerObjectType.JOBCHAIN, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/job_chain" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getJobChain(), JobSchedulerObjectType.JOBCHAIN, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/schedule" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getSchedule(), filter.getDocumentation(), JobSchedulerObjectType.SCHEDULE, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/schedule" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getSchedule(), JobSchedulerObjectType.SCHEDULE, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/lock" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getLock(), filter.getDocumentation(), JobSchedulerObjectType.LOCK, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/lock" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getLock(), JobSchedulerObjectType.LOCK, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/process_class" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getProcessClass(), filter.getDocumentation(), JobSchedulerObjectType.PROCESSCLASS, apiCall);
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
        // TODO Permission
        try {
            String apiCall = "/process_class" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getProcessClass(), JobSchedulerObjectType.PROCESSCLASS, apiCall);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse assignCalendarClassDocu(String xAccessToken, CalendarDocuFilter filter) throws Exception {
        // TODO Permission
        try {
            String apiCall = "/calendar" + API_CALL_ASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            assignDocu(filter.getJobschedulerId(), filter.getCalendar(), filter.getDocumentation(), JobSchedulerObjectType.WORKINGDAYSCALENDAR, apiCall);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse unassignCalendarClassDocu(String xAccessToken, CalendarDocuFilter filter) throws Exception {
        // TODO Permission
        try {
            String apiCall = "/calendar" + API_CALL_UNASSIGN;
            JOCDefaultResponse jocDefaultResponse = init(apiCall, filter, xAccessToken, filter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            unassignDocu(filter.getJobschedulerId(), filter.getCalendar(), JobSchedulerObjectType.WORKINGDAYSCALENDAR, apiCall);
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void assignDocu(String jobschedulerId, String jsObjectPath, String docPath, JobSchedulerObjectType type, String apiCall) throws Exception {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            if (type.equals(JobSchedulerObjectType.WORKINGDAYSCALENDAR)) {
                CalendarsDBLayer calDbLayer = new CalendarsDBLayer(connection);
                DBItemInventoryClusterCalendar dbCalendar = calDbLayer.getCalendar(jobschedulerId, jsObjectPath);
                if (!type.equals(JobSchedulerObjectType.fromValue(dbCalendar.getType()))) {
                    type = JobSchedulerObjectType.fromValue(dbCalendar.getType());
                }
            }
            DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type.name());
            Path path = Paths.get(docPath);
            String docDirectory = path.getParent().toString().replace('\\', '/');
            String docName = path.getFileName().toString();
            DBItemDocumentation dbDoc = dbLayer.getDocumentation(jobschedulerId, docDirectory, docName);
            if (dbDocUsage != null) {
                dbDocUsage.setDocumentationId(dbDoc.getId());
                dbDocUsage.setModified(Date.from(Instant.now()));
                connection.update(dbDocUsage);
            } else {
                DBItemDocumentationUsage newUsage = new DBItemDocumentationUsage();
                newUsage.setSchedulerId(jobschedulerId);
                newUsage.setPath(jsObjectPath);
                newUsage.setObjectType(type.name());
                newUsage.setDocumentationId(dbDoc.getId());
                newUsage.setCreated(Date.from(Instant.now()));
                newUsage.setModified(newUsage.getCreated());
                connection.save(newUsage);
            }
        } finally {
            Globals.disconnect(connection);
        }
    }

    private void unassignDocu(String jobschedulerId, String jsObjectPath, JobSchedulerObjectType type, String apiCall) throws Exception {
        SOSHibernateSession connection = null;
        try {
            connection = Globals.createSosHibernateStatelessConnection(apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            DBItemDocumentationUsage dbDocUsage = dbLayer.getDocumentationUsageForAssignment(jobschedulerId, jsObjectPath, type.name());
            if (dbDocUsage != null) {
                connection.delete(dbDocUsage);
            }
        } finally {
            Globals.disconnect(connection);
        }
    }

}
