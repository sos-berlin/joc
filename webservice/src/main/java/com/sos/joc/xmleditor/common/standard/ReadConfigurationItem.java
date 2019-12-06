package com.sos.joc.xmleditor.common.standard;

import java.net.URI;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.xmleditor.common.Xml2JsonConverter;

public class ReadConfigurationItem {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadConfigurationItem.class);

    private final URI schema;
    private final ObjectType type;
    private String configuration;
    private String configurationJson;
    private boolean recreateJson;
    private Date modified;

    public ReadConfigurationItem(ObjectType objectType, URI schemaLocation) {
        type = objectType;
        schema = schemaLocation;
    }

    public void set(ReadConfigurationItem other) {
        if (other != null) {
            configuration = other.getConfiguration();
            configurationJson = other.getConfigurationJson();
            recreateJson = other.recreateJson();
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
            try {
                Xml2JsonConverter converter = new Xml2JsonConverter();
                configurationJson = converter.convert(type, schema, configuration);
                recreateJson = true;
            } catch (Exception ex) {
                LOGGER.error(ex.toString(), ex);
            }
        }
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getConfigurationJson() {
        return configurationJson;
    }

    public boolean recreateJson() {
        return recreateJson;
    }

    public Date getModified() {
        return modified;
    }

}
