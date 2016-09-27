package com.sos.joc.jobs.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.Job__;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.joc.model.job.JobsVSchema;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImpl.class);

    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsFilterSchema jobsFilterSchema) throws Exception {
        LOGGER.debug("init jobs");

        try {
            JOCDefaultResponse jocDefaultResponse = init(jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobsVSchema entity = new JobsVSchema();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getCommandUrl());
            List<Job__> jobs = jobsFilterSchema.getJobs();
            List<FoldersSchema> folders = jobsFilterSchema.getFolders();
            if (jobs != null && !jobs.isEmpty()) {
                entity.setJobs(jocXmlCommand.getJobsFromShowJob(jobs, jobsFilterSchema));
            } else if (folders != null && !folders.isEmpty()) {
                entity.setJobs(jocXmlCommand.getJobsFromShowState(folders, jobsFilterSchema));
            } else {
                entity.setJobs(jocXmlCommand.getJobsFromShowState(jobsFilterSchema));
            }
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}