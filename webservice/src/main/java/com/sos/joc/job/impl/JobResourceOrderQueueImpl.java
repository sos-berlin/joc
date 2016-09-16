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
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.Job200VSchema;
import com.sos.joc.model.job.JobOrderQueueFilterSchema;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImpl.class);

    @Override
    public JOCDefaultResponse postJobOrderQueue(String accessToken, JobOrderQueueFilterSchema jobOrderQueueFilterSchema) throws Exception {

        LOGGER.debug("init job/order_queue");
        JOCDefaultResponse jocDefaultResponse = init(jobOrderQueueFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        
        try {
            Job200VSchema entity = new Job200VSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("job", jobOrderQueueFilterSchema.getJob())) {
                jocXmlCommand.excutePost(JobsUtils.createJobPostCommand(jobOrderQueueFilterSchema.getJob(), jobOrderQueueFilterSchema.getCompact()));
                Element jobElem = (Element) jocXmlCommand.getSosxml().selectSingleNode("/spooler/answer/job");
                JobsVCallable j = new JobsVCallable(jobElem, jocXmlCommand, false, true);
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
