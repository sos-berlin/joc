package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

public class SOSSecurityConfigurationUserEntry {

    private String[] values;

    public SOSSecurityConfigurationUserEntry(String entry) {
        super();
        values = entry.split(",");
    }

    public String getPassword() {
        if (values.length > 0) {
            return values[0];
        } else {
            return "";
        }
    }

    public List<String> getRoles() {
        List<String> listOfRoles = new ArrayList<String>();
        for (int i = 1; i < values.length; i++) {
            listOfRoles.add(values[i]);
        }
        return listOfRoles;
    }
}
