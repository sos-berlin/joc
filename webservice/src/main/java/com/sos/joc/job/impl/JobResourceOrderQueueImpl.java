package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.JobV200;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImpl.class);
    private static final String API_CALL = "./job/order_queue";

    @Override
    public JOCDefaultResponse postJobOrderQueue(String accessToken, JobFilter jobFilter) throws Exception {

        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobV200 entity = new JobV200();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance.getUrl());
            JOCJsonCommand jocJsonCommand = new JOCJsonCommand();
            jocJsonCommand.setUriBuilderForOrders(dbItemInventoryInstance.getUrl());
            jocJsonCommand.addOrderCompactQuery(jobFilter.getCompact());
            jocXmlCommand.setUriForJsonCommand(jocJsonCommand.getURI());
            if (checkRequiredParameter("job", jobFilter.getJob())) {
                entity.setJob(jocXmlCommand.getJobWithOrderQueue(jobFilter.getJob(), jobFilter.getCompact()));
                entity.setDeliveryDate(new Date());
            }
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
