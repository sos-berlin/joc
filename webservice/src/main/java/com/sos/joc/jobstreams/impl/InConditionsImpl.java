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

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.eventhandler.handler.EventHandlerSettings;
import com.sos.jitl.jobstreams.Constants;
import com.sos.jitl.jobstreams.classes.JSEventKey;
import com.sos.jitl.jobstreams.db.DBItemCalendarWithUsages;
import com.sos.jitl.jobstreams.db.DBItemConsumedInCondition;
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemInConditionWithCommand;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBItemOutConditionWithConfiguredEvent;
import com.sos.jitl.jobstreams.db.DBItemOutConditionWithEvent;
import com.sos.jitl.jobstreams.db.DBLayerCalendarUsages;
import com.sos.jitl.jobstreams.db.DBLayerConsumedInConditions;
import com.sos.jitl.jobstreams.db.DBLayerEvents;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterCalendarUsage;
import com.sos.jitl.jobstreams.db.FilterConsumedInConditions;
import com.sos.jitl.jobstreams.db.FilterEvents;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.jobstreams.resolver.JSCondition;
import com.sos.jobstreams.resolver.JSConditionResolver;
import com.sos.jobstreams.resolver.JSConditions;
import com.sos.jobstreams.resolver.JSInCondition;
import com.sos.jobstreams.resolver.JSInConditionCommand;
import com.sos.jobstreams.resolver.JSJobConditionKey;
import com.sos.jobstreams.resolver.JSJobInConditions;
import com.sos.jobstreams.resolver.JSJobOutConditions;
import com.sos.jobstreams.resolver.JSOutCondition;
import com.sos.jobstreams.resolver.JSOutConditions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobstreams.resource.IInConditionsResource;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.jobstreams.ConditionExpression;
import com.sos.joc.model.jobstreams.ConditionJobsFilter;
import com.sos.joc.model.jobstreams.ConditionRef;
import com.sos.joc.model.jobstreams.InCondition;
import com.sos.joc.model.jobstreams.InConditionCommand;
import com.sos.joc.model.jobstreams.InConditions;
import com.sos.joc.model.jobstreams.JobInCondition;
import com.sos.joc.model.jobstreams.JobstreamConditions;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class InConditionsImpl extends JOCResourceImpl implements IInConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InConditionsImpl.class);
    private static final String API_CALL = "./conditions/in_condition";
    private SOSHibernateSession sosHibernateSession = null;
    private JSConditionResolver jsConditionResolver;

    @Override
    public JOCDefaultResponse getJobInConditions(String accessToken, byte[] filterBytes) {
        boolean compact = false;
        try {

            JsonValidator.validateFailFast(filterBytes, JobsFilter.class);
            ConditionJobsFilter conditionJobsFilterSchema = Globals.objectMapper.readValue(filterBytes, ConditionJobsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionJobsFilterSchema, accessToken, conditionJobsFilterSchema
                    .getJobschedulerId(), getPermissonsJocCockpit(conditionJobsFilterSchema.getJobschedulerId(), accessToken).getJobStream().getView()
                            .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if ((conditionJobsFilterSchema.getJobs() == null) && (conditionJobsFilterSchema.getFolder() == null)) {
                throw new JocMissingRequiredParameterException(String.format("undefined '%1$s'", "jobs or folder"));
            }

            if (conditionJobsFilterSchema.getCompact() != null) {
                compact = conditionJobsFilterSchema.getCompact();
            }
            if (Globals.schedulerVariables == null) {
                readJobSchedulerVariables();
                Constants.periodBegin = Globals.schedulerVariables.get("sos.jobstream_period_begin");
                Constants.settings = new EventHandlerSettings();
                Constants.settings.setTimezone(dbItemInventoryInstance.getTimeZone());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

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

            InConditions inConditions = new InConditions();
            inConditions.setJobschedulerId(conditionJobsFilterSchema.getJobschedulerId());

            jsConditionResolver = new JSConditionResolver(conditionJobsFilterSchema.getJobschedulerId());
            jsConditionResolver.setWorkingDirectory(dbItemInventoryInstance.getLiveDirectory() + "/../../");

            FilterCalendarUsage filterCalendarUsage = new FilterCalendarUsage();
            filterCalendarUsage.setSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
            filterCalendarUsage.setObjectType("JOB");
            DBLayerCalendarUsages dbLayer = new DBLayerCalendarUsages(sosHibernateSession);
            Map<String, List<DBItemCalendarWithUsages>> listOfCalendarUsages = new HashMap<String, List<DBItemCalendarWithUsages>>();
            List<DBItemCalendarWithUsages> l = dbLayer.getCalendarUsages(filterCalendarUsage, 0);
            for (DBItemCalendarWithUsages item : l) {
                if (listOfCalendarUsages.get(item.getPath()) == null) {
                    List<DBItemCalendarWithUsages> usages = new ArrayList<DBItemCalendarWithUsages>();
                    listOfCalendarUsages.put(item.getPath(), usages);
                }
                listOfCalendarUsages.get(item.getPath()).add(item);
            }

            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
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

                FilterOutConditions filterOutConditions = new FilterOutConditions();
                filterOutConditions.setFolder(conditionJobsFilterSchema.getFolder());
                filterOutConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
                for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
                    listOfJobs.add(dbItemOutCondition.getJob());
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

            if (!compact) {
                jsConditionResolver.initEvents(sosHibernateSession, contextId);
            }

            for (String job : listOfJobs) {
                JobInCondition jobInCondition = new JobInCondition();

                DBLayerConsumedInConditions dbLayerCoumsumedInConditions = new DBLayerConsumedInConditions(sosHibernateSession);

                filterInConditions = new FilterInConditions();
                FilterConsumedInConditions filterConsumedInConditions = new FilterConsumedInConditions();

                filterConsumedInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                filterConsumedInConditions.setJob(job);
                if (contextId != null) {
                    filterConsumedInConditions.setSession(contextId.toString());
                }

                filterInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                filterInConditions.setJob(job);

                List<DBItemInConditionWithCommand> listOfInConditions = dbLayerInConditions.getInConditionsList(filterInConditions, 0);
                if (!compact) {
                    List<DBItemConsumedInCondition> listOfConsumedInConditions = dbLayerCoumsumedInConditions.getConsumedInConditionsListByJob(
                            filterConsumedInConditions, 0);
                    HashMap<Long, DBItemConsumedInCondition> mapOfConsumedInCondition = new HashMap<Long, DBItemConsumedInCondition>();
                    for (DBItemConsumedInCondition dbItemConsumedInCondition : listOfConsumedInConditions) {
                        mapOfConsumedInCondition.put(dbItemConsumedInCondition.getInConditionId(), dbItemConsumedInCondition);
                    }

                    for (DBItemInConditionWithCommand dbItemInCondition : listOfInConditions) {
                        for (DBItemConsumedInCondition dbItemConsumedInCondition : listOfConsumedInConditions) {
                            mapOfConsumedInCondition.put(dbItemConsumedInCondition.getInConditionId(), dbItemConsumedInCondition);
                            dbItemInCondition.setConsumed(dbItemConsumedInCondition.getSession());
                        }
                    }
                }
                EventHandlerSettings settings = new EventHandlerSettings();
                settings.setSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                JSJobInConditions jsJobInConditions = new JSJobInConditions();
                jsJobInConditions.setListOfJobInConditions(sosHibernateSession, listOfCalendarUsages, listOfInConditions);

                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job);
                jsJobConditionKey.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());

                jobInCondition.setJob(jsJobConditionKey.getJob());
                if (jsJobInConditions.getInConditions(jsJobConditionKey) != null) {
                    for (JSInCondition jsInCondition : jsJobInConditions.getInConditions(jsJobConditionKey).getListOfInConditions().values()) {

                        InCondition inCondition = new InCondition();
                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsInCondition.getExpression());

                        List<JSCondition> listOfConditions = JSConditions.getListOfConditions(jsInCondition.getExpression());
                        if (!compact || true) {
                            for (JSCondition jsCondition : listOfConditions) {
                                if (jsCondition.isNonContextEvent()) {
                                    FilterEvents filterEvents = jsCondition.getFilterEventsNonContextEvent(conditionJobsFilterSchema
                                            .getJobschedulerId());
                                    DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
                                    List<DBItemOutConditionWithEvent> listOfNonContextEvents = dbLayerEvents.getEventsList(filterEvents, 0);
                                    jsConditionResolver.addEventsFromList(listOfNonContextEvents);
                                }

                                if (!conditionExpression.getJobStreamEvents().contains(jsCondition.getConditionValueShort())) {
                                    if (jsCondition.getConditionJobStream().isEmpty() || jsInCondition.getJobStream().equals(jsCondition
                                            .getConditionJobStream())) {
                                        if (jsCondition.haveCustomSession()) {
                                            conditionExpression.getJobStreamEvents().add(jsCondition.getConditionValueShort());
                                        } else {
                                            conditionExpression.getJobStreamEvents().add(jsCondition.getEventName());
                                        }
                                    }
                                }
                            }

                            if (contextId != null && !compact) {
                                conditionExpression.setValue(jsConditionResolver.validate(sosHibernateSession, null, contextId, jsInCondition));
                                conditionExpression.setValidatedExpression(jsConditionResolver.getBooleanExpression().getNormalizedBoolExpr());
                            }
                        }
                        inCondition.setConditionExpression(conditionExpression);
                        inCondition.setJobStream(jsInCondition.getJobStream());
                        inCondition.setId(jsInCondition.getId());
                        inCondition.setConsumed(jsInCondition.isConsumed(contextId));
                        inCondition.setMarkExpression(jsInCondition.isMarkExpression());
                        inCondition.setSkipOutCondition(jsInCondition.isSkipOutCondition());
                        inCondition.setNextPeriod(jsInCondition.getNextPeriod());

                        inCondition.setOutconditions(getOutConditions(jsJobConditionKey, conditionJobsFilterSchema.getJobschedulerId(), jsInCondition,
                                conditionJobsFilterSchema.getSession()));

                        for (JSInConditionCommand jsInConditionCommand : jsInCondition.getListOfInConditionCommand()) {
                            InConditionCommand inConditionCommand = new InConditionCommand();
                            inConditionCommand.setCommand(jsInConditionCommand.getCommand());
                            inConditionCommand.setCommandParam(jsInConditionCommand.getCommandParam());
                            inConditionCommand.setId(jsInConditionCommand.getId());
                            inCondition.getInconditionCommands().add(inConditionCommand);
                        }
                        jobInCondition.getInconditions().add(inCondition);
                    }
                }
                inConditions.getJobsInconditions().add(jobInCondition);
            }

            inConditions.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(inConditions);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

            Globals.disconnect(sosHibernateSession);
        }
    }

    private List<JobstreamConditions> getOutConditions(JSJobConditionKey jsJobConditionKey, String schedulerId, JSInCondition jsInCondition,
            String session) throws SOSHibernateException {

        List<JobstreamConditions> listOfJobStreamConditions = new ArrayList<JobstreamConditions>();
        Set<JobstreamConditions> listOfJobStreamConditionsSet = new HashSet<JobstreamConditions>();
        List<JSCondition> listOfConditions = JSConditions.getListOfConditions(jsInCondition.getExpression());
        DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
        FilterOutConditions filterOutConditions = new FilterOutConditions();

        filterOutConditions.setJobSchedulerId(schedulerId);
        for (JSCondition jsCondition : listOfConditions) {
            if (jsCondition.typeIsEvent()) {
                JSEventKey jsEventKey = new JSEventKey();
                if (jsCondition.haveCustomSession()) {
                    jsEventKey.setEvent(jsCondition.getConditionValueShort());
                } else {
                    jsEventKey.setEvent(jsCondition.getEventName());
                }

                jsEventKey.setJobStream(jsCondition.getConditionJobStream());
                jsEventKey.setSchedulerId(schedulerId);
                jsEventKey.setGlobalEvent(jsCondition.typeIsGlobalEvent());
                filterOutConditions.addEvent(jsEventKey);
            }
        }
        if (filterOutConditions.getListOfEvents() != null && filterOutConditions.getListOfEvents().size() > 0) {
            List<DBItemOutConditionWithConfiguredEvent> listOfOutConditionsItems = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
            JSJobOutConditions jsJobOutConditions = new JSJobOutConditions();
            jsJobOutConditions.setListOfJobOutConditions(listOfOutConditionsItems);

            JSEventKey jsEventKey = new JSEventKey();
            jsEventKey.setSession(session);

            Map<JSJobConditionKey, JSOutConditions> mapOfjsOutConditions = jsJobOutConditions.getListOfJobOutConditions();
            Map<String, HashMap<String, ArrayList<String>>> listOfJobstreams = new HashMap<String, HashMap<String, ArrayList<String>>>();

            for (JSOutConditions jsOutConditions : mapOfjsOutConditions.values()) {
                for (JSOutCondition jsOutCondition : jsOutConditions.getListOfOutConditions().values()) {
                    HashMap<String, ArrayList<String>> listOfJobs = null;
                    ArrayList<String> listOfExpressions = null;
                    if (listOfJobstreams.get(jsOutCondition.getJobStream()) == null) {
                        listOfJobs = new HashMap<String, ArrayList<String>>();
                    } else {
                        listOfJobs = listOfJobstreams.get(jsOutCondition.getJobStream());
                    }

                    if (listOfJobs.get(jsOutCondition.getJob()) == null) {
                        listOfExpressions = new ArrayList<String>();
                    } else {
                        listOfExpressions = listOfJobs.get(jsOutCondition.getJob());
                    }

                    listOfExpressions.add(jsOutCondition.getExpression());
                    listOfJobs.put(jsOutCondition.getJob(), listOfExpressions);
                    listOfJobstreams.put(jsOutCondition.getJobStream(), listOfJobs);
                }
            }

            listOfJobstreams.forEach((jobStream, jobs) -> {
                JobstreamConditions jobstreamConditions = new JobstreamConditions();
                jobstreamConditions.setJobStream(jobStream);
                jobs.forEach((job, expressions) -> {
                    ConditionRef conditionRef = new ConditionRef();
                    conditionRef.setJob(job);
                    conditionRef.setExpressions(expressions);
                    jobstreamConditions.getJobs().add(conditionRef);
                });

                for (JSCondition jsCondition : listOfConditions) {
                    if (jobstreamConditions.getJobStream().equals(jsInCondition.getJobStream()) || jobstreamConditions.getJobStream().equals(
                            jsCondition.getConditionJobStream())) {
                        listOfJobStreamConditionsSet.add(jobstreamConditions);
                    }
                }

            });
            for (JobstreamConditions jobstreamConditions : listOfJobStreamConditionsSet) {
                listOfJobStreamConditions.add(jobstreamConditions);
            }

        }

        return listOfJobStreamConditions;
    }

}