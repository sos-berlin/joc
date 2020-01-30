package com.sos.joc.lock.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.lock.resource.ILockResourceConfiguration;
import com.sos.joc.model.common.Configuration;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.lock.LockConfigurationFilter;
import com.sos.schema.JsonValidator;

@Path("lock")
public class LockResourceConfigurationImpl extends JOCResourceImpl implements ILockResourceConfiguration {

    private static final String API_CALL = "./lock/configuration";

    @Override
    public JOCDefaultResponse postLockConfiguration(String accessToken, byte[] lockBodyBytes) {
        try {
            JsonValidator.validateFailFast(lockBodyBytes, LockConfigurationFilter.class);
            LockConfigurationFilter lockBody = Globals.objectMapper.readValue(lockBodyBytes, LockConfigurationFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, lockBody, accessToken, lockBody.getJobschedulerId(), getPermissonsJocCockpit(
                    lockBody.getJobschedulerId(), accessToken).getLock().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("lock", lockBody.getLock());
            Configuration200 entity = new Configuration200();
            String lockPath = normalizePath(lockBody.getLock());
            boolean responseInHtml = lockBody.getMime() == ConfigurationMime.HTML;
            if (versionIsOlderThan("1.13.1")) {
                entity.setConfiguration(getConfiguration(lockPath, responseInHtml));
            } else {
                try {
                    entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(new JOCHotFolder(this), lockPath, JobSchedulerObjectType.LOCK,
                            responseInHtml));
                } catch (JobSchedulerObjectNotExistException e) {
                    entity.setConfiguration(getConfiguration(lockPath, responseInHtml));
                }
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
    
    private Configuration getConfiguration(String path, boolean responseInHtml) throws JocException {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
        String xPath = String.format("/spooler/answer//locks/lock[@path='%s']", path);
        String lockCommand = jocXmlCommand.getShowStateCommand("folder lock", "folders no_subfolders source", getParent(path));
        return ConfigurationUtils.getConfigurationSchema(jocXmlCommand, lockCommand, xPath, "lock", responseInHtml,
                getAccessToken());
    }
}