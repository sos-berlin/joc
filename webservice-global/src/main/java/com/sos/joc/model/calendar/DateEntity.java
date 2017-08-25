
package com.sos.joc.model.calendar;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum DateEntity {

    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH");
    private final String value;
    private final static Map<String, DateEntity> CONSTANTS = new HashMap<String, DateEntity>();

    static {
        for (DateEntity c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private DateEntity(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static DateEntity fromValue(String value) {
        DateEntity constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
