
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum OrderHistoryStateText {

    SUCCESSFUL("SUCCESSFUL"),
    INCOMPLETE("INCOMPLETE"),
    FAILED("FAILED");
    private final String value;
    private final static Map<String, OrderHistoryStateText> CONSTANTS = new HashMap<String, OrderHistoryStateText>();

    static {
        for (OrderHistoryStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private OrderHistoryStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static OrderHistoryStateText fromValue(String value) {
        OrderHistoryStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
