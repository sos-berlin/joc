package com.sos.joc.job.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobP200;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {

    private static final String API_CALL = "./job/p";

    @Override
    public JOCDefaultResponse postJobP(String accessToken, JobFilter jobFilter) throws Exception {
        try {
            initLogging(API_CALL, jobFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobFilter.getJobschedulerId(), getPermissons(accessToken).getJob().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("job", jobFilter.getJob());
            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection);
            Long instanceId = dbItemInventoryInstance.getId();
            DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(normalizePathForDB(jobFilter.getJob()), instanceId);

            JobP job = JobPermanent.getJob(inventoryJob, dbLayer, jobFilter.getCompact(), instanceId);
            JobP200 entity = new JobP200();
            entity.setJob(job);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}