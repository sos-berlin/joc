package com.sos.joc.security.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.security.SOSSecurityConfiguration;
import com.sos.joc.classes.security.SOSSecurityConfigurationMasters;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.security.resource.ISecurityConfigurationResourceRead;

@Path("security_configuration")
public class SecurityConfigurationResourceImpl extends JOCResourceImpl implements ISecurityConfigurationResourceRead {

	private static final String API_CALL_READ = "./security_configuration/read";
	private static final String API_CALL_WRITE = "./security_configuration/write";

	@Override
	public JOCDefaultResponse postSecurityConfigurationRead(String xAccessToken, String accessToken) throws Exception {
		return postSecurityConfigurationRead(getAccessToken(xAccessToken, accessToken));
	}

	public JOCDefaultResponse postSecurityConfigurationRead(String accessToken) throws Exception {
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
            entity.setProfiles(jocConfigurationDBLayer.getJocConfigurationProfiles());
			
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
	public JOCDefaultResponse postSecurityConfigurationWrite(String xAccessToken, String accessToken,
			SecurityConfiguration securityConfiguration) throws Exception {
		return postSecurityConfigurationWrite(getAccessToken(xAccessToken, accessToken), securityConfiguration);
	}

	public JOCDefaultResponse postSecurityConfigurationWrite(String accessToken,
			SecurityConfiguration securityConfiguration) throws Exception {
		try {
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