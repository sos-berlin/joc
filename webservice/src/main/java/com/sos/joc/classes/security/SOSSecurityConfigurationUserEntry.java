package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.model.security.SecurityConfigurationUser;

public class SOSSecurityConfigurationUserEntry {

    private String[] listOfUsers;
    SecurityConfigurationUser securityConfigurationUser;

    public SOSSecurityConfigurationUserEntry(String entry) {
        super();
        listOfUsers = entry.split(",");
    }

    public SOSSecurityConfigurationUserEntry(SecurityConfigurationUser securityConfigurationUser) {
        super();
        this.securityConfigurationUser = securityConfigurationUser;
    }

    public String getPassword() {
        if (listOfUsers.length > 0) {
            return listOfUsers[0];
        } else {
            return "";
        }
    }

    public List<String> getRoles() {
        List<String> listOfRoles = new ArrayList<String>();
        for (int i = 1; i < listOfUsers.length; i++) {
            listOfRoles.add(listOfUsers[i]);
        }
        return listOfRoles;
    }

    public String getIniWriteString() {
        String s = "";
        for (int i=0;i<securityConfigurationUser.getRoles().size();i++){
            s = s + securityConfigurationUser.getRoles().get(i) + ",";
        }
        s = s.substring(0,s.length());
       return s;
    }
}
