package com.sos.joc.job.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobs.JOCXmlJobCommand;
import com.sos.joc.classes.jobs.JobsVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceOrderQueue;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobV200;

@Path("job")
public class JobResourceOrderQueueImpl extends JOCResourceImpl implements IJobResourceOrderQueue {

	private static final String API_CALL = "./job/order_queue";

	@Override
	public JOCDefaultResponse postJobOrderQueue(String xAccessToken, String accessToken, JobFilter jobFilter)
			throws Exception {
		return postJobOrderQueue(getAccessToken(xAccessToken, accessToken), jobFilter);
	}

	public JOCDefaultResponse postJobOrderQueue(String accessToken, JobFilter jobFilter) throws Exception {
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken,
					jobFilter.getJobschedulerId(),
					getPermissonsJocCockpit(jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			checkRequiredParameter("job", jobFilter.getJob());
			String jobPath = normalizePath(jobFilter.getJob());
			JobV200 entity = new JobV200();
			if (versionIsOlderThan("1.12.6")) {
			    JOCXmlJobCommand jocXmlCommand = new JOCXmlJobCommand(this, accessToken);
			    entity.setJob(jocXmlCommand.getJobWithOrderQueue(jobPath, jobFilter.getCompact()));
			} else {
			    JOCJsonCommand command = new JOCJsonCommand(this);
                command.setUriBuilderForJobs();
                command.addJobCompactQuery(jobFilter.getCompact());
                jobFilter.setJob(jobPath);
                JobsVCallable j = new JobsVCallable(jobFilter, command, accessToken, true, null);
                entity.setJob(j.getJob());
			}
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
