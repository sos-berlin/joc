package com.sos.joc.jobstreams.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.eventhandler.handler.EventHandlerSettings;
import com.sos.jitl.jobstreams.Constants;
import com.sos.jitl.jobstreams.classes.JSEventKey;
import com.sos.jitl.jobstreams.db.DBItemConsumedInCondition;
import com.sos.jitl.jobstreams.db.DBItemInConditionWithCommand;
import com.sos.jitl.jobstreams.db.DBItemOutConditionWithConfiguredEvent;
import com.sos.jitl.jobstreams.db.DBLayerConsumedInConditions;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterConsumedInConditions;
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

        try {
            JsonValidator.validateFailFast(filterBytes, JobsFilter.class);
            ConditionJobsFilter conditionJobsFilterSchema = Globals.objectMapper.readValue(filterBytes, ConditionJobsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionJobsFilterSchema, accessToken, conditionJobsFilterSchema
                    .getJobschedulerId(), getPermissonsJocCockpit(conditionJobsFilterSchema.getJobschedulerId(), accessToken).getJobStream().getView()
                            .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            checkRequiredParameter("jobs", conditionJobsFilterSchema.getJobs());

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

            for (JobPath job : conditionJobsFilterSchema.getJobs()) {
                JobInCondition jobInCondition = new JobInCondition();

                DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
                DBLayerConsumedInConditions dbLayerCoumsumedInConditions = new DBLayerConsumedInConditions(sosHibernateSession);

                FilterInConditions filterInConditions = new FilterInConditions();
                FilterConsumedInConditions filterConsumedInConditions = new FilterConsumedInConditions();

                filterConsumedInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                filterConsumedInConditions.setJob(job.getJob());
                if (contextId != null) {
                    filterConsumedInConditions.setSession(contextId.toString());
                }

                filterInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                filterInConditions.setJob(job.getJob());

                jsConditionResolver = new JSConditionResolver(sosHibernateSession, conditionJobsFilterSchema.getJobschedulerId());
                jsConditionResolver.setWorkingDirectory(dbItemInventoryInstance.getLiveDirectory() + "/../../");
                jsConditionResolver.initEvents(sosHibernateSession);

                List<DBItemInConditionWithCommand> listOfInConditions = dbLayerInConditions.getInConditionsList(filterInConditions, 0);
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

                EventHandlerSettings settings = new EventHandlerSettings();
                settings.setSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                JSJobInConditions jsJobInConditions = new JSJobInConditions(settings);
                jsJobInConditions.setListOfJobInConditions(sosHibernateSession, listOfInConditions);
                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job.getJob());
                jsJobConditionKey.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());

                jobInCondition.setJob(jsJobConditionKey.getJob());
                if (jsJobInConditions.getInConditions(jsJobConditionKey) != null) {
                    for (JSInCondition jsInCondition : jsJobInConditions.getInConditions(jsJobConditionKey).getListOfInConditions().values()) {
                        InCondition inCondition = new InCondition();
                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsInCondition.getExpression());
                        if (contextId != null) {
                            conditionExpression.setValue(jsConditionResolver.validate(null, contextId, jsInCondition));
                        }
                        conditionExpression.setValidatedExpression(jsConditionResolver.getBooleanExpression().getNormalizedBoolExpr());
                        List<JSCondition> listOfConditions = JSConditions.getListOfConditions(jsInCondition.getExpression());
                        for (JSCondition jsCondition : listOfConditions) {
                            if (!conditionExpression.getJobStreamEvents().contains(jsCondition.getEventNameWithType())) {
                                if (jsCondition.getConditionJobStream().isEmpty() || jsInCondition.getJobStream().equals(jsCondition
                                        .getConditionJobStream())) {
                                    conditionExpression.getJobStreamEvents().add(jsCondition.getEventNameWithType());
                                }
                            }
                        }
                        inCondition.setConditionExpression(conditionExpression);
                        inCondition.setJobStream(jsInCondition.getJobStream());
                        inCondition.setId(jsInCondition.getId());
                        // TODO: inCondition.setConsumed(jsInCondition.isConsumed());
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
        List<JSCondition> listOfConditions = JSConditions.getListOfConditions(jsInCondition.getExpression());
        DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
        FilterOutConditions filterOutConditions = new FilterOutConditions();

        filterOutConditions.setJobSchedulerId(schedulerId);
        for (JSCondition jsCondition : listOfConditions) {
            if (jsCondition.typeIsEvent()) {
                JSEventKey jsEventKey = new JSEventKey();
                jsEventKey.setEvent(jsCondition.getEventName());
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
                listOfJobStreamConditions.add(jobstreamConditions);
            });
        }

        return listOfJobStreamConditions;
    }

}