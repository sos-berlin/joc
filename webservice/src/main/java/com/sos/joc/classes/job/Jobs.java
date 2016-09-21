package com.sos.joc.classes.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.job.Job;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.Lock;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskQueue;
import com.sos.joc.model.job.RunningTask.Cause;

public class Jobs {

    public static List<Lock_> getLocks() {
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
        return listOfLocks;
    }

    public static List<Lock> getJobLocks() {
        List<Lock> listOfLocks = new ArrayList<Lock>();
        Lock lock = new Lock();
        lock.setExclusive(false);
        // lock.setAvailable(true);
        lock.setPath("myPath");
        listOfLocks.add(lock);
        Lock lock2 = new Lock();
        lock2.setExclusive(true);
        // lock2.setAvailable(false);
        lock2.setPath("myPath2");
        listOfLocks.add(lock2);
        return listOfLocks;
    }

    public static List<RunningTask> getRunningTasks() {
        List<RunningTask> listOfRunningTask = new ArrayList<RunningTask>();
        RunningTask runningTask = new RunningTask();
        runningTask.setCause(Cause.NONE);
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
        return listOfRunningTask;

    }

    public static List<TaskQueue> getTaskQueue() {
        List<TaskQueue> listOfTasks = new ArrayList<TaskQueue>();
        TaskQueue taskQueue = new TaskQueue();
        taskQueue.setTaskId(-1);
        listOfTasks.add(taskQueue);
        return listOfTasks;
    }

    public static Job_ getJob(boolean compact) {
        Job_ job = new Job_();

        job.setName("myName");
        job.setPath("myPath");

        com.sos.joc.model.job.State_ state = new com.sos.joc.model.job.State_();
        state.setSeverity(0);
        state.setText(com.sos.joc.model.job.State_.Text.LOADED);
        job.setState(state);
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
    
    public static Job getPJob(boolean compact) {
        Job job = new Job();

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
