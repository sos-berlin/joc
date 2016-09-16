package com.sos.joc.classes.job;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.OrdersSummary;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.State_;
import com.sos.joc.model.job.TaskQueue;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.job.State_.Text;


public class JobV extends Job_ {
    
//    private static final Logger LOGGER = LoggerFactory.getLogger(JobV.class);
    private final Element job;
    private final Boolean orderJob;
    private final Boolean withOrderQueue;
    private final JOCXmlCommand jocXmlCommand;

    public JobV(Element job, JOCXmlCommand jocXmlCommand, Boolean withOrderQueue) {
        this.job = job;
        this.jocXmlCommand = jocXmlCommand;
        this.orderJob = "yes".equals(getAttributeValue("order", "no"));
        this.withOrderQueue = withOrderQueue;
    }
    
    public boolean isOrderJob() {
        return orderJob;
    }

    public void setFields(boolean compact) throws Exception {
        if (compact) {
            setCompactFields();
        } else {
            setDetailedFields();
        }
    }

    private void setDetailedFields() throws Exception {
        setCompactFields();
        setAllSteps(Integer.valueOf(jocXmlCommand.getAttributeValue(job, "all_steps", "0")));
        setParams(Parameters.getParameters(job));
        setTaskQueue();
        setRunningTasks();
        setTemporary(WebserviceConstants.YES.equals(job.getAttribute("temporary")));
        Date startTime = JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(job, WebserviceConstants.NEXT_START_TIME, null));
        if (isOrderJob()) {
            if (getState().getText() == Text.NOT_IN_PERIOD) {
              //TODO setNextPeriodBegin(startTime);//TODO Is it the right time?
            }
        } else {
            setDelayUntil(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(job, "delay_after_error", null)));
            setNextStartTime(startTime);
        }
    }

    private void setCompactFields() throws Exception {
        setSurveyDate(jocXmlCommand.getSurveyDate());
        setName(job.getAttribute(WebserviceConstants.NAME));
        setPath(job.getAttribute(WebserviceConstants.PATH));
        setNumOfQueuedTasks(Integer.valueOf(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "queued_tasks/@length", "0")));
        setLocks(getLocks(jocXmlCommand.getSosxml().selectNodeList(job, "lock.requestor/lock.use")));
        setStateText(job.getAttribute("state_text"));
        setNumOfRunningTasks(Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "tasks/@count", "0")));
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(job));
        setState();
        setOrderQueueAndSummary();
    }
    
    private String getAttributeValue(String attributeName, String default_) {
        String val = job.getAttribute(attributeName);
        if (val == null || val.isEmpty()) {
            val = default_;
        }
        return val;
    }
    
    private List<Lock_> getLocks(NodeList lockList) {
        if (lockList != null && lockList.getLength() > 0) {
            List<Lock_> listOfLocks = new ArrayList<Lock_>();
            for (int j = 0; j < lockList.getLength(); j++) {
                Element lockElement = (Element) lockList.item(j);
                Lock_ lock = new Lock_();
                lock.setExclusive(JobsUtils.getBoolValue(lockElement.getAttribute(WebserviceConstants.EXCLUSIVE),false));
                lock.setAvailable(JobsUtils.getBoolValue(lockElement.getAttribute(WebserviceConstants.IS_AVAILABLE),true));
                lock.setPath(lockElement.getAttribute(WebserviceConstants.LOCK));
                listOfLocks.add(lock);
            }
            return listOfLocks;
        }
        return null;
    }
    
    private void setState() throws Exception {
        setState(new State_());
        if (!JobsUtils.getBoolValue(job.getAttribute("enabled"), true)) {
            getState().setText(State_.Text.DISABLED); 
        } else {
            try {
                getState().setText(State_.Text.fromValue(job.getAttribute(WebserviceConstants.STATE).toUpperCase()));
            } catch (Exception e) {
            }
            if (JobsUtils.getBoolValue(job.getAttribute(WebserviceConstants.WAITING_FOR_AGENT), false)) {
                getState().setText(State_.Text.WAITING_FOR_AGENT);
            } else if (JobsUtils.getBoolValue(job.getAttribute(WebserviceConstants.WAITING_FOR_PROCESS), false)) {
                getState().setText(State_.Text.WAITING_FOR_PROCESS);
            } else if (jocXmlCommand.getSosxml().selectNodeList(job, "lock.requestor/lock.use[@is_available='no']").getLength() > 0) {
                getState().setText(State_.Text.WAITING_FOR_LOCK);
            } else if (getNumOfRunningTasks() == Integer.valueOf(getAttributeValue("tasks", "1")) && getNumOfQueuedTasks() > 0) {
                // TODO: WaitingForTask has to improved
                // Look into queue items where start_time in the past
                // it could be that a task is queued caused of a delayed start instead of max tasks is reached 
                getState().setText(State_.Text.WAITING_FOR_TASK);
            } else if (isOrderJob() && getState().getText() == State_.Text.PENDING
                    && !JobsUtils.getBoolValue(job.getAttribute(WebserviceConstants.IN_PERIOD), true)) {
                getState().setText(State_.Text.NOT_IN_PERIOD);
            }
        }
        setSeverity(getState());
    }
    
    private void setSeverity(State_ state) {
        switch (state.getText()) {
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
            List<TaskQueue> queuedTasks = new ArrayList<TaskQueue>();
            for (int queuedTasksCount = 0; queuedTasksCount < queuedTasksList.getLength(); queuedTasksCount++) {
                TaskQueue taskQueue = new TaskQueue();
                Element taskQueueElement = (Element) queuedTasksList.item(queuedTasksCount);
                taskQueue.setTaskId(Integer.parseInt(taskQueueElement.getAttribute(WebserviceConstants.ID)));
                taskQueue.setEnqueued(JobSchedulerDate.getDate(taskQueueElement.getAttribute(WebserviceConstants.ENQUEUED)));
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
                    task.setCause(RunningTask.Cause.fromValue(taskElement.getAttribute(WebserviceConstants.CAUSE).toUpperCase()));
                } catch (Exception e) {
                }
                task.setEnqueued(JobSchedulerDate.getDate(taskElement.getAttribute(WebserviceConstants.ENQUEUED)));
                task.setIdleSince(JobSchedulerDate.getDate(taskElement.getAttribute(WebserviceConstants.IDLE_SINCE)));
                task.setPid(Integer.parseInt(jocXmlCommand.getAttributeValue(taskElement,WebserviceConstants.PID,"0")));
                task.setStartedAt(JobSchedulerDate.getDate(taskElement.getAttribute(WebserviceConstants.START_AT)));
                task.setSteps(Integer.parseInt(jocXmlCommand.getAttributeValue(taskElement,WebserviceConstants.STEPS,"1")));
                task.setTaskId(Integer.parseInt(jocXmlCommand.getAttributeValue(taskElement,WebserviceConstants.ID,"0")));
                Element orderElement = (Element) jocXmlCommand.getSosxml().selectSingleNode(taskElement, WebserviceConstants.ORDER);
                if (orderElement != null) {
                    Order order = new Order();
                    order.setInProcessSince(JobSchedulerDate.getDate(orderElement.getAttribute(WebserviceConstants.IN_PROCESS_SINCE)));
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
            setRunningTasks(null);
        }
    }
    
    private void setOrderQueueAndSummary() throws Exception {
        NodeList orders = jocXmlCommand.getSosxml().selectNodeList(job, "order_queue/order");
        OrdersSummary ordersSummary = new OrdersSummary();
        int setback = 0;
        int suspended = 0;
        int running = 0;
        int pending = 0;
        int waiting = 0;
        
        String masterUrl = "http://localhost:40410";
        JOCJsonCommand command = new JOCJsonCommand(masterUrl);
        command.addCompactQuery(false);
        URI uri = command.getURI();
        List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();
        Map<String, OrderQueue> listOrderQueue = new HashMap<String, OrderQueue>();
        for (int i=0; i < orders.getLength(); i++) {
            Element order = (Element) orders.item(i);
            if (withOrderQueue) {
                Order_ o = new Order_();
                o.setJobChain(order.getAttribute("job_chain"));
                o.setOrderId(order.getAttribute("order"));
                tasks.add(new OrdersVCallable(o, false, uri));
            }
            if (order.hasAttribute("setback")) {
                setback += 1;  
            } else if (order.hasAttribute("task")) {
                running += 1;
            } else if (order.hasAttribute("suspended")) {
                suspended += 1;
            } else if (!order.hasAttribute("touched")) {
                //that's not exact, orders are untouched too, if they waiting at the first node
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
        if (withOrderQueue) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<Map<String, OrderQueue>> result : executorService.invokeAll(tasks)) {
                listOrderQueue.putAll(result.get());
            }
            setOrderQueue(new ArrayList<OrderQueue>(listOrderQueue.values()));
        } else {
            setOrderQueue((List<OrderQueue>) null);
        }
    }
}
