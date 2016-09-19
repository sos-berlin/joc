package com.sos.joc.lock.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.lock.resource.ILockResourceConfiguration;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.lock.LockConfigurationFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("lock")
public class LockResourceConfigurationImpl extends JOCResourceImpl implements ILockResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postLockConfiguration(String accessToken, LockConfigurationFilterSchema lockBody) throws Exception {

        LOGGER.debug("init lock/configuration");
        JOCDefaultResponse jocDefaultResponse = init(lockBody.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isConfiguration());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("lock", lockBody.getLock())) {
                boolean responseInHtml = lockBody.getMime() == LockConfigurationFilterSchema.Mime.HTML;
                String xPath = String.format("/spooler/answer//locks/lock[@path='%s']",normalizePath(lockBody.getLock())); 
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createLockConfigurationPostCommand(), xPath, "lock", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private String createLockConfigurationPostCommand() {
        JSCmdShowState showLocks = new JSCmdShowState(Globals.schedulerObjectFactory);
        showLocks.setSubsystems("folder lock");
        showLocks.setWhat("folders source");
        return Globals.schedulerObjectFactory.toXMLString(showLocks);
    }

}