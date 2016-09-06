package com.sos.joc.job.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.job.Jobs;
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

            entity.setDeliveryDate(new Date());
            entity.setJob(Jobs.getJob(jobFilterSchema.getCompact()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }
}
