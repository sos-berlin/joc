package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.job.post.JobBody;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.Job;
import com.sos.joc.model.job.Job200PSchema;
import com.sos.joc.model.job.Lock;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {
    private static final Logger LOGGER = Logger.getLogger(JobResourcePImpl.class);

    @Override
    public JOCDefaultResponse postJobP(String accessToken, JobBody jobBody) throws Exception {

        LOGGER.debug("init JobsP");
        JOCDefaultResponse jocDefaultResponse = init(jobBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            Job200PSchema entity = new Job200PSchema();

            // TODO JOC Cockpit Webservice: Reading from database
            entity.setDeliveryDate(new Date());

            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(jobschedulerUser.getSosShiroCurrentUser().getSosHibernateConnection(), jobBody.getJobschedulerId());
            DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(jobBody.getJob());
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

            List<Lock> listOfLocks = new ArrayList<Lock>();
            Lock lock = new Lock();
            lock.setExclusive(false);
            // lock.setAvailable(true);
            lock.setPath("myPath");
            listOfLocks.add(lock);
            Lock lock2 = new Lock();
            lock2.setExclusive(true);
            // lock2.setAvailable(false);
            lock2.setPath("myPath2");
            listOfLocks.add(lock2);
            job.setLocks(listOfLocks);

            job.setMaxTasks(-1);
            job.setName(inventoryJob.getBaseName());

            List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
            NameValuePairsSchema param1 = new NameValuePairsSchema();
            NameValuePairsSchema param2 = new NameValuePairsSchema();
            param1.setName("param1");
            param1.setValue("value1");
            param2.setName("param2");
            param2.setValue("value2");
            parameters.add(param1);
            parameters.add(param1);

            job.setParams(parameters);
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
