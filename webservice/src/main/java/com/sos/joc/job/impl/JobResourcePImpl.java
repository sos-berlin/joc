package com.sos.joc.job.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JobPermanent;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceP;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.job.JobP200;

@Path("job")
public class JobResourcePImpl extends JOCResourceImpl implements IJobResourceP {

	private static final String API_CALL = "./job/p";

	@Override
	public JOCDefaultResponse postJobP(String xAccessToken, String accessToken, JobFilter jobFilter) throws Exception {
		return postJobP(getAccessToken(xAccessToken, accessToken), jobFilter);
	}

	public JOCDefaultResponse postJobP(String accessToken, JobFilter jobFilter) {

		SOSHibernateSession connection = null;
		try {
		    // TODO: folder permissions
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken,
					jobFilter.getJobschedulerId(),
					getPermissonsJocCockpit(jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(connection);
			checkRequiredParameter("job", jobFilter.getJob());
			Long instanceId = dbItemInventoryInstance.getId();
			String jobPath = normalizePath(jobFilter.getJob());
			DBItemInventoryJob inventoryJob = dbLayer.getInventoryJobByName(jobPath, instanceId);
			if ("/scheduler_file_order_sink".equals(jobPath)) {
				inventoryJob = new DBItemInventoryJob();
				inventoryJob.setName("/scheduler_file_order_sink");
			} else if (inventoryJob == null) {
				throw new DBMissingDataException("no entry found in DB for job: " + jobFilter.getJob());
			}
			JobP job = JobPermanent.getJob(inventoryJob, dbLayer, jobFilter.getCompact(), instanceId);
			JobP200 entity = new JobP200();
			entity.setJob(job);
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