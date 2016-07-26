
package com.sos.joc.model.jobChain;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum State____ {

    ACTIVE("active"),
    STOPPED("stopped");
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
