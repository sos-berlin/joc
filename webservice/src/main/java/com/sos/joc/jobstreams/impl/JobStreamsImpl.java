package com.sos.joc.jobstreams.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemJobStreamParameter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarterJob;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamParameters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamStarters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreams;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamsStarterJobs;
import com.sos.jitl.jobstreams.db.FilterJobStreamParameters;
import com.sos.jitl.jobstreams.db.FilterJobStreamStarterJobs;
import com.sos.jitl.jobstreams.db.FilterJobStreamStarters;
import com.sos.jitl.jobstreams.db.FilterJobStreams;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobstreams.JobStreamMigrator;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobstreams.resource.IJobStreamsResource;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamJob;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreams;
import com.sos.joc.model.jobstreams.JobStreamsFilter;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamsImpl extends JOCResourceImpl implements IJobStreamsResource {

    private static final String API_CALL_LIST_JOBSTREAM = "./jobstreams/list_jobstreams";
    private static final String API_CALL_ADD_JOBSTREAM = "./jobstreams/add_jobstream";
    private static final String API_CALL_DELETE_JOBSTREAM = "./jobstreams/delete_jobstreams";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamsImpl.class);

    @Override
    public JOCDefaultResponse getJobStreams(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsFilter.class);
            JobStreamsFilter jobStreamsFilter = Globals.objectMapper.readValue(filterBytes, JobStreamsFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_LIST_JOBSTREAM, jobStreamsFilter, accessToken, jobStreamsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamsFilter.getJobschedulerId(), accessToken).getJobStream().getView().isEventlist());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_LIST_JOBSTREAM);

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            DBLayerJobStreamsStarterJobs dbLayerJobStreamsStarterJobs = new DBLayerJobStreamsStarterJobs(sosHibernateSession);
            DBLayerJobStreamParameters dbLayerJobStreamParameters = new DBLayerJobStreamParameters(sosHibernateSession);

            FilterJobStreams filterJobStreams = new FilterJobStreams();
            FilterJobStreamStarters filterJobStreamStarters = new FilterJobStreamStarters();
            FilterJobStreamStarterJobs filterJobStreamStarterJobs = new FilterJobStreamStarterJobs();
            FilterJobStreamParameters filterJobStreamParameters = new FilterJobStreamParameters();

            filterJobStreams.setJobStream(jobStreamsFilter.getJobStream());
            filterJobStreams.setSchedulerId(jobStreamsFilter.getJobschedulerId());
            filterJobStreams.setStatus(jobStreamsFilter.getStatus());
            filterJobStreams.setFolder(jobStreamsFilter.getFolder());

            JobStreamMigrator jobStreamMigrator = new JobStreamMigrator();
            if (jobStreamMigrator.migrate(sosHibernateSession)) {
                notifyEventHandler(accessToken);
            }

            List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, jobStreamsFilter.getLimit());
            JobStreams jobStreams = new JobStreams();
            jobStreams.setDeliveryDate(new Date());

            for (DBItemJobStream dbItemJobStream : listOfJobStreams) {

                try {
                    checkFolderPermissions(dbItemJobStream.getFolder() +"/item");
                } catch (JocFolderPermissionsException e) {
                    LOGGER.debug("Folder permission for " + dbItemJobStream.getFolder() + " is missing. Job stream " + dbItemJobStream.getJobStream()
                            + " ignored.");
                    continue;
                }

                JobStream jobStream = new JobStream();
                jobStream.setJobStreamId(dbItemJobStream.getId());
                jobStream.setDeliveryDate(new Date());
                jobStream.setJobschedulerId(dbItemJobStream.getSchedulerId());
                jobStream.setJobStream(dbItemJobStream.getJobStream());
                jobStream.setState(dbItemJobStream.getState());
                jobStream.setFolder(dbItemJobStream.getFolder());

                filterJobStreamStarters.setJobStreamId(dbItemJobStream.getId());
                for (DBItemJobStreamStarter dbItemJobStreamStarter : dbLayerJobStreamStarters.getJobStreamStartersList(filterJobStreamStarters, 0)) {
                    JobStreamStarter jobstreamStarter = new JobStreamStarter();
                    jobstreamStarter.setJobStreamStarterId(dbItemJobStreamStarter.getId());
                    if (dbItemJobStreamStarter.getRunTime() != null) {
                        RunTime runTime = Globals.objectMapper.readValue(dbItemJobStreamStarter.getRunTime(), RunTime.class);
                        jobstreamStarter.setRunTime(runTime);
                    }
                    jobstreamStarter.setState(dbItemJobStreamStarter.getState());
                    jobstreamStarter.setTitle(dbItemJobStreamStarter.getTitle());
                    jobstreamStarter.setEndOfJobStream(dbItemJobStreamStarter.getEndOfJobStream());
                    jobstreamStarter.setRequiredJob(dbItemJobStreamStarter.getRequiredJob());
                    jobstreamStarter.setNextStart(dbItemJobStreamStarter.getNextStart());
                    filterJobStreamParameters.setJobStreamStarterId(dbItemJobStreamStarter.getId());
                    filterJobStreamStarterJobs.setJobStreamStarter(dbItemJobStreamStarter.getId());
                    for (DBItemJobStreamStarterJob dbItemJobStreamStarterJobs : dbLayerJobStreamsStarterJobs.getJobStreamStarterJobsList(
                            filterJobStreamStarterJobs, 0)) {
                        JobStreamJob jobStreamJob = new JobStreamJob();
                        jobStreamJob.setJob(dbItemJobStreamStarterJobs.getJob());
                        jobStreamJob.setStartDelay(dbItemJobStreamStarterJobs.getDelay());
                        jobStreamJob.setSkipOutCondition(dbItemJobStreamStarterJobs.getSkipOutCondition());
                        jobStreamJob.setNextPeriod(dbItemJobStreamStarterJobs.getNextPeriod());
                        jobstreamStarter.getJobs().add(jobStreamJob);
                    }
                    for (DBItemJobStreamParameter dbItemJobStreamParameter : dbLayerJobStreamParameters.getJobStreamParametersList(
                            filterJobStreamParameters, 0)) {
                        NameValuePair param = new NameValuePair();
                        param.setName(dbItemJobStreamParameter.getName());
                        param.setValue(dbItemJobStreamParameter.getValue());
                        jobstreamStarter.getParams().add(param);
                    }
                    jobStream.getJobstreamStarters().add(jobstreamStarter);
                }

                jobStreams.getJobstreams().add(jobStream);

            }
            return JOCDefaultResponse.responseStatus200(jobStreams);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse addJobStream(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            JobStream jobStream = Globals.objectMapper.readValue(filterBytes, JobStream.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM, jobStream, accessToken, jobStream.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobStream", jobStream.getJobStream());
            
            for (JobStreamStarter jobstreamStarter : jobStream.getJobstreamStarters()) {
                for (JobStreamJob jobStreamJob : jobstreamStarter.getJobs()) {
                    try {
                        checkFolderPermissions(jobStreamJob.getJob());
                    } catch (JocFolderPermissionsException e) {
                        LOGGER.debug("Folder permission for " + jobStreamJob.getJob() + " is missing. job stream " + jobStream.getJobStream() + " will not be added ");
                        jobStream.setJobStream("");
                    }
                 }
            }
           
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM);
            sosHibernateSession.setAutoCommit(false);
            
            if (jobStream.getOldJobStreamName() != null) {
                DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
                sosHibernateSession.beginTransaction();
                FilterJobStreams filterJobStreams = new FilterJobStreams();
                filterJobStreams.setSchedulerId(jobStream.getJobschedulerId());
                filterJobStreams.setJobStream(jobStream.getOldJobStreamName());
                filterJobStreams.setFolder(jobStream.getFolder());
                dbLayerJobStreams.deleteCascading(filterJobStreams, false);
            }
            
            sosHibernateSession.beginTransaction();
            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            dbLayerJobStreams.deleteInsert(jobStream, dbItemInventoryInstance.getTimeZone());
            sosHibernateSession.commit();

            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(jobStream);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse deleteJobStream(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            JobStream jobStream = Globals.objectMapper.readValue(filterBytes, JobStream.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE_JOBSTREAM, jobStream, accessToken, jobStream.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                checkRequiredParameter("jobStreamId", jobStream.getJobStreamId());
            } catch (JocMissingRequiredParameterException e) {
                checkRequiredParameter("jobStream", jobStream.getJobStream());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM);
            sosHibernateSession.setAutoCommit(false);
            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            FilterJobStreams filterJobStreams = new FilterJobStreams();
            filterJobStreams.setSchedulerId(jobStream.getJobschedulerId());
            filterJobStreams.setJobStreamId(jobStream.getJobStreamId());
            filterJobStreams.setJobStream(jobStream.getJobStream());
            filterJobStreams.setFolder(jobStream.getFolder());
            dbLayerJobStreams.deleteCascading(filterJobStreams, true);

            sosHibernateSession.commit();

            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(jobStream);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(EditInConditionsImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }
}