package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.JobSchedulerVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResource;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;
import com.sos.schema.JsonValidator;

@Path("jobscheduler")
public class JobSchedulerResourceImpl extends JOCResourceImpl implements IJobSchedulerResource {

	private static final String API_CALL = "./jobscheduler";

	@Override
	public JOCDefaultResponse postJobscheduler(String accessToken, byte[] filterBytes) {
		try {
		    JsonValidator.validateFailFast(filterBytes, HostPortParameter.class);
		    HostPortParameter jobSchedulerBody = Globals.objectMapper.readValue(filterBytes, HostPortParameter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerBody, accessToken,
					jobSchedulerBody.getJobschedulerId(),
					getPermissonsJocCockpit(jobSchedulerBody.getJobschedulerId(), accessToken).getJobschedulerMaster()
							.getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			getJobSchedulerInstanceByHostPort(jobSchedulerBody.getHost(), jobSchedulerBody.getPort(),
					jobSchedulerBody.getJobschedulerId());
			JobSchedulerV200 entity = new JobSchedulerV200();
			entity.setJobscheduler(new JobSchedulerVCallable(dbItemInventoryInstance, accessToken).call());
			entity.setDeliveryDate(new Date());
			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

}
