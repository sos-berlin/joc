package com.sos.joc.configuration.impl;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.configuration.resource.ILoginConfigurationResource;
import com.sos.joc.model.configuration.Login;
import com.sos.joc.model.configuration.LoginLogo;
import com.sos.joc.model.configuration.LoginLogoPosition;

@Path("configuration")
public class LoginConfigurationResourceImpl extends JOCResourceImpl implements ILoginConfigurationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginConfigurationResourceImpl.class);
    private static final String LOGO_LOCATION = "webapps/root/ext/images/";

    @Override
    public JOCDefaultResponse postLoginConfiguration() {
        return getLoginConfiguration();
    }

    @Override
    public JOCDefaultResponse getLoginConfiguration() {
        Login login = new Login();
        try {
            //if (Globals.sosShiroProperties == null) {
                Globals.sosShiroProperties = new JocCockpitProperties();
            //}
            String logoName = Globals.sosShiroProperties.getProperty("custom_logo_name", "").trim();
            if (!logoName.isEmpty()) {
                java.nio.file.Path p = Paths.get(LOGO_LOCATION + logoName);
                if (!Files.exists(p)) {
                    LOGGER.warn("logo image '" + p.toString() + "' doesn't exist but configured.");
                    logoName = "";
                }
            }
            if (logoName != null && !logoName.isEmpty()) {
                LoginLogo loginLogo = new LoginLogo();
                loginLogo.setName(logoName);
                
                
                String regEx = "(\\d+(cm|mm|in|px|pt|pc|em|ex|ch|rem|vw|vh|vmin|vmax|%)|auto)";
                String logoHeight = Globals.sosShiroProperties.getProperty("custom_logo_height", "").trim();
                if (logoHeight.matches("\\d+")) {
                    loginLogo.setHeight(logoHeight + "px");
                } else if (logoHeight.matches(regEx)) {
                    loginLogo.setHeight(logoHeight);
                } else {
                    LOGGER.warn("logo height '" + logoHeight + "' doesn't match " + regEx);
                }
                
                String logoPosition = Globals.sosShiroProperties.getProperty("custom_logo_position", "").trim();
                try {
                    loginLogo.setPosition(LoginLogoPosition.fromValue(logoPosition.toUpperCase()));
                } catch (Exception e) {
                    loginLogo.setPosition(LoginLogoPosition.BOTTOM);
                }
                login.setCustomLogo(loginLogo);
            }
            
            String defaultProfileAccount = Globals.sosShiroProperties.getProperty("default_profile_account", "").trim();
            if (!defaultProfileAccount.isEmpty()) {
                login.setDefaultProfileAccount(defaultProfileAccount);
            }
            
            return JOCDefaultResponse.responseStatus200(login);
        } catch (Exception e) {
            LOGGER.error("", e);
            return JOCDefaultResponse.responseStatus200(login);
        }
    }

}