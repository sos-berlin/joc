package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerVCallable;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSupervisor;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorImpl extends JOCResourceImpl implements IJobSchedulerResourceSupervisor {

    private static final String API_CALL = "./jobscheduler/supervisor";

    @Override
    public JOCDefaultResponse postJobschedulerSupervisor(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {
        SOSHibernateConnection connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection();

            Globals.beginTransaction(connection);
            initLogging(API_CALL, jobSchedulerId);

            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobSchedulerV200 entity = new JobSchedulerV200();

            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
                DBItemInventoryInstance dbItemInventorySupervisorInstance = dbLayer.getInventoryInstancesByKey(supervisorId);

                if (dbItemInventorySupervisorInstance == null) {
                    String errMessage = String.format("jobschedulerId for supervisor of %s with internal id %s not found in table %s", jobSchedulerId.getJobschedulerId(),
                            supervisorId, DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new DBInvalidDataException(errMessage);
                }
                entity.setJobscheduler(new JobSchedulerVCallable(dbItemInventorySupervisorInstance, accessToken).call());
            } else {
                entity.setJobscheduler(new JobSchedulerV());
            }
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
