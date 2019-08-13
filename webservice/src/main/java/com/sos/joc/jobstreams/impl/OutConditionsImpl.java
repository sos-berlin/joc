package com.sos.joc.jobstreams.impl;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.eventhandlerservice.classes.Constants;
import com.sos.eventhandlerservice.db.DBItemOutConditionWithEvent;
import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.eventhandlerservice.db.FilterOutConditions;
import com.sos.eventhandlerservice.resolver.JSConditionResolver;
import com.sos.eventhandlerservice.resolver.JSEventKey;
import com.sos.eventhandlerservice.resolver.JSJobConditionKey;
import com.sos.eventhandlerservice.resolver.JSJobOutConditions;
import com.sos.eventhandlerservice.resolver.JSOutCondition;
import com.sos.eventhandlerservice.resolver.JSOutConditionEvent;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportTask;
import com.sos.jitl.reporting.db.ReportTaskExecutionsDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IOutConditionsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.ConditionExpression;
import com.sos.joc.model.conditions.JobOutCondition;
import com.sos.joc.model.conditions.OutCondition;
import com.sos.joc.model.conditions.OutConditionEvent;
import com.sos.joc.model.conditions.OutConditions;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;

@Path("conditions")
public class OutConditionsImpl extends JOCResourceImpl implements IOutConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutConditionsImpl.class);
    private static final String API_CALL = "./conditions/out_conditions";

    @Override
    public JOCDefaultResponse getJobOutConditions(String accessToken, JobsFilter jobFilterSchema) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilterSchema, accessToken, jobFilterSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(jobFilterSchema.getJobschedulerId(), accessToken).getCondition().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            checkRequiredParameter("job", jobFilterSchema.getJobs());

            OutConditions outConditions = new OutConditions();
            outConditions.setJobschedulerId(jobFilterSchema.getJobschedulerId());

            for (JobPath job : jobFilterSchema.getJobs()) {

                JobOutCondition jobOutCondition = new JobOutCondition();
                ReportTaskExecutionsDBLayer reportTaskExecutionsDBLayer = new ReportTaskExecutionsDBLayer(sosHibernateSession);
                reportTaskExecutionsDBLayer.getFilter().setSortMode("desc");
                reportTaskExecutionsDBLayer.getFilter().setOrderCriteria("endTime");
                reportTaskExecutionsDBLayer.getFilter().addJobPath(job.getJob());
                reportTaskExecutionsDBLayer.getFilter().setSchedulerId(jobFilterSchema.getJobschedulerId());
                DBItemReportTask dbItemReportTask = reportTaskExecutionsDBLayer.getHistoryItem();
                Integer exit = null;
                if (dbItemReportTask != null) {
                    exit = dbItemReportTask.getExitCode();
                }

                DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
                FilterOutConditions filterOutConditions = new FilterOutConditions();
                filterOutConditions.setJob(job.getJob());
                filterOutConditions.setJobSchedulerId(jobFilterSchema.getJobschedulerId());

                JSConditionResolver jsConditionResolver = new JSConditionResolver(sosHibernateSession, accessToken, this.getCommandUrl());
                jsConditionResolver.initEvents();

                List<DBItemOutConditionWithEvent> listOfOutConditions = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
                JSJobOutConditions jsJobOutConditions = new JSJobOutConditions();
                jsJobOutConditions.setListOfJobOutConditions(listOfOutConditions);
                JSJobConditionKey jsJobConditionKey = new JSJobConditionKey();
                jsJobConditionKey.setJob(job.getJob());
                jsJobConditionKey.setJobSchedulerId(jobFilterSchema.getJobschedulerId());

                jobOutCondition.setJob(job.getJob());

                if (jsJobOutConditions.getOutConditions(jsJobConditionKey) != null) {

                    JSEventKey jsEventKey = new JSEventKey();
                    jsEventKey.setSession(Constants.getSession());

                    for (JSOutCondition jsOutCondition : jsJobOutConditions.getOutConditions(jsJobConditionKey).getListOfOutConditions().values()) {
                        OutCondition outCondition = new OutCondition();
                        ConditionExpression conditionExpression = new ConditionExpression();
                        conditionExpression.setExpression(jsOutCondition.getExpression());
                        conditionExpression.setValue(jsConditionResolver.validate(exit, jsOutCondition));
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
                            }else {
                                outConditionEvent.setExistsInJobStream(false);
                                outConditionEvent.setExists(false);
                            }
                            outConditionEvent.setId(jsOutConditionEvent.getId());
                            outCondition.getOutconditionEvents().add(outConditionEvent);
                        }
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

}