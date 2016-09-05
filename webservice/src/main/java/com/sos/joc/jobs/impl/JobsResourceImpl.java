package com.sos.joc.jobs.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.ConfigurationStatusSchema.Text;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.JobsVSchema;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.OrderQueue.Type;
import com.sos.joc.model.job.OrdersSummary;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.RunningTask.Cause;
import com.sos.joc.model.job.State_;
import com.sos.joc.model.job.TaskQueue;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImpl.class);
    
    private String createPostCommand(JobsFilterSchema jobsFilterSchema) {
        StringBuilder postCommand = new StringBuilder();
        postCommand.append("<commands>");
        if (!jobsFilterSchema.getFolders().isEmpty()) {
            for (FoldersSchema folder : jobsFilterSchema.getFolders()) {
                postCommand.append("<show_state subsystems=\"job folder\" what=\"job_orders task_queue");
                String path = folder.getFolder();
                Boolean recursive = folder.getRecursive();
                if(!recursive) {
                    postCommand.append(" no_subfolders");
                }
                postCommand.append("\" ");
                postCommand.append("path=\"").append(path).append("\"/>");
            }
        } else {
            postCommand.append("<show_state subsystems=\"job\" what=\"job_orders task_queue\" path=\"/\"/>");
        }
        postCommand.append("</commands>");
        return postCommand.toString();
    }

    private Boolean getBoolValue(String value) {
        if("yes".equalsIgnoreCase(value)){
            return true;
        } else if("no".equalsIgnoreCase(value)){
            return false;
        } else {
            return null;
        }
    }
    
    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsFilterSchema jobsFilterSchema) throws Exception {
        LOGGER.debug("init Jobs");
        JOCDefaultResponse jocDefaultResponse = init(jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        jobsFilterSchema.getCompact();
        jobsFilterSchema.getDateFrom();
        jobsFilterSchema.getDateTo();
        /* jobsBody.getFolders(); ERLEDIGT*/ 
        jobsFilterSchema.getIsOrderJob();
        jobsFilterSchema.getJobs();
        jobsFilterSchema.getRegex();
        jobsFilterSchema.getTimeZone();
        jobsFilterSchema.getState();
        try {
 
            JobsVSchema entity = new JobsVSchema();
            List<Job_> listJobs = new ArrayList<Job_>();
            // TODO Use correct url
            //FOLDERS ERLEDIGT
            //COMPACT
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String postCommand = createPostCommand(jobsFilterSchema);
            jocXmlCommand.excutePost(postCommand);
            entity.setDeliveryDate(new Date());
            Date surveyDate = jocXmlCommand.getSurveyDate();
            
            SimpleDateFormat sdf = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT);
            SimpleDateFormat sdf2 = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT2);

            
            if(jobsFilterSchema.getIsOrderJob() == null) {
                // all jobs
                jocXmlCommand.createNodeList("//jobs/job");
            } else if(jobsFilterSchema.getIsOrderJob()) {
                // only order jobs
                jocXmlCommand.createNodeList("//jobs/job[@order='yes']");
            } else {
                // only standalone jobs
                jocXmlCommand.createNodeList("//jobs/job[not(@order) or @order='no']");
            }
            for(int i = 0; i < jocXmlCommand.getNodeList().getLength(); i++) {
                Node jobNode = jocXmlCommand.getNodeList().item(i);
                Job_ job = new Job_();
                if (surveyDate != null) {
                    job.setSurveyDate(surveyDate);
                }
                if (jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "queued_tasks[@length]") != null) {
                    job.setNumOfQueuedTasks(Integer.valueOf(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode,
                            "queued_tasks/@length")));
                }
                NodeList lockNodes = jocXmlCommand.getSosxml().selectNodeList(jobNode, "lock.requestor/lock.use");
                if (lockNodes != null && lockNodes.getLength() > 0) {
                    List<Lock_> listOfLocks = new ArrayList<Lock_>();
                    for (int j = 0; j < lockNodes.getLength(); j ++) {
                        Node lockNode = lockNodes.item(j);
                        Lock_ lock = new Lock_(); 
                        Element lockElement = (Element) lockNode;
                        if (lockElement.getAttribute("exclusive") != null) {
                            lock.setExclusive(getBoolValue(lockElement.getAttribute("exclusive")));
                        }
                        if (lockElement.getAttribute("is_available") != null) {
                            lock.setAvailable(getBoolValue(lockElement.getAttribute("is_available")));
                        }
                        if (lockElement.getAttribute("lock") != null) {
                            lock.setPath(lockElement.getAttribute("lock"));
                        }
                        listOfLocks.add(lock);
                    }
                    job.setLocks(listOfLocks);
                }
                NamedNodeMap attributes = jobNode.getAttributes();
                job.setName(attributes.getNamedItem("name").getNodeValue());
                job.setPath(attributes.getNamedItem("path").getNodeValue());
                String stateText = attributes.getNamedItem("state").getNodeValue();
                State_ state = new State_();
                switch(stateText) {
                    case "running":
                        state.setSeverity(0);
                        break;
                    case "pending":
                        state.setSeverity(1);
                        break;
                    case "not_initialized":
                    case "waiting_for_agent":
                    case "stopping":
                    case "stopped":
                    case "removed":
                        state.setSeverity(2);
                        break;
                    case "initialized": 
                    case "loaded": 
                    case "waiting_for_process": 
                    case "waiting_for_lock": 
                    case "waiting_for_task": 
                    case "not_in_period": 
                        state.setSeverity(3);
                        break;
                    case "disabled": 
                        state.setSeverity(4);
                        break;
                }
                state.setText(com.sos.joc.model.job.State_.Text.valueOf(stateText));
                job.setState(state);
                job.setStateText(stateText);

                OrdersSummary ordersSummary = new OrdersSummary();
                ordersSummary.setPending(-1);
                ordersSummary.setRunning(-1);
                ordersSummary.setSetback(-1);
                ordersSummary.setSuspended(-1);
                ordersSummary.setWaitingForResource(-1);
                // TODO: job.setOrdersSummary(ordersSummary);
                job.setOrdersSummary(ordersSummary);
                // TODO: job.setNumOfRunningTasks(-1);
                job.setNumOfRunningTasks(-1);
                
                ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
                configurationStatusSchema.setMessage("myMessage");
                configurationStatusSchema.setSeverity(-1);
                configurationStatusSchema.setText(Text.CHANGED_FILE_NOT_LOADED);
                // TODO: job.setConfigurationStatus(configurationStatusSchema);
                job.setConfigurationStatus(configurationStatusSchema);
                
                if (!jobsFilterSchema.getCompact()) {
                    job.setAllSteps(Integer.valueOf(attributes.getNamedItem("all_steps").getNodeValue()));
                    job.setAllTasks(Integer.valueOf(attributes.getNamedItem("all_tasks").getNodeValue()));
                    // TODO: job.setDelayUntil(new Date());
                    job.setDelayUntil(new Date());
                    // TODO: job.setNextPeriodBegin("myNextPeriodBegin");
                    job.setNextPeriodBegin("myNextPeriodBegin");
                    if(attributes.getNamedItem("next_start_time") != null) {
                        String nextStartTime = attributes.getNamedItem("next_start_time").getNodeValue();
                        if (!nextStartTime.contains("T")) {
                            job.setNextStartTime(sdf.parse(nextStartTime));
                        } else {
                            job.setNextStartTime(sdf2.parse(nextStartTime));
                            
                        }
                    }
                    List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();
                    OrderQueue orderQueue = new OrderQueue();
                    entity.setDeliveryDate(new Date());
                    ConfigurationStatusSchema configurationStatus = new ConfigurationStatusSchema();
                    configurationStatus.setMessage("myMessage");
                    configurationStatus.setSeverity(0);
                    configurationStatus.setText(Text.CHANGED_FILE_NOT_LOADED);
                    orderQueue.setConfigurationStatus(configurationStatus);
                    orderQueue.setEndState("myEndState");
                    orderQueue.setHistoryId(-1);
                    orderQueue.setInProcessSince(new Date());
                    orderQueue.setJob("myJob");
                    orderQueue.setJobChain("myJobChain");
                    orderQueue.setLock("myLock");
                    orderQueue.setNextStartTime(new Date());
                    orderQueue.setOrderId("myOrderId");

                    List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                    NameValuePairsSchema param1 = new NameValuePairsSchema();
                    NameValuePairsSchema param2 = new NameValuePairsSchema();
                    param1.setName("param1");
                    param1.setValue("value1");
                    param2.setName("param2");
                    param2.setValue("value2");
                    parameters.add(param1);
                    parameters.add(param1);
                    orderQueue.setParams(parameters);

                    orderQueue.setPath("myPath");
                    orderQueue.setPriority(-1);
                    orderQueue.setProcessClass("myProcessClass");
                    orderQueue.setProcessedBy("myProcessedBy");

                    ProcessingState processingState = new ProcessingState();
                    processingState.setSeverity(1);
                    processingState.setText(ProcessingState.Text.RUNNING);

                    orderQueue.setProcessingState(processingState);

                    orderQueue.setSetback(new Date());
                    orderQueue.setStartedAt(new Date());
                    orderQueue.setState("myState");
                    orderQueue.setStateText("myStateText");
                    orderQueue.setSurveyDate(new Date());
                    orderQueue.setTaskId(-1);
                    orderQueue.setType(Type.FILE_ORDER);
                    listOrderQueue.add(orderQueue);
                    // TODO: job.setOrderQueue(listOrderQueue);
                    job.setOrderQueue(listOrderQueue);
                    // TODO: job.setParams(parameters);
                    job.setParams(parameters);

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
                    // TODO: job.setRunningTasks(listOfRunningTask);
                    job.setRunningTasks(listOfRunningTask);

                    List<TaskQueue> listOfTasks = new ArrayList<TaskQueue>();
                    TaskQueue taskQueue = new TaskQueue();
                    taskQueue.setTaskId(-1);
                    job.setTaskQueue(listOfTasks);

                    job.setTemporary(false);
                }

                listJobs.add(job);
            }
            entity.setJobs(listJobs);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
}
