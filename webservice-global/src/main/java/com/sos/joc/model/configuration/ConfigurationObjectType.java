
package com.sos.joc.model.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum ConfigurationObjectType {

    JOB("JOB"),
    JOBCHAIN("JOBCHAIN"),
    ORDER("ORDER"),
    PROCESSCLASS("PROCESSCLASS"),
    AGENTCLUSTER("AGENTCLUSTER"),
    LOCK("LOCK"),
    SCHEDULE("SCHEDULE"),
    FOLDER("FOLDER"),
    JOBSCHEDULER("JOBSCHEDULER"),
    DAILYPLAN("DAILYPLAN"),
    TASK_HISTORY("TASK_HISTORY"),
    ORDER_HISTORY("ORDER_HISTORY");
    private final String value;
    private final static Map<String, ConfigurationObjectType> CONSTANTS = new HashMap<String, ConfigurationObjectType>();

    static {
        for (ConfigurationObjectType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ConfigurationObjectType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ConfigurationObjectType fromValue(String value) {
        ConfigurationObjectType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
