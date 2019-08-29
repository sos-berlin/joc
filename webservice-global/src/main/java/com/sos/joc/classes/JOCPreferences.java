package com.sos.joc.classes;

import java.util.prefs.Preferences;

public class JOCPreferences {
    private Preferences prefs;
    private String userName;

    public JOCPreferences(String userName) {
        prefs = Preferences.userNodeForPackage(this.getClass());
        this.userName = userName;
    }

    public void put(String key, String value){
        prefs.node(WebserviceConstants.JOC_COCKPIT).put(getKey(key),value);
        prefs.node(WebserviceConstants.JOC_COCKPIT).put(getKey(key).toLowerCase(),value);
    }
    public String get(String key, String defaultValue){
        String pref = prefs.node(WebserviceConstants.JOC_COCKPIT).get(getKey(key), null);
        if (pref == null) {
            pref = prefs.node(WebserviceConstants.JOC_COCKPIT).get(getKey(key).toLowerCase(), defaultValue);
        }
        return pref;
    }
    
    private String getKey(String key){
        return key + ":" + userName;
    }
}
