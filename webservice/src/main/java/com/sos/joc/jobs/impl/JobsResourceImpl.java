package com.sos.joc.jobs.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsV;

@Path("jobs")
public class JobsResourceImpl extends JOCResourceImpl implements IJobsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImpl.class);
    private static final String API_CALL = "./jobs";

    @Override
    public JOCDefaultResponse postJobs(String accessToken, JobsFilter jobsFilter) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobsFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobsV entity = new JobsV();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getUrl());
            List<JobPath> jobs = jobsFilter.getJobs();
            List<Folder> folders = jobsFilter.getFolders();
            if (jobs != null && !jobs.isEmpty()) {
                entity.setJobs(jocXmlCommand.getJobsFromShowJob(jobs, jobsFilter));
            } else if (folders != null && !folders.isEmpty()) {
                entity.setJobs(jocXmlCommand.getJobsFromShowState(folders, jobsFilter));
            } else {
                entity.setJobs(jocXmlCommand.getJobsFromShowState(jobsFilter));
            }
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobsFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobsFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}