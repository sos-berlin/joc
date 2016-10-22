package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.JobV200;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceImpl.class);
    private static final String API_CALL = "API-CALL: ./job";

    public JOCDefaultResponse postJob(String accessToken, JobFilter jobFilter) throws Exception {
        LOGGER.debug(API_CALL);
        JOCDefaultResponse jocDefaultResponse = init(jobFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            JobV200 entity = new JobV200();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("job", jobFilter.getJob())) {
                entity.setJob(jocXmlCommand.getJob(jobFilter.getJob(), jobFilter.getCompact(), true));
                entity.setDeliveryDate(new Date());
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(API_CALL, "USER: "+getJobschedulerUser().getSosShiroCurrentUser().getUsername());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}