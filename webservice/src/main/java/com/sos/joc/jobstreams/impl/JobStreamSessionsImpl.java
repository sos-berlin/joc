package com.sos.joc.jobstreams.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.jobstreams.db.DBItemJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBItemJobStreamTaskContext;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamsTaskContext;
import com.sos.jitl.jobstreams.db.FilterJobStreamHistory;
import com.sos.jitl.jobstreams.db.FilterJobStreamTaskContext;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.jobstreams.resource.IJobStreamSessionsResource;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamSessions;
import com.sos.joc.model.jobstreams.JobStreamSessionsFilter;
import com.sos.joc.model.jobstreams.JobStreamSesssion;
import com.sos.joc.model.jobstreams.JobStreamTask;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamSessionsImpl extends JOCResourceImpl implements IJobStreamSessionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamSessionsImpl.class);
    private static final String API_CALL_ADD_JOBSTREAM_STARTER = "./jobstreams/sessions";

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
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamSessionFilter, accessToken, jobStreamSessionFilter
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamSessionFilter.getJobschedulerId(), accessToken).getJobStream()
                            .isSetView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

       
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM_STARTER);
            DBLayerJobStreamHistory dbLayerJobStreamHistory = new DBLayerJobStreamHistory(sosHibernateSession);
            FilterJobStreamHistory filterJobStreamHistory = new FilterJobStreamHistory();
            if ("running".equals(jobStreamSessionFilter.getStatus())) {
                filterJobStreamHistory.setRunning(true);
            }
            int limit = 0;
            if (jobStreamSessionFilter.getLimit() != null){
                limit = jobStreamSessionFilter.getLimit();
            }
            filterJobStreamHistory.setContextId(jobStreamSessionFilter.getSession());
            filterJobStreamHistory.setJobStreamId(jobStreamSessionFilter.getJobStreamId());
            filterJobStreamHistory.setStartedFrom(JobSchedulerDate.getDateTo(jobStreamSessionFilter.getDateFrom(), jobStreamSessionFilter
                    .getTimeZone()));
            filterJobStreamHistory.setStartedTo(JobSchedulerDate.getDateTo(jobStreamSessionFilter.getDateTo(), jobStreamSessionFilter.getTimeZone()));
            filterJobStreamHistory.setJobStreamStarter(jobStreamSessionFilter.getJobStreamStarterId());
            List<DBItemJobStreamHistory> listOfSessions = dbLayerJobStreamHistory.getJobStreamHistoryList(filterJobStreamHistory, limit);

            DBLayerJobStreamsTaskContext dbLayerJobStreamsTaskContext = new DBLayerJobStreamsTaskContext(sosHibernateSession);
            FilterJobStreamTaskContext filterJobStreamTaskContext = new FilterJobStreamTaskContext();

            JobStreamSessions jobStreamSesssions = new JobStreamSessions();
            jobStreamSesssions.setDeliveryDate(new Date());
            jobStreamSesssions.setJobstreamSessions(new ArrayList<JobStreamSesssion>());
            for (DBItemJobStreamHistory dbItemJobStreamHistory : listOfSessions) {
                JobStreamSesssion jobStreamSesssion = new JobStreamSesssion();
                jobStreamSesssion.setId(dbItemJobStreamHistory.getId());
                jobStreamSesssion.setJobStreamId(dbItemJobStreamHistory.getJobStream());
                jobStreamSesssion.setSession(dbItemJobStreamHistory.getContextId());
                jobStreamSesssion.setStarted(dbItemJobStreamHistory.getStarted());
                jobStreamSesssion.setEnded(dbItemJobStreamHistory.getEnded());
                jobStreamSesssion.setRunning(dbItemJobStreamHistory.getRunning());
                jobStreamSesssion.setJobstreamTasks(new ArrayList<JobStreamTask>());
                filterJobStreamTaskContext.setJobstreamHistoryId(dbItemJobStreamHistory.getContextId());
                List<DBItemJobStreamTaskContext> listOfTaskContexts = dbLayerJobStreamsTaskContext.getJobStreamStarterJobsList(
                        filterJobStreamTaskContext, 0);
                for (DBItemJobStreamTaskContext dbItemJobStreamTaskContext : listOfTaskContexts) {
                    JobStreamTask jobStreamTask = new JobStreamTask();
                    jobStreamTask.setCreated(dbItemJobStreamTaskContext.getCreated());
                    jobStreamTask.setId(dbItemJobStreamTaskContext.getId());
                    jobStreamTask.setJob(dbItemJobStreamTaskContext.getJob());
                    jobStreamSesssion.setJobStream(dbItemJobStreamTaskContext.getJobStream());
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