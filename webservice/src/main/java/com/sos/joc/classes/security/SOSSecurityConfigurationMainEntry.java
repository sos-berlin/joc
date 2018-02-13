package com.sos.joc.classes.security;

import java.util.Map;

public class SOSSecurityConfigurationMainEntry {

    private String removeCharInQuotes(String s, char source, char target) {
        boolean inQuote = false;
        StringBuffer str = new StringBuffer(s);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ('"' == c) {
                inQuote = !inQuote;
            }
            if (inQuote && source == c) {
                str.setCharAt(i, target);
            }

        }
        return str.toString();

    }
    
    public String getIniWriteString(Map.Entry<String, String> entry) {
        if (entry.getKey().contains(".groupRolesMap")) {
            String value = removeCharInQuotes(entry.getValue(), ',', '°');

            String s = "  ";
            String[] map = value.split(",");
            if (map.length > 1) {
                s = "\\" + "\n" + "  ";
            }

            for (int i = 0; i < map.length; i++) {
                s = s + map[i].trim();
                s = removeCharInQuotes(s, '°', ',');
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
