
package com.sos.joc.model.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ConfigurationType {

    CUSTOMIZATION("CUSTOMIZATION"),
    IGNORELIST("IGNORELIST"),
    PROFILE("PROFILE"),
    SETTING("SETTING");
    private final String value;
    private final static Map<String, ConfigurationType> CONSTANTS = new HashMap<String, ConfigurationType>();

    static {
        for (ConfigurationType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ConfigurationType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ConfigurationType fromValue(String value) {
        ConfigurationType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
