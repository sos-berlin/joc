package com.sos.joc.conditions.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.eventhandlerservice.classes.Constants;
import com.sos.eventhandlerservice.db.DBItemConsumedInCondition;
import com.sos.eventhandlerservice.db.DBItemInConditionWithCommand;
import com.sos.eventhandlerservice.db.DBLayerConsumedInConditions;
import com.sos.eventhandlerservice.db.DBLayerInConditions;
import com.sos.eventhandlerservice.db.FilterConsumedInConditions;
import com.sos.eventhandlerservice.db.FilterInConditions;
import com.sos.eventhandlerservice.resolver.JSConditionResolver;
import com.sos.eventhandlerservice.resolver.JSInCondition;
import com.sos.eventhandlerservice.resolver.JSInConditionCommand;
import com.sos.eventhandlerservice.resolver.JSJobConditionKey;
import com.sos.eventhandlerservice.resolver.JSJobInConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
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
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;

@Path("conditions")
public class InConditionsImpl extends JOCResourceImpl implements IInConditionsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InConditionsImpl.class);
    private static final String API_CALL = "./conditions/job_in_conditions";

    @Override
    public JOCDefaultResponse getJobInConditions(String accessToken, JobsFilter jobFilterSchema) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilterSchema, accessToken, jobFilterSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(jobFilterSchema.getJobschedulerId(), accessToken).getCondition().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            checkRequiredParameter("jobs", jobFilterSchema.getJobs());
            checkRequiredParameter("jobschedulerId", jobFilterSchema.getJobschedulerId());

            InConditions inConditions = new InConditions();
            inConditions.setJobSchedulerId(jobFilterSchema.getJobschedulerId());

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

                JSConditionResolver jsConditionResolver = new JSConditionResolver(sosHibernateSession, accessToken, this.getCommandUrl());
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
                        inCondition.setWorkflow(jsInCondition.getWorkflow());
                        inCondition.setId(jsInCondition.getId());
                        inCondition.setConsumed(jsInCondition.isConsumed());
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

}