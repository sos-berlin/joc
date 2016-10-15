
package com.sos.joc.model.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ConfigurationMime {

    HTML("HTML"),
    XML("XML");
    private final String value;
    private final static Map<String, ConfigurationMime> CONSTANTS = new HashMap<String, ConfigurationMime>();

    static {
        for (ConfigurationMime c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ConfigurationMime(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ConfigurationMime fromValue(String value) {
        ConfigurationMime constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
