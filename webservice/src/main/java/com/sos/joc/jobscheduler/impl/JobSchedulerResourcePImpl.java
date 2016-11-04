package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerPermanent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceP;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;

@Path("jobscheduler")
public class JobSchedulerResourcePImpl extends JOCResourceImpl implements IJobSchedulerResourceP {

    private static final String API_CALL = "./jobscheduler/p";

    @Override
    public JOCDefaultResponse postJobschedulerP(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {
        try {
            initLogging(API_CALL, jobSchedulerId);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Globals.beginTransaction();
            JobSchedulerP200 entity = new JobSchedulerP200();
            entity.setJobscheduler(JobSchedulerPermanent.getJobScheduler(dbItemInventoryInstance, false));
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }
    
}