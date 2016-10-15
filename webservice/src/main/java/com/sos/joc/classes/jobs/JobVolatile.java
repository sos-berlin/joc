package com.sos.joc.classes.jobs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.job.JobState;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.LockUseV;
import com.sos.joc.model.job.OrderInRunningTask;
import com.sos.joc.model.job.QueuedTask;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskCause;
import com.sos.joc.model.order.OrdersSummary;


public class JobVolatile extends JobV {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobVolatile.class);
    private final Element job;
    private final Boolean orderJob;
    private final Boolean withOrderQueue;
    private final JOCXmlCommand jocXmlCommand;

    public JobVolatile(Element job, JOCXmlCommand jocXmlCommand, Boolean withOrderQueue) {
        this.job = job;
        this.jocXmlCommand = jocXmlCommand;
        this.orderJob = "yes".equals(getAttributeValue("order", "no"));
        this.withOrderQueue = withOrderQueue;
    }
    
    public JobVolatile(Element job, JOCXmlCommand jocXmlCommand) {
        this.job = job;
        this.jocXmlCommand = jocXmlCommand;
        this.orderJob = "yes".equals(getAttributeValue("order", "no"));
        this.withOrderQueue = false;
    }
    
    public boolean isOrderJob() {
        return orderJob;
    }
    
    public void setPath() {
        if (getPath() == null) {
            setPath(job.getAttribute(WebserviceConstants.PATH));
            LOGGER.info("...processing job: " + getPath());
        }
    }

    public void setState() throws Exception {
        if (getState() == null) {
            setState(new JobState());
            if (!jocXmlCommand.getBoolValue(job.getAttribute("enabled"), true)) {
                getState().set_text(JobStateText.DISABLED);
            } else {
                try {
                    getState().set_text(JobStateText.fromValue(job.getAttribute(WebserviceConstants.STATE).toUpperCase()));
                } catch (Exception e) {
                }
                if (jocXmlCommand.getBoolValue(job.getAttribute(WebserviceConstants.WAITING_FOR_AGENT), false)) {
                    getState().set_text(JobStateText.WAITING_FOR_AGENT);
                } else if (jocXmlCommand.getBoolValue(job.getAttribute(WebserviceConstants.WAITING_FOR_PROCESS), false)) {
                    getState().set_text(JobStateText.WAITING_FOR_PROCESS);
                } else if (jocXmlCommand.getSosxml().selectNodeList(job, "lock.requestor/lock.use[@is_available='no']").getLength() > 0) {
                    getState().set_text(JobStateText.WAITING_FOR_LOCK);
                } else if (getNumOfRunningTasks() == Integer.valueOf(getAttributeValue("tasks", "1")) && getNumOfQueuedTasks() > 0) {
                    // TODO: WaitingForTask has to be improved
                    // Look into queue items where start_time in the past
                    // it could be that a task is queued caused of a delayed
                    // start instead of max tasks is reached
                    getState().set_text(JobStateText.WAITING_FOR_TASK);
                } else if (isOrderJob() && getState().get_text() == JobStateText.PENDING
                        && !jocXmlCommand.getBoolValue(job.getAttribute(WebserviceConstants.IN_PERIOD), true)) {
                    getState().set_text(JobStateText.NOT_IN_PERIOD);
                }
            }
            setSeverity(getState());
        }
    }
    
    public void setFields(boolean compact) throws Exception {
        if (compact) {
            setCompactFields();
            cleanArrays();
        } else {
            setDetailedFields();
        }
    }

    private void cleanArrays() {
        setParams(null);
        setTaskQueue(null);
        setRunningTasks(null);
        setOrderQueue(null);
    }

    private void setDetailedFields() throws Exception {
        setCompactFields();
        setAllSteps(Integer.valueOf(jocXmlCommand.getAttributeValue(job, "all_steps", "0")));
        setParams(Parameters.getParameters(job));
        setTaskQueue();
        setRunningTasks();
        setTemporary(WebserviceConstants.YES.equals(job.getAttribute("temporary")) ? true : null);
        setNextStartTime(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(job, WebserviceConstants.NEXT_START_TIME, null)));
        setDelayUntil(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(job, "delay_after_error", null)));
    }

    private void setCompactFields() throws Exception {
        setPath();
        setState();
        setSurveyDate(jocXmlCommand.getSurveyDate());
        setName(job.getAttribute(WebserviceConstants.NAME));
        setNumOfQueuedTasks(Integer.valueOf(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "queued_tasks/@length", "0")));
        setLocks(getLocks(jocXmlCommand.getSosxml().selectNodeList(job, "lock.requestor/lock.use")));
        setStateText(job.getAttribute("state_text"));
        setNumOfRunningTasks(Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "tasks/@count", "0")));
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(job));
        setSummary();
        setOrderQueue();
    }
    
    private String getAttributeValue(String attributeName, String default_) {
        String val = job.getAttribute(attributeName);
        if (val == null || val.isEmpty()) {
            val = default_;
        }
        return val;
    }
    
    private List<LockUseV> getLocks(NodeList lockList) {
        if (lockList != null && lockList.getLength() > 0) {
            List<LockUseV> listOfLocks = new ArrayList<LockUseV>();
            for (int j = 0; j < lockList.getLength(); j++) {
                Element lockElement = (Element) lockList.item(j);
                LockUseV lock = new LockUseV();
                lock.setExclusive(jocXmlCommand.getBoolValue(lockElement.getAttribute(WebserviceConstants.EXCLUSIVE),false));
                lock.setAvailable(jocXmlCommand.getBoolValue(lockElement.getAttribute(WebserviceConstants.IS_AVAILABLE),null));
                lock.setPath(lockElement.getAttribute(WebserviceConstants.LOCK));
                listOfLocks.add(lock);
            }
            return listOfLocks;
        }
        return null;
    }
    
    private void setSeverity(JobState state) {
        switch (state.get_text()) {
        case RUNNING:
            state.setSeverity(0);
            break;
        case PENDING:
            state.setSeverity(1);
            break;
        case NOT_INITIALIZED:
        case WAITING_FOR_AGENT:
        case STOPPING:
        case STOPPED:
            state.setSeverity(2);
            break;
        case INITIALIZED:
        case LOADED:
        case WAITING_FOR_PROCESS:
        case WAITING_FOR_LOCK:
        case WAITING_FOR_TASK:
        case NOT_IN_PERIOD:
            state.setSeverity(3);
            break;
        case DISABLED:
            state.setSeverity(4);
            break;
        }
    }
    
    private void setTaskQueue() throws Exception {
        NodeList queuedTasksList = jocXmlCommand.getSosxml().selectNodeList(job, "queued_tasks/queued_task");
        if (queuedTasksList != null && queuedTasksList.getLength() > 0) {
            List<QueuedTask> queuedTasks = new ArrayList<QueuedTask>();
            for (int queuedTasksCount = 0; queuedTasksCount < queuedTasksList.getLength(); queuedTasksCount++) {
                QueuedTask taskQueue = new QueuedTask();
                Element taskQueueElement = (Element) queuedTasksList.item(queuedTasksCount);
                taskQueue.setTaskId(taskQueueElement.getAttribute(WebserviceConstants.ID));
                taskQueue.setEnqueued(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskQueueElement, WebserviceConstants.ENQUEUED, null)));
            }
            setTaskQueue(queuedTasks);
        } else {
            setTaskQueue(null);
        }
    }
    
    private void setRunningTasks() throws Exception {
        NodeList runningTaskList = jocXmlCommand.getSosxml().selectNodeList(job, "tasks/task");
        if (runningTaskList != null && runningTaskList.getLength() > 0) {
            List<RunningTask> runningTasks = new ArrayList<RunningTask>();
            for (int runningTasksCount = 0; runningTasksCount < runningTaskList.getLength(); runningTasksCount++) {
                RunningTask task = new RunningTask();
                Element taskElement = (Element) runningTaskList.item(runningTasksCount);
                try {
                    task.set_cause(TaskCause.fromValue(taskElement.getAttribute(WebserviceConstants.CAUSE).toUpperCase()));
                } catch (Exception e) {
                }
                task.setEnqueued(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElement, WebserviceConstants.ENQUEUED, null)));
                task.setIdleSince(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElement, WebserviceConstants.IDLE_SINCE, null)));
                task.setPid(Integer.parseInt(jocXmlCommand.getAttributeValue(taskElement,WebserviceConstants.PID,"0")));
                task.setStartedAt(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(taskElement, WebserviceConstants.START_AT, null)));
                task.setSteps(Integer.parseInt(jocXmlCommand.getAttributeValue(taskElement,WebserviceConstants.STEPS,"1")));
                task.setTaskId(jocXmlCommand.getAttributeValue(taskElement,WebserviceConstants.ID,"0"));
                Element orderElement = (Element) jocXmlCommand.getSosxml().selectSingleNode(taskElement, WebserviceConstants.ORDER);
                if (orderElement != null) {
                    OrderInRunningTask order = new OrderInRunningTask();
                    order.setInProcessSince(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(orderElement, WebserviceConstants.IN_PROCESS_SINCE, null)));
                    order.setJobChain(("/"+orderElement.getAttribute(WebserviceConstants.JOB_CHAIN)).replaceAll("//+", "/"));
                    order.setOrderId(orderElement.getAttribute(WebserviceConstants.ID));
                    order.setPath(("/"+orderElement.getAttribute(WebserviceConstants.PATH)).replaceAll("//+", "/"));
                    order.setState(orderElement.getAttribute(WebserviceConstants.STATE));
                    task.setOrder(order);
                }
                runningTasks.add(task);
            }
            setRunningTasks(runningTasks);
        } else {
            setRunningTasks((List<RunningTask>) null);
        }
    }
    
    private void setOrderQueue() throws Exception {
        if (isOrderJob() && withOrderQueue) {
            setOrderQueue(new OrdersVCallable(getPath(), false, jocXmlCommand.getUriForJsonCommand()).getOrdersOfJob());
        } else {
            setOrderQueue(null);
        }
    }
    
    private void setSummary() throws Exception {
        if (isOrderJob()) {
            NodeList orders = jocXmlCommand.getSosxml().selectNodeList(job, "order_queue/order");
            OrdersSummary ordersSummary = new OrdersSummary();
            int setback = 0;
            int suspended = 0;
            int running = 0;
            int pending = 0;
            int waiting = 0;

            //TODO maybe use JsonApi
            for (int i = 0; i < orders.getLength(); i++) {
                Element order = (Element) orders.item(i);
                if (order.hasAttribute("setback")) {
                    setback += 1;
                } else if (order.hasAttribute("task")) {
                    running += 1;
                } else if (order.hasAttribute("suspended")) {
                    suspended += 1;
                } else if (!order.hasAttribute("touched")) {
                    // that's not exact, orders are untouched too, if they
                    // waitingForResource at the first node
                    pending += 1;
                } else {
                    waiting += 1;
                }
            }
            ordersSummary.setPending(pending);
            ordersSummary.setSetback(setback);
            ordersSummary.setRunning(running);
            ordersSummary.setSuspended(suspended);
            ordersSummary.setWaitingForResource(waiting);
            setOrdersSummary(ordersSummary);
        } 
    }
}
