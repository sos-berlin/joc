package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.Job200VSchema;
import com.sos.joc.model.job.JobFilterSchema;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImpl.class);

    @Override
    public JOCDefaultResponse postJobOrderQueue(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {

        LOGGER.debug("init job/order_queue");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        
        try {
            // TODO URL "http://localhost:40410" has to read from database
            String masterJsonUrl = "http://localhost:40410";
            
            Job200VSchema entity = new Job200VSchema();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.setUriForJsonCommand(masterJsonUrl, jobFilterSchema.getCompact());
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
