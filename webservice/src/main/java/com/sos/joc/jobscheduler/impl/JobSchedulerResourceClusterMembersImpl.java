package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerVCallable;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.joc.model.jobscheduler.MastersV;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {

    private static final String API_CALL = "./jobscheduler/cluster/members";
    private static final String MASTER_VIEW_PERM = "sos:products:joc_cockpit:jobscheduler_master:view:status";
    private static final String CLUSTER_VIEW_PERM = "sos:products:joc_cockpit:jobscheduler_master_cluster:view:status";

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String xAccessToken, String accessToken, JobSchedulerId jobSchedulerFilter) {
        return postJobschedulerClusterMembers(getAccessToken(xAccessToken, accessToken), jobSchedulerFilter);
    }

    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilter) {
        SOSHibernateSession connection = null;

        try {
            boolean isPermitted = true;
            if (jobSchedulerFilter == null) {
                jobSchedulerFilter = new JobSchedulerId();
            }
            if (jobSchedulerFilter.getJobschedulerId() == null) {
                jobSchedulerFilter.setJobschedulerId("");
            }
            if (!jobSchedulerFilter.getJobschedulerId().isEmpty()) {
                isPermitted = getPermissonsJocCockpit(accessToken).getJobschedulerMasterCluster().getView().isStatus() || getPermissonsJocCockpit(
                        accessToken).getJobschedulerMaster().getView().isStatus();
            }

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerFilter, accessToken, jobSchedulerFilter.getJobschedulerId(),
                    isPermitted);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            List<JobSchedulerV> masters = new ArrayList<JobSchedulerV>();

            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(jobSchedulerFilter.getJobschedulerId());
            if (schedulersFromDb != null && !schedulersFromDb.isEmpty()) {

                SOSShiroCurrentUser shiroUser = jobschedulerUser.getSosShiroCurrentUser();
                List<JobSchedulerVCallable> tasks = new ArrayList<JobSchedulerVCallable>();
                String masterId = "";
                for (DBItemInventoryInstance instance : schedulersFromDb) {
                    if (jobSchedulerFilter.getJobschedulerId().isEmpty()) {
                        if (instance.getSchedulerId() == null || instance.getSchedulerId().isEmpty()) {
                            continue;
                        }
                        if (!masterId.equals(instance.getSchedulerId())) {
                            masterId = instance.getSchedulerId();
                            isPermitted = shiroUser.isPermitted(CLUSTER_VIEW_PERM, masterId) || shiroUser.isPermitted(MASTER_VIEW_PERM, masterId);
                        }
                        if (!isPermitted) {
                            continue;
                        }
                    }
                    tasks.add(new JobSchedulerVCallable(setMappedUrl(instance), accessToken));
                }
                if (!tasks.isEmpty()) {
                    ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                    try {
                        for (Future<JobSchedulerV> result : executorService.invokeAll(tasks)) {
                            try {
                                masters.add(result.get());
                            } catch (ExecutionException e) {
                                if (e.getCause() instanceof JocException) {
                                    throw (JocException) e.getCause();
                                } else {
                                    throw (Exception) e.getCause();
                                }
                            }
                        }
                    } finally {
                        executorService.shutdown();
                    }
                } else {
                    if (jobSchedulerFilter.getJobschedulerId().isEmpty()) {
                        return accessDeniedResponse();
                    }
                }
            }
            MastersV entity = new MastersV();
            entity.setMasters(masters);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback(connection);
        }
    }

    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance) {
        if (Globals.jocConfigurationProperties != null) {
            return Globals.jocConfigurationProperties.setUrlMapping(instance);
        }
        return instance;
    }
}
