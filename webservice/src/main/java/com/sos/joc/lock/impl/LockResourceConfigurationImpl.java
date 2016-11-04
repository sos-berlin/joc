package com.sos.joc.lock.impl;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.lock.resource.ILockResourceConfiguration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.lock.LockConfigurationFilter;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("lock")
public class LockResourceConfigurationImpl extends JOCResourceImpl implements ILockResourceConfiguration {

    private static final String API_CALL = "./lock/configuration";

    @Override
    public JOCDefaultResponse postLockConfiguration(String accessToken, LockConfigurationFilter lockBody) throws Exception {
        try {
            initLogging(API_CALL, lockBody);
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
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createLockConfigurationPostCommand(), xPath, "lock", responseInHtml,
                        accessToken);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private String createLockConfigurationPostCommand() {
        JSCmdShowState showLocks = new JSCmdShowState(Globals.schedulerObjectFactory);
        showLocks.setSubsystems("folder lock");
        showLocks.setWhat("folders source");
        return Globals.schedulerObjectFactory.toXMLString(showLocks);
    }

}