package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.JobV200;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImpl.class);

    @Override
    public JOCDefaultResponse postJobOrderQueue(String accessToken, JobFilter jobFilterSchema) throws Exception {

        LOGGER.debug("init job/order_queue");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobV200 entity = new JobV200();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getUrl());
            JOCJsonCommand jocJsonCommand = new JOCJsonCommand(dbItemInventoryInstance.getUrl());
            jocJsonCommand.addOrderCompactQuery(jobFilterSchema.getCompact());
            jocXmlCommand.setUriForJsonCommand(jocJsonCommand.getURI());
            if (checkRequiredParameter("job", jobFilterSchema.getJob())) {
                entity.setDeliveryDate(new Date());
                entity.setJob(jocXmlCommand.getJobWithOrderQueue(jobFilterSchema.getJob(), jobFilterSchema.getCompact()));
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}
