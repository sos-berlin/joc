package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.JobV200;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceImpl.class);
    private static final String API_CALL = "./job";

    public JOCDefaultResponse postJob(String accessToken, JobFilter jobFilter) throws Exception {
        LOGGER.debug(API_CALL);
        JOCDefaultResponse jocDefaultResponse = init(accessToken, jobFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView()
                .isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            checkRequiredParameter("job", jobFilter.getJob());
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getUrl());
            JobV200 entity = new JobV200();
            entity.setJob(jocXmlCommand.getJob(jobFilter.getJob(), jobFilter.getCompact(), true));
            entity.setDeliveryDate(new Date());
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}