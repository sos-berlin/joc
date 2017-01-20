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
import com.sos.joc.exceptions.DBMissingDataException;
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
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJob().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection);
            checkRequiredParameter("job", jobFilter.getJob());
            Long instanceId = dbItemInventoryInstance.getId();
            DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(normalizePath(jobFilter.getJob()), instanceId);
            if (inventoryJob == null) {
                throw new DBMissingDataException(String.format("API CALL %1$s: no entry found in DB for job: %2$s", API_CALL, jobFilter.getJob()));
            }
            JobP job = JobPermanent.getJob(inventoryJob, dbLayer, jobFilter.getCompact(), instanceId);
            JobP200 entity = new JobP200();
            entity.setJob(job);
            entity.setDeliveryDate(Date.from(Instant.now()));
            dbLayer.closeSession();
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