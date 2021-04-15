package com.sos.joc.jobstreams.impl;

import java.io.IOException;
import java.nio.file.Paths;
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
import com.sos.jitl.jobstreams.db.DBItemInConditionCommand;
import com.sos.jitl.jobstreams.db.DBItemInConditionWithCommand;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemJobStreamParameter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarterJob;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBItemOutConditionEvent;
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
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobstreams.resource.IJobStreamsResource;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.jobstreams.ConditionExpression;
import com.sos.joc.model.jobstreams.ImportJobStreams;
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
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joe.common.XmlSerializer;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamsImpl extends JOCResourceImpl implements IJobStreamsResource {

    private static final String API_CALL_LIST = "./jobstreams/list";
    private static final String API_CALL_EDIT = "./jobstreams/edit";
    private static final String API_CALL_ADD = "./jobstreams/add";
    private static final String API_CALL_DELETE = "./jobstreams/delete";
    private static final String API_CALL_IMPORT = "./jobstreams/import";
    private static final String API_CALL_EXPORT = "./jobstreams/export";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamsImpl.class);

    private List<InCondition> getJobInconditions(SOSHibernateSession sosHibernateSession, Boolean forExport, String jobSchedulerId, String job)
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
                if (!forExport) {
                    inCondition.setJobStream(jsInCondition.getJobStream());
                    inCondition.setId(jsInCondition.getId());
                }
                inCondition.setMarkExpression(jsInCondition.isMarkExpression());
                inCondition.setSkipOutCondition(jsInCondition.isSkipOutCondition());

                for (JSInConditionCommand jsInConditionCommand : jsInCondition.getListOfInConditionCommand()) {
                    InConditionCommand inConditionCommand = new InConditionCommand();
                    inConditionCommand.setCommand(jsInConditionCommand.getCommand());
                    inConditionCommand.setCommandParam(jsInConditionCommand.getCommandParam());
                    if (!forExport) {
                        inConditionCommand.setId(jsInConditionCommand.getId());
                    }
                    inCondition.getInconditionCommands().add(inConditionCommand);
                }
                listOfInConditions.add(inCondition);
            }
        }
        return listOfInConditions;

    }

    private List<OutCondition> getJobOutConditions(SOSHibernateSession sosHibernateSession, Boolean forExport, String jobSchedulerId, String job)
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
                if (!forExport) {
                    outCondition.setJobStream(jsOutCondition.getJobStream());
                    outCondition.setId(jsOutCondition.getId());
                }

                for (JSOutConditionEvent jsOutConditionEvent : jsOutCondition.getListOfOutConditionEvent()) {
                    OutConditionEvent outConditionEvent = new OutConditionEvent();
                    outConditionEvent.setEvent(jsOutConditionEvent.getEventValue());
                    outConditionEvent.setCommand(jsOutConditionEvent.getCommand());
                    outConditionEvent.setGlobalEvent(jsOutConditionEvent.isGlobal());

                    jsEventKey.setGlobalEvent(jsOutConditionEvent.isGlobal());
                    if (!forExport) {
                        jsEventKey.setSchedulerId(jobSchedulerId);
                        outConditionEvent.setId(jsOutConditionEvent.getId());
                    }
                    outCondition.getOutconditionEvents().add(outConditionEvent);
                }

                listOfOutConditions.add(outCondition);
            }
        }
        return listOfOutConditions;
    }

    private JobStreams getListOfJobstreams(SOSHibernateSession sosHibernateSession, FilterJobStreams filterJobStreams, Boolean forExport)
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
        jobStreams.setJobschedulerId(filterJobStreams.getSchedulerId());

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
            if (!forExport) {
                jobStream.setJobStreamId(dbItemJobStream.getId());
                jobStream.setDeliveryDate(new Date());
                jobStream.setJobschedulerId(dbItemJobStream.getSchedulerId());
            }
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
                jobstreamStarter.setStarterName(dbItemJobStreamStarter.getStarterName());
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
                    if (forExport) {
                        jobStreamJob.setNextPeriod(dbItemJobStreamStarterJobs.getNextPeriod());
                        jobStreamJob.setInconditions(getJobInconditions(sosHibernateSession, forExport, jobStreams.getJobschedulerId(),
                                dbItemJobStreamStarterJobs.getJob()));
                        jobStreamJob.setOutconditions(getJobOutConditions(sosHibernateSession, forExport, jobStreams.getJobschedulerId(),
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

            if (forExport) {

                DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
                DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);

                Set<String> listOfJobs = new HashSet<String>();
                FilterOutConditions filterOutConditions = new FilterOutConditions();
                filterOutConditions.setJobStream(jobStream.getJobStream());
                filterOutConditions.setJobSchedulerId(jobStreams.getJobschedulerId());
                List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
                for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
                    listOfJobs.add(dbItemOutCondition.getJob());
                }
                FilterInConditions filterInConditions = new FilterInConditions();
                filterInConditions.setJobStream(jobStream.getJobStream());
                filterInConditions.setJobSchedulerId(jobStreams.getJobschedulerId());
                List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);
                for (DBItemInCondition dbItemInCondition : listOfInConditions) {
                    listOfJobs.add(dbItemInCondition.getJob());
                }

                List<JobStreamJob> jobStreamJobs = new ArrayList<JobStreamJob>();
                for (String job : listOfJobs) {
                    JobStreamJob jobStreamJob = new JobStreamJob();
                    jobStreamJob.setJob(job);
                    jobStreamJob.setInconditions(getJobInconditions(sosHibernateSession, forExport, jobStreams.getJobschedulerId(), job));
                    jobStreamJob.setOutconditions(getJobOutConditions(sosHibernateSession, forExport, jobStreams.getJobschedulerId(), job));
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

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_LIST, jobStreamsFilter, accessToken, jobStreamsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamsFilter.getJobschedulerId(), accessToken).getJobStream().getView().isSetStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_LIST);

            JobStreamMigrator jobStreamMigrator = new JobStreamMigrator();

            if (jobStreamMigrator.migrate(sosHibernateSession)) {
                notifyEventHandler(accessToken);
            }

            JobStreams jobStreams = new JobStreams();
            jobStreams.setJobstreams(new ArrayList<JobStream>());
            FilterJobStreams filterJobStreams = new FilterJobStreams();

            if ((jobStreamsFilter.getJobStream() != null) || (jobStreamsFilter.getFolder() != null)) {
                filterJobStreams.setJobStream(jobStreamsFilter.getJobStream());
                filterJobStreams.setSchedulerId(jobStreamsFilter.getJobschedulerId());
                filterJobStreams.setStatus(jobStreamsFilter.getStatus());
                filterJobStreams.setLimit(jobStreamsFilter.getLimit());

                if (jobStreamsFilter.getJobStream() != null) {
                    jobStreams = getListOfJobstreams(sosHibernateSession, filterJobStreams, false);
                }

                if (jobStreamsFilter.getFolder() != null) {
                    filterJobStreams.setJobStream(null);
                    filterJobStreams.setFolder(jobStreamsFilter.getFolder());
                    jobStreams.getJobstreams().addAll(getListOfJobstreams(sosHibernateSession, filterJobStreams, false).getJobstreams());
                }

                filterJobStreams.setFolder(null);
                filterJobStreams.setJobStream(null);
            }

            for (Folder folder : jobStreamsFilter.getFolders()) {
                filterJobStreams.setFolderItem(folder);
                JobStreams jobStreamsFromFolders = getListOfJobstreams(sosHibernateSession, filterJobStreams, false);
                jobStreams.getJobstreams().addAll(jobStreamsFromFolders.getJobstreams());
            }

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

            List<Calendar> listOfCalendars = null;
            String runTimeXml = "";
            if (jobstreamStarter.getRunTime() != null) {
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
                    CalendarObjectType.JOBSTREAM, jobStream.getJobStream(), runTimeXml, listOfCalendars);
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

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_EDIT);
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
        return jobStream(accessToken, filterBytes, API_CALL_EDIT);
    }

    @Override
    public JOCDefaultResponse addJobStream(String accessToken, byte[] filterBytes) {
        return jobStream(accessToken, filterBytes, API_CALL_ADD);
    }

    @Override
    public JOCDefaultResponse deleteJobStream(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            JobStream jobStream = Globals.objectMapper.readValue(filterBytes, JobStream.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE, jobStream, accessToken, jobStream.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().isJobStream());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            try {
                checkRequiredParameter("jobStreamId", jobStream.getJobStreamId());
            } catch (JocMissingRequiredParameterException e) {
                checkRequiredParameter("jobStream", jobStream.getJobStream());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE);
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

    private String getUniqueStarterName(SOSHibernateSession sosHibernateSession, String schedulerId, String starterName)
            throws SOSHibernateException {
        DBLayerJobStreamStarters dbLayerJobStreamStarters = new DBLayerJobStreamStarters(sosHibernateSession);
        FilterJobStreamStarters filterJobStreamStarters = new FilterJobStreamStarters();
        int size = 0;
        int index = 0;
        String uniqueStarterName = starterName;
        do {
            filterJobStreamStarters.setStarterName(uniqueStarterName);
            size = dbLayerJobStreamStarters.getJobStreamStartersList(filterJobStreamStarters, 0).size();
            if (size > 0) {
                index = index + 1;
                uniqueStarterName = starterName + "(" + index + ")";
            }
        } while (size > 0);
        return uniqueStarterName;
    }

    private String getJobPath(String folder, String job) {
        if (job == null) {
            return null;
        }
        if (folder == null) {
            return job;
        }
        return folder + "/" + Paths.get(job).getFileName().toString();
    }

    private void importJobStream(SOSHibernateSession sosHibernateSession, JobStream jobStream) throws SOSHibernateException, JsonProcessingException {

        DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
        DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
        DBLayerJobStreamsStarterJobs dbLayerJobStreamsStarterJobs = new DBLayerJobStreamsStarterJobs(sosHibernateSession);

        DBItemJobStream dbItemJobStream = new DBItemJobStream();
        dbItemJobStream.setCreated(new Date());
        dbItemJobStream.setFolder(jobStream.getFolder());
        dbItemJobStream.setJobStream(jobStream.getJobStream());
        dbItemJobStream.setSchedulerId(jobStream.getJobschedulerId());
        dbItemJobStream.setState(jobStream.getState());
        sosHibernateSession.save(dbItemJobStream);
        List<JobStreamJob> listOfJobStreamJobs = new ArrayList<JobStreamJob>();

        for (JobStreamStarter jobStreamStarter : jobStream.getJobstreamStarters()) {
            DBItemJobStreamStarter dbItemJobStreamStarter = new DBItemJobStreamStarter();
            dbItemJobStreamStarter.setCreated(new Date());
            dbItemJobStreamStarter.setEndOfJobStream(getJobPath(jobStream.getFolder(), jobStreamStarter.getEndOfJobStream()));
            dbItemJobStreamStarter.setJobStream(dbItemJobStream.getId());
            dbItemJobStreamStarter.setRequiredJob(getJobPath(jobStream.getFolder(), jobStreamStarter.getRequiredJob()));
            dbItemJobStreamStarter.setRunTime(Globals.objectMapper.writeValueAsString(jobStreamStarter.getRunTime()));
            dbItemJobStreamStarter.setState(jobStreamStarter.getState());
            dbItemJobStreamStarter.setTitle(jobStreamStarter.getTitle());
            dbItemJobStreamStarter.setStarterName(getUniqueStarterName(sosHibernateSession, jobStream.getJobschedulerId(), jobStreamStarter
                    .getStarterName()));
            sosHibernateSession.save(dbItemJobStreamStarter);

            for (NameValuePair nameValuePair : jobStreamStarter.getParams()) {
                DBItemJobStreamParameter dbItemJobStreamParameter = new DBItemJobStreamParameter();
                dbItemJobStreamParameter.setCreated(new Date());
                dbItemJobStreamParameter.setJobStreamStarter(dbItemJobStreamStarter.getId());
                dbItemJobStreamParameter.setName(nameValuePair.getName());
                dbItemJobStreamParameter.setValue(nameValuePair.getValue());
                sosHibernateSession.save(dbItemJobStreamParameter);

            }

            for (JobStreamJob jobStreamJob : jobStreamStarter.getJobs()) {
                String jobPath = getJobPath(jobStream.getFolder(), jobStreamJob.getJob());
                FilterJobStreamStarterJobs filterJobStreamStarterJobs = new FilterJobStreamStarterJobs();
                filterJobStreamStarterJobs.setJob(jobPath);
                if (dbLayerJobStreamsStarterJobs.getJobStreamStarterJobsList(filterJobStreamStarterJobs, 0).size() == 0) {
                    DBItemJobStreamStarterJob dbItemJobStreamStarterJob = new DBItemJobStreamStarterJob();
                    dbItemJobStreamStarterJob.setCreated(new Date());
                    dbItemJobStreamStarterJob.setDelay(jobStreamJob.getStartDelay());
                    dbItemJobStreamStarterJob.setJob(jobPath);
                    dbItemJobStreamStarterJob.setJobStreamStarter(dbItemJobStreamStarter.getId());
                    dbItemJobStreamStarterJob.setSkipOutCondition(jobStreamJob.getSkipOutCondition());
                    sosHibernateSession.save(dbItemJobStreamStarterJob);
                }
            }

        }

        listOfJobStreamJobs.addAll(jobStream.getJobs());

        for (JobStreamJob jobStreamJob : listOfJobStreamJobs) {
            for (OutCondition outCondition : jobStreamJob.getOutconditions()) {
                String jobPath = getJobPath(jobStream.getFolder(), jobStreamJob.getJob());

                FilterOutConditions filterOutConditions = new FilterOutConditions();
                filterOutConditions.setJob(jobPath);
                if (dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0).size() == 0) {

                    DBItemOutCondition dbItemOutCondition = new DBItemOutCondition();
                    dbItemOutCondition.setCreated(new Date());
                    dbItemOutCondition.setExpression(outCondition.getConditionExpression().getExpression());
                    dbItemOutCondition.setFolder(jobStream.getFolder());
                    dbItemOutCondition.setJob(jobPath);
                    dbItemOutCondition.setJobStream(jobStream.getJobStream());
                    dbItemOutCondition.setSchedulerId(jobStream.getJobschedulerId());
                    sosHibernateSession.save(dbItemOutCondition);
                    for (OutConditionEvent outConditionEvent : outCondition.getOutconditionEvents()) {
                        DBItemOutConditionEvent dbItemOutConditionEvent = new DBItemOutConditionEvent();
                        dbItemOutConditionEvent.setCreated(new Date());
                        dbItemOutConditionEvent.setCommand(outConditionEvent.getCommand());
                        dbItemOutConditionEvent.setEvent(outConditionEvent.getEvent());
                        dbItemOutConditionEvent.setGlobalEvent(outConditionEvent.getGlobalEvent());
                        dbItemOutConditionEvent.setOutConditionId(dbItemOutCondition.getId());
                        sosHibernateSession.save(dbItemOutConditionEvent);
                    }
                }
            }
            for (InCondition inCondition : jobStreamJob.getInconditions()) {
                String jobPath = getJobPath(jobStream.getFolder(), jobStreamJob.getJob());

                FilterInConditions filterInConditions = new FilterInConditions();
                filterInConditions.setJob(jobPath);
                if (dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0).size() == 0) {

                    DBItemInCondition dbItemInCondition = new DBItemInCondition();
                    dbItemInCondition.setCreated(new Date());
                    dbItemInCondition.setExpression(inCondition.getConditionExpression().getExpression());
                    dbItemInCondition.setJobStream(inCondition.getJobStream());
                    dbItemInCondition.setFolder(jobStream.getFolder());
                    dbItemInCondition.setJob(jobPath);
                    dbItemInCondition.setJobStream(jobStream.getJobStream());
                    dbItemInCondition.setMarkExpression(inCondition.getMarkExpression());

                    dbItemInCondition.setSchedulerId(jobStream.getJobschedulerId());
                    dbItemInCondition.setSkipOutCondition(inCondition.getSkipOutCondition());
                    sosHibernateSession.save(dbItemInCondition);
                    for (InConditionCommand inConditionCommand : inCondition.getInconditionCommands()) {
                        DBItemInConditionCommand dbItemInConditionCommand = new DBItemInConditionCommand();
                        dbItemInConditionCommand.setCreated(new Date());
                        dbItemInConditionCommand.setCommand(inConditionCommand.getCommand());
                        dbItemInConditionCommand.setCommandParam(inConditionCommand.getCommandParam());
                        dbItemInConditionCommand.setInConditionId(dbItemInCondition.getId());
                        sosHibernateSession.save(dbItemInConditionCommand);
                    }
                }

            }
        }

        EditJobStreamsAudit editJobStreamAudit = new EditJobStreamsAudit(jobStream);
        logAuditMessage(editJobStreamAudit);
        storeAuditLogEntry(editJobStreamAudit);

    }

    @Override
    public JOCDefaultResponse importJobStreams(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStream.class);
            ImportJobStreams jobStreams = Globals.objectMapper.readValue(filterBytes, ImportJobStreams.class);
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_IMPORT);

            sosHibernateSession.setAutoCommit(false);
            sosHibernateSession.beginTransaction();

            for (JobStream jobStream : jobStreams.getJobstreams()) {
                jobStream.setJobschedulerId(jobStreams.getJobschedulerId());

                JOCDefaultResponse jocDefaultResponse = init(API_CALL_IMPORT, jobStreams, accessToken, jobStream.getJobschedulerId(),
                        getPermissonsJocCockpit(jobStream.getJobschedulerId(), accessToken).getJobStream().getChange().isJobStream());
                if (jocDefaultResponse != null) {
                    return jocDefaultResponse;
                }

                checkRequiredParameter("jobStream", jobStream.getJobStream());
                int count = 0;

                DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
                FilterJobStreams filterJobStreams = new FilterJobStreams();
                filterJobStreams.setSchedulerId(jobStream.getJobschedulerId());
                filterJobStreams.setJobStream(jobStream.getJobStream());

                List<DBItemJobStream> listOfJobStreams;
                do {
                    listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
                    if (listOfJobStreams.size() > 0) {
                        count = count + 1;
                        filterJobStreams.setJobStream(jobStream.getJobStream() + "(" + count + ")");
                    }
                } while (listOfJobStreams.size() > 0);
                jobStream.setJobStream(filterJobStreams.getJobStream());
                importJobStream(sosHibernateSession, jobStream);
            }
            sosHibernateSession.commit();

            if (dbItemInventoryInstance != null) {
                notifyEventHandler(accessToken);
            }
            return JOCDefaultResponse.responseStatusJSOk(null);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse exportJobStreams(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, JobStreamsSelector.class);
            JobStreamsSelector jobStreamSelector = Globals.objectMapper.readValue(filterBytes, JobStreamsSelector.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_EXPORT, jobStreamSelector, accessToken, jobStreamSelector.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamSelector.getJobschedulerId(), accessToken).getJobStream().getChange().isJobStream());
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
            jobStreams.setJobschedulerId(jobStreamSelector.getJobschedulerId());
            JobStreams jobStreamsLocation = new JobStreams();
            JobStreams jobStreamsFolders = new JobStreams();
            jobStreams.setJobstreams(new ArrayList<JobStream>());
            jobStreamsFolders.setJobstreams(new ArrayList<JobStream>());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE);
            FilterJobStreams filterJobStreams = new FilterJobStreams();
            filterJobStreams.setSchedulerId(jobStreamSelector.getJobschedulerId());

            for (JobStreamLocation jobStreamLocation : jobStreamSelector.getJobStreams()) {
                if (jobStreamLocation.getJobStream() != null) {
                    filterJobStreams.setJobStream(jobStreamLocation.getJobStream());
                    filterJobStreams.setFolder(jobStreamLocation.getFolder());
                    jobStreamsLocation = getListOfJobstreams(sosHibernateSession, filterJobStreams, true);
                    for (JobStream jobStream : jobStreamsLocation.getJobstreams()) {
                        jobStreams.getJobstreams().add(jobStream);
                    }
                }
            }

            filterJobStreams = new FilterJobStreams();
            for (Folder folder : jobStreamSelector.getFolders()) {
                filterJobStreams.setFolderItem(folder);
                jobStreamsFolders = getListOfJobstreams(sosHibernateSession, filterJobStreams, true);
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