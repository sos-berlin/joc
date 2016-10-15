
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum DBStateText {

    RUNNING("RUNNING"),
    UNREACHABLE("UNREACHABLE");
    private final String value;
    private final static Map<String, DBStateText> CONSTANTS = new HashMap<String, DBStateText>();

    static {
        for (DBStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private DBStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static DBStateText fromValue(String value) {
        DBStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
