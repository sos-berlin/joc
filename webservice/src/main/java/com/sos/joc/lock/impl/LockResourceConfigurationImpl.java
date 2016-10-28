package com.sos.joc.lock.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.lock.resource.ILockResourceConfiguration;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.lock.LockConfigurationFilter;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("lock")
public class LockResourceConfigurationImpl extends JOCResourceImpl implements ILockResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockResourceConfigurationImpl.class);
    private static final String API_CALL = "./lock/configuration";

    @Override
    public JOCDefaultResponse postLockConfiguration(String accessToken, LockConfigurationFilter lockBody) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, lockBody.getJobschedulerId(), getPermissons(accessToken).getLock().getView()
                    .isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("lock", lockBody.getLock())) {
                boolean responseInHtml = lockBody.getMime() == ConfigurationMime.HTML;
                String xPath = String.format("/spooler/answer//locks/lock[@path='%s']", normalizePath(lockBody.getLock()));
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createLockConfigurationPostCommand(), xPath, "lock",
                        responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, lockBody));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, lockBody));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private String createLockConfigurationPostCommand() {
        JSCmdShowState showLocks = new JSCmdShowState(Globals.schedulerObjectFactory);
        showLocks.setSubsystems("folder lock");
        showLocks.setWhat("folders source");
        return Globals.schedulerObjectFactory.toXMLString(showLocks);
    }

}