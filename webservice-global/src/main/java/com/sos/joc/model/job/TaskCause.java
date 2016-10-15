
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum TaskCause {

    NONE("NONE"),
    MIN_TASKS("MIN_TASKS"),
    PERIOD_ONCE("PERIOD_ONCE"),
    PERIOD_SINGLE("PERIOD_SINGLE"),
    PERIOD_REPEAT("PERIOD_REPEAT"),
    QUEUE("QUEUE"),
    QUEUE_AT("QUEUE_AT"),
    DIRECTORY("DIRECTORY"),
    DELAY_AFTER_ERROR("DELAY_AFTER_ERROR"),
    ORDER("ORDER");
    private final String value;
    private final static Map<String, TaskCause> CONSTANTS = new HashMap<String, TaskCause>();

    static {
        for (TaskCause c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private TaskCause(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static TaskCause fromValue(String value) {
        TaskCause constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
