package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.job.post.JobBody;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.ConfigurationStatusSchema.Text;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.Job200VSchema;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskQueue;
import com.sos.joc.model.job.RunningTask.Cause;
import com.sos.joc.model.job.OrdersSummary;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {
    private static final Logger LOGGER = Logger.getLogger(JobResourceImpl.class);

    public JOCDefaultResponse postJob(String accessToken, JobBody jobBody) throws Exception {
        LOGGER.debug("init Job");
        JOCDefaultResponse jocDefaultResponse = init(jobBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
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
            state.setText(com.sos.joc.model.job.State_.Text.loaded);
            job.setState(state);
            job.setStateText("myStateText");
            job.setSurveyDate(new Date());
            OrdersSummary ordersSummary = new OrdersSummary();
            ordersSummary.setPending(-1);
            ordersSummary.setRunning(-1);
            ordersSummary.setSetback(-1);
            ordersSummary.setSuspended(-1);
            ordersSummary.setWaitingForResource(-1);
            job.setOrdersSummary(ordersSummary);

            job.setNumOfQueuedTasks(-1);
            job.setNumOfRunningTasks(-1);

            ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
            configurationStatusSchema.setMessage("myMessage");
            configurationStatusSchema.setSeverity(-1);
            configurationStatusSchema.setText(Text.changed_file_not_loaded);
            job.setConfigurationStatus(configurationStatusSchema);
            List<Lock_> listOfLocks = new ArrayList<Lock_>();
            Lock_ lock = new Lock_();
            lock.setExclusive(false);
            lock.setAvailable(true);
            lock.setPath("myPath");
            listOfLocks.add(lock);
            Lock_ lock2 = new Lock_();
            lock2.setExclusive(true);
            lock2.setAvailable(false);
            lock2.setPath("myPath2");
            listOfLocks.add(lock2);
            job.setLocks(listOfLocks);

            if (jobBody.getCompact()) {
                job.setAllSteps(-1);
                job.setAllTasks(-1);

                job.setDelayUntil(new Date());

                job.setNextPeriodBegin("myNextPeriodBegin");
                job.setNextStartTime(new Date());

                List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                NameValuePairsSchema param1 = new NameValuePairsSchema();
                NameValuePairsSchema param2 = new NameValuePairsSchema();
                param1.setName("param1");
                param1.setValue("value1");
                param2.setName("param2");
                param2.setValue("value2");
                parameters.add(param1);
                parameters.add(param1);

                job.setParams(parameters);

                List<RunningTask> listOfRunningTask = new ArrayList<RunningTask>();
                RunningTask runningTask = new RunningTask();
                runningTask.setCause(Cause.none);
                runningTask.setEnqueued(new Date());
                runningTask.setIdleSince(new Date());

                Order order = new Order();
                order.setInProcessSince(new Date());
                order.setJobChain("myJobChain");
                order.setOrderId("myOrderId");
                order.setPath("myPath");
                order.setState("myState");
                runningTask.setOrder(order);

                runningTask.setPid(-1);
                runningTask.setStartedAt(new Date());
                runningTask.setSteps(-1);
                runningTask.setTaskId(-1);
                listOfRunningTask.add(runningTask);

                job.setRunningTasks(listOfRunningTask);

                List<TaskQueue> listOfTasks = new ArrayList<TaskQueue>();
                TaskQueue taskQueue = new TaskQueue();
                taskQueue.setTaskId(-1);
                job.setTaskQueue(listOfTasks);

                job.setTemporary(false);
            }

            entity.setJob(job);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
}
