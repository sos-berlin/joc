package com.sos.joc.classes.jobs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskQueue;


public class JobsUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT);
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT2);

    public static Boolean getBoolValue(final String value) {
        if("yes".equalsIgnoreCase(value)){
            return true;
        } else if("no".equalsIgnoreCase(value)){
            return false;
        }
        return null;
    }
    
    public static Date getDateFromString(final String dateString) throws Exception{
        Date date = null;
        if (!dateString.contains("T")) {
            date = SDF.parse(dateString);
        } else {
            date = SDF2.parse(dateString);
        }
        return date;
    }
    
    public static String createPostCommand(final JobsFilterSchema body) {
        StringBuilder postCommand = new StringBuilder();
        boolean compact = body.getCompact();
        postCommand.append("<commands>");
        if (!body.getFolders().isEmpty()) {
            for (FoldersSchema folder : body.getFolders()) {
                postCommand.append("<show_state subsystems=\"job folder\" what=\"job_orders task_queue");
                if(!compact){
                    postCommand.append(" job_params");
                }
                String path = folder.getFolder();
                Boolean recursive = folder.getRecursive();
                if(!recursive) {
                    postCommand.append(" no_subfolders");
                }
                postCommand.append("\" ");
                postCommand.append("path=\"").append(path).append("\"/>");
            }
        } else {
            postCommand.append("<show_state subsystems=\"job\" what=\"job_orders task_queue");
            if(!compact){
                postCommand.append(" job_params");
            }
            postCommand.append("\" path=\"/\"/>");
        }
        postCommand.append("</commands>");
        return postCommand.toString();
    }

    public static Integer getSeverityFromStateText(String stateText) {
        switch(stateText.toUpperCase()) {
        case "RUNNING":
            return 0;
        case "PENDING":
            return 1;
        case "NOT_INITIALIZED":
        case "WAITING_FOR_AGENT":
        case "STOPPING":
        case "STOPPED":
        case "REMOVED":
            return 2;
        case "INITIALIZED": 
        case "LOADED": 
        case "WAITING_FOR_PROCESS": 
        case "WAITING_FOR_LOCK": 
        case "WAITING_FOR_TASK": 
        case "NOT_IN_PERIOD": 
            return 3;
        case "DISABLED": 
            return 4;
        }
        return null;
    }
    
    public static List<Lock_> initLocks(NodeList lockList) {
        if (lockList != null && lockList.getLength() > 0) {
            List<Lock_> listOfLocks = new ArrayList<Lock_>();
            for (int j = 0; j < lockList.getLength(); j ++) {
                Node lockNode = lockList.item(j);
                Lock_ lock = new Lock_(); 
                Element lockElement = (Element) lockNode;
                if (lockElement.getAttribute("exclusive") != null) {
                    lock.setExclusive(JobsUtils.getBoolValue(lockElement.getAttribute("exclusive")));
                }
                if (lockElement.getAttribute("is_available") != null) {
                    lock.setAvailable(JobsUtils.getBoolValue(lockElement.getAttribute("is_available")));
                }
                if (lockElement.getAttribute("lock") != null) {
                    lock.setPath(lockElement.getAttribute("lock"));
                }
                listOfLocks.add(lock);
            }
            return listOfLocks;
        }
        return null;
    }
    
    public static List<TaskQueue> initQueuedTasks(NodeList queuedTasksList) throws Exception {
        List<TaskQueue> queuedTasks = new ArrayList<TaskQueue>();
        if (queuedTasksList != null && queuedTasksList.getLength() > 0) {
            for(int queuedTasksCount = 0; queuedTasksCount < queuedTasksList.getLength(); queuedTasksCount++) {
                TaskQueue taskQueue = new TaskQueue();
                Element taskQueueElement = (Element)queuedTasksList.item(queuedTasksCount);
                taskQueue.setTaskId(Integer.parseInt(taskQueueElement.getAttribute("id")));
                taskQueue.setEnqueued(JobsUtils.getDateFromString(taskQueueElement.getAttribute("enqueued")));
            }
            return queuedTasks;
        } else {
            return null;
        }
    }
    
    public static List<NameValuePairsSchema> initParameters(NodeList paramList) {
        List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
        if (paramList != null && paramList.getLength() > 0) {
            for(int paramsCount = 0; paramsCount < paramList.getLength(); paramsCount++) {
                NameValuePairsSchema param = new NameValuePairsSchema();
                Element paramElement = (Element)paramList.item(paramsCount);
                param.setName(paramElement.getAttribute("name"));
                param.setValue(paramElement.getAttribute("value"));
                params.add(param);
            }
            return params;
        } else {
            return null;
        }
    }
    
    public static List<RunningTask> initRunningTasks(NodeList runningTaskList, JOCXmlCommand jocXmlCommand) throws Exception {
        List<RunningTask> runningTasks = new ArrayList<RunningTask>();
        if (runningTaskList != null && runningTaskList.getLength() > 0) {
            for (int runningTasksCount = 0; runningTasksCount < runningTaskList.getLength(); runningTasksCount++) {
                RunningTask task = new RunningTask();
                Element taskElement = (Element) runningTaskList.item(runningTasksCount);
                task.setCause(RunningTask.Cause.valueOf(taskElement.getAttribute("cause")));
                task.setEnqueued(getDateFromString(taskElement.getAttribute("enqueued")));
                task.setIdleSince(getDateFromString(taskElement.getAttribute("idle_since")));
                if (taskElement.getAttribute("pid") != null && !taskElement.getAttribute("pid").isEmpty()) {
                    task.setPid(Integer.parseInt(taskElement.getAttribute("pid")));
                }
                task.setStartedAt(getDateFromString(taskElement.getAttribute("start_at")));
                if (taskElement.getAttribute("steps") != null && !taskElement.getAttribute("steps").isEmpty()) {
                    task.setSteps(Integer.parseInt(taskElement.getAttribute("steps")));
                }
                if (taskElement.getAttribute("id") != null && !taskElement.getAttribute("id").isEmpty()) {
                    task.setTaskId(Integer.parseInt(taskElement.getAttribute("id")));
                }
                Element orderElement = (Element) jocXmlCommand.getSosxml().selectSingleNode(taskElement, "order");
                if (orderElement != null) {
                    Order order = new Order();
                    order.setInProcessSince(getDateFromString(orderElement.getAttribute("in_process_since")));
                    order.setJobChain(orderElement.getAttribute("job_chain"));
                    order.setOrderId(orderElement.getAttribute("id"));
                    order.setPath(orderElement.getAttribute("path"));
                    order.setState(orderElement.getAttribute("state"));
                    task.setOrder(order);
                }
                runningTasks.add(task);
            }
            return runningTasks;
        } else {
            return null;
        }
    }
    
    public static boolean filterJob (JobsFilterSchema filter, Node node) {
        boolean isAvailable = false;
//      filter.getDateFrom(); TODO
//      filter.getDateTo(); TODO
//      filter.getRegex(); TODO
//      filter.getTimeZone(); TODO
//      filter.getState(); TODO
        return isAvailable;
    }

}