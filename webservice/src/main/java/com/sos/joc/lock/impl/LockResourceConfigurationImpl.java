package com.sos.joc.lock.impl;

import java.time.Instant;
import java.util.Date;

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
import com.sos.schema.JsonValidator;

@Path("lock")
public class LockResourceConfigurationImpl extends JOCResourceImpl implements ILockResourceConfiguration {

	private static final String API_CALL = "./lock/configuration";

	@Override
	public JOCDefaultResponse postLockConfiguration(String accessToken, byte[] lockBodyBytes) {
		try {
		    JsonValidator.validateFailFast(lockBodyBytes, LockConfigurationFilter.class);
		    LockConfigurationFilter lockBody = Globals.objectMapper.readValue(lockBodyBytes, LockConfigurationFilter.class);
            
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, lockBody, accessToken, lockBody.getJobschedulerId(),
					getPermissonsJocCockpit(lockBody.getJobschedulerId(), accessToken).getLock().getView()
							.isConfiguration());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			Configuration200 entity = new Configuration200();
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
			if (checkRequiredParameter("lock", lockBody.getLock())) {
				String lockPath = normalizePath(lockBody.getLock());
				boolean responseInHtml = lockBody.getMime() == ConfigurationMime.HTML;
				String xPath = String.format("/spooler/answer//locks/lock[@path='%s']", lockPath);
				String lockCommand = jocXmlCommand.getShowStateCommand("folder lock", "folders no_subfolders source",
						getParent(lockPath));
				entity.setConfiguration(ConfigurationUtils.getConfigurationSchema(jocXmlCommand, lockCommand, xPath,
						"lock", responseInHtml, accessToken));
				entity.setDeliveryDate(Date.from(Instant.now()));
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