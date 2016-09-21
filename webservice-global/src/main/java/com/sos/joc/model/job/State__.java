
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public enum State__ {

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
    REMOVED("REMOVED"),
    DISABLED("DISABLED");
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

    @Override
    public String toString() {
        return this.value;
    }

    public static State__ fromValue(String value) {
        State__ constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
