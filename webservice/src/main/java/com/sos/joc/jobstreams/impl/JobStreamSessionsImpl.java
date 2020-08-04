package com.sos.joc.jobstreams.impl;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.sos.joc.exceptions.JocFolderPermissionsException;
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

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            FilterJobStreams filterJobStreams = new FilterJobStreams();

            if (jobStreamSessionFilter.getJobStream() != null) {
                filterJobStreams.setJobStream(jobStreamSessionFilter.getJobStream());
                filterJobStreams.setSchedulerId(jobStreamSessionFilter.getJobschedulerId());

                List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
                if (listOfJobStreams.size() > 0) {
                    filterJobStreamHistory.setJobStreamId(listOfJobStreams.get(0).getId());
                }
            }

            if (filterJobStreamHistory.getJobStreamId() == null) {
                filterJobStreamHistory.setJobStreamId(jobStreamSessionFilter.getJobStreamId());
            }

            DBLayerJobStreamHistory dbLayerJobStreamHistory = new DBLayerJobStreamHistory(sosHibernateSession);
            if ("running".equals(jobStreamSessionFilter.getStatus())) {
                filterJobStreamHistory.setRunning(true);
            }
            int limit = 0;
            if (jobStreamSessionFilter.getLimit() != null) {
                limit = jobStreamSessionFilter.getLimit();
            }
            filterJobStreamHistory.setContextId(jobStreamSessionFilter.getSession());
            filterJobStreamHistory.setStartedFrom(JobSchedulerDate.getDateFrom(jobStreamSessionFilter.getDateFrom(), jobStreamSessionFilter
                    .getTimeZone()));
            filterJobStreamHistory.setStartedTo(JobSchedulerDate.getDateTo(jobStreamSessionFilter.getDateTo(), jobStreamSessionFilter.getTimeZone()));
            filterJobStreamHistory.setJobStreamStarter(jobStreamSessionFilter.getJobStreamStarterId());
            List<DBItemJobStreamHistory> listOfSessions = dbLayerJobStreamHistory.getJobStreamHistoryList(filterJobStreamHistory, limit);

            DBLayerJobStreamsTaskContext dbLayerJobStreamsTaskContext = new DBLayerJobStreamsTaskContext(sosHibernateSession);
            FilterJobStreamTaskContext filterJobStreamTaskContext = new FilterJobStreamTaskContext();
            DBLayerJobStreamStarters dbLayerJobStreamStarter = new DBLayerJobStreamStarters(sosHibernateSession);
             
            
            JobStreamSessions jobStreamSesssions = new JobStreamSessions();
            jobStreamSesssions.setDeliveryDate(new Date());
            jobStreamSesssions.setJobstreamSessions(new ArrayList<JobStreamSesssion>());
            for (DBItemJobStreamHistory dbItemJobStreamHistory : listOfSessions) {
                JobStreamSesssion jobStreamSesssion = new JobStreamSesssion();
                jobStreamSesssion.setJobStreamStarter(new JobStreamStarter());
                jobStreamSesssion.setId(dbItemJobStreamHistory.getId());
                jobStreamSesssion.setJobStreamId(dbItemJobStreamHistory.getJobStream());
                DBItemJobStreamStarter dbItemJobStreamStarter = dbLayerJobStreamStarter.getJobStreamStartersDbItem(dbItemJobStreamHistory.getJobStreamStarter());
                if (dbItemJobStreamStarter != null){
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

            return JOCDefaultResponse.responseStatus200(jobStreamSesssions);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}