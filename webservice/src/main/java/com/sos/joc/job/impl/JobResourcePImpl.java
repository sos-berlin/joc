package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobP200;
import com.sos.joc.model.job.LockUseP;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourcePImpl.class);

    @Override
    public JOCDefaultResponse postJobP(String accessToken, JobFilter jobFilterSchema) throws Exception {
        LOGGER.debug("init jobs/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection, jobFilterSchema.getJobschedulerId());
            DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(jobFilterSchema.getJob());
            // FILTER
            Boolean compact = jobFilterSchema.getCompact();
            JobP job = new JobP();
            job.setHasDescription(inventoryJob.getHasDescription());
            job.setIsOrderJob(inventoryJob.getIsOrderJob());
            job.setMaxTasks(inventoryJob.getMaxTasks());
            job.setName(inventoryJob.getBaseName());
            job.setPath(inventoryJob.getName());
            job.setSurveyDate(inventoryJob.getModified());
            job.setTitle(inventoryJob.getTitle());
            job.setUsedInJobChains(inventoryJob.getUsedInJobChains());
            Integer estimatedDuration = getEstimatedDurationInSeconds(inventoryJob);
            if(estimatedDuration != null) {
                job.setEstimatedDuration(estimatedDuration);
            } else {
                job.setEstimatedDuration(0);
            }
            if(compact == null || !compact) {
                if (inventoryJob.getProcessClassName() != null && !inventoryJob.getProcessClassName().isEmpty() 
                        && !inventoryJob.getProcessClassName().equalsIgnoreCase(DBLayer.DEFAULT_NAME)) {
                    job.setProcessClass(inventoryJob.getProcessClassName());
                }
                List<LockUseP> locks = getLocks(inventoryJob, dbLayer);
                if(locks != null && !locks.isEmpty()) {
                    job.setLocks(locks);
                }
                if (inventoryJob.getIsOrderJob()) {
                    List<DBItemInventoryJobChain> jobChainsFromDb = getJobChains(inventoryJob, dbLayer);
                    if (jobChainsFromDb != null && !jobChainsFromDb.isEmpty()) {
                        ArrayList<String> jobChains = new ArrayList<String>();
                        for (DBItemInventoryJobChain chain : jobChainsFromDb) {
                            jobChains.add(chain.getName());
                        }
                        job.setJobChains(jobChains);
                    } else {
                        job.setJobChains(null);
                    }
                } else {
                    job.setJobChains(null);
                }
                Date configDate = dbLayer.getJobConfigurationDate(inventoryJob.getId());
                if(configDate != null) {
                    job.setConfigurationDate(configDate);
                }
            } else {
                job.setLocks(null);
                job.setJobChains(null);
            }
            JobP200 entity = new JobP200();
            entity.setDeliveryDate(new Date());
            entity.setJob(job);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private Integer getEstimatedDurationInSeconds(DBItemInventoryJob job) throws Exception {
        JobSchedulerTaskHistoryDBLayer dbLayer = new JobSchedulerTaskHistoryDBLayer(Globals.sosHibernateConnection);
        Long estimatedDurationInMillis = dbLayer.getTaskEstimatedDuration(job.getName());
        if (estimatedDurationInMillis != null) {
            return estimatedDurationInMillis.intValue()/1000;
        }
        return null;
    }

    private List<LockUseP> getLocks(DBItemInventoryJob job, InventoryJobsDBLayer dbLayer) throws Exception {
        List<DBItemInventoryLock> locksFromDb = dbLayer.getLocksIfExists(job.getId());
        List<LockUseP> locks = new ArrayList<LockUseP>();
        if (locksFromDb != null) {
            for (DBItemInventoryLock lockFromDb : locksFromDb) {
                LockUseP lock = new LockUseP();
                lock.setPath(lockFromDb.getName());
                locks.add(lock);
            }
            return locks;
        } else {
            return null;
        }
    }
    
    private List<DBItemInventoryJobChain> getJobChains(DBItemInventoryJob job, InventoryJobsDBLayer dbLayer) throws Exception {
        return dbLayer.getJobChains(job.getId());
    }
    
}