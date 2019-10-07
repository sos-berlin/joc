package com.sos.joc.jobstreams.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.eventhandler.handler.EventHandlerSettings;
import com.sos.jobstreams.classes.Constants;
import com.sos.jobstreams.db.DBItemConsumedInCondition;
import com.sos.jobstreams.db.DBItemInConditionWithCommand;
import com.sos.jobstreams.db.DBItemOutConditionWithConfiguredEvent;
import com.sos.jobstreams.db.DBLayerConsumedInConditions;
import com.sos.jobstreams.db.DBLayerInConditions;
import com.sos.jobstreams.db.DBLayerOutConditions;
import com.sos.jobstreams.db.FilterConsumedInConditions;
import com.sos.jobstreams.db.FilterInConditions;
import com.sos.jobstreams.db.FilterOutConditions;
import com.sos.jobstreams.resolver.JSCondition;
import com.sos.jobstreams.resolver.JSConditionResolver;
import com.sos.jobstreams.resolver.JSConditions;
import com.sos.jobstreams.resolver.JSEventKey;
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
import com.sos.joc.model.jobstreams.ConditionRef;
import com.sos.joc.model.jobstreams.InCondition;
import com.sos.joc.model.jobstreams.InConditionCommand;
import com.sos.joc.model.jobstreams.InConditions;
import com.sos.joc.model.jobstreams.JobInCondition;
import com.sos.joc.model.jobstreams.JobstreamConditions;

@Path("jobstreams")
public class InConditionsImpl extends JOCResourceImpl implements IInConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InConditionsImpl.class);
    private static final String API_CALL = "./conditions/in_condition";
    private SOSHibernateSession sosHibernateSession = null;
    private JSConditionResolver jsConditionResolver;

    @Override
    public JOCDefaultResponse getJobInConditions(String accessToken, JobsFilter jobFilterSchema) throws Exception {

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilterSchema, accessToken, jobFilterSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(jobFilterSchema.getJobschedulerId(), accessToken).getJobStream().getView().isStatus());
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

                jsConditionResolver = new JSConditionResolver(sosHibernateSession, jobFilterSchema.getJobschedulerId());
                jsConditionResolver.setWorkingDirectory(dbItemInventoryInstance.getLiveDirectory() + "/../../");
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
                jsJobInConditions.setListOfJobInConditions(sosHibernateSession, listOfInConditions);
                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job.getJob());
                jsJobConditionKey.setJobSchedulerId(jobFilterSchema.getJobschedulerId());

                jobInCondition.setJob(jsJobConditionKey.getJob());
                if (jsJobInConditions.getInConditions(jsJobConditionKey) != null) {
                    for (JSInCondition jsInCondition : jsJobInConditions.getInConditions(jsJobConditionKey).getListOfInConditions().values()) {
                        InCondition inCondition = new InCondition();
                        JSConditions jsConditions = new JSConditions();
                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsInCondition.getExpression());
                        conditionExpression.setValue(jsConditionResolver.validate(null, jsInCondition));
                        conditionExpression.setValidatedExpression(jsConditionResolver.getBooleanExpression().getNormalizedBoolExpr());
                        List<JSCondition> listOfConditions = jsConditions.getListOfConditions(jsInCondition.getExpression());
                        for (JSCondition jsCondition : listOfConditions) {
                            if (!conditionExpression.getJobStreamEvents().contains(jsCondition.getEventName())) {
                                if (jsCondition.getConditionJobStream().isEmpty() || jsInCondition.getJobStream().equals(jsCondition
                                        .getConditionJobStream())) {
                                    conditionExpression.getJobStreamEvents().add(jsCondition.getEventName());
                                }
                            }
                        }
                        inCondition.setConditionExpression(conditionExpression);
                        inCondition.setJobStream(jsInCondition.getJobStream());
                        inCondition.setId(jsInCondition.getId());
                        inCondition.setConsumed(jsInCondition.isConsumed());
                        inCondition.setMarkExpression(jsInCondition.isMarkExpression());
                        inCondition.setSkipOutCondition(jsInCondition.isSkipOutCondition());
                        inCondition.setOutconditions(getOutConditions(jsJobConditionKey, jobFilterSchema.getJobschedulerId(), jsInCondition));
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

    private List<JobstreamConditions> getOutConditions(JSJobConditionKey jsJobConditionKey, String schedulerId, JSInCondition jsInCondition)
            throws SOSHibernateException {
        JSConditions jsConditions = new JSConditions();
        List<JobstreamConditions> listOfJobStreamConditions = new ArrayList<JobstreamConditions>();
        List<JSCondition> listOfConditions = jsConditions.getListOfConditions(jsInCondition.getExpression());
        DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
        FilterOutConditions filterOutConditions = new FilterOutConditions();

        filterOutConditions.setJobSchedulerId(schedulerId);
        for (JSCondition jsCondition : listOfConditions) {
            if ("event".equals(jsCondition.getConditionType())) {
                JSEventKey jsEventKey = new JSEventKey();
                jsEventKey.setEvent(jsCondition.getEventName());
                jsEventKey.setJobStream(jsCondition.getConditionJobStream());
                jsEventKey.setSchedulerId(schedulerId);
                filterOutConditions.addEvent(jsEventKey);
            }
        }
        if (filterOutConditions.getListOfEvents() != null && filterOutConditions.getListOfEvents().size() > 0) {
            List<DBItemOutConditionWithConfiguredEvent> listOfOutConditionsItems = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
            JSJobOutConditions jsJobOutConditions = new JSJobOutConditions();
            jsJobOutConditions.setListOfJobOutConditions(listOfOutConditionsItems);

            JSEventKey jsEventKey = new JSEventKey();
            jsEventKey.setSession(Constants.getSession());

            Map<JSJobConditionKey, JSOutConditions> mapOfjsOutConditions = jsJobOutConditions.getListOfJobOutConditions();
            Map<String, HashMap<String, ArrayList<String>>> listOfJobstreams = new HashMap<String, HashMap<String, ArrayList<String>>>();
            // List<> listOfJobs = new HashMap<String,LinkedHashSet<OutConditionRef>>();

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