package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResource;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobV200;

@Path("job")
public class JobResourceImpl extends JOCResourceImpl implements IJobResource {

	private static final String API_CALL = "./job";

	@Override
	public JOCDefaultResponse postJob(String xAccessToken, String accessToken, JobFilter jobFilter) throws Exception {
		return postJob(getAccessToken(xAccessToken, accessToken), jobFilter);
	}

	public JOCDefaultResponse postJob(String accessToken, JobFilter jobFilter) throws Exception {
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken,
					jobFilter.getJobschedulerId(),
					getPermissonsJocCockpit(jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			checkRequiredParameter("job", jobFilter.getJob());
			String jobPath = normalizePath(jobFilter.getJob());
			JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(dbItemInventoryInstance, accessToken);
			JobV200 entity = new JobV200();
			entity.setJob(jocXmlCommand.getJob(jobPath, jobFilter.getCompact(), true));
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