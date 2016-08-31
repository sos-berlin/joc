
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum State_____ {

    initialized("initialized"),
    not_initialized("not_initialized"),
    loaded("loaded"),
    pending("pending"),
    running("running"),
    waiting_for_process("waiting_for_process"),
    waiting_for_lock("waiting_for_lock"),
    waiting_for_agent("waiting_for_agent"),
    waiting_for_task("waiting_for_task"),
    not_in_period("not_in_period"),
    stopping("stopping"),
    stopped("stopped"),
    removed("removed"),
    disabled("disabled");
    private final String value;
    private final static Map<String, State_____> CONSTANTS = new HashMap<String, State_____>();

    static {
        for (State_____ c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private State_____(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static State_____ fromValue(String value) {
        State_____ constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
