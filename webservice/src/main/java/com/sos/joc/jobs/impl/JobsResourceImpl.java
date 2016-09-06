package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.JobsVSchema;
import com.sos.joc.model.job.State_;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsFilterSchema jobsFilterSchema) throws Exception {
        LOGGER.debug("init Jobs");
        JOCDefaultResponse jocDefaultResponse = init(jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init Jobs");
 
            JobsVSchema entity = new JobsVSchema();
            List<Job_> listJobs = new ArrayList<Job_>();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String postCommand = JobsUtils.createPostCommand(jobsFilterSchema);
            jocXmlCommand.excutePost(postCommand);
            entity.setDeliveryDate(new Date());
            Date surveyDate = jocXmlCommand.getSurveyDate();
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
                if (!JobsUtils.filterJob(jobsFilterSchema, (Element)jobNode, jocXmlCommand.getSosxml())) {
                    continue;
                }
                Job_ job = new Job_();
                if (surveyDate != null) {
                    job.setSurveyDate(surveyDate);
                }

                if (jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "queued_tasks/@length") != null) {
                    job.setNumOfQueuedTasks(Integer.valueOf(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, 
                            "queued_tasks/@length")));
                } else {
                    job.setNumOfQueuedTasks(0);
                }
                
                NodeList lockNodes = jocXmlCommand.getSosxml().selectNodeList(jobNode, "lock.requestor/lock.use");
                job.setLocks(JobsUtils.initLocks(lockNodes));

                NamedNodeMap attributes = jobNode.getAttributes();
                job.setName(attributes.getNamedItem("name").getNodeValue());
                job.setPath(attributes.getNamedItem("path").getNodeValue());

                String stateText = attributes.getNamedItem("state").getNodeValue();
                State_ state = new State_();
                state.setSeverity(JobsUtils.getSeverityFromStateText(stateText));
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

                if (jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count") != null &&
                        !"".equalsIgnoreCase(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count"))) {
                    job.setNumOfRunningTasks(Integer.parseInt(
                            jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count")));
                } else {
                    job.setNumOfRunningTasks(0);
                }

                job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus((Element)jobNode));
                
                if (!jobsFilterSchema.getCompact()) {
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
                    job.setParams(JobsUtils.initParameters(paramsNodes));

                    NodeList queuedTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "queued_tasks/queued_task");
                    job.setTaskQueue(JobsUtils.initQueuedTasks(queuedTasksNodes));
                    
                    if (job.getNumOfRunningTasks() != null && job.getNumOfRunningTasks() > 0) {
                        NodeList runningTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "tasks/task");
                        job.setRunningTasks(JobsUtils.initRunningTasks(runningTasksNodes, jocXmlCommand));
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