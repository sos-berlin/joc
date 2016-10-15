
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum OrderStateText {

    PENDING("PENDING"),
    RUNNING("RUNNING"),
    SUSPENDED("SUSPENDED"),
    SETBACK("SETBACK"),
    BLACKLIST("BLACKLIST"),
    JOB_NOT_IN_PERIOD("JOB_NOT_IN_PERIOD"),
    NODE_DELAY("NODE_DELAY"),
    WAITING_FOR_LOCK("WAITING_FOR_LOCK"),
    WAITING_FOR_PROCESS("WAITING_FOR_PROCESS"),
    WAITING_FOR_AGENT("WAITING_FOR_AGENT"),
    JOB_CHAIN_STOPPED("JOB_CHAIN_STOPPED"),
    NODE_STOPPED("NODE_STOPPED"),
    JOB_STOPPED("JOB_STOPPED"),
    WAITING_FOR_TASK("WAITING_FOR_TASK");
    private final String value;
    private final static Map<String, OrderStateText> CONSTANTS = new HashMap<String, OrderStateText>();

    static {
        for (OrderStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private OrderStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static OrderStateText fromValue(String value) {
        OrderStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
