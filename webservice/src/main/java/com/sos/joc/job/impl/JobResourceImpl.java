package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.job.JobsVCallable;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.Job200VSchema;
import com.sos.joc.model.job.JobFilterSchema;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceImpl.class);

    public JOCDefaultResponse postJob(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {
        LOGGER.debug("init job");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            Job200VSchema entity = new Job200VSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("job", jobFilterSchema.getJob())) {
                jocXmlCommand.excutePost(JobsUtils.createJobPostCommand(jobFilterSchema.getJob(), jobFilterSchema.getCompact()));
                Element jobElem = (Element) jocXmlCommand.getSosxml().selectSingleNode("/spooler/answer/job");
                JobsVCallable j = new JobsVCallable(jobElem, jocXmlCommand, jobFilterSchema.getCompact(), false);
                entity.setDeliveryDate(new Date());
                entity.setJob(j.getJob());
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}