package com.sos.joc.classes.jobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.jitl.reporting.db.DBLayerReporting;
import com.sos.joc.Globals;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.LockUseP;

public class JobPermanent {

    public static Integer getEstimatedDurationInSeconds(DBItemInventoryJob job) throws Exception {
        SOSHibernateSession connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection("getEstimatedDurationInSeconds");

            DBLayerReporting dbLayer = new DBLayerReporting(connection);
            Long estimatedDurationInMillis = dbLayer.getTaskEstimatedDuration(job.getName(), Globals.sosShiroProperties.getProperty("limit_for_average_calculation",
                    WebserviceConstants.DEFAULT_LIMIT));
            if (estimatedDurationInMillis != null) {
                return estimatedDurationInMillis.intValue() / 1000;
            }
            return null;
        } finally {
            Globals.disconnect(connection);
        }
    }

    public static List<LockUseP> getLocks(DBItemInventoryJob job, InventoryJobsDBLayer dbLayer, Long instanceId) throws Exception {
        if ("/scheduler_file_order_sink".equals(job.getName())) {
            return null;
        }
        List<DBItemInventoryLock> locksFromDb = dbLayer.getLocksIfExists(job.getId(), instanceId);
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

    public static List<DBItemInventoryJobChain> getJobChains(DBItemInventoryJob job, InventoryJobsDBLayer dbLayer, Long instanceId) throws Exception {
        if ("/scheduler_file_order_sink".equals(job.getName())) {
            return dbLayer.getJobChainsWithFileSink(instanceId);
        }
        return dbLayer.getJobChainsByJobId(job.getId(), instanceId);
    }

    public static JobP getJob(DBItemInventoryJob inventoryJob, InventoryJobsDBLayer dbLayer, Boolean compact, Long instanceId) throws Exception {
        JobP job = new JobP();
        if ("/scheduler_file_order_sink".equals(inventoryJob.getName())) {
            job.setHasDescription(false);
            job.setIsOrderJob(true);
            job.setMaxTasks(1);
            job.setName("scheduler_file_order_sink");
            job.setPath(inventoryJob.getName());
            job.setEstimatedDuration(0);
        } else {
            job.setHasDescription(inventoryJob.getHasDescription());
            job.setIsOrderJob(inventoryJob.getIsOrderJob());
            job.setMaxTasks(inventoryJob.getMaxTasks());
            job.setName(inventoryJob.getBaseName());
            job.setPath(inventoryJob.getName());
            job.setSurveyDate(inventoryJob.getModified());
            job.setTitle(inventoryJob.getTitle());
            job.setUsedInJobChains(inventoryJob.getUsedInJobChains());
            Integer estimatedDuration = JobPermanent.getEstimatedDurationInSeconds(inventoryJob);
            if (estimatedDuration != null) {
                job.setEstimatedDuration(estimatedDuration);
            } else {
                job.setEstimatedDuration(0);
            }
        }
        
        if (compact == null || !compact) {
            if (inventoryJob.getProcessClassName() != null && !inventoryJob.getProcessClassName().isEmpty() && !inventoryJob.getProcessClassName().equalsIgnoreCase(
                    DBLayer.DEFAULT_NAME)) {
                job.setProcessClass(inventoryJob.getProcessClassName());
            } else if (inventoryJob.getProcessClass() != null) {
                job.setProcessClass(inventoryJob.getProcessClass());
            }
            List<LockUseP> locks = JobPermanent.getLocks(inventoryJob, dbLayer, instanceId);
            //if (locks != null && !locks.isEmpty()) {
                job.setLocks(locks);
            //}
            if (job.getIsOrderJob()) {
                List<DBItemInventoryJobChain> jobChainsFromDb = JobPermanent.getJobChains(inventoryJob, dbLayer, instanceId);
                if (jobChainsFromDb != null && !jobChainsFromDb.isEmpty()) {
                    Set<String> jobChains = new HashSet<String>();
                    for (DBItemInventoryJobChain chain : jobChainsFromDb) {
                        jobChains.add(chain.getName());
                    }
                    job.setJobChains(new ArrayList<String>(jobChains));
                } else {
                    job.setJobChains(null);
                }
            } else {
                job.setJobChains(null);
            }
            if ("/scheduler_file_order_sink".equals(job.getName())) {
                job.setConfigurationDate(null);
            } else {
                job.setConfigurationDate(dbLayer.getJobConfigurationDate(inventoryJob.getId()));
            }
        } else {
            job.setLocks(null);
            job.setJobChains(null);
        }
        return job;
    }

}