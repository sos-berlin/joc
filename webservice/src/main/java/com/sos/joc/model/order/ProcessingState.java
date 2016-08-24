
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ProcessingState {

    pending("pending"),
    running("running"),
    waitingForResource("waitingForResource"),
    suspended("suspended"),
    setback("setback"),
    blacklist("blacklist");
    private final String value;
    private final static Map<String, ProcessingState> CONSTANTS = new HashMap<String, ProcessingState>();

    static {
        for (ProcessingState c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ProcessingState(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ProcessingState fromValue(String value) {
        ProcessingState constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
