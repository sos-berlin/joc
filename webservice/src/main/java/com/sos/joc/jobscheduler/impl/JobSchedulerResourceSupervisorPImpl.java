package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourcePSupervisor;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerP;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorPImpl implements IJobSchedulerResourcePSupervisor {

    @Override
    public JOCDefaultResponse postJobschedulerSupervisorP(String accessToken, JobSchedulerId jobSchedulerFilterSchema) throws Exception {
        JobSchedulerResourceP jobSchedulerPResource = new JobSchedulerResourceP(accessToken, jobSchedulerFilterSchema);

        try {
            Globals.beginTransaction();
            DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerPResource.getJobschedulerUser().getSchedulerInstance(
                    new JobSchedulerIdentifier(jobSchedulerFilterSchema.getJobschedulerId()));
            if (dbItemInventoryInstance == null) {
                String errMessage = String.format("jobschedulerId %s not found in table %s", jobSchedulerFilterSchema.getJobschedulerId(),
                        DBLayer.TABLE_INVENTORY_INSTANCES);
                throw new DBInvalidDataException(errMessage);
            }
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                dbItemInventoryInstance = dbLayer.getInventoryInstancesByKey(supervisorId);
                if (dbItemInventoryInstance == null) {
                    String errMessage = String.format("jobschedulerId for supervisor of %s with internal id %s not found in table %s",
                            jobSchedulerFilterSchema.getJobschedulerId(), supervisorId, DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new DBInvalidDataException(errMessage);
                }
                jobSchedulerFilterSchema.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
                jobSchedulerPResource.setJobSchedulerFilterSchema(jobSchedulerFilterSchema);
                return jobSchedulerPResource.postJobschedulerP();
            } else {
                JobSchedulerP200 entity = new JobSchedulerP200();
                entity.setDeliveryDate(new Date());
                entity.setJobscheduler(new JobSchedulerP());
                return JOCDefaultResponse.responseStatus200(entity);
            }
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } finally {
            Globals.rollback();
        }
    }

}