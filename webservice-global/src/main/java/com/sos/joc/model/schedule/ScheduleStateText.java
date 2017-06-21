
package com.sos.joc.model.schedule;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ScheduleStateText {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    UNKNOWN("UNKNOWN");
    private final String value;
    private final static Map<String, ScheduleStateText> CONSTANTS = new HashMap<String, ScheduleStateText>();

    static {
        for (ScheduleStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ScheduleStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ScheduleStateText fromValue(String value) {
        ScheduleStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
