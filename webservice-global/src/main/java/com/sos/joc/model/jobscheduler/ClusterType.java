
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ClusterType {

    STANDALONE("standalone"),
    ACTIVE("active"),
    PASSIVE("passive");
    private final String value;
    private final static Map<String, ClusterType> CONSTANTS = new HashMap<String, ClusterType>();

    static {
        for (ClusterType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ClusterType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ClusterType fromValue(String value) {
        ClusterType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
