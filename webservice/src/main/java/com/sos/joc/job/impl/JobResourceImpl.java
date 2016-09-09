package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.job.Job;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.Job200VSchema;
import com.sos.joc.model.job.JobFilterSchema;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceImpl.class);

    public JOCDefaultResponse postJob(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {
        LOGGER.debug("init Job");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            Job200VSchema entity = new Job200VSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String postCommand = JobsUtils.createJobPostCommand(jobFilterSchema);
            jocXmlCommand.excutePost(postCommand);
            entity.setDeliveryDate(new Date());
            Node jobNode = jocXmlCommand.getSosxml().selectSingleNode("//job");
            entity.setJob(Job.getJob_(jobNode, jocXmlCommand, jobFilterSchema.getCompact()));
            entity.getJob().setSurveyDate(jocXmlCommand.getSurveyDate());
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}