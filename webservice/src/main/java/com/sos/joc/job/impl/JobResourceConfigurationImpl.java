package com.sos.joc.job.impl;

import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationEntity;
import com.sos.joc.job.post.JobConfigurationBody;
import com.sos.joc.job.resource.IJobResourceConfiguration;

@Path("job")
public class JobResourceConfigurationImpl extends JOCResourceImpl implements IJobResourceConfiguration {
    private static final Logger LOGGER = Logger.getLogger(JobResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobConfiguration(String accessToken, JobConfigurationBody jobConfigurationBody) throws Exception {

        LOGGER.debug("init Jobs Configuration");
        JOCDefaultResponse jocDefaultResponse = init(jobConfigurationBody.getJobschedulerId(), getPermissons(accessToken).getJob().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            // TODO JOC Cockpit Webservice
            ConfigurationEntity configurationEntity = new ConfigurationEntity();
            return JOCDefaultResponse.responseStatus200(configurationEntity.getEntity());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
