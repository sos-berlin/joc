
package com.sos.joc.model.calendar;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum FrequencyMode {

    INCLUDE("INCLUDE"),
    EXCLUDE("EXCLUDE");
    private final String value;
    private final static Map<String, FrequencyMode> CONSTANTS = new HashMap<String, FrequencyMode>();

    static {
        for (FrequencyMode c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private FrequencyMode(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static FrequencyMode fromValue(String value) {
        FrequencyMode constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
