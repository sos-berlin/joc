package com.sos.joc.classes.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.LockUseP;
import com.sos.joc.model.job.LockUseV;
import com.sos.joc.model.job.OrderInRunningTask;
import com.sos.joc.model.job.QueuedTask;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskCause;

public class Jobs {

    public static List<LockUseV> getLocks() {
        List<LockUseV> listOfLocks = new ArrayList<LockUseV>();
        LockUseV lock = new LockUseV();
        lock.setExclusive(false);
        lock.setAvailable(true);
        lock.setPath("myPath");
        listOfLocks.add(lock);
        LockUseV lock2 = new LockUseV();
        lock2.setExclusive(true);
        lock2.setAvailable(false);
        lock2.setPath("myPath2");
        listOfLocks.add(lock2);
        return listOfLocks;
    }

    public static List<LockUseP> getJobLocks() {
        List<LockUseP> listOfLocks = new ArrayList<LockUseP>();
        LockUseP lock = new LockUseP();
        lock.setExclusive(false);
        // lock.setAvailable(true);
        lock.setPath("myPath");
        listOfLocks.add(lock);
        LockUseP lock2 = new LockUseP();
        lock2.setExclusive(true);
        // lock2.setAvailable(false);
        lock2.setPath("myPath2");
        listOfLocks.add(lock2);
        return listOfLocks;
    }

    public static List<RunningTask> getRunningTasks() {
        List<RunningTask> listOfRunningTask = new ArrayList<RunningTask>();
        RunningTask runningTask = new RunningTask();
        runningTask.set_cause(TaskCause.NONE);
        runningTask.setEnqueued(new Date());
        runningTask.setIdleSince(new Date());

        OrderInRunningTask order = new OrderInRunningTask();
        order.setInProcessSince(new Date());
        order.setJobChain("myJobChain");
        order.setOrderId("myOrderId");
        order.setPath("myPath");
        order.setState("myState");
        runningTask.setOrder(order);

        runningTask.setPid(-1);
        runningTask.setStartedAt(new Date());
        runningTask.setSteps(-1);
        runningTask.setTaskId("-1");
        listOfRunningTask.add(runningTask);
        return listOfRunningTask;

    }

    public static List<QueuedTask> getTaskQueue() {
        List<QueuedTask> listOfTasks = new ArrayList<QueuedTask>();
        QueuedTask taskQueue = new QueuedTask();
        taskQueue.setTaskId("-1");
        listOfTasks.add(taskQueue);
        return listOfTasks;
    }

    public static JobV getJob(boolean compact) {
        JobV job = new JobV();

        job.setName("myName");
        job.setPath("myPath");

//        com.sos.joc.model.job.State_ state = new com.sos.joc.model.job.State_();
//        state.setSeverity(0);
//        state.setText(com.sos.joc.model.job.State_.Text.LOADED);
        job.setState(null);
        job.setStateText("myStateText");
        job.setSurveyDate(new Date());

        job.setOrdersSummary(Orders.getOrdersSummary());

        job.setNumOfQueuedTasks(-1);
        job.setNumOfRunningTasks(-1);

        job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());

        job.setLocks(Jobs.getLocks());

        if (compact) {
            job.setAllSteps(-1);
            job.setAllTasks(-1);

            job.setDelayUntil(new Date());

            job.setNextStartTime(new Date());

            job.setParams(Parameters.getParameters());
            job.setRunningTasks(Jobs.getRunningTasks());

            job.setTaskQueue(Jobs.getTaskQueue());

            job.setTemporary(false);
        }
        return job;
    }
    
    public static JobP getPJob(boolean compact) {
        JobP job = new JobP();

        job.setName("myName");
        job.setPath("myPath");
        job.setConfigurationDate(new Date());
        job.setEstimatedDuration(-1);
        job.setHasDescription(false);
        job.setIsOrderJob(true);
        List <String> listOfJobChains = new ArrayList<String>();
        listOfJobChains.add("job_chain1");
        listOfJobChains.add("job_chain2");
        job.setJobChains(listOfJobChains);
        
        job.setLocks(getJobLocks());
        job.setMaxTasks(-1);
        job.setName("myName");
        job.setParams(Parameters.getParameters());
        job.setPath("myPath");
        job.setProcessClass("myProcessClass");
        job.setSurveyDate(new Date());
        return job;
    }
}
