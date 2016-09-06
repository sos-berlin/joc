package com.sos.joc.classes.jobs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sos.xml.SOSXMLXPath;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.State___;
import com.sos.joc.model.job.TaskQueue;


public class JobsUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT);
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat(WebserviceConstants.JOBSCHEDULER_DATE_FORMAT2);

    public static Boolean getBoolValue(final String value) {
        if(WebserviceConstants.YES.equalsIgnoreCase(value)){
            return true;
        } else if(WebserviceConstants.NO.equalsIgnoreCase(value)){
            return false;
        }
        return null;
    }
    
    public static Date getDateFromString(final String dateString) throws Exception{
        if (dateString != null) {
            Date date = null;
            if (!dateString.contains("T")) {
                date = SDF.parse(dateString);
            } else {
                date = SDF2.parse(dateString);
            }
            return date;
        } else {
            return null;
        }
    }
    
    public static String createPostCommand(final JobsFilterSchema body) {
        StringBuilder postCommand = new StringBuilder();
        boolean compact = body.getCompact();
        postCommand.append("<commands>");
        if (!body.getFolders().isEmpty()) {
            for (FoldersSchema folder : body.getFolders()) {
                postCommand.append("<show_state subsystems=\"job folder\" what=\"job_orders task_queue");
                postCommand.append(" folders");
                if(!compact){
                    postCommand.append(" job_params");
                }
                String path = folder.getFolder();
                Boolean recursive = folder.getRecursive();
                if(!recursive) {
                    postCommand.append(" no_subfolders");
                }
                postCommand.append("\"");
                postCommand.append(" path=\"").append(path).append("\"/>");
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
                if (lockElement.getAttribute(WebserviceConstants.EXCLUSIVE) != null) {
                    lock.setExclusive(JobsUtils.getBoolValue(lockElement.getAttribute(WebserviceConstants.EXCLUSIVE)));
                }
                if (lockElement.getAttribute(WebserviceConstants.IS_AVAILABLE) != null) {
                    lock.setAvailable(JobsUtils.getBoolValue(lockElement.getAttribute(WebserviceConstants.IS_AVAILABLE)));
                }
                if (lockElement.getAttribute(WebserviceConstants.LOCK) != null) {
                    lock.setPath(lockElement.getAttribute(WebserviceConstants.LOCK));
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
                taskQueue.setTaskId(Integer.parseInt(taskQueueElement.getAttribute(WebserviceConstants.ID)));
                taskQueue.setEnqueued(JobsUtils.getDateFromString(taskQueueElement.getAttribute(WebserviceConstants.ENQUEUED)));
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
                param.setName(paramElement.getAttribute(WebserviceConstants.NAME));
                param.setValue(paramElement.getAttribute(WebserviceConstants.VALUE));
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
                task.setCause(RunningTask.Cause.valueOf(taskElement.getAttribute(WebserviceConstants.CAUSE)));
                task.setEnqueued(getDateFromString(taskElement.getAttribute(WebserviceConstants.ENQUEUED)));
                task.setIdleSince(getDateFromString(taskElement.getAttribute(WebserviceConstants.IDLE_SINCE)));
                if (taskElement.getAttribute(WebserviceConstants.PID) != null && !taskElement.getAttribute(WebserviceConstants.PID).isEmpty()) {
                    task.setPid(Integer.parseInt(taskElement.getAttribute(WebserviceConstants.PID)));
                }
                task.setStartedAt(getDateFromString(taskElement.getAttribute(WebserviceConstants.START_AT)));
                if (taskElement.getAttribute(WebserviceConstants.STEPS) != null && !taskElement.getAttribute(WebserviceConstants.STEPS).isEmpty()) {
                    task.setSteps(Integer.parseInt(taskElement.getAttribute(WebserviceConstants.STEPS)));
                }
                if (taskElement.getAttribute(WebserviceConstants.ID) != null && !taskElement.getAttribute(WebserviceConstants.ID).isEmpty()) {
                    task.setTaskId(Integer.parseInt(taskElement.getAttribute(WebserviceConstants.ID)));
                }
                Element orderElement = (Element) jocXmlCommand.getSosxml().selectSingleNode(taskElement, WebserviceConstants.ORDER);
                if (orderElement != null) {
                    Order order = new Order();
                    order.setInProcessSince(getDateFromString(orderElement.getAttribute(WebserviceConstants.IN_PROCESS_SINCE)));
                    order.setJobChain(orderElement.getAttribute(WebserviceConstants.JOB_CHAIN));
                    order.setOrderId(orderElement.getAttribute(WebserviceConstants.ID));
                    order.setPath(orderElement.getAttribute(WebserviceConstants.PATH));
                    order.setState(orderElement.getAttribute(WebserviceConstants.STATE));
                    task.setOrder(order);
                }
                runningTasks.add(task);
            }
            return runningTasks;
        } else {
            return null;
        }
    }
    
    public static boolean filterJob (JobsFilterSchema filter, Element node, SOSXMLXPath sosXml) throws Exception{
        boolean isAvailable = false;
        // no property to compare dateFrom and dateTo to
        // Klären was für ein Datum relevant ist für den Vergleich
        Date dateFrom = getDateFromString(filter.getDateFrom());
        Date dateTo = getDateFromString(filter.getDateTo());
        // ??? What to do with regex 
        String regex = filter.getRegex();
        // ??? What to do with timezone
        String timezone = filter.getTimeZone();
        if(dateFrom == null && dateTo == null && regex == null && timezone == null) {
            return true;
        }
//        Date runningSince = getDateFromString(sosXml.selectSingleNodeValue(node, "tasks/task/@running_since"));
//        Date startTime = getDateFromString(sosXml.selectSingleNodeValue(node, "tasks/task/order/@start_time"));
//        Date nextStartTime = getDateFromString(node.getAttribute(NEXT_START_TIME));
        if(node.getAttribute(WebserviceConstants.STATE) != null && !node.getAttribute(WebserviceConstants.STATE).isEmpty() 
                && stateAvailable(node.getAttribute(WebserviceConstants.STATE), filter.getState())){
            isAvailable = true;
        }
//        if (dateFrom != null && runningSince != null && dateFrom.compareTo(runningSince) <= 0) {
//            isAvailable = true;
//        } else {
//            isAvailable = false;
//        }
//        if (dateTo != null && runningSince != null && runningSince.compareTo(dateTo) <= 0) {
//            isAvailable = true;
//        } else {
//            isAvailable = false;
//        }
        return isAvailable;
    }

    private static boolean stateAvailable(String stateText, List<State___> states) {
        for(State___ state : states) {
            if(state.toString().equalsIgnoreCase(stateText)){
                return true;
            }
        }
        return false;
    }
}