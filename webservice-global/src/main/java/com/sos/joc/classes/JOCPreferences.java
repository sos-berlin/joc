package com.sos.joc.classes;

import java.util.prefs.Preferences;

public class JOCPreferences {
    private Preferences prefs;

    public JOCPreferences() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    public void put(String key, String value){
        prefs.node(WebserviceConstants.JOC_COCKPIT).put(key,value);
    }
    public String get(String key, String defaultValue){
        return prefs.node(WebserviceConstants.JOC_COCKPIT).get(key,defaultValue);
    }
}
