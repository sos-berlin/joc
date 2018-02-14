package com.sos.joc.security.impl;

import javax.ws.rs.Path;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.security.SOSSecurityConfiguration;
import com.sos.joc.classes.security.SOSSecurityConfigurationMasters;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.security.resource.ISecurityConfigurationResourceRead;

@Path("security_configuration")
public class SecurityConfigurationResourceImpl extends JOCResourceImpl implements ISecurityConfigurationResourceRead {

	private static final String API_CALL = "./security_configuration/read";

	@Override
	public JOCDefaultResponse postSecurityConfigurationRead(String xAccessToken, String accessToken) throws Exception {
		return postSecurityConfigurationRead(getAccessToken(xAccessToken, accessToken));
	}

	public JOCDefaultResponse postSecurityConfigurationRead(String accessToken) throws Exception {
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", true);
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

			return JOCDefaultResponse.responseStatus200(sosSecurityConfiguration.readConfiguration());
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
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
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", true);
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