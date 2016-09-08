package com.sos.joc.jobchain.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.jobchain.resource.IJobChainResourceConfiguration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.jobChain.JobChainConfigurationFilterSchema;

@Path("job_chain")
public class JobChainResourceConfigurationImpl extends JOCResourceImpl implements IJobChainResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postJobChainConfiguration(String accessToken, JobChainConfigurationFilterSchema jobChainConfigurationFilterSchema) throws Exception {

        LOGGER.debug("init job_chain/configuration");
        JOCDefaultResponse jocDefaultResponse =
                init(jobChainConfigurationFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = ConfigurationUtils.getEntity();
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}