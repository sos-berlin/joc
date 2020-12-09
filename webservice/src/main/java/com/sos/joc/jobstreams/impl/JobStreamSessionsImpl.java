package com.sos.joc.jobstreams.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamTaskContext;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamStarters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreams;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamsTaskContext;
import com.sos.jitl.jobstreams.db.FilterJobStreamHistory;
import com.sos.jitl.jobstreams.db.FilterJobStreamTaskContext;
import com.sos.jitl.jobstreams.db.FilterJobStreams;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobstreams.resource.IJobStreamSessionsResource;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamSessions;
import com.sos.joc.model.jobstreams.JobStreamSessionsFilter;
import com.sos.joc.model.jobstreams.JobStreamSesssion;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreamTask;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamSessionsImpl extends JOCResourceImpl implements IJobStreamSessionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamSessionsImpl.class);
    private static final String API_CALL_JOBSTREAM_SESSIONS = "./jobstreams/sessions";
    private static final String API_CALL_JOBSTREAM_SESSIONS_RUNNING = "./jobstreams/sessions/running";

    @Override
    public JOCDefaultResponse postJobStreamSessions(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamSessionsFilter jobStreamSessionFilter = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamSessionFilter = Globals.objectMapper.readValue(filterBytes, JobStreamSessionsFilter.class);

            if (jobStreamSessionFilter.getJobschedulerId() == null) {
                jobStreamSessionFilter.setJobschedulerId("");
            }

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_JOBSTREAM_SESSIONS, jobStreamSessionFilter, accessToken, jobStreamSessionFilter
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamSessionFilter.getJobschedulerId(), accessToken).getJobStream()
                            .isSetView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_JOBSTREAM_SESSIONS);
            FilterJobStreamHistory filterJobStreamHistory = new FilterJobStreamHistory();

            List<Long> listOfJobStreamIds = new ArrayList<Long>();
            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            FilterJobStreams filterJobStreams = new FilterJobStreams();

            if (jobStreamSessionFilter.getJobStream() != null) {
                filterJobStreams.setJobStream(jobStreamSessionFilter.getJobStream());
                filterJobStreams.setSchedulerId(jobStreamSessionFilter.getJobschedulerId());

                List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
                for (DBItemJobStream dbItemJobStream : listOfJobStreams) {
                    listOfJobStreamIds.add(dbItemJobStream.getId());
                }

            }

            if (jobStreamSessionFilter.getJobStreamId() != null) {
                listOfJobStreamIds.add(jobStreamSessionFilter.getJobStreamId());
            }

            DBLayerJobStreamHistory dbLayerJobStreamHistory = new DBLayerJobStreamHistory(sosHibernateSession);
            if ("running".equals(jobStreamSessionFilter.getStatus())) {
                filterJobStreamHistory.setRunning(true);
            }
            int limit = 0;
            if (jobStreamSessionFilter.getLimit() != null) {
                limit = jobStreamSessionFilter.getLimit();
            }

            for (String session : jobStreamSessionFilter.getSession()) {
                filterJobStreamHistory.addContextId(session);
            }
            filterJobStreamHistory.setStartedFrom(JobSchedulerDate.getDateFrom(jobStreamSessionFilter.getDateFrom(), jobStreamSessionFilter
                    .getTimeZone()));
            filterJobStreamHistory.setStartedTo(JobSchedulerDate.getDateTo(jobStreamSessionFilter.getDateTo(), jobStreamSessionFilter.getTimeZone()));
            filterJobStreamHistory.setJobStreamStarter(jobStreamSessionFilter.getJobStreamStarterId());
            filterJobStreamHistory.setSchedulerId(jobStreamSessionFilter.getJobschedulerId());

            if ("completed".equalsIgnoreCase(jobStreamSessionFilter.getStatus()) && "running".equalsIgnoreCase(jobStreamSessionFilter.getStatus())) {
                filterJobStreamHistory.setRunning(null);
                filterJobStreamHistory.setCompleted(null);
            } else {
                if ("running".equalsIgnoreCase(jobStreamSessionFilter.getStatus())) {
                    filterJobStreamHistory.setCompleted(false);
                    filterJobStreamHistory.setRunning(true);
                }
                if ("completed".equalsIgnoreCase(jobStreamSessionFilter.getStatus())) {
                    filterJobStreamHistory.setRunning(false);
                    filterJobStreamHistory.setCompleted(true);
                }
            }

            JobStreamSessions jobStreamSesssions = new JobStreamSessions();
            jobStreamSesssions.setDeliveryDate(new Date());
            jobStreamSesssions.setJobstreamSessions(new ArrayList<JobStreamSesssion>());

            for (Long id : listOfJobStreamIds) {
                filterJobStreamHistory.setJobStreamId(id);

                List<DBItemJobStreamHistory> listOfSessions = dbLayerJobStreamHistory.getJobStreamHistoryList(filterJobStreamHistory, limit);

                DBLayerJobStreamsTaskContext dbLayerJobStreamsTaskContext = new DBLayerJobStreamsTaskContext(sosHibernateSession);
                FilterJobStreamTaskContext filterJobStreamTaskContext = new FilterJobStreamTaskContext();
                filterJobStreamTaskContext.setOrderCriteria("taskId");
                filterJobStreamTaskContext.setSortMode("desc");
                
                DBLayerJobStreamStarters dbLayerJobStreamStarter = new DBLayerJobStreamStarters(sosHibernateSession);

                for (DBItemJobStreamHistory dbItemJobStreamHistory : listOfSessions) {
                    JobStreamSesssion jobStreamSesssion = new JobStreamSesssion();
                    jobStreamSesssion.setJobStreamStarter(new JobStreamStarter());
                    jobStreamSesssion.setId(dbItemJobStreamHistory.getId());
                    jobStreamSesssion.setJobStreamId(dbItemJobStreamHistory.getJobStream());
                    DBItemJobStreamStarter dbItemJobStreamStarter = dbLayerJobStreamStarter.getJobStreamStartersDbItem(dbItemJobStreamHistory
                            .getJobStreamStarter());
                    if (dbItemJobStreamStarter != null) {
                        jobStreamSesssion.getJobStreamStarter().setTitle(dbItemJobStreamStarter.getTitle());
                        jobStreamSesssion.getJobStreamStarter().setJobStreamStarterId(dbItemJobStreamHistory.getJobStreamStarter());
                        jobStreamSesssion.getJobStreamStarter().setNextStart(dbItemJobStreamStarter.getNextStart());
                        jobStreamSesssion.getJobStreamStarter().setEndOfJobStream(dbItemJobStreamStarter.getEndOfJobStream());
                        jobStreamSesssion.getJobStreamStarter().setRequiredJob(dbItemJobStreamStarter.getRequiredJob());
                        jobStreamSesssion.getJobStreamStarter().setState(dbItemJobStreamStarter.getState());
                    }

                    jobStreamSesssion.setSession(dbItemJobStreamHistory.getContextId());
                    jobStreamSesssion.setStarted(dbItemJobStreamHistory.getStarted());
                    jobStreamSesssion.setEnded(dbItemJobStreamHistory.getEnded());
                    jobStreamSesssion.setRunning(dbItemJobStreamHistory.getRunning());
                    jobStreamSesssion.setJobschedulerId(dbItemJobStreamHistory.getSchedulerId());
                    jobStreamSesssion.setJobstreamTasks(new ArrayList<JobStreamTask>());
                    filterJobStreamTaskContext.setJobstreamHistoryId(dbItemJobStreamHistory.getContextId());
                    List<DBItemJobStreamTaskContext> listOfTaskContexts = dbLayerJobStreamsTaskContext.getJobStreamStarterJobsList(
                            filterJobStreamTaskContext, 0);
                    if (listOfTaskContexts.size() > 0) {
                        jobStreamSesssion.setJobStream(listOfTaskContexts.get(0).getJobStream());
                        try {
                            checkFolderPermissions(listOfTaskContexts.get(0).getJob());
                        } catch (JocFolderPermissionsException e) {
                            LOGGER.debug("Folder permission for " + listOfTaskContexts.get(0).getJob() + " is missing. Session " + jobStreamSesssion
                                    .getSession() + " ignored.");
                        }
                    }

                    for (DBItemJobStreamTaskContext dbItemJobStreamTaskContext : listOfTaskContexts) {
                        JobStreamTask jobStreamTask = new JobStreamTask();
                        jobStreamTask.setCreated(dbItemJobStreamTaskContext.getCreated());
                        jobStreamTask.setId(dbItemJobStreamTaskContext.getId());
                        jobStreamTask.setJob(dbItemJobStreamTaskContext.getJob());
                        jobStreamTask.setTaskId(dbItemJobStreamTaskContext.getTaskId());
                        jobStreamSesssion.getJobstreamTasks().add(jobStreamTask);
                    }
                    jobStreamSesssions.getJobstreamSessions().add(jobStreamSesssion);
                }
            }

            return JOCDefaultResponse.responseStatus200(jobStreamSesssions);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse setRunningJobStreamSessions(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamSessionsFilter jobStreamSessionFilter = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamSessionFilter = Globals.objectMapper.readValue(filterBytes, JobStreamSessionsFilter.class);

            if (jobStreamSessionFilter.getJobschedulerId() == null) {
                jobStreamSessionFilter.setJobschedulerId("");
            }

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_JOBSTREAM_SESSIONS_RUNNING, jobStreamSessionFilter, accessToken,
                    jobStreamSessionFilter.getJobschedulerId(), getPermissonsJocCockpit(jobStreamSessionFilter.getJobschedulerId(), accessToken)
                            .getJobStream().isSetView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_JOBSTREAM_SESSIONS_RUNNING);
            FilterJobStreamHistory filterJobStreamHistory = new FilterJobStreamHistory();

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            FilterJobStreams filterJobStreams = new FilterJobStreams();

            if (jobStreamSessionFilter.getJobStream() != null) {
                filterJobStreams.setJobStream(jobStreamSessionFilter.getJobStream());
                filterJobStreams.setSchedulerId(jobStreamSessionFilter.getJobschedulerId());

                List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
                if (listOfJobStreams.size() > 0) {
                    filterJobStreamHistory.setJobStreamId(listOfJobStreams.get(0).getId());
                } else {
                    throw new JocMissingRequiredParameterException(String.format("not found: jobstream " + jobStreamSessionFilter.getJobStream()
                            + " not found"));
                }
            }

            if (filterJobStreamHistory.getJobStreamId() == null) {
                filterJobStreamHistory.setJobStreamId(jobStreamSessionFilter.getJobStreamId());
            }

            for (String session : jobStreamSessionFilter.getSession()) {
                filterJobStreamHistory.addContextId(session);
            }

            if ((filterJobStreamHistory.getJobStreamId() == null) && (filterJobStreamHistory.getListContextIds() == null)) {
                throw new JocMissingRequiredParameterException(String.format("undefined: jobstream, jobstreamId or sessionId required"));
            }

            boolean valueRunning = ("running".equals(jobStreamSessionFilter.getStatus()));

            sosHibernateSession.setAutoCommit(false);
            sosHibernateSession.beginTransaction();

            DBLayerJobStreamHistory dbLayerJobStreamHistory = new DBLayerJobStreamHistory(sosHibernateSession);

            filterJobStreamHistory.setStartedFrom(JobSchedulerDate.getDateFrom(jobStreamSessionFilter.getDateFrom(), jobStreamSessionFilter
                    .getTimeZone()));
            filterJobStreamHistory.setStartedTo(JobSchedulerDate.getDateTo(jobStreamSessionFilter.getDateTo(), jobStreamSessionFilter.getTimeZone()));
            filterJobStreamHistory.setJobStreamStarter(jobStreamSessionFilter.getJobStreamStarterId());
            filterJobStreamHistory.setSchedulerId(jobStreamSessionFilter.getJobschedulerId());

            dbLayerJobStreamHistory.updateRunning(filterJobStreamHistory, valueRunning);
            sosHibernateSession.commit();
            notifyEventHandler(accessToken);

            return JOCDefaultResponse.responseStatusJSOk(new Date());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ResetJobStreamImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();

        customEventsUtil.addEvent("InitConditionResolver", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }

}