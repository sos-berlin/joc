package com.sos.joc.job.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.JSObjectConfiguration;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobResourceConfiguration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.job.JobConfigurationFilter;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {

	private static final String API_CALL = "./job/configuration";

	@Override
	public JOCDefaultResponse postJobConfiguration(String xAccessToken, String accessToken,
			JobConfigurationFilter jobBody) throws Exception {
		return postJobConfiguration(getAccessToken(xAccessToken, accessToken), jobBody);
	}

	public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationFilter jobBody)
			throws Exception {

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobBody, accessToken, jobBody.getJobschedulerId(),
					getPermissonsJocCockpit(jobBody.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			Configuration200 entity = new Configuration200();
			if (checkRequiredParameter("job", jobBody.getJob())) {
				JSObjectConfiguration jocConfiguration = new JSObjectConfiguration(accessToken);
				boolean responseInHtml = jobBody.getMime() == ConfigurationMime.HTML;
				entity = jocConfiguration.getJobConfiguration(this, jobBody.getJob(), responseInHtml);
			}

			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}
}