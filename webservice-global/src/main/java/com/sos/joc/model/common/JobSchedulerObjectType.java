
package com.sos.joc.model.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum JobSchedulerObjectType {

    JOB("JOB"),
    JOBCHAIN("JOBCHAIN"),
    ORDER("ORDER"),
    PROCESSCLASS("PROCESSCLASS"),
    AGENTCLUSTER("AGENTCLUSTER"),
    LOCK("LOCK"),
    SCHEDULE("SCHEDULE"),
    CALENDAR("CALENDAR"),
    FOLDER("FOLDER"),
    JOBSCHEDULER("JOBSCHEDULER"),
    OTHER("OTHER");
    private final String value;
    private final static Map<String, JobSchedulerObjectType> CONSTANTS = new HashMap<String, JobSchedulerObjectType>();

    static {
        for (JobSchedulerObjectType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private JobSchedulerObjectType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static JobSchedulerObjectType fromValue(String value) {
        JobSchedulerObjectType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
