
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum TaskStateText {

    NONE("NONE"),
    LOADING("LOADING"),
    STARTING("STARTING"),
    RUNNING("RUNNING"),
    RUNNING_PROCESS("RUNNING_PROCESS"),
    RUNNING_REMOTE_PROCESS("RUNNING_REMOTE_PROCESS"),
    WAITING_FOR_PROCESS("WAITING_FOR_PROCESS"),
    WAITING_FOR_LOCKS("WAITING_FOR_LOCKS"),
    WAITING_FOR_ORDER("WAITING_FOR_ORDER"),
    SUSPENDED("SUSPENDED"),
    ENDING("ENDING"),
    CLOSED("CLOSED");
    private final String value;
    private final static Map<String, TaskStateText> CONSTANTS = new HashMap<String, TaskStateText>();

    static {
        for (TaskStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private TaskStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static TaskStateText fromValue(String value) {
        TaskStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
