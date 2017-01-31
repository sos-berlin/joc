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

import com.sos.hibernate.classes.SOSHibernateConnection;
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

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilter) {
        String jobSchedulerId = jobSchedulerFilter.getJobschedulerId();
        SOSHibernateConnection connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId, getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMasterCluster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            List<JobSchedulerV> masters = new ArrayList<JobSchedulerV>();
            
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
            List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(jobSchedulerId);
            if(schedulersFromDb != null && !schedulersFromDb.isEmpty()) {
                List<JobSchedulerVCallable> tasks = new ArrayList<JobSchedulerVCallable>();
                for (DBItemInventoryInstance instance : schedulersFromDb) {
                    tasks.add(new JobSchedulerVCallable(instance, accessToken));
                }
                if(!tasks.isEmpty()) {
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    for (Future<JobSchedulerV> result : executorService.invokeAll(tasks)) {
                        try {
                            masters.add(result.get());
                        } catch (ExecutionException e) {
                            executorService.shutdown();
                            if (e.getCause() instanceof JocException) {
                                throw (JocException) e.getCause();
                            } else {
                                throw (Exception) e.getCause();
                            }
                        }
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
}
