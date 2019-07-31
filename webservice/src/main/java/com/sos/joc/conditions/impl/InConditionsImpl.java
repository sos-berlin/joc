package com.sos.joc.conditions.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.eventhandlerservice.classes.Constants;
import com.sos.eventhandlerservice.db.DBItemConsumedInCondition;
import com.sos.eventhandlerservice.db.DBItemInConditionWithCommand;
import com.sos.eventhandlerservice.db.DBItemOutConditionWithEvent;
import com.sos.eventhandlerservice.db.DBLayerConsumedInConditions;
import com.sos.eventhandlerservice.db.DBLayerInConditions;
import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.eventhandlerservice.db.FilterConsumedInConditions;
import com.sos.eventhandlerservice.db.FilterInConditions;
import com.sos.eventhandlerservice.db.FilterOutConditions;
import com.sos.eventhandlerservice.resolver.JSCondition;
import com.sos.eventhandlerservice.resolver.JSConditionResolver;
import com.sos.eventhandlerservice.resolver.JSConditions;
import com.sos.eventhandlerservice.resolver.JSEventKey;
import com.sos.eventhandlerservice.resolver.JSInCondition;
import com.sos.eventhandlerservice.resolver.JSInConditionCommand;
import com.sos.eventhandlerservice.resolver.JSJobConditionKey;
import com.sos.eventhandlerservice.resolver.JSJobInConditions;
import com.sos.eventhandlerservice.resolver.JSJobOutConditions;
import com.sos.eventhandlerservice.resolver.JSOutCondition;
import com.sos.eventhandlerservice.resolver.JSOutConditionEvent;
import com.sos.eventhandlerservice.resolver.JSOutConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.classes.event.EventHandlerSettings;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.conditions.resource.IInConditionsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.ConditionExpression;
import com.sos.joc.model.conditions.InCondition;
import com.sos.joc.model.conditions.InConditionCommand;
import com.sos.joc.model.conditions.InConditions;
import com.sos.joc.model.conditions.JobInCondition;
import com.sos.joc.model.conditions.JobOutCondition;
import com.sos.joc.model.conditions.OutCondition;
import com.sos.joc.model.conditions.OutConditionEvent;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;

@Path("conditions")
public class InConditionsImpl extends JOCResourceImpl implements IInConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InConditionsImpl.class);
    private static final String API_CALL = "./conditions/jobstream_folders";
    private SOSHibernateSession sosHibernateSession = null;
    private JSConditionResolver jsConditionResolver;

    @Override
    public JOCDefaultResponse getJobInConditions(String accessToken, JobsFilter jobFilterSchema) throws Exception {

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilterSchema, accessToken, jobFilterSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(jobFilterSchema.getJobschedulerId(), accessToken).getCondition().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            checkRequiredParameter("jobs", jobFilterSchema.getJobs());

            InConditions inConditions = new InConditions();
            inConditions.setJobschedulerId(jobFilterSchema.getJobschedulerId());

            for (JobPath job : jobFilterSchema.getJobs()) {
                JobInCondition jobInCondition = new JobInCondition();

                DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
                DBLayerConsumedInConditions dbLayerCoumsumedInConditions = new DBLayerConsumedInConditions(sosHibernateSession);

                FilterInConditions filterInConditions = new FilterInConditions();
                FilterConsumedInConditions filterConsumedInConditions = new FilterConsumedInConditions();

                filterConsumedInConditions.setJobSchedulerId(jobFilterSchema.getJobschedulerId());
                filterConsumedInConditions.setJob(job.getJob());
                filterConsumedInConditions.setSession(Constants.getSession());

                filterInConditions.setJobSchedulerId(jobFilterSchema.getJobschedulerId());
                filterInConditions.setJob(job.getJob());

                jsConditionResolver = new JSConditionResolver(sosHibernateSession, accessToken, this.getCommandUrl());
                jsConditionResolver.initEvents();

                List<DBItemInConditionWithCommand> listOfInConditions = dbLayerInConditions.getInConditionsList(filterInConditions, 0);
                List<DBItemConsumedInCondition> listOfConsumedInConditions = dbLayerCoumsumedInConditions.getConsumedInConditionsListByJob(
                        filterConsumedInConditions, 0);
                HashMap<Long, DBItemConsumedInCondition> mapOfConsumedInCondition = new HashMap<Long, DBItemConsumedInCondition>();
                for (DBItemConsumedInCondition dbItemConsumedInCondition : listOfConsumedInConditions) {
                    mapOfConsumedInCondition.put(dbItemConsumedInCondition.getInConditionId(), dbItemConsumedInCondition);
                }

                for (DBItemInConditionWithCommand dbItemInCondition : listOfInConditions) {
                    dbItemInCondition.setConsumed((mapOfConsumedInCondition.get(dbItemInCondition.getDbItemInCondition().getId()) != null));
                }

                EventHandlerSettings settings = new EventHandlerSettings();
                JSJobInConditions jsJobInConditions = new JSJobInConditions(settings);
                jsJobInConditions.setListOfJobInConditions(listOfInConditions);
                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job.getJob());
                jsJobConditionKey.setJobSchedulerId(jobFilterSchema.getJobschedulerId());

                jobInCondition.setJob(jsJobConditionKey.getJob());
                if (jsJobInConditions.getInConditions(jsJobConditionKey) != null) {
                    for (JSInCondition jsInCondition : jsJobInConditions.getInConditions(jsJobConditionKey).getListOfInConditions().values()) {
                        InCondition inCondition = new InCondition();
                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsInCondition.getExpression());
                        conditionExpression.setValue(jsConditionResolver.validate(null, jsInCondition));
                        conditionExpression.setValidatedExpression(jsConditionResolver.getBooleanExpression().getNormalizedBoolExpr());
                        inCondition.setConditionExpression(conditionExpression);
                        inCondition.setJobStream(jsInCondition.getJobStream());
                        inCondition.setId(jsInCondition.getId());
                        inCondition.setConsumed(jsInCondition.isConsumed());
                        inCondition.setMarkExpression(jsInCondition.isMarkExpression());
                        inCondition.setOutconditions(getOutConditions(jsJobConditionKey, jobFilterSchema.getJobschedulerId(), jsInCondition
                                .getExpression()));
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

    private List<JobOutCondition> getOutConditions(JSJobConditionKey jsJobConditionKey, String schedulerId, String expression)
            throws SOSHibernateException {
        JSConditions jsConditions = new JSConditions();
        List<JobOutCondition> listOfOutConditions = new ArrayList<JobOutCondition>();
        List<JSCondition> listOfConditions = jsConditions.getListOfConditions(expression);
        DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
        FilterOutConditions filterOutConditions = new FilterOutConditions();

        filterOutConditions.setJobSchedulerId(schedulerId);
        for (JSCondition jsCondition : listOfConditions) {
            if ("event".equals(jsCondition.getConditionType())) {
                filterOutConditions.addEvent(jsCondition.getEventName());
            }
        }
        List<DBItemOutConditionWithEvent> listOfOutConditionsItems = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
        JSJobOutConditions jsJobOutConditions = new JSJobOutConditions();
        jsJobOutConditions.setListOfJobOutConditions(listOfOutConditionsItems);

        JSEventKey jsEventKey = new JSEventKey();
        jsEventKey.setSession(Constants.getSession());

        Map<JSJobConditionKey, JSOutConditions> mapOfjsOutConditions = jsJobOutConditions.getListOfJobOutConditions();

        for (JSOutConditions jsOutConditions : mapOfjsOutConditions.values()) {
            for (JSOutCondition jsOutCondition : jsOutConditions.getListOfOutConditions().values()) {
                JobOutCondition jobOutCondition = new JobOutCondition();
                jobOutCondition.setJob(jsOutCondition.getJob());
                OutCondition outCondition = new OutCondition();
                ConditionExpression conditionExpression = new ConditionExpression();
                conditionExpression.setExpression(jsOutCondition.getExpression());
                conditionExpression.setValue(jsConditionResolver.validate(-1, jsOutCondition));
                conditionExpression.setValidatedExpression(jsConditionResolver.getBooleanExpression().getNormalizedBoolExpr());
                outCondition.setConditionExpression(conditionExpression);
                outCondition.setJobStream(jsOutCondition.getJobStream());
                outCondition.setId(jsOutCondition.getId());
                for (JSOutConditionEvent jsOutConditionEvent : jsOutCondition.getListOfOutConditionEvent()) {
                    OutConditionEvent outConditionEvent = new OutConditionEvent();
                    outConditionEvent.setEvent(jsOutConditionEvent.getEventValue());
                    outConditionEvent.setCommand(jsOutConditionEvent.getCommand());
                    jsEventKey.setEvent(jsOutConditionEvent.getEvent());
                    if (jsOutConditionEvent.isCreateCommand()) {
                        outConditionEvent.setExistsInJobStream(jsConditionResolver.eventExist(jsEventKey, outCondition.getJobStream()));
                        outConditionEvent.setExists(jsConditionResolver.eventExist(jsEventKey, ""));
                    } else {
                        outConditionEvent.setExistsInJobStream(false);
                        outConditionEvent.setExists(false);
                    }
                    outConditionEvent.setId(jsOutConditionEvent.getId());
                    outCondition.getOutconditionEvents().add(outConditionEvent);
                }
                jobOutCondition.getOutconditions().add(outCondition);
                listOfOutConditions.add(jobOutCondition);
            }

        }

        return listOfOutConditions;
    }

}