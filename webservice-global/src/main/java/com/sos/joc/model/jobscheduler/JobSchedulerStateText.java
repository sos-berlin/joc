
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum JobSchedulerStateText {

    STARTING("STARTING"),
    RUNNING("RUNNING"),
    PAUSED("PAUSED"),
    WAITING_FOR_ACTIVATION("WAITING_FOR_ACTIVATION"),
    TERMINATING("TERMINATING"),
    WAITING_FOR_DATABASE("WAITING_FOR_DATABASE"),
    DEAD("DEAD"),
    UNREACHABLE("UNREACHABLE"),
    UNKNOWN_AGENT("UNKNOWN_AGENT");
    private final String value;
    private final static Map<String, JobSchedulerStateText> CONSTANTS = new HashMap<String, JobSchedulerStateText>();

    static {
        for (JobSchedulerStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private JobSchedulerStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static JobSchedulerStateText fromValue(String value) {
        JobSchedulerStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
