package com.sos.joc.xmleditor.common.standard;

import java.net.URI;
import java.util.Date;

import com.sos.joc.xmleditor.common.Utils;

public class ReadConfigurationItem {

    private final URI schema;
    private String configuration;
    private String configurationJson;
    private Date modified;

    public ReadConfigurationItem(URI schemaLocation) {
        schema = schemaLocation;
    }

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
        handleJson();
    }

    private void handleJson() {
        if (configuration != null && configurationJson == null) {
            configurationJson = Utils.xml2json(configuration, schema);
        }
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
