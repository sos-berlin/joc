package com.sos.joc.configuration.impl;

import java.nio.file.Files;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.configuration.resource.ICustomLogoConfigurationResource;
import com.sos.joc.model.configuration.LoginLogo;

@Path("configuration")
public class CustomLogoConfigurationResourceImpl extends JOCResourceImpl implements ICustomLogoConfigurationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoConfigurationResourceImpl.class);
    private static final String LOGO_LOCATION = "../../webapps/root/ext/images/";

    @Override
    public JOCDefaultResponse postCustomLogo() {
        return getCustomLogo();
    }

    @Override
    public JOCDefaultResponse getCustomLogo() {
        LoginLogo loginLogo = new LoginLogo();
        loginLogo.setName("");
        loginLogo.setWidth("");
        loginLogo.setHeight("");
        loginLogo.setPosition("");
        try {
            if (Globals.sosShiroProperties == null) {
                Globals.sosShiroProperties = new JocCockpitProperties();
            }
            String logoName = Globals.sosShiroProperties.getProperty("custom_logo_name", "").trim();
            if (!logoName.isEmpty()) {
                java.nio.file.Path p = Globals.sosShiroProperties.resolvePath(LOGO_LOCATION + logoName);
                if (!Files.exists(p)) {
                    LOGGER.warn("logo image '" + p.toString() + "' doesn't exist but configured.");
                    logoName = "";
                }
            }
            loginLogo.setName(logoName);
            String regEx = "\\d+(cm|mm|in|px|pt|pc|em|ex|ch|rem|vw|vh|vmin|vmax|%)?";
            String logoWidth = Globals.sosShiroProperties.getProperty("custom_logo_width", "").trim();
            if (logoWidth.matches(regEx)) {
                loginLogo.setWidth(logoWidth);
            } else if (logoWidth.matches("\\d+")) {
                loginLogo.setWidth(logoWidth+"px");
            } else {
                LOGGER.warn("logo width '" + logoWidth + "' doesn't match " + regEx);
            }
            String logoHeight = Globals.sosShiroProperties.getProperty("custom_logo_height", "").trim();
            if (logoHeight.matches(regEx)) {
                loginLogo.setHeight(logoHeight);
            } else if (logoHeight.matches("\\d+")) {
                loginLogo.setHeight(logoHeight+"px");
            } else {
                LOGGER.warn("logo height '" + logoHeight + "' doesn't match " + regEx);
            }
            String logoPosition = Globals.sosShiroProperties.getProperty("custom_logo_position", "").trim();
            if (logoPosition.matches("(top|bottom):" + regEx)) {
                loginLogo.setPosition(logoPosition);
            } else if (logoWidth.matches("(top|bottom):\\d+")) {
                loginLogo.setPosition(logoPosition+"px");
            } else {
                LOGGER.warn("logo position '" + logoPosition + "' doesn't match (top|bottom):" + regEx);
            }
            return JOCDefaultResponse.responseStatus200(loginLogo);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatus200(loginLogo);
        }
    }

}