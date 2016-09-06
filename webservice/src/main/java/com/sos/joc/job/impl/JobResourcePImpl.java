package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.job.Job;
import com.sos.joc.model.job.Job200PSchema;
import com.sos.joc.model.job.JobFilterSchema;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourcePImpl.class);

    @Override
    public JOCDefaultResponse postJobP(String accessToken, JobFilterSchema jobFilterSchema) throws Exception {

        LOGGER.debug("init JobsP");
        JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            Job200PSchema entity = new Job200PSchema();

            // TODO JOC Cockpit Webservice: Reading from database
            entity.setDeliveryDate(new Date());

            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(jobschedulerUser.getSosShiroCurrentUser().getSosHibernateConnection(), jobFilterSchema.getJobschedulerId());
            DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(jobFilterSchema.getJob());
            Job job = new Job();
            job.setConfigurationDate(new Date());
            job.setEstimatedDuration(-1);
            job.setHasDescription(false);
            job.setIsOrderJob(inventoryJob.getIsOrderJob());
            ArrayList<String> jobChains = new ArrayList<String>();
            jobChains.add("myJobChain1");
            jobChains.add("myJobChain2");
            jobChains.add("myJobChain3");
            job.setJobChains(jobChains);

  
            job.setLocks(Jobs.getJobLocks());

            job.setMaxTasks(-1);
            job.setName(inventoryJob.getBaseName());
 
            job.setParams(Parameters.getParameters());
            job.setPath(inventoryJob.getName());

            job.setProcessClass("myProcessClass");
            job.setSurveyDate(inventoryJob.getModified());
            job.setTitle(inventoryJob.getTitle());
            job.setUsedInJobChains(-1);

            entity.setJob(job);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
