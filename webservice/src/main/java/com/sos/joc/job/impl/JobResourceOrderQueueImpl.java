package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobV200;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {

    private static final String API_CALL = "./job/order_queue";

    @Override
    public JOCDefaultResponse postJobOrderQueue(String accessToken, JobFilter jobFilter) throws Exception {

        try {
            initLogging(API_CALL, jobFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobV200 entity = new JobV200();
            JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(this, accessToken);
            if (checkRequiredParameter("job", jobFilter.getJob())) {
                entity.setJob(jocXmlCommand.getJobWithOrderQueue(normalizePath(jobFilter.getJob()), jobFilter.getCompact()));
                entity.setDeliveryDate(new Date());
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
