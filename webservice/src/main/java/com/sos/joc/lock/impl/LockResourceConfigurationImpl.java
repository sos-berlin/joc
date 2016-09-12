package com.sos.joc.lock.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.lock.resource.ILockResourceConfiguration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.lock.LockConfigurationFilterSchema;

@Path("lock")
public class LockResourceConfigurationImpl extends JOCResourceImpl implements ILockResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postLockConfiguration(String accessToken, LockConfigurationFilterSchema lockConfigurationFilterSchema) throws Exception {

        LOGGER.debug("init lock/configuration");
        JOCDefaultResponse jocDefaultResponse = init(lockConfigurationFilterSchema.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isConfiguration());
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