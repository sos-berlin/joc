package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerVolatile;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSupervisor;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorImpl extends JOCResourceImpl implements IJobSchedulerResourceSupervisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceSupervisorImpl.class);
    private static final String API_CALL = "./jobscheduler/supervisor";

    @Override
    public JOCDefaultResponse postJobschedulerSupervisor(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            Globals.beginTransaction();
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobSchedulerV200 entity = new JobSchedulerV200();

            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
                DBItemInventoryInstance dbItemInventorySupervisorInstance = dbLayer.getInventoryInstancesByKey(supervisorId);

                if (dbItemInventorySupervisorInstance == null) {
                    String errMessage = String.format("jobschedulerId for supervisor of %s with internal id %s not found in table %s", jobSchedulerId
                            .getJobschedulerId(), supervisorId, DBLayer.TABLE_INVENTORY_INSTANCES);
                    throw new DBInvalidDataException(errMessage);
                }
                entity.setJobscheduler(new JobSchedulerVolatile(dbItemInventorySupervisorInstance).getJobScheduler());
            } else {
                entity.setJobscheduler(new JobSchedulerV());
            }
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobSchedulerId));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobSchedulerId));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        } finally {
            Globals.rollback();
        }
    }

}
