package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerPermanent;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourcePSupervisor;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerP;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorPImpl extends JOCResourceImpl implements IJobSchedulerResourcePSupervisor {

	private static final String API_CALL = "./jobscheduler/supervisor/p";

	@Override
	public JOCDefaultResponse postJobschedulerSupervisorP(String xAccessToken, String accessToken,
			JobSchedulerId jobSchedulerId) throws Exception {
		return postJobschedulerSupervisorP(getAccessToken(xAccessToken, accessToken), jobSchedulerId);
	}

	public JOCDefaultResponse postJobschedulerSupervisorP(String accessToken, JobSchedulerId jobSchedulerId)
			throws Exception {
		SOSHibernateSession connection = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerId, accessToken,
					jobSchedulerId.getJobschedulerId(),
					getPermissonsJocCockpit(jobSchedulerId.getJobschedulerId(), accessToken).getJobschedulerMaster()
							.getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			Globals.beginTransaction(connection);
			JobSchedulerP200 entity = new JobSchedulerP200();

			Long supervisorId = dbItemInventoryInstance.getSupervisorId();
			if (supervisorId != DBLayer.DEFAULT_ID) {
				InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
				DBItemInventoryInstance dbItemInventorySupervisorInstance = dbLayer
						.getInventoryInstanceByKey(supervisorId);

				if (dbItemInventorySupervisorInstance == null) {
					String errMessage = String.format(
							"jobschedulerId for supervisor of %s with internal id %s not found in table %s",
							jobSchedulerId.getJobschedulerId(), supervisorId, DBLayer.TABLE_INVENTORY_INSTANCES);
					throw new DBMissingDataException(errMessage);
				}
				entity.setJobscheduler(JobSchedulerPermanent.getJobScheduler(dbItemInventorySupervisorInstance, true));
			} else {
				entity.setJobscheduler(new JobSchedulerP());
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