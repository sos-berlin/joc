package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerVolatile;
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
        try {
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId, getPermissonsJocCockpit(accessToken)
                    .getJobschedulerMasterCluster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            List<JobSchedulerV> masters = new ArrayList<JobSchedulerV>();
            
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(jobSchedulerId);
            if(schedulersFromDb != null && !schedulersFromDb.isEmpty()) {
                for (DBItemInventoryInstance instance : schedulersFromDb) {
                    masters.add(new JobSchedulerVolatile(instance, accessToken).getJobScheduler());
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
            Globals.rollback();
        }
    }
}
