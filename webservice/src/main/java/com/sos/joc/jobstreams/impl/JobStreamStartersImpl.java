package com.sos.joc.jobstreams.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
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
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.EditJobStreamStarterAudit;
import com.sos.joc.classes.audit.StartJobAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IJobStreamStartersResource;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamStartData;
import com.sos.joc.model.jobstreams.JobStreamStarted;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreamStarterExist;
import com.sos.joc.model.jobstreams.JobStreamStarters;
import com.sos.joc.model.jobstreams.JobStreamStartersFilter;
import com.sos.joc.model.jobstreams.JobStreamStartersSelector;
import com.sos.joc.model.jobstreams.JobStreamStartersStarted;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joe.common.XmlSerializer;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamStartersImpl extends JOCResourceImpl implements IJobStreamStartersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamStartersImpl.class);
    private static final String API_CALL_ADD_JOBSTREAM_STARTER = "./jobstreams/edit_jobstream_starter";
    private static final String API_CALL_GET_JOBSTREAM_STARTER = "./jobstreams/get_jobstream_starter";
    private static final String API_CALL_START = "jobstreams/start_jobstream";

    private void addCalendarUsage(SOSHibernateSession sosHibernateSession, List<DBItemInventoryInstance> clusterMembers, String jobStream,
            JobStreamStarter jobstreamStarter) throws Exception {

        List<Calendar> listOfCalendars = null;
        String runTimeXml = "";
        if ((jobstreamStarter.getRunTime() != null)) {
            RunTime runTime = XmlSerializer.serializeAbstractSchedule(jobstreamStarter.getRunTime());
            runTimeXml = Globals.xmlMapper.writeValueAsString(runTime);
            if (runTime.getCalendars() != null) {
                Calendars calendars = Globals.objectMapper.readValue(runTime.getCalendars(), (Calendars.class));
                listOfCalendars = calendars.getCalendars();
            } else {
                listOfCalendars = new ArrayList<Calendar>();
            }
        }

        List<String> listOfCalendarPaths = new ArrayList<String>();

        for (Calendar calendar : listOfCalendars) {
            listOfCalendarPaths.add(calendar.getBasedOn());
        }

        CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(sosHibernateSession, dbItemInventoryInstance.getSchedulerId(),
                CalendarObjectType.JOBSTREAM, jobStream, runTimeXml, listOfCalendars);
        calendarUsedByWriter.updateUsedByList(listOfCalendarPaths);
        CalendarEvent calEvt = calendarUsedByWriter.getCalendarEvent();
        if (calEvt != null) {
            if (clusterMembers != null) {
                SendCalendarEventsUtil.sendEvent(calEvt, clusterMembers, getAccessToken());
            } else {
                SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
            }
        }
    }

    @Override
    public JOCDefaultResponse editJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStarters jobStreamStarters = null;
        try {
            // JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarters = Globals.objectMapper.readValue(filterBytes, JobStreamStarters.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarters, accessToken, jobStreamStarters
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobStreamId", jobStreamStarters.getJobStreamId());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM_STARTER);
            sosHibernateSession.setAutoCommit(false);

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            DBItemJobStream dbItemJobStream = dbLayerJobStreams.getJobStreamsDbItem(jobStreamStarters.getJobStreamId());

            if (dbItemJobStream == null) {
                throw new JobSchedulerObjectNotExistException(String.format("Could not find jobstream with ith '%1'", jobStreamStarters
                        .getJobStreamId()));
            }

            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerJobStreamStarters.saveOrUpdate(jobStreamStarters, dbItemInventoryInstance.getTimeZone());

            List<DBItemInventoryInstance> clusterMembers = null;

            if ("active".equals(dbItemInventoryInstance.getClusterType())) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(sosHibernateSession);
                clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(jobStreamStarters.getJobschedulerId());
            }
            sosHibernateSession.commit();

            for (JobStreamStarter jobStreamStarter : jobStreamStarters.getJobstreamStarters()) {
                addCalendarUsage(sosHibernateSession, clusterMembers, dbItemJobStream.getJobStream(), jobStreamStarter);

                EditJobStreamStarterAudit editJobStreamStartersAudit = new EditJobStreamStarterAudit(dbItemJobStream.getJobStream(),
                        jobStreamStarters, jobStreamStarter);
                logAuditMessage(editJobStreamStartersAudit);
                storeAuditLogEntry(editJobStreamStartersAudit);
            }

            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Add JobStreamStarter: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Data are stored in Database");
            }
            return JOCDefaultResponse.responseStatus200(jobStreamStarters);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse getJobStreamStarter(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStartersFilter jobStreamStarterFilter = null;
        try {
            // JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarterFilter = Globals.objectMapper.readValue(filterBytes, JobStreamStartersFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarterFilter, accessToken, jobStreamStarterFilter
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarterFilter.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_GET_JOBSTREAM_STARTER);

            if (jobStreamStarterFilter.getJobStreamId() != null) {
                DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
                DBItemJobStream dbItemJobStream = dbLayerJobStreams.getJobStreamsDbItem(jobStreamStarterFilter.getJobStreamId());

                if (dbItemJobStream == null) {
                    throw new JobSchedulerObjectNotExistException(String.format("Could not find jobstream with ith '%1'", jobStreamStarterFilter
                            .getJobStreamId()));
                } else {
                    jobStreamStarterFilter.setJobStream(dbItemJobStream.getJobStream());
                }
            }

            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            FilterJobStreamStarters filterJobStreamStarters = new FilterJobStreamStarters();
            filterJobStreamStarters.setJobStream(jobStreamStarterFilter.getJobStream());
            filterJobStreamStarters.setStarterName(jobStreamStarterFilter.getStarterName());
            List<DBItemJobStreamStarter> l = dbLayerJobStreamStarters.getJobStreamStartersList(filterJobStreamStarters, 0);

            JobStreamStarterExist jobStreamStarterExist = new JobStreamStarterExist();
            if (l.size() > 0) {
                jobStreamStarterExist.setExist(true);
                jobStreamStarterExist.setJobStreamStarterId(l.get(0).getId());
                jobStreamStarterExist.setNextStart(l.get(0).getNextStart());
                jobStreamStarterExist.setRequiredJob(l.get(0).getRequiredJob());
                jobStreamStarterExist.setStarterName(l.get(0).getStarterName());
                jobStreamStarterExist.setState(l.get(0).getState());
                jobStreamStarterExist.setTitle(l.get(0).getTitle());
            } else {
                jobStreamStarterExist.setExist(false);
                jobStreamStarterExist.setStarterName(filterJobStreamStarters.getStarterName());
                jobStreamStarterExist.setEndOfJobStream(filterJobStreamStarters.getJobStream());
            }
            return JOCDefaultResponse.responseStatus200(jobStreamStarterExist);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse deleteJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStarters jobStreamStarters = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStarters = Globals.objectMapper.readValue(filterBytes, JobStreamStarters.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_JOBSTREAM_STARTER, jobStreamStarters, accessToken, jobStreamStarters
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStarters.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobStreamId", jobStreamStarters.getJobStreamId());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_JOBSTREAM_STARTER);
            sosHibernateSession.setAutoCommit(false);

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            DBItemJobStream dbItemJobStream = dbLayerJobStreams.getJobStreamsDbItem(jobStreamStarters.getJobStreamId());

            if (dbItemJobStream == null) {
                throw new JobSchedulerObjectNotExistException(String.format("Could not find jobstream with ith '%1'", jobStreamStarters
                        .getJobStreamId()));
            }

            DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
            sosHibernateSession.beginTransaction();
            dbLayerJobStreamStarters.deleteStarters(jobStreamStarters, dbItemInventoryInstance.getTimeZone());
            sosHibernateSession.commit();

            for (JobStreamStarter jobStreamStarter : jobStreamStarters.getJobstreamStarters()) {
                EditJobStreamStarterAudit editJobStreamStartersAudit = new EditJobStreamStarterAudit(dbItemJobStream.getJobStream(),
                        jobStreamStarters, jobStreamStarter);
                logAuditMessage(editJobStreamStartersAudit);
                storeAuditLogEntry(editJobStreamStartersAudit);
            }

            try {
                notifyEventHandler(accessToken);
            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Add JobStreamStarter: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running. Data are stored in Database");
            }
            return JOCDefaultResponse.responseStatus200(jobStreamStarters);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse startJobStreamStarters(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        JobStreamStartersSelector jobStreamStartersSelector = null;
        JobStreamStartersStarted jobStreamStartesStarted = new JobStreamStartersStarted();
        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            jobStreamStartersSelector = Globals.objectMapper.readValue(filterBytes, JobStreamStartersSelector.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_START, jobStreamStartersSelector, accessToken, jobStreamStartersSelector
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamStartersSelector.getJobschedulerId(), accessToken).getJobStream().getView()
                            .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            jobStreamStartesStarted.setJobschedulerId(jobStreamStartersSelector.getJobschedulerId());

            try {
                sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_START);
                jobStreamStartesStarted.setJobstreamStarted(new ArrayList<JobStreamStarted>());
                DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
                for (JobStreamStartData jobStreamStartData : jobStreamStartersSelector.getJobstreamStarters()) {
                    if ((jobStreamStartData.getJobStreamStarterId() != null) || (jobStreamStartData.getStarterName() != null)) {

                        JobStreamStarted jobStreamStarted = new JobStreamStarted();

                        DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
                        FilterJobStreamStarters filter = new FilterJobStreamStarters();
                        filter.setId(jobStreamStartData.getJobStreamStarterId());
                        filter.setStarterName(jobStreamStartData.getStarterName());
                        List<DBItemJobStreamStarter> listOfStartes = dbLayerJobStreamStarters.getJobStreamStartersList(filter, 0);
                        if (listOfStartes.size() > 0) {
                            String jobStream = "";
                            DBItemJobStream dbItemJobStream = dbLayerJobStreams.getJobStreamsDbItem(listOfStartes.get(0).getJobStream());
                            if (dbItemJobStream != null) {
                                jobStream = dbLayerJobStreams.getJobStreamsDbItem(listOfStartes.get(0).getJobStream()).getJobStream();
                            }

                            jobStreamStarted.setStarterExists(true);
                            jobStreamStarted.setStarterName(listOfStartes.get(0).getStarterName());
                            jobStreamStarted.setJobStreamStarterId(listOfStartes.get(0).getId());

                            jobStreamStartData.setJobStreamStarterId(listOfStartes.get(0).getId());
                            setAudit(jobStream, jobStreamStartData, jobStreamStartersSelector);
                            jobStreamStarted.setParams(jobStreamStartData.getParams());
                            UUID contextId = UUID.randomUUID();
                            String session = contextId.toString();
                            jobStreamStarted.setSession(session);
                            notifyEventHandlerStart(accessToken, contextId, jobStreamStartData);
                        } else {
                            jobStreamStarted.setParams(null);
                            jobStreamStarted.setJobStreamStarterId(jobStreamStartData.getJobStreamStarterId());
                            jobStreamStarted.setStarterName(jobStreamStartData.getStarterName());
                            jobStreamStarted.setStarterExists(false);
                        }
                        jobStreamStartesStarted.getJobstreamStarted().add(jobStreamStarted);

                    }
                }

            } catch (JobSchedulerConnectionRefusedException e) {
                LOGGER.warn(
                        "Add JobStreamStarter: Could not send custom event to Job Stream Event Handler as JobScheduler seems not to be up and running.");
            }
            return JOCDefaultResponse.responseStatus200(jobStreamStartesStarted);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void setAudit(String jobStream, JobStreamStartData jobStreamStartData, JobStreamStartersSelector jobStreamStartersSelector)
            throws JocConfigurationException, DBOpenSessionException, SOSHibernateException {
        SOSHibernateSession sosHibernateSession = null;
        try {
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_START);
            DBLayerJobStreamParameters dbLayerJobStreamParameters = new DBLayerJobStreamParameters(sosHibernateSession);
            DBLayerJobStreamsStarterJobs dbLayerJobStreamStarterJobs = new DBLayerJobStreamsStarterJobs(sosHibernateSession);
            FilterJobStreamStarterJobs filter = new FilterJobStreamStarterJobs();
            filter.setJobStreamStarter(jobStreamStartData.getJobStreamStarterId());

            List<DBItemJobStreamStarterJob> listOfStartedJobs = dbLayerJobStreamStarterJobs.getJobStreamStarterJobsList(filter, 0);

            for (DBItemJobStreamStarterJob dbItemJobStreamStarterJob : listOfStartedJobs) {
                FilterJobStreamParameters filterJobStreamParameters = new FilterJobStreamParameters();
                filterJobStreamParameters.setJobStreamStarterId(jobStreamStartData.getJobStreamStarterId());
                List<DBItemJobStreamParameter> listOfJobStreamParameters = dbLayerJobStreamParameters.getJobStreamParametersList(
                        filterJobStreamParameters, 0);

                StartJob startJob = new StartJob();
                startJob.setAt("now");
                startJob.setJob(dbItemJobStreamStarterJob.getJob());
                startJob.setJobStream(jobStream);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (DBItemJobStreamParameter dbItemJobStreamParameter : listOfJobStreamParameters) {
                    NameValuePair param = new NameValuePair();
                    param.setName(dbItemJobStreamParameter.getName());
                    param.setValue(dbItemJobStreamParameter.getValue());
                    params.add(param);
                }
                startJob.setParams(params);

                StartJobs startJobs = new StartJobs();
                startJobs.setAuditLog(jobStreamStartersSelector.getAuditLog());
                startJobs.setJobschedulerId(jobStreamStartersSelector.getJobschedulerId());

                StartJobAudit jobAudit = new StartJobAudit(startJob, startJobs);
                logAuditMessage(jobAudit);
                storeAuditLogEntry(jobAudit);

            }
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandlerStart(String accessToken, UUID contextId, JobStreamStartData jobStreamStartData) throws JsonProcessingException,
            JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(JobStreamStartersImpl.class.getName());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("starterId", String.valueOf(jobStreamStartData.getJobStreamStarterId()));

        parameters.put("session", contextId.toString());

        for (NameValuePair param : jobStreamStartData.getParams()) {
            parameters.put("#" + param.getName(), param.getValue());
        }

        customEventsUtil.addEvent("StartJobStream", parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(JobStreamStartersImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }
}