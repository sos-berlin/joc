package com.sos.joc.jobstreams.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.eventhandler.handler.EventHandlerSettings;
import com.sos.jitl.jobstreams.classes.JSEventKey;
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemInConditionWithCommand;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemJobStreamParameter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarterJob;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBItemOutConditionWithConfiguredEvent;
import com.sos.jitl.jobstreams.db.DBLayerEvents;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamHistory;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamParameters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamStarters;
import com.sos.jitl.jobstreams.db.DBLayerJobStreams;
import com.sos.jitl.jobstreams.db.DBLayerJobStreamsStarterJobs;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterJobStreamParameters;
import com.sos.jitl.jobstreams.db.FilterJobStreamStarterJobs;
import com.sos.jitl.jobstreams.db.FilterJobStreamStarters;
import com.sos.jitl.jobstreams.db.FilterJobStreams;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobstreams.resolver.JSInCondition;
import com.sos.jobstreams.resolver.JSInConditionCommand;
import com.sos.jobstreams.resolver.JSJobConditionKey;
import com.sos.jobstreams.resolver.JSJobInConditions;
import com.sos.jobstreams.resolver.JSJobOutConditions;
import com.sos.jobstreams.resolver.JSOutCondition;
import com.sos.jobstreams.resolver.JSOutConditionEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.EditJobStreamsAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.jobstreams.JobStreamMigrator;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobstreams.resource.IJobStreamsResource;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.jobstreams.ConditionExpression;
import com.sos.joc.model.jobstreams.InCondition;
import com.sos.joc.model.jobstreams.InConditionCommand;
import com.sos.joc.model.jobstreams.JobStream;
import com.sos.joc.model.jobstreams.JobStreamJob;
import com.sos.joc.model.jobstreams.JobStreamLocation;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreams;
import com.sos.joc.model.jobstreams.JobStreamsFilter;
import com.sos.joc.model.jobstreams.JobStreamsSelector;
import com.sos.joc.model.jobstreams.OutCondition;
import com.sos.joc.model.jobstreams.OutConditionEvent;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joe.common.XmlSerializer;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamsImpl extends JOCResourceImpl implements IJobStreamsResource {

    private static final String API_CALL_LIST_JOBSTREAM = "./jobstreams/list_jobstreams";
    private static final String API_CALL_EDIT_JOBSTREAM = "./jobstreams/edit_jobstream";
    private static final String API_CALL_ADD_JOBSTREAM = "./jobstreams/add_jobstream";
    private static final String API_CALL_DELETE_JOBSTREAM = "./jobstreams/delete_jobstreams";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamsImpl.class);

    private List<InCondition> getJobInconditions(SOSHibernateSession sosHibernateSession, String jobSchedulerId, String job)
            throws SOSHibernateException {

        List<InCondition> listOfInConditions = new ArrayList<InCondition>();
        DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);

        FilterInConditions filterInConditions = new FilterInConditions();

        filterInConditions.setJobSchedulerId(jobSchedulerId);
        filterInConditions.setJob(job);

        List<DBItemInConditionWithCommand> listOfInConditionsWithCommand = dbLayerInConditions.getInConditionsList(filterInConditions, 0);

        EventHandlerSettings settings = new EventHandlerSettings();
        settings.setSchedulerId(jobSchedulerId);
        JSJobInConditions jsJobInConditions = new JSJobInConditions();
        jsJobInConditions.setListOfJobInConditions(sosHibernateSession, null, listOfInConditionsWithCommand);

        JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
        jsJobConditionKey.setJob(job);
        jsJobConditionKey.setJobSchedulerId(jobSchedulerId);

        if (jsJobInConditions.getInConditions(jsJobConditionKey) != null) {
            for (JSInCondition jsInCondition : jsJobInConditions.getInConditions(jsJobConditionKey).getListOfInConditions().values()) {

                InCondition inCondition = new InCondition();
                ConditionExpression conditionExpression = new ConditionExpression();
                conditionExpression.setJobStreamEvents(null);
                conditionExpression.setExpression(jsInCondition.getExpression());

                inCondition.setConditionExpression(conditionExpression);
                inCondition.setJobStream(jsInCondition.getJobStream());
                inCondition.setId(jsInCondition.getId());
                inCondition.setMarkExpression(jsInCondition.isMarkExpression());
                inCondition.setSkipOutCondition(jsInCondition.isSkipOutCondition());

                for (JSInConditionCommand jsInConditionCommand : jsInCondition.getListOfInConditionCommand()) {
                    InConditionCommand inConditionCommand = new InConditionCommand();
                    inConditionCommand.setCommand(jsInConditionCommand.getCommand());
                    inConditionCommand.setCommandParam(jsInConditionCommand.getCommandParam());
                    inConditionCommand.setId(jsInConditionCommand.getId());
                    inCondition.getInconditionCommands().add(inConditionCommand);
                }
                listOfInConditions.add(inCondition);
            }
        }
        return listOfInConditions;

    }

    private List<OutCondition> getJobOutConditions(SOSHibernateSession sosHibernateSession, String jobSchedulerId, String job)
            throws SOSHibernateException {

        List<OutCondition> listOfOutConditions = new ArrayList<OutCondition>();

        DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);

        FilterOutConditions filterOutConditions = new FilterOutConditions();
        filterOutConditions.setJob(job);
        filterOutConditions.setJobSchedulerId(jobSchedulerId);

        List<DBItemOutConditionWithConfiguredEvent> listOfOutConditionsWithEvent = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
        JSJobOutConditions jsJobOutConditions = new JSJobOutConditions();
        jsJobOutConditions.setListOfJobOutConditions(listOfOutConditionsWithEvent);

        JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
        jsJobConditionKey.setJob(job);
        jsJobConditionKey.setJobSchedulerId(jobSchedulerId);

        if (jsJobOutConditions.getOutConditions(jsJobConditionKey) != null) {

            JSEventKey jsEventKey = new JSEventKey();

            for (JSOutCondition jsOutCondition : jsJobOutConditions.getOutConditions(jsJobConditionKey).getListOfOutConditions().values()) {

                OutCondition outCondition = new OutCondition();
                ConditionExpression conditionExpression = new ConditionExpression();
                conditionExpression.setJobStreamEvents(null);
                conditionExpression.setExpression(jsOutCondition.getExpression());

                outCondition.setConditionExpression(conditionExpression);
                outCondition.setJobStream(jsOutCondition.getJobStream());
                outCondition.setId(jsOutCondition.getId());

                for (JSOutConditionEvent jsOutConditionEvent : jsOutCondition.getListOfOutConditionEvent()) {
                    OutConditionEvent outConditionEvent = new OutConditionEvent();
                    outConditionEvent.setEvent(jsOutConditionEvent.getEventValue());
                    outConditionEvent.setCommand(jsOutConditionEvent.getCommand());
                    outConditionEvent.setGlobalEvent(jsOutConditionEvent.isGlobal());

                    jsEventKey.setGlobalEvent(jsOutConditionEvent.isGlobal());
                    jsEventKey.setSchedulerId(jobSchedulerId);
                    outConditionEvent.setId(jsOutConditionEvent.getId());
                    outCondition.getOutconditionEvents().add(outConditionEvent);
                }

                listOfOutConditions.add(outCondition);
            }
        }
        return listOfOutConditions;
    }

    private JobStreams getListOfJobstreams(SOSHibernateSession sosHibernateSession, FilterJobStreams filterJobStreams, Boolean collapsed)
            throws SOSHibernateException, JsonParseException, JsonMappingException, IOException {
        DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
        DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
        DBLayerJobStreamsStarterJobs dbLayerJobStreamsStarterJobs = new DBLayerJobStreamsStarterJobs(sosHibernateSession);
        DBLayerJobStreamParameters dbLayerJobStreamParameters = new DBLayerJobStreamParameters(sosHibernateSession);

        FilterJobStreamStarters filterJobStreamStarters = new FilterJobStreamStarters();
        FilterJobStreamStarterJobs filterJobStreamStarterJobs = new FilterJobStreamStarterJobs();
        FilterJobStreamParameters filterJobStreamParameters = new FilterJobStreamParameters();

        List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, filterJobStreams.getLimit());
        JobStreams jobStreams = new JobStreams();
        jobStreams.setDeliveryDate(new Date());

        for (DBItemJobStream dbItemJobStream : listOfJobStreams) {

            try {
                String p;
                if ("/".equals(dbItemJobStream.getFolder())) {
                    p = "/item";
                } else {
                    p = dbItemJobStream.getFolder() + "/item";
                }
                checkFolderPermissions(p);
            } catch (JocFolderPermissionsException e) {
                LOGGER.debug("Folder permission for " + dbItemJobStream.getFolder() + " is missing. Job stream " + dbItemJobStream.getJobStream()
                        + " ignored.");
                continue;
            }

            JobStream jobStream = new JobStream();
            jobStream.setJobs(new ArrayList<JobStreamJob>());
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
                    if (!collapsed) {
                        jobStreamJob.setInconditions(getJobInconditions(sosHibernateSession, jobStream.getJobschedulerId(), dbItemJobStreamStarterJobs
                                .getJob()));
                        jobStreamJob.setOutconditions(getJobOutConditions(sosHibernateSession, jobStream.getJobschedulerId(),
                                dbItemJobStreamStarterJobs.getJob()));
                    }

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

            if (!collapsed) {

                DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
                DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);

                Set<String> listOfJobs = new HashSet<String>();
                FilterOutConditions filterOutConditions = new FilterOutConditions();
                filterOutConditions.setJobStream(jobStream.getJobStream());
                filterOutConditions.setJobSchedulerId(jobStream.getJobschedulerId());
                List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
                for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
                    listOfJobs.add(dbItemOutCondition.getJob());
                }
                FilterInConditions filterInConditions = new FilterInConditions();
                filterInConditions.setJobStream(jobStream.getJobStream());
                filterInConditions.setJobSchedulerId(jobStream.getJobschedulerId());
                List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);
                for (DBItemInCondition dbItemInCondition : listOfInConditions) {
                    listOfJobs.add(dbItemInCondition.getJob());
                }

                List<JobStreamJob> jobStreamJobs = new ArrayList<JobStreamJob>();
                for (String job : listOfJobs) {
                    JobStreamJob jobStreamJob = new JobStreamJob();
                    jobStreamJob.setJob(job);
                    jobStreamJob.setInconditions(getJobInconditions(sosHibernateSession, jobStream.getJobschedulerId(), job));
                    jobStreamJob.setOutconditions(getJobOutConditions(sosHibernateSession, jobStream.getJobschedulerId(), job));
                    jobStreamJobs.add(jobStreamJob);
                }
                jobStream.setJobs(jobStreamJobs);
            }

            jobStreams.getJobstreams().add(jobStream);
        }
        return jobStreams;
    }

    @Override
    public JOCDefaultResponse getJobStreams(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsFilter.class);
            JobStreamsFilter jobStreamsFilter = Globals.objectMapper.readValue(filterBytes, JobStreamsFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_LIST_JOBSTREAM, jobStreamsFilter, accessToken, jobStreamsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamsFilter.getJobschedulerId(), accessToken).getJobStream().getView().isSetStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_LIST_JOBSTREAM);

            FilterJobStreams filterJobStreams = new FilterJobStreams();

            filterJobStreams.setJobStream(jobStreamsFilter.getJobStream());
            filterJobStreams.setSchedulerId(jobStreamsFilter.getJobschedulerId());
            filterJobStreams.setStatus(jobStreamsFilter.getStatus());
            filterJobStreams.setFolder(jobStreamsFilter.getFolder());
            filterJobStreams.setLimit(jobStreamsFilter.getLimit());

            JobStreamMigrator jobStreamMigrator = new JobStreamMigrator();
            if (jobStreamMigrator.migrate(sosHibernateSession)) {
                notifyEventHandler(accessToken);
            }

            JobStreams jobStreams = getListOfJobstreams(sosHibernateSession, filterJobStreams, true);
            return JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(jobStreams));
            // return JOCDefaultResponse.responseStatus200(jobStreams);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void addCalendarUsage(SOSHibernateSession sosHibernateSession, List<DBItemInventoryInstance> clusterMembers, JobStream jobStream)
            throws Exception {
        for (JobStreamStarter jobstreamStarter : jobStream.getJobstreamStarters()) {

            if (jobstreamStarter.getRunTime() != null) {
                RunTime runTime = XmlSerializer.serializeAbstractSchedule(jobstreamStarter.getRunTime());
                String runTimeXml = Globals.xmlMapper.writeValueAsString(runTime);
                Calendars calendars = Globals.objectMapper.readValue(runTime.getCalendars(), (Calendars.class));

                List<String> listOfCalendarPaths = new ArrayList<String>();
                for (Calendar calendar : calendars.getCalendars()) {
                    listOfCalendarPaths.add(calendar.getBasedOn());
                }

                CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(sosHibernateSession, dbItemInventoryInstance.getSchedulerId(),
                        CalendarObjectType.JOBSTREAM, jobStream.getJobStream(), runTimeXml, calendars.getCalendars());
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
        }
    }

    public JOCDefaultResponse jobStream(String accessToken, byte[] filterBytes, String apiCall) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            JobStream jobStream = Globals.objectMapper.readValue(filterBytes, JobStream.class);

            JOCDefaultResponse jocDefaultResponse = init(apiCall, jobStream, accessToken, jobStream.getJobschedulerId(), getPermissonsJocCockpit(
                    jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobStream", jobStream.getJobStream());

            for (JobStreamStarter jobstreamStarter : jobStream.getJobstreamStarters()) {
                for (JobStreamJob jobStreamJob : jobstreamStarter.getJobs()) {
                    try {
                        checkFolderPermissions(jobStreamJob.getJob());
                    } catch (JocFolderPermissionsException e) {
                        LOGGER.debug("Folder permission for " + jobStreamJob.getJob() + " is missing. job stream " + jobStream.getJobStream()
                                + " will not be added ");
                        jobStream.setJobStream("");
                    }
                }
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_EDIT_JOBSTREAM);
            sosHibernateSession.setAutoCommit(false);

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);

            Long oldId = null;
            if (jobStream.getOldJobStreamName() != null && !jobStream.getOldJobStreamName().equals(jobStream.getJobStream())) {

                sosHibernateSession.beginTransaction();
                FilterJobStreams filterJobStreams = new FilterJobStreams();
                filterJobStreams.setSchedulerId(jobStream.getJobschedulerId());
                filterJobStreams.setJobStream(jobStream.getOldJobStreamName());
                filterJobStreams.setFolder(jobStream.getFolder());

                List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 1);

                if (listOfJobStreams.size() > 0) {
                    oldId = listOfJobStreams.get(0).getId();
                }

                sosHibernateSession.commit();

            }

            sosHibernateSession.beginTransaction();
            Long newId = dbLayerJobStreams.deleteInsert(jobStream, dbItemInventoryInstance.getTimeZone());

            List<DBItemInventoryInstance> clusterMembers = null;

            if ("active".equals(dbItemInventoryInstance.getClusterType())) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(sosHibernateSession);
                clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(jobStream.getJobschedulerId());
            }

            if (oldId != null && oldId != newId) {
                DBLayerJobStreamHistory dbLayerJobStreamHistory = new DBLayerJobStreamHistory(sosHibernateSession);
                dbLayerJobStreamHistory.updateHistoryWithJobStream(oldId, newId);
                DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
                dbLayerEvents.updateEventsWithJobStream(jobStream.getOldJobStreamName(), jobStream.getJobStream());
                FilterJobStreams filterJobStreams = new FilterJobStreams();
                filterJobStreams.setJobStreamId(oldId);
                dbLayerJobStreams.deleteCascading(filterJobStreams, true);
            }

            sosHibernateSession.commit();

            addCalendarUsage(sosHibernateSession, clusterMembers, jobStream);

            EditJobStreamsAudit editJobStreamAudit = new EditJobStreamsAudit(jobStream);
            logAuditMessage(editJobStreamAudit);
            storeAuditLogEntry(editJobStreamAudit);

            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(jobStream);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse editJobStream(String accessToken, byte[] filterBytes) {
        return jobStream(accessToken, filterBytes, API_CALL_EDIT_JOBSTREAM);
    }

    @Override
    public JOCDefaultResponse addJobStream(String accessToken, byte[] filterBytes) {
        return jobStream(accessToken, filterBytes, API_CALL_ADD_JOBSTREAM);
    }

    @Override
    public JOCDefaultResponse deleteJobStream(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            JobStream jobStream = Globals.objectMapper.readValue(filterBytes, JobStream.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE_JOBSTREAM, jobStream, accessToken, jobStream.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                checkRequiredParameter("jobStreamId", jobStream.getJobStreamId());
            } catch (JocMissingRequiredParameterException e) {
                checkRequiredParameter("jobStream", jobStream.getJobStream());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE_JOBSTREAM);
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

            EditJobStreamsAudit editJobStreamAudit = new EditJobStreamsAudit(jobStream);
            logAuditMessage(editJobStreamAudit);
            storeAuditLogEntry(editJobStreamAudit);

            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(jobStream);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse importJobStream(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            JobStream jobStream = Globals.objectMapper.readValue(filterBytes, JobStream.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE_JOBSTREAM, jobStream, accessToken, jobStream.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                checkRequiredParameter("jobStreamId", jobStream.getJobStreamId());
            } catch (JocMissingRequiredParameterException e) {
                checkRequiredParameter("jobStream", jobStream.getJobStream());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE_JOBSTREAM);
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

            EditJobStreamsAudit editJobStreamAudit = new EditJobStreamsAudit(jobStream);
            logAuditMessage(editJobStreamAudit);
            storeAuditLogEntry(editJobStreamAudit);

            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(jobStream);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse exportJobStream(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsSelector.class);
            JobStreamsSelector jobStreamSelector = Globals.objectMapper.readValue(filterBytes, JobStreamsSelector.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE_JOBSTREAM, jobStreamSelector, accessToken, jobStreamSelector
                    .getJobschedulerId(), getPermissonsJocCockpit(jobStreamSelector.getJobschedulerId(), accessToken).getJobStream().getChange()
                            .isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (((jobStreamSelector.getFolders() == null) || (jobStreamSelector.getFolders().size() == 0)) && ((jobStreamSelector
                    .getJobStreams() == null) || (jobStreamSelector.getJobStreams().size() == 0))) {
                throw new JocMissingRequiredParameterException("undefined folders or jobstreams");

            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"));

            JobStreams jobStreams = new JobStreams();
            JobStreams jobStreamsLocation = new JobStreams();
            JobStreams jobStreamsFolders = new JobStreams();
            jobStreams.setJobstreams(new ArrayList<JobStream>());
            jobStreamsFolders.setJobstreams(new ArrayList<JobStream>());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE_JOBSTREAM);
            FilterJobStreams filterJobStreams = new FilterJobStreams();
            filterJobStreams.setSchedulerId(jobStreamSelector.getJobschedulerId());

            for (JobStreamLocation jobStreamLocation : jobStreamSelector.getJobStreams()) {
                filterJobStreams.setJobStream(jobStreamLocation.getJobStream());
                filterJobStreams.setFolder(jobStreamLocation.getFolder());
                jobStreamsLocation = getListOfJobstreams(sosHibernateSession, filterJobStreams, false);
                for (JobStream jobStream : jobStreamsLocation.getJobstreams()) {
                    jobStreams.getJobstreams().add(jobStream);
                }
            }

            for (String folder : jobStreamSelector.getFolders()) {
                filterJobStreams.setFolder(folder);
                jobStreamsFolders = getListOfJobstreams(sosHibernateSession, filterJobStreams, false);
                for (JobStream jobStream : jobStreamsFolders.getJobstreams()) {
                    jobStreams.getJobstreams().add(jobStream);
                }
            }

            jobStreams.setDeliveryDate(new Date());
            String now = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String downloadFileName = "jobstreams-export-" + now + ".json";

            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(objectMapper.writeValueAsString(jobStreams), downloadFileName);

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