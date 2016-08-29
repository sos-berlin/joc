
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum State____ {

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
    private final static Map<String, State____> CONSTANTS = new HashMap<String, State____>();

    static {
        for (State____ c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private State____(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static State____ fromValue(String value) {
        State____ constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
