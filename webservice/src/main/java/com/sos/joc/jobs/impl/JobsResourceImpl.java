package com.sos.joc.jobs.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.jobs.post.JobsBody;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.ConfigurationStatusSchema.Text;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.JobsVSchema;
import com.sos.joc.model.job.Lock_;
import com.sos.joc.model.job.Order;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.OrderQueue.Type;
import com.sos.joc.model.job.OrdersSummary;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.RunningTask.Cause;
import com.sos.joc.model.job.TaskQueue;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {
    private static final Logger LOGGER = Logger.getLogger(JobsResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsBody jobsBody) throws Exception {
        LOGGER.debug("init Jobs");
        JOCDefaultResponse jocDefaultResponse = init(jobsBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
 
            JobsVSchema entity = new JobsVSchema();
            List<Job_> listJobs = new ArrayList<Job_>();

            // TODO Use correct url
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand("http://localhost:4444");
            String postCommand = "<commands><show_state subsystems=\"job process_class lock\" what=\"job_orders task_queue\" path=\"/test\"/><show_state subsystems=\"job process_class lock\" what=\"job_orders task_queue\" path=\"/sos\"/></commands>";
            jocXmlCommand.excutePost(postCommand);
                    );
            entity.setDeliveryDate(new Date());

            Date surveyDate = jocXmlCommand.getSurveyDate();
            SimpleDateFormat sdf = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT);
            SimpleDateFormat sdf2 = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT2);

            
            jocXmlCommand.createNodeList("//jobs");
            for(int i = 0; i < jocXmlCommand.getNodeList().getLength(); i++) {
                Node jobsItem = jocXmlCommand.getNodeList().item(i);
                NodeList jobNodes = jobsItem.getChildNodes();
                for(int j = 0; j < jobNodes.getLength(); j++){
                    if("job".equalsIgnoreCase(jobNodes.item(j).getNodeName())) {
                        Job_ job = new Job_();
                        if (surveyDate != null) {
                            job.setSurveyDate(surveyDate);
                        }
                        Node jobNode = jobNodes.item(j);
                        NodeList jobChilds = jobNode.getChildNodes();
                        for(int k = 0; k < jobChilds.getLength(); k++){
                            if("queued_tasks".equalsIgnoreCase(jobChilds.item(k).getNodeName())) {
                                Node queuedTasks = jobChilds.item(k);
                                NamedNodeMap queuedTasksAttributes = queuedTasks.getAttributes();
                                for(int l = 0; l < queuedTasksAttributes.getLength(); l++) {
                                    if("length".equalsIgnoreCase(queuedTasksAttributes.item(l).getNodeName())){
                                        job.setNumOfQueuedTasks(Integer.valueOf(queuedTasksAttributes.item(l).getNodeValue()));
                                    }
                                }
                            } else if ("lock.requestor".equalsIgnoreCase(jobChilds.item(k).getNodeName())) {
                                List<Lock_> listOfLocks = new ArrayList<Lock_>();
                                Node lockRequestor = jobChilds.item(k);
                                NodeList lockUses = lockRequestor.getChildNodes();
                                for (int count = 0; count < lockUses.getLength(); count++) {
                                    Lock_ lock = new Lock_();
                                    if("lock.use".equalsIgnoreCase(lockUses.item(count).getNodeName())) {
                                        Node lockItem = lockUses.item(count);
                                        NamedNodeMap lockAttributes = lockItem.getAttributes();
                                        for (int attribute = 0; attribute < lockAttributes.getLength(); attribute++) {
                                            if("exclusive".equalsIgnoreCase(lockAttributes.item(attribute).getNodeName())) {
                                                if ("yes".equalsIgnoreCase(lockAttributes.item(attribute).getNodeValue())) {
                                                    lock.setExclusive(true);
                                                } else {
                                                    lock.setExclusive(false);
                                                }
                                            } else if ("is_available".equalsIgnoreCase(lockAttributes.item(attribute).getNodeName())) {
                                                if ("yes".equalsIgnoreCase(lockAttributes.item(attribute).getNodeValue())) {
                                                    lock.setAvailable(true);
                                                } else {
                                                    lock.setAvailable(false);
                                                }
                                                
                                            } else if ("lock".equalsIgnoreCase(lockAttributes.item(attribute).getNodeName())) {
                                                lock.setPath(lockAttributes.item(attribute).getNodeValue());
                                                
                                            }
                                        }
                                        listOfLocks.add(lock);
                                    }
                                }
                                job.setLocks(listOfLocks);
                            }
                        }
                        NamedNodeMap attributes = jobNode.getAttributes();
                        job.setName(attributes.getNamedItem("name").getNodeValue());
                        job.setPath(attributes.getNamedItem("path").getNodeValue());
                        String stateText = attributes.getNamedItem("state").getNodeValue();
                        com.sos.joc.model.job.State state = new com.sos.joc.model.job.State();
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
                        state.setText(com.sos.joc.model.job.State.Text.valueOf(stateText));
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
                        configurationStatusSchema.setText(Text.changed_file_not_loaded);
                        // TODO: job.setConfigurationStatus(configurationStatusSchema);
                        job.setConfigurationStatus(configurationStatusSchema);
                        
                        if (!jobsBody.getCompact()) {
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
                            configurationStatus.setText(Text.changed_file_not_loaded);
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
                            processingState.setText(ProcessingState.Text.running);

                            orderQueue.setProcessingState(processingState);

                            orderQueue.setSetback(new Date());
                            orderQueue.setStartedAt(new Date());
                            orderQueue.setState("myState");
                            orderQueue.setStateText("myStateText");
                            orderQueue.setSurveyDate(new Date());
                            orderQueue.setTaskId(-1);
                            orderQueue.setType(Type.file_order);
                            listOrderQueue.add(orderQueue);
                            // TODO: job.setOrderQueue(listOrderQueue);
                            job.setOrderQueue(listOrderQueue);
                            // TODO: job.setParams(parameters);
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
                }                
            }
            entity.setJobs(listJobs);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
}
