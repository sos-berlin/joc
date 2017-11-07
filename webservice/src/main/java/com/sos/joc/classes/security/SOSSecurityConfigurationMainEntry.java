package com.sos.joc.classes.security;

import java.util.Map;

public class SOSSecurityConfigurationMainEntry {

    public String getIniWriteString(Map.Entry<String, String> entry) {
        if (entry.getKey().contains(".groupRolesMap")) {
            String s = "  ";
            String[] map = entry.getValue().split(",");
            if (map.length > 1) {
                s = "\\" + "\n" + " ";
            }

            for (int i = 0; i < map.length; i++) {
                s = s + map[i].trim();
                if (i < map.length - 1) {
                    s = s + ", \\" + "\n" + "  ";
                } else {
                    s = s + "\n";
                }
            }
            return s;
        } else {
            return entry.getValue();
        }
    }
}
