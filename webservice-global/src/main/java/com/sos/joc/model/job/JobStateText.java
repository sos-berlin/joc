
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum JobStateText {

    INITIALIZED("INITIALIZED"),
    NOT_INITIALIZED("NOT_INITIALIZED"),
    LOADED("LOADED"),
    PENDING("PENDING"),
    RUNNING("RUNNING"),
    WAITING_FOR_PROCESS("WAITING_FOR_PROCESS"),
    WAITING_FOR_LOCK("WAITING_FOR_LOCK"),
    WAITING_FOR_AGENT("WAITING_FOR_AGENT"),
    WAITING_FOR_TASK("WAITING_FOR_TASK"),
    NOT_IN_PERIOD("NOT_IN_PERIOD"),
    STOPPING("STOPPING"),
    STOPPED("STOPPED"),
    DISABLED("DISABLED"),
    ERROR("ERROR"),
    UNKNOWN("UNKNOWN");
    private final String value;
    private final static Map<String, JobStateText> CONSTANTS = new HashMap<String, JobStateText>();

    static {
        for (JobStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private JobStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static JobStateText fromValue(String value) {
        JobStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
