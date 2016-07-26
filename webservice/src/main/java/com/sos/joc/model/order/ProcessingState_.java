
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ProcessingState_ {

    PENDING("pending"),
    RUNNING("running"),
    WAITING_FOR_RESSOURCE("waitingForRessource"),
    SUSPENDED("suspended"),
    SETBACK("setback"),
    BLACKLIST("blacklist");
    private final String value;
    private final static Map<String, ProcessingState_> CONSTANTS = new HashMap<String, ProcessingState_>();

    static {
        for (ProcessingState_ c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ProcessingState_(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ProcessingState_ fromValue(String value) {
        ProcessingState_ constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
