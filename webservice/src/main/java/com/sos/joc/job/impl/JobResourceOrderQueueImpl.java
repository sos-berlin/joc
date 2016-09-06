package com.sos.joc.job.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.Job200VSchema;
import com.sos.joc.model.job.JobOrderQueueFilterSchema;
import com.sos.joc.model.job.Job_;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImpl.class);

    @Override
    public JOCDefaultResponse postJobOrderQueue(String accessToken, JobOrderQueueFilterSchema jobOrderQueueFilterSchema) throws Exception {

        LOGGER.debug("init JobsP");
        JOCDefaultResponse jocDefaultResponse = init(jobOrderQueueFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            Job200VSchema entity = new Job200VSchema();

            entity.setDeliveryDate(new Date());
            Job_ job = new Job_();

            job.setName("myName");
            job.setPath("myPath");

            com.sos.joc.model.job.State_ state = new com.sos.joc.model.job.State_();
            state.setSeverity(0);
            state.setText(com.sos.joc.model.job.State_.Text.PENDING);
            job.setState(state);
            job.setStateText("myStateText");
            job.setSurveyDate(new Date());
             
            job.setOrdersSummary(Orders.getOrdersSummary());

            job.setNumOfQueuedTasks(-1);
            job.setNumOfRunningTasks(-1);

            
            job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            job.setLocks(Jobs.getLocks());

            if (jobOrderQueueFilterSchema.getCompact()) {
                job.setAllSteps(-1);
                job.setAllTasks(-1);

                job.setNextPeriodBegin("myNextPeriodBegin");

              
                entity.setDeliveryDate(new Date());
               
                job.setOrderQueue(Orders.getOrderQueueList());
                job.setParams(Parameters.getParameters());
                job.setRunningTasks(Jobs.getRunningTasks());
                job.setTaskQueue(Jobs.getTaskQueue());
                job.setTemporary(false);
            }

            entity.setJob(job);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
