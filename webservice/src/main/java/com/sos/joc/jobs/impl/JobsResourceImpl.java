package com.sos.joc.jobs.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobs.JobsUtils;
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
import com.sos.joc.model.job.State_;
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

        /* jobsBody.getCompact(); ERLEDIGT */
        /* jobsBody.getFolders(); ERLEDIGT */ 
        /* jobsBody.getIsOrderJob(); ERLEDIGT */
        /* jobsBody.getJobs(); ERLEDIGT */
//        jobsBody.getDateFrom();
//        jobsBody.getDateTo();
//        jobsBody.getRegex();
//        jobsBody.getTimeZone();
//        jobsBody.getState();
        try {
 
            JobsVSchema entity = new JobsVSchema();
            List<Job_> listJobs = new ArrayList<Job_>();
            //FOLDERS ERLEDIGT
            //COMPACT ERLEDIGT
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String postCommand = JobsUtils.createPostCommand(jobsBody);
            jocXmlCommand.excutePost(postCommand);
            entity.setDeliveryDate(new Date());
            Date surveyDate = jocXmlCommand.getSurveyDate();
            
            if(jobsBody.getIsOrderJob() == null) {
                // all jobs
                jocXmlCommand.createNodeList("//jobs/job");
            } else if(jobsBody.getIsOrderJob()) {
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
                    job.setLocks(listOfLocks);
                } else {
                    job.setLocks(null);
                }
                NamedNodeMap attributes = jobNode.getAttributes();
                job.setName(attributes.getNamedItem("name").getNodeValue());
                job.setPath(attributes.getNamedItem("path").getNodeValue());
                String stateText = attributes.getNamedItem("state").getNodeValue();
                State_ state = new State_();
                Integer severity = JobsUtils.getSeverityFromStateText(stateText);
                if (severity != null) {
                    state.setSeverity(JobsUtils.getSeverityFromStateText(stateText));
                }
                // UpperCase only to prevent error in actual body, re-adjust when model changes
                state.setText(com.sos.joc.model.job.State_.Text.valueOf(stateText.toUpperCase()));
                job.setState(state);
                job.setStateText(stateText);

                // BIG TODO
//                OrdersSummary ordersSummary = new OrdersSummary();
//                ordersSummary.setPending(-1);
//                ordersSummary.setRunning(-1);
//                ordersSummary.setSetback(-1);
//                ordersSummary.setSuspended(-1);
//                ordersSummary.setWaitingForResource(-1);
//                job.setOrdersSummary(ordersSummary);

                if (jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks[@count]") != null &&
                        !"".equalsIgnoreCase(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks[@count]"))) {
                    job.setNumOfRunningTasks(Integer.parseInt(
                            jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks[@count]")));
                }
                // TODO ConfigurationStatusSchema
//                ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
//                configurationStatusSchema.setMessage("myMessage");
//                configurationStatusSchema.setSeverity(-1);
//                configurationStatusSchema.setText(Text.CHANGED_FILE_NOT_LOADED);
//                job.setConfigurationStatus(configurationStatusSchema);
                
                if (!jobsBody.getCompact()) {
                    job.setAllSteps(Integer.valueOf(attributes.getNamedItem("all_steps").getNodeValue()));
                    job.setAllTasks(Integer.valueOf(attributes.getNamedItem("all_tasks").getNodeValue()));
                    
                    // TODO: job.setDelayUntil(new Date());
//                    job.setDelayUntil(new Date());
                    
                    // TODO: Joacim
//                    job.setNextPeriodBegin("myNextPeriodBegin");
                    
                    if(attributes.getNamedItem("next_start_time") != null) {
                        job.setNextStartTime(getDateFromString(attributes.getNamedItem("next_start_time").getNodeValue()));
                    }

                    NodeList paramsNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "params/param");
                    List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
                    if (paramsNodes != null && paramsNodes.getLength() > 0) {
                        for(int paramsCount = 0; paramsCount < paramsNodes.getLength(); paramsCount++) {
                            NameValuePairsSchema param = new NameValuePairsSchema();
                            Element paramElement = (Element)paramsNodes.item(paramsCount);
                            param.setName(paramElement.getAttribute("name"));
                            param.setValue(paramElement.getAttribute("value"));
                            params.add(param);
                        }
                        job.setParams(params);
                    } else {
                        job.setParams(null);
                    }

                    NodeList queuedTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "queued_tasks/queued_task");
                    List<TaskQueue> queuedTasks = new ArrayList<TaskQueue>();
                    if (queuedTasksNodes != null && queuedTasksNodes.getLength() > 0) {
                        for(int queuedTasksCount = 0; queuedTasksCount < paramsNodes.getLength(); queuedTasksCount++) {
                            TaskQueue taskQueue = new TaskQueue();
                            Element taskQueueElement = (Element)queuedTasksNodes.item(queuedTasksCount);
                            taskQueue.setTaskId(Integer.parseInt(taskQueueElement.getAttribute("id")));
                            taskQueue.setEnqueued(getDateFromString(taskQueueElement.getAttribute("enqueued")));
                        }
                        job.setTaskQueue(queuedTasks);
                    } else {
                        job.setTaskQueue(null);
                    }
                    
                    if (job.getNumOfRunningTasks() > 0) {
                        NodeList runningTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "tasks/task");
                        List<RunningTask> runningTasks = new ArrayList<RunningTask>();
                        for (int runningTasksCount = 0; runningTasksCount < runningTasksNodes.getLength(); runningTasksCount++) {
                            RunningTask task = new RunningTask();
                            Element taskElement = (Element)runningTasksNodes.item(runningTasksCount);
                            task.setCause(RunningTask.Cause.valueOf(taskElement.getAttribute("cause")));
                            task.setEnqueued(getDateFromString(taskElement.getAttribute("enqueued")));
                            task.setIdleSince(getDateFromString(taskElement.getAttribute("idle_since")));
                            if(taskElement.getAttribute("pid") != null && !taskElement.getAttribute("pid").isEmpty()){
                                task.setPid(Integer.parseInt(taskElement.getAttribute("pid")));
                            }
                            task.setStartedAt(getDateFromString(taskElement.getAttribute("start_at")));
                            if(taskElement.getAttribute("steps") != null && !taskElement.getAttribute("steps").isEmpty()) {
                            task.setSteps(Integer.parseInt(taskElement.getAttribute("steps")));
                            }
                            if(taskElement.getAttribute("id") != null && !taskElement.getAttribute("id").isEmpty()){
                                task.setTaskId(Integer.parseInt(taskElement.getAttribute("id")));
                            }
                            Element orderElement = (Element)jocXmlCommand.getSosxml().selectSingleNode(taskElement, "order");
                            if(orderElement != null) {
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
                        job.setRunningTasks(runningTasks);
                    } else {
                        job.setRunningTasks(null);
                    }

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