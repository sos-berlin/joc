package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.job.Job;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.JobsVSchema;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsFilterSchema jobsFilterSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init jobs");
 
            JobsVSchema entity = new JobsVSchema();
            List<Job_> listJobs = new ArrayList<Job_>();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
//            String postCommand = JobsUtils.createJobsPostCommand(jobsFilterSchema);
            String postCommand = JobsUtils.createJobsPostCommand(jobsFilterSchema);
            LOGGER.info(postCommand);
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
                if (!JobsUtils.filterJobs(jobsFilterSchema, (Element)jobNode, jocXmlCommand.getSosxml())) {
                    continue;
                }
                Job_ job = Job.getJob_(jobNode, jocXmlCommand, jobsFilterSchema.getCompact());
                if(job != null) {
                    if (surveyDate != null) {
                        job.setSurveyDate(surveyDate);
                    }
                    listJobs.add(job);
                }
            }
            entity.setJobs(listJobs);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}