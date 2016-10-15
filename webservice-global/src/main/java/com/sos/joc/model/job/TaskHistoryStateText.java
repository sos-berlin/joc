
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum TaskHistoryStateText {

    SUCCESSFUL("SUCCESSFUL"),
    INCOMPLETE("INCOMPLETE"),
    FAILED("FAILED");
    private final String value;
    private final static Map<String, TaskHistoryStateText> CONSTANTS = new HashMap<String, TaskHistoryStateText>();

    static {
        for (TaskHistoryStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private TaskHistoryStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static TaskHistoryStateText fromValue(String value) {
        TaskHistoryStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
