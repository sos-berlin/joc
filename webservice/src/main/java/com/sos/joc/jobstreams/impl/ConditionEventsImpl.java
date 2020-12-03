package com.sos.joc.jobstreams.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.eventhandler.handler.EventHandlerSettings;
import com.sos.jitl.jobstreams.Constants;
import com.sos.jitl.jobstreams.db.DBItemCalendarWithUsages;
import com.sos.jitl.jobstreams.db.DBItemConsumedInCondition;
import com.sos.jitl.jobstreams.db.DBItemEvent;
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemInConditionWithCommand;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBItemOutConditionWithEvent;
import com.sos.jitl.jobstreams.db.DBLayerCalendarUsages;
import com.sos.jitl.jobstreams.db.DBLayerConsumedInConditions;
import com.sos.jitl.jobstreams.db.DBLayerEvents;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerJobStreams;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterCalendarUsage;
import com.sos.jitl.jobstreams.db.FilterConsumedInConditions;
import com.sos.jitl.jobstreams.db.FilterEvents;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterJobStreams;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.jobstreams.resolver.JSCondition;
import com.sos.jobstreams.resolver.JSConditionResolver;
import com.sos.jobstreams.resolver.JSConditions;
import com.sos.jobstreams.resolver.JSInCondition;
import com.sos.jobstreams.resolver.JSInConditionCommand;
import com.sos.jobstreams.resolver.JSJobConditionKey;
import com.sos.jobstreams.resolver.JSJobInConditions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobstreams.resource.IConditionEventsResource;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.jobscheduler.DB;
import com.sos.joc.model.jobstreams.ConditionEvent;
import com.sos.joc.model.jobstreams.ConditionEvents;
import com.sos.joc.model.jobstreams.ConditionEventsFilter;
import com.sos.joc.model.jobstreams.ConditionExpression;
import com.sos.joc.model.jobstreams.ConditionJobsFilter;
import com.sos.joc.model.jobstreams.InCondition;
import com.sos.joc.model.jobstreams.InConditionCommand;
import com.sos.joc.model.jobstreams.InConditions;
import com.sos.joc.model.jobstreams.JobInCondition;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class ConditionEventsImpl extends JOCResourceImpl implements IConditionEventsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionEventsImpl.class);
    private static final String API_CALL_EVENTLIST = "./jobstreams/eventlist";
    private static final String API_CALL_MISSING_EVENTLIST = "./jobstreams/missingvents";
    private static final String API_CALL_ADD_EVENT = "./jobstreams/add_event";
    private static final String API_CALL_DELETE_EVENT = "./jobstreams/delete_event";

    @Override
    public JOCDefaultResponse getEvents(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, ConditionEventsFilter.class);
            ConditionEventsFilter conditionEventsFilter = Globals.objectMapper.readValue(filterBytes, ConditionEventsFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_EVENTLIST, conditionEventsFilter, accessToken, conditionEventsFilter
                    .getJobschedulerId(), getPermissonsJocCockpit(conditionEventsFilter.getJobschedulerId(), accessToken).getJobStream().getView()
                            .isEventlist());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            ConditionEvents conditionEvents = new ConditionEvents();

            if (conditionEventsFilter.getSession() != null && !conditionEventsFilter.getSession().isEmpty()) {

                sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_EVENTLIST);

                DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
                DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
                FilterEvents filter = new FilterEvents();
                filter.setIncludingGlobalEvent(true);
                filter.setSchedulerId(conditionEventsFilter.getJobschedulerId());
                filter.setOutConditionId(conditionEventsFilter.getOutConditionId());

                filter.setJobStream(conditionEventsFilter.getJobStream());
                filter.setSession(conditionEventsFilter.getSession());

                List<DBItemOutConditionWithEvent> listOfEvents = dbLayerEvents.getEventsList(filter, conditionEventsFilter.getLimit());
                conditionEvents.setDeliveryDate(new Date());
                conditionEvents.setSession(filter.getSession());

                for (DBItemOutConditionWithEvent dbItemEvent : listOfEvents) {
                    ConditionEvent conditionEvent = new ConditionEvent();
                    if (dbItemEvent != null) {
                        DBItemOutCondition dbItemOutCondition = dbLayerOutConditions.getOutConditionsDbItem(dbItemEvent.getOutConditionId());
                        if (dbItemOutCondition != null) {
                            conditionEvent.setEvent(dbItemEvent.getEvent());
                            conditionEvent.setOutConditionId(dbItemEvent.getOutConditionId());
                            conditionEvent.setSession(dbItemEvent.getSession());
                            conditionEvent.setJobStream(dbItemEvent.getJobStream());
                            conditionEvent.setPath(dbItemOutCondition.getPath());
                            conditionEvent.setGlobalEvent(dbItemEvent.getGlobalEvent());

                            if (conditionEventsFilter.getPath() == null || conditionEventsFilter.getPath().isEmpty() || dbItemOutCondition.getFolder()
                                    .equals(conditionEventsFilter.getPath())) {

                                try {
                                    checkFolderPermissions(dbItemOutCondition.getJob());
                                } catch (JocFolderPermissionsException e) {
                                    LOGGER.debug("Folder permission for " + dbItemOutCondition.getJob() + " is missing. Outconditon " + conditionEvent
                                            .getJobStream() + "." + conditionEvent.getEvent() + " ignored.");
                                }
                                conditionEvents.getConditionEvents().add(conditionEvent);

                            }
                        }
                    }
                }

                if (Globals.schedulerVariables == null) {
                    readJobSchedulerVariables();
                    Constants.periodBegin = Globals.schedulerVariables.get("sos.jobstream_period_begin");
                    Constants.settings = new EventHandlerSettings();
                    Constants.settings.setTimezone(dbItemInventoryInstance.getTimeZone());
                }

                filter.setJobStream("");
                filter.setGlobalEvent(true);
                filter.setSession(Constants.getSession());

                listOfEvents = dbLayerEvents.getEventsList(filter, conditionEventsFilter.getLimit());

                for (DBItemOutConditionWithEvent dbItemEvent : listOfEvents) {
                    ConditionEvent conditionEvent = new ConditionEvent();
                    if (dbItemEvent != null) {
                        DBItemOutCondition dbItemOutCondition = dbLayerOutConditions.getOutConditionsDbItem(dbItemEvent.getOutConditionId());
                        if (dbItemOutCondition != null) {
                            conditionEvent.setEvent(dbItemEvent.getEvent());
                            conditionEvent.setOutConditionId(dbItemEvent.getOutConditionId());
                            conditionEvent.setSession(dbItemEvent.getSession());
                            conditionEvent.setJobStream(dbItemEvent.getJobStream());
                            conditionEvent.setPath(dbItemOutCondition.getPath());
                            conditionEvent.setGlobalEvent(dbItemEvent.getGlobalEvent());

                        }
                    }
                }
            }
            return JOCDefaultResponse.responseStatus200(conditionEvents);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse getMissingEvents(String accessToken, byte[] filterBytes) {

        SOSHibernateSession sosHibernateSession = null;

        try {

            JsonValidator.validateFailFast(filterBytes, JobsFilter.class);
            ConditionJobsFilter conditionJobsFilterSchema = Globals.objectMapper.readValue(filterBytes, ConditionJobsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_MISSING_EVENTLIST, conditionJobsFilterSchema, accessToken, conditionJobsFilterSchema
                    .getJobschedulerId(), getPermissonsJocCockpit(conditionJobsFilterSchema.getJobschedulerId(), accessToken).getJobStream().getView()
                            .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if ((conditionJobsFilterSchema.getJobs() == null) && (conditionJobsFilterSchema.getFolder() == null)) {
                throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", "jobs or folder"));
            }

            if (Globals.schedulerVariables == null) {
                readJobSchedulerVariables();
                Constants.periodBegin = Globals.schedulerVariables.get("sos.jobstream_period_begin");
                Constants.settings = new EventHandlerSettings();
                Constants.settings.setTimezone(dbItemInventoryInstance.getTimeZone());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_MISSING_EVENTLIST);

            if ((conditionJobsFilterSchema.getJobs() == null) && (conditionJobsFilterSchema.getFolder() == null)) {
                throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", "jobs or folder"));
            }

            UUID contextId = null;
            try {
                if (conditionJobsFilterSchema.getSession() != null && !conditionJobsFilterSchema.getSession().isEmpty()) {
                    contextId = UUID.fromString(conditionJobsFilterSchema.getSession());
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Could not get session from: " + conditionJobsFilterSchema.getSession());
            }
            ConditionEvents conditionEvents = new ConditionEvents();
            conditionEvents.setDeliveryDate(new Date());
            conditionEvents.setSession(conditionJobsFilterSchema.getSession());

            JSConditionResolver jsConditionResolver = new JSConditionResolver(conditionJobsFilterSchema.getJobschedulerId());
            jsConditionResolver.setWorkingDirectory(dbItemInventoryInstance.getLiveDirectory() + "/../../");

            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            FilterInConditions filterInConditions;
            Set<String> listOfJobs = new HashSet<String>();

            if (conditionJobsFilterSchema.getFolder() != null) {
                try {
                    String p;
                    if ("/".equals(conditionJobsFilterSchema.getFolder())) {
                        p = "/item";
                    } else {
                        p = conditionJobsFilterSchema.getFolder() + "/item";
                    }
                    checkFolderPermissions(p);
                } catch (JocFolderPermissionsException e) {
                    LOGGER.debug("Folder permission for " + conditionJobsFilterSchema.getFolder() + " is missing for inconditons");
                }

                filterInConditions = new FilterInConditions();
                filterInConditions.setFolder(conditionJobsFilterSchema.getFolder());
                filterInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);
                for (DBItemInCondition dbItemInCondition : listOfInConditions) {
                    listOfJobs.add(dbItemInCondition.getJob());
                }
            } else {
                for (JobPath job : conditionJobsFilterSchema.getJobs()) {
                    try {
                        checkFolderPermissions(job.getJob());
                        listOfJobs.add(job.getJob());
                    } catch (JocFolderPermissionsException e) {
                        LOGGER.debug("Folder permission for " + job.getJob() + " is missing for inconditons");
                    }
                }
            }

            jsConditionResolver.initEvents(sosHibernateSession, contextId);

            for (String job : listOfJobs) {
                JobInCondition jobInCondition = new JobInCondition();

                filterInConditions = new FilterInConditions();
                filterInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                filterInConditions.setJob(job);

                List<DBItemInConditionWithCommand> listOfInConditions = dbLayerInConditions.getInConditionsList(filterInConditions, 0);

                EventHandlerSettings settings = new EventHandlerSettings();
                settings.setSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                JSJobInConditions jsJobInConditions = new JSJobInConditions();
                Map<String, List<DBItemCalendarWithUsages>> listOfCalendarUsages = new HashMap<String, List<DBItemCalendarWithUsages>>();
                jsJobInConditions.setListOfJobInConditions(sosHibernateSession, listOfCalendarUsages, listOfInConditions);

                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job);
                jsJobConditionKey.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());

                jobInCondition.setJob(jsJobConditionKey.getJob());
                if (jsJobInConditions.getInConditions(jsJobConditionKey) != null) {
                    for (JSInCondition jsInCondition : jsJobInConditions.getInConditions(jsJobConditionKey).getListOfInConditions().values()) {

                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsInCondition.getExpression());

                        List<JSCondition> listOfConditions = JSConditions.getListOfConditions(jsInCondition.getExpression());

                        for (JSCondition jsCondition : listOfConditions) {
                            if (jsCondition.isNonContextEvent()) {
                                FilterEvents filterEvents = jsCondition.getFilterEventsNonContextEvent(conditionJobsFilterSchema.getJobschedulerId());
                                DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
                                List<DBItemOutConditionWithEvent> listOfNonContextEvents = dbLayerEvents.getEventsList(filterEvents, 0);
                                jsConditionResolver.addEventsFromList(listOfNonContextEvents);
                            }

                            if (!conditionExpression.getJobStreamEvents().contains(jsCondition.getEventNameWithType())) {
                                if (jsCondition.getConditionJobStream().isEmpty() || jsInCondition.getJobStream().equals(jsCondition
                                        .getConditionJobStream())) {
                                    conditionExpression.getJobStreamEvents().add(jsCondition.getEventNameWithType());
                                }
                            }
                        }

                        conditionExpression.setValue(jsConditionResolver.validate(sosHibernateSession, null, contextId, jsInCondition));
                        for (String missingEvent : jsConditionResolver.getListOfMissingEvents()) {
                            ConditionEvent conditionEvent = new ConditionEvent();
                            conditionEvent.setEvent(missingEvent);
                            conditionEvents.getConditionEvents().add(conditionEvent);
                        }
                    }
                }
            }

            return JOCDefaultResponse.responseStatus200(conditionEvents);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse addEvent(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, ConditionEvent.class);
            ConditionEvent conditionEvent = Globals.objectMapper.readValue(filterBytes, ConditionEvent.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_EVENT, conditionEvent, accessToken, conditionEvent.getJobschedulerId(),
                    getPermissonsJocCockpit(conditionEvent.getJobschedulerId(), accessToken).getJobStream().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_ADD_EVENT);

            checkRequiredParameter("jobStream", conditionEvent.getJobStream());
            checkRequiredParameter("outConditionId", conditionEvent.getOutConditionId());
            checkRequiredParameter("event", conditionEvent.getEvent());
            checkRequiredParameter("session", conditionEvent.getSession());

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            FilterJobStreams filterJobStreams = new FilterJobStreams();

            filterJobStreams.setJobStream(conditionEvent.getJobStream());
            filterJobStreams.setSchedulerId(conditionEvent.getJobschedulerId());
            List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
            if (listOfJobStreams.size() > 0) {
                DBItemJobStream dbItemJobStream = listOfJobStreams.get(0);
                try {

                    String p;
                    if ("/".equals(dbItemJobStream.getFolder())) {
                        p = "/item";
                    } else {
                        p = dbItemJobStream.getFolder() + "/item";
                    }
                    checkFolderPermissions(p);

                    FilterEvents filter = new FilterEvents();
                    filter.setEvent(conditionEvent.getEvent());
                    filter.setSession(conditionEvent.getSession());
                    filter.setJobStream(conditionEvent.getJobStream());
                    filter.setOutConditionId(conditionEvent.getOutConditionId());
                    if (conditionEvent.getGlobalEvent() != null) {
                        filter.setGlobalEvent(conditionEvent.getGlobalEvent());
                    } else {
                        filter.setGlobalEvent(false);
                    }

                    notifyEventHandler(accessToken, "AddEvent", filter);
                } catch (JocFolderPermissionsException e) {
                    LOGGER.debug("Folder permission for " + dbItemJobStream.getFolder() + " is missing. Event " + conditionEvent.getEvent()
                            + " not added.");
                }

            }
            return JOCDefaultResponse.responseStatus200(conditionEvent);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse deleteEvent(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;

        try {
            JsonValidator.validateFailFast(filterBytes, ConditionEvent.class);
            ConditionEvent conditionEvent = Globals.objectMapper.readValue(filterBytes, ConditionEvent.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE_EVENT, conditionEvent, accessToken, conditionEvent.getJobschedulerId(),
                    getPermissonsJocCockpit(conditionEvent.getJobschedulerId(), accessToken).getJobStream().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("event", conditionEvent.getEvent());
            checkRequiredParameter("session", conditionEvent.getSession());
            checkRequiredParameter("jobStream", conditionEvent.getJobStream());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE_EVENT);

            conditionEvent.setSession(conditionEvent.getSession());

            DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
            FilterJobStreams filterJobStreams = new FilterJobStreams();

            filterJobStreams.setJobStream(conditionEvent.getJobStream());
            filterJobStreams.setSchedulerId(conditionEvent.getJobschedulerId());
            List<DBItemJobStream> listOfJobStreams = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
            if (listOfJobStreams.size() > 0) {
                DBItemJobStream dbItemJobStream = listOfJobStreams.get(0);
                try {

                    String p;
                    if ("/".equals(dbItemJobStream.getFolder())) {
                        p = "/item";
                    } else {
                        p = dbItemJobStream.getFolder() + "/item";
                    }
                    checkFolderPermissions(p);

                    FilterEvents filter = new FilterEvents();
                    filter.setEvent(conditionEvent.getEvent());
                    filter.setSession(conditionEvent.getSession());
                    filter.setJobStream(conditionEvent.getJobStream());
                    filter.setGlobalEvent(conditionEvent.getGlobalEvent());

                    notifyEventHandler(accessToken, "RemoveEvent", filter);
                } catch (JocFolderPermissionsException e) {
                    LOGGER.debug("Folder permission for " + dbItemJobStream.getFolder() + " is missing. Event " + conditionEvent.getEvent()
                            + " not deleted.");
                }
            }
            return JOCDefaultResponse.responseStatus200(conditionEvent);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken, String eventKey, FilterEvents filter) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ConditionEventsImpl.class.getName());
        Map<String, String> parameters = new HashMap<String, String>();
        if (filter != null) {
            parameters.put("event", filter.getEvent());
            parameters.put("session", filter.getSession());
            if (filter.getOutConditionId() != null) {
                parameters.put("outConditionId", String.valueOf(filter.getOutConditionId()));
            }
            if (filter.getJobStream() != null) {
                parameters.put("jobStream", filter.getJobStream());
            }
            parameters.put("globalEvent", filter.getGlobalEventAsString());
        }
        customEventsUtil.addEvent(eventKey, parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
        jocXmlCommand.throwJobSchedulerError();
    }
}