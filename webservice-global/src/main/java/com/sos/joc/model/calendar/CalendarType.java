
package com.sos.joc.model.calendar;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum CalendarType {

    WORKING_DAYS("WORKING_DAYS"),
    NON_WORKING_DAYS("NON_WORKING_DAYS");
    private final String value;
    private final static Map<String, CalendarType> CONSTANTS = new HashMap<String, CalendarType>();

    static {
        for (CalendarType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private CalendarType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static CalendarType fromValue(String value) {
        CalendarType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
