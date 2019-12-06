package com.sos.joc.xmleditor.common.standard;

import java.util.Date;

public class ReadConfigurationItem {

    private String configuration;
    private String configurationJson;
    private Date modified;

    public void set(ReadConfigurationItem other) {
        if (other != null) {
            configuration = other.getConfiguration();
            configurationJson = other.getConfigurationJson();
            modified = other.getModified();
        }
    }

    public void set(String conf, String confJson, Date date) {
        configuration = conf;
        configurationJson = confJson;
        modified = date;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getConfigurationJson() {
        return configurationJson;
    }

    public Date getModified() {
        return modified;
    }

}
