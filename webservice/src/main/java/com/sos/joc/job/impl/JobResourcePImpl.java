package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobP200;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourcePImpl.class);
    private Long instanceId;

    @Override
    public JOCDefaultResponse postJobP(String accessToken, JobFilter jobFilterSchema) throws Exception {
        LOGGER.debug("init jobs/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection);
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(jobFilterSchema.getJobschedulerId());
            instanceId = instance.getId();
            DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(jobFilterSchema.getJob(), instanceId);
            // FILTER
            Boolean compact = jobFilterSchema.getCompact();
            
            JobP job = JobPermanent.getJob(inventoryJob, dbLayer, compact, instanceId); 
            JobP200 entity = new JobP200();
            entity.setDeliveryDate(new Date());
            entity.setJob(job);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}