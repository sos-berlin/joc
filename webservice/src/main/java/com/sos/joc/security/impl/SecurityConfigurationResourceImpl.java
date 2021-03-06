package com.sos.joc.security.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.security.SOSSecurityConfiguration;
import com.sos.joc.classes.security.SOSSecurityConfigurationMasters;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.db.configuration.JocConfigurationFilter;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.security.resource.ISecurityConfigurationResourceRead;
import com.sos.schema.JsonValidator;

@Path("security_configuration")
public class SecurityConfigurationResourceImpl extends JOCResourceImpl implements ISecurityConfigurationResourceRead {

	private static final String API_CALL_READ = "./security_configuration/read";
	private static final String API_CALL_WRITE = "./security_configuration/write";

	@Override
	public JOCDefaultResponse postSecurityConfigurationRead(String accessToken) {
	    SOSHibernateSession connection = null;
	    try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL_READ, null, accessToken, "", true);
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			/** check permissions */
			if (!getPermissonsJocCockpit("", accessToken).getJobschedulerMaster().getAdministration()
					.isEditPermissions()) {
				return this.accessDeniedResponse();
			}

			SOSSecurityConfigurationMasters.resetInstance();
			SOSSecurityConfiguration sosSecurityConfiguration = new SOSSecurityConfiguration();
			SecurityConfiguration entity = sosSecurityConfiguration.readConfiguration();
			
			connection = Globals.createSosHibernateStatelessConnection(API_CALL_READ);
            JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);
            JocConfigurationFilter filter = new JocConfigurationFilter();
            filter.setConfigurationType("PROFILE");
     
            entity.setProfiles(jocConfigurationDBLayer.getJocConfigurationProfiles(filter));
			
            entity.setDeliveryDate(Date.from(Instant.now()));

			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
		    Globals.disconnect(connection);
		}

	}

	@Override
	public JOCDefaultResponse postSecurityConfigurationWrite(String accessToken, byte[] securityConfigurationBytes) {
		try {
		    JsonValidator.validateFailFast(securityConfigurationBytes, SecurityConfiguration.class);
		    SecurityConfiguration securityConfiguration = Globals.objectMapper.readValue(securityConfigurationBytes, SecurityConfiguration.class);
            
			JOCDefaultResponse jocDefaultResponse = init(API_CALL_WRITE, null, accessToken, "", true);
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			/** check permissions */
			if (!getPermissonsJocCockpit("", accessToken).getJobschedulerMaster().getAdministration()
					.isEditPermissions()) {
				return this.accessDeniedResponse();
			}

			SOSSecurityConfiguration sosSecurityConfiguration = new SOSSecurityConfiguration();

			return JOCDefaultResponse
					.responseStatus200(sosSecurityConfiguration.writeConfiguration(securityConfiguration));
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
		}

	}

}