package com.sos.joc.jobs.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceP;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobsFilter;
import com.sos.joc.model.job.JobsP;

@Path("jobs")
public class JobsResourcePImpl extends JOCResourceImpl implements IJobsResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourcePImpl.class);

    @Override
    public JOCDefaultResponse postJobsP(String accessToken, JobsFilter jobsFilterSchema) throws Exception {

        LOGGER.debug("init jobs p");

        try {

            // TODO JOC Cockpit Webservice: Reading from database
            
            Globals.beginTransaction();
            JOCDefaultResponse jocDefaultResponse = init(jobsFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            List<JobP> listJobs = new ArrayList<JobP>();

            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection, jobsFilterSchema.getJobschedulerId());
            List<DBItemInventoryJob> listOfJobs = dbLayer.getInventoryJobs();
            for (DBItemInventoryJob inventoryJob : listOfJobs) {
                JobP job = new JobP();
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

                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                NameValuePair param1 = new NameValuePair();
                NameValuePair param2 = new NameValuePair();
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
                listJobs.add(job);
            }

            JobsP entity = new JobsP();
            entity.setDeliveryDate(new Date());
            entity.setJobs(listJobs);
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
        finally{
            Globals.rollback();
        }
    }

}
