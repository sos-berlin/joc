
package com.sos.joc.model.plan;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public enum State_ {

    SUCCESSFUL("SUCCESSFUL"),
    INCOMPLETE("INCOMPLETE"),
    FAILED("FAILED"),
    PLANNED("PLANNED");
    private final String value;
    private final static Map<String, State_> CONSTANTS = new HashMap<String, State_>();

    static {
        for (State_ c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private State_(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static State_ fromValue(String value) {
        State_ constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
