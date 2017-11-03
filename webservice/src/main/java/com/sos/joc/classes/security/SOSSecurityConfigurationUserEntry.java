package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.StringUtils;
import org.ini4j.Profile;

import com.sos.joc.model.security.SecurityConfigurationUser;

public class SOSSecurityConfigurationUserEntry {

    private static final String DEFAULT_PASSWORD_512_ALGORITHM_NAME = "SHA-512";
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

    protected ByteSource createByteSource(Object o) {
        return ByteSource.Util.bytes(o);
    }

    protected HashRequest createHashRequest(ByteSource plaintext) {
        return new HashRequest.Builder().setSource(plaintext).setIterations(sosSecurityHashSettings.getHashIterations()).setAlgorithmName(
                sosSecurityHashSettings.getHashingAlgorithm()).build();
    }

    public String crypt(String s) {
        DefaultHashService hashService = new DefaultHashService();

        if (sosSecurityHashSettings.isCrypt() && s != null && !s.isEmpty()) {
            String alg = DEFAULT_PASSWORD_512_ALGORITHM_NAME;
            if (StringUtils.hasText(this.sosSecurityHashSettings.getHashingAlgorithm())) {
                alg = this.sosSecurityHashSettings.getHashingAlgorithm();
            }

            ByteSource plaintextBytes = createByteSource(s);

            hashService.setHashAlgorithmName(alg);
            hashService.setHashIterations(this.sosSecurityHashSettings.getHashIterations());
            if (sosSecurityHashSettings.getPrivateSalt() != null) {
                hashService.setPrivateSalt(sosSecurityHashSettings.getPrivateSalt());
            }

            hashService.setGeneratePublicSalt(sosSecurityHashSettings.isGeneratePublicSalt());

            HashFormat hashFormat = sosSecurityHashSettings.getFormatObject();

            HashRequest request = createHashRequest(plaintextBytes);
            Hash computed = hashService.computeHash(request);
            return hashFormat.format(computed);

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
        if (s == null) {
            s="";
        }
        String oldUserEntry = oldSection.get(securityConfigurationUser.getUser());
        String oldPwd = "";
        if (oldUserEntry != null) {
            oldPwd = oldUserEntry.split(",")[0];
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
