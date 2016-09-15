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
import com.sos.joc.classes.job.JobOrderQueue;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.JobOrderQueue200VSchema;
import com.sos.joc.model.job.JobOrderQueueFilterSchema;
import com.sos.joc.model.job.Job__;

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
            JobOrderQueue200VSchema entity = new JobOrderQueue200VSchema();
            entity.setDeliveryDate(new Date());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            String postCommand = JobOrderQueue.createJobOrderQueuePostCommand(jobOrderQueueFilterSchema);
            jocXmlCommand.excutePost(postCommand);
            Node jobNode = jocXmlCommand.getSosxml().selectSingleNode("//job");
            Job__ job = Job.getJob__(jobNode, jocXmlCommand, jobOrderQueueFilterSchema.getCompact());
            job.setSurveyDate(jocXmlCommand.getSurveyDate());
            Node orderQueueNode = jocXmlCommand.getSosxml().selectSingleNode("//job/order_queue");
            job.setOrderQueue(JobOrderQueue.getOrderQueue(orderQueueNode, jocXmlCommand));
            entity.setJob(job);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
