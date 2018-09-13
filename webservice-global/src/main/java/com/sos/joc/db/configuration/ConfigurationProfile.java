package com.sos.joc.db.configuration;

import java.util.Date;

import com.sos.joc.model.configuration.Profile;

public class ConfigurationProfile extends Profile {

    public ConfigurationProfile(String account, Date lastLogin) {
        setAccount(account);
        setLastLogin(lastLogin);
    }
}
