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
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemOutConditionWithConfiguredEvent;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
import com.sos.jobstreams.resolver.JSCondition;
import com.sos.jobstreams.resolver.JSConditionResolver;
import com.sos.jobstreams.resolver.JSConditions;
import com.sos.jobstreams.resolver.JSJobConditionKey;
import com.sos.jobstreams.resolver.JSJobOutConditions;
import com.sos.jobstreams.resolver.JSOutCondition;
import com.sos.jobstreams.resolver.JSOutConditionEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IOutConditionsResource;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.jobstreams.ConditionExpression;
import com.sos.joc.model.jobstreams.ConditionJobsFilter;
import com.sos.joc.model.jobstreams.ConditionRef;
import com.sos.joc.model.jobstreams.JobOutCondition;
import com.sos.joc.model.jobstreams.JobstreamConditions;
import com.sos.joc.model.jobstreams.OutCondition;
import com.sos.joc.model.jobstreams.OutConditionEvent;
import com.sos.joc.model.jobstreams.OutConditions;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class OutConditionsImpl extends JOCResourceImpl implements IOutConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutConditionsImpl.class);
    private static final String API_CALL = "./conditions/out_conditions";

    @Override
    public JOCDefaultResponse getJobOutConditions(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JsonValidator.validateFailFast(filterBytes, JobsFilter.class);
            ConditionJobsFilter conditionJobsFilterSchema = Globals.objectMapper.readValue(filterBytes, ConditionJobsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionJobsFilterSchema, accessToken, conditionJobsFilterSchema
                    .getJobschedulerId(), getPermissonsJocCockpit(conditionJobsFilterSchema.getJobschedulerId(), accessToken).getJobStream().getView()
                            .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            readJobSchedulerVariables();
            Constants.periodBegin = Globals.schedulerVariables.get("sos.jobstream_period_begin");
            Constants.settings = new EventHandlerSettings();
            Constants.settings.setTimezone(dbItemInventoryInstance.getTimeZone());
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            checkRequiredParameter("job", conditionJobsFilterSchema.getJobs());

            UUID contextId = null;
            try {
                if (conditionJobsFilterSchema.getSession() != null && !conditionJobsFilterSchema.getSession().isEmpty()) {
                    contextId = UUID.fromString(conditionJobsFilterSchema.getSession());
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Could not get session from: " + conditionJobsFilterSchema.getSession());
            }
            OutConditions outConditions = new OutConditions();
            outConditions.setJobschedulerId(conditionJobsFilterSchema.getJobschedulerId());

            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            FilterInConditions filterInConditions = new FilterInConditions();
            filterInConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());

            List<DBItemInCondition> listOfInConditionsItems = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);

            for (JobPath job : conditionJobsFilterSchema.getJobs()) {

                JobOutCondition jobOutCondition = new JobOutCondition();
                ReportTaskExecutionsDBLayer reportTaskExecutionsDBLayer = new ReportTaskExecutionsDBLayer(sosHibernateSession);
                reportTaskExecutionsDBLayer.getFilter().setSortMode("desc");
                reportTaskExecutionsDBLayer.getFilter().setOrderCriteria("endTime");
                reportTaskExecutionsDBLayer.getFilter().addJobPath(job.getJob());
                reportTaskExecutionsDBLayer.getFilter().setSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                DBItemReportTask dbItemReportTask = reportTaskExecutionsDBLayer.getHistoryItem();
                Integer exit = null;
                if (dbItemReportTask != null) {
                    exit = dbItemReportTask.getExitCode();
                }

                DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
                FilterOutConditions filterOutConditions = new FilterOutConditions();
                filterOutConditions.setJob(job.getJob());
                filterOutConditions.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());

                JSConditionResolver jsConditionResolver = new JSConditionResolver(conditionJobsFilterSchema.getJobschedulerId());
                jsConditionResolver.setWorkingDirectory(dbItemInventoryInstance.getLiveDirectory() + "/../../");
                jsConditionResolver.initEvents(sosHibernateSession);

                List<DBItemOutConditionWithConfiguredEvent> listOfOutConditions = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
                JSJobOutConditions jsJobOutConditions = new JSJobOutConditions();
                jsJobOutConditions.setListOfJobOutConditions(listOfOutConditions);
                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job.getJob());
                jsJobConditionKey.setJobSchedulerId(conditionJobsFilterSchema.getJobschedulerId());

                jobOutCondition.setJob(job.getJob());

                if (jsJobOutConditions.getOutConditions(jsJobConditionKey) != null) {

                    JSEventKey jsEventKey = new JSEventKey();
                    jsEventKey.setSession(conditionJobsFilterSchema.getSession());

                    for (JSOutCondition jsOutCondition : jsJobOutConditions.getOutConditions(jsJobConditionKey).getListOfOutConditions().values()) {
                        OutCondition outCondition = new OutCondition();
                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsOutCondition.getExpression());
                        if (contextId != null) {
                            conditionExpression.setValue(jsConditionResolver.validate(sosHibernateSession,exit, contextId, jsOutCondition));
                        }
                        conditionExpression.setValidatedExpression(jsConditionResolver.getBooleanExpression().getNormalizedBoolExpr());
                        outCondition.setConditionExpression(conditionExpression);
                        outCondition.setJobStream(jsOutCondition.getJobStream());
                        outCondition.setId(jsOutCondition.getId());

                        for (JSOutConditionEvent jsOutConditionEvent : jsOutCondition.getListOfOutConditionEvent()) {
                            OutConditionEvent outConditionEvent = new OutConditionEvent();
                            outConditionEvent.setEvent(jsOutConditionEvent.getEventValue());
                            outConditionEvent.setCommand(jsOutConditionEvent.getCommand());
                            outConditionEvent.setGlobalEvent(jsOutConditionEvent.isGlobal());
                            jsEventKey.setEvent(jsOutConditionEvent.getEvent());
                            jsEventKey.setGlobalEvent(jsOutConditionEvent.isGlobal());
                            jsEventKey.setSchedulerId(conditionJobsFilterSchema.getJobschedulerId());
                            if (jsOutConditionEvent.isCreateCommand() && jsEventKey.getSession() != null) {
                                jsEventKey.setJobStream(outCondition.getJobStream());
                                outConditionEvent.setExistsInJobStream(jsConditionResolver.eventExists(jsEventKey));

                                jsEventKey.setJobStream("");
                                outConditionEvent.setExists(jsConditionResolver.eventExists(jsEventKey));
                            } else {
                                outConditionEvent.setExistsInJobStream(false);
                                outConditionEvent.setExists(false);
                            }
                            outConditionEvent.setId(jsOutConditionEvent.getId());
                            outCondition.getOutconditionEvents().add(outConditionEvent);
                        }

                        outCondition.setInconditions(getInConditions(sosHibernateSession, outCondition, listOfInConditionsItems));
                        jobOutCondition.getOutconditions().add(outCondition);
                    }
                }
                outConditions.getJobsOutconditions().add(jobOutCondition);
            }
            outConditions.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(outConditions);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private List<JobstreamConditions> getInConditions(SOSHibernateSession sosHibernateSession, OutCondition outCondition,
            List<DBItemInCondition> listOfInConditionsItems) throws SOSHibernateException {

        List<JobstreamConditions> listOfJobStreamConditions = new ArrayList<JobstreamConditions>();
        Map<String, HashMap<String, ArrayList<String>>> listOfJobstreams = new HashMap<String, HashMap<String, ArrayList<String>>>();
        for (DBItemInCondition dbItemInCondition : listOfInConditionsItems) {
            String expression = " " + dbItemInCondition.getExpression() + " ";
            String jobStream = dbItemInCondition.getJobStream();

            List<JSCondition> listOfConditions = JSConditions.getListOfConditions(expression);

            for (OutConditionEvent event : outCondition.getOutconditionEvents()) {
                boolean eventIsUsedInExpression = false;
                if ("create".equals(event.getCommand())) {
                    for (JSCondition jsCondition : listOfConditions) {
                        if (jsCondition.typeIsEvent() && (jsCondition.typeIsGlobalEvent() == event.getGlobalEvent())) {
                            String jsConditionJobStream = "";
                            if (jsCondition.getConditionJobStream().isEmpty()) {
                                jsConditionJobStream = outCondition.getJobStream();
                            } else {
                                jsConditionJobStream = jsCondition.getConditionJobStream();
                            }
                            if (jsCondition.getEventName().equals(event.getEvent()) && (jobStream.equals(jsConditionJobStream) || outCondition
                                    .getJobStream().equals(jsCondition.getConditionJobStream()))) {
                                eventIsUsedInExpression = true;
                                continue;
                            }
                        }
                    }
                }

                if (eventIsUsedInExpression) {

                    HashMap<String, ArrayList<String>> listOfJobs = null;
                    ArrayList<String> listOfExpressions = null;
                    if (listOfJobstreams.get(jobStream) == null) {
                        listOfJobs = new HashMap<String, ArrayList<String>>();
                    } else {
                        listOfJobs = listOfJobstreams.get(jobStream);
                    }

                    if (listOfJobs.get(dbItemInCondition.getJob()) == null) {
                        listOfExpressions = new ArrayList<String>();
                    } else {
                        listOfExpressions = listOfJobs.get(dbItemInCondition.getJob());
                    }

                    if (!listOfExpressions.contains(expression)) {
                        listOfExpressions.add(expression);
                    }
                    listOfJobs.put(dbItemInCondition.getJob(), listOfExpressions);
                    listOfJobstreams.put(jobStream, listOfJobs);
                }
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

        return listOfJobStreamConditions;
    }

}