
package com.sos.joc.model.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ConfigurationStateText {

    ERROR_IN_CONFIGURATION_FILE("ERROR_IN_CONFIGURATION_FILE"),
    CHANGED_FILE_NOT_LOADED("CHANGED_FILE_NOT_LOADED"),
    REMOVING_DELAYED("REMOVING_DELAYED"),
    RESOURCE_IS_MISSING("RESOURCE_IS_MISSING"),
    REPLACEMENT_IS_STANDING_BY("REPLACEMENT_IS_STANDING_BY"),
    OK("OK");
    private final String value;
    private final static Map<String, ConfigurationStateText> CONSTANTS = new HashMap<String, ConfigurationStateText>();

    static {
        for (ConfigurationStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ConfigurationStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ConfigurationStateText fromValue(String value) {
        ConfigurationStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
