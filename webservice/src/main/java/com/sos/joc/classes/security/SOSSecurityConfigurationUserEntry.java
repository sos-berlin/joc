package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HashFormatFactory;
import org.apache.shiro.util.StringUtils;
import org.ini4j.Profile;

import com.sos.joc.model.security.SecurityConfigurationUser;

public class SOSSecurityConfigurationUserEntry {

    private static final String DEFAULT_PASSWORD_512_ALGORITHM_NAME = "SHA-512";
    private static final HashFormatFactory HASH_FORMAT_FACTORY = new DefaultHashFormatFactory();
    private Profile.Section oldSection;
    private SOSSecurityHashSettings sosSecurityHashSettings;

    private String[] listOfRolesAndPassword;
    SecurityConfigurationUser securityConfigurationUser;

    public SOSSecurityConfigurationUserEntry(String entry, Profile.Section oldSection, SOSSecurityHashSettings sosSecurityHashSettings) {
        super();
        this.oldSection = oldSection;
        this.sosSecurityHashSettings = sosSecurityHashSettings;
        listOfRolesAndPassword = entry.split(",");
    }

    public SOSSecurityConfigurationUserEntry(SecurityConfigurationUser securityConfigurationUser, Profile.Section oldSection,
            SOSSecurityHashSettings sosSecurityHashSettings) {
        super();
        this.oldSection = oldSection;
        this.sosSecurityHashSettings = sosSecurityHashSettings;
        this.securityConfigurationUser = securityConfigurationUser;
    }

   

    public String crypt(String s) {
        if (sosSecurityHashSettings.isCrypt()) {
            String alg = DEFAULT_PASSWORD_512_ALGORITHM_NAME;
            if (StringUtils.hasText(this.sosSecurityHashSettings.getHashingAlgorithm())) {
                alg = this.sosSecurityHashSettings.getHashingAlgorithm();
            }

            Hash hash = new SimpleHash(alg, s, sosSecurityHashSettings.getSalt(), this.sosSecurityHashSettings.getHashIterations());
            HashFormat format;
            format = HASH_FORMAT_FACTORY.getInstance(sosSecurityHashSettings.getFormat());
            return format.format(hash);
        } else {
            return s;
        }
    }

    public String getPassword() {
        if (listOfRolesAndPassword.length > 0) {
            return listOfRolesAndPassword[0];
        } else {
            return "";
        }
    }

    public List<String> getRoles() {
        List<String> listOfRoles = new ArrayList<String>();
        for (int i = 1; i < listOfRolesAndPassword.length; i++) {
            listOfRoles.add(listOfRolesAndPassword[i].trim());
        }
        return listOfRoles;
    }

    public String getIniWriteString() {
        String s = securityConfigurationUser.getPassword();
        String oldUser = oldSection.get(securityConfigurationUser.getUser());
        String oldPwd = "";
        if (oldUser != null) {
            oldPwd = oldSection.get(oldUser).split(",")[0];
            oldPwd = crypt(oldPwd);
            if (oldPwd.equals(s)) {
                s = oldPwd;
            } else {
                s = crypt(s);
            }
        } else {
            s = crypt(s);
        }

        s = s + ",";
        for (int i = 0; i < securityConfigurationUser.getRoles().size(); i++) {
            s = s + securityConfigurationUser.getRoles().get(i) + ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }
}
