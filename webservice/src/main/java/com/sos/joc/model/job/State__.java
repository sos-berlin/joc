
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum State__ {

    INITIALIZED("initialized"),
    NOT_INITIALIZED("not_initialized"),
    LOADED("loaded"),
    PENDING("pending"),
    RUNNING("running"),
    WAITING_FOR_PROCESS("waiting_for_process"),
    WAITING_FOR_LOCK("waiting_for_lock"),
    WAITING_FOR_AGENT("waiting_for_agent"),
    WAITING_FOR_TASK("waiting_for_task"),
    NOT_IN_PERIOD("not_in_period"),
    STOPPING("stopping"),
    STOPPED("stopped"),
    REMOVED("removed"),
    DISABLED("disabled");
    private final String value;
    private final static Map<String, State__> CONSTANTS = new HashMap<String, State__>();

    static {
        for (State__ c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private State__(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static State__ fromValue(String value) {
        State__ constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
