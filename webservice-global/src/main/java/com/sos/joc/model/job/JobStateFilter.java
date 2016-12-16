
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum JobStateFilter {

    PENDING("PENDING"),
    RUNNING("RUNNING"),
    WAITINGFORRESOURCE("WAITINGFORRESOURCE"),
    QUEUED("QUEUED"),
    STOPPED("STOPPED");
    private final String value;
    private final static Map<String, JobStateFilter> CONSTANTS = new HashMap<String, JobStateFilter>();

    static {
        for (JobStateFilter c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private JobStateFilter(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static JobStateFilter fromValue(String value) {
        JobStateFilter constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
