
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum OrderType {

    PERMANENT("PERMANENT"),
    AD_HOC("AD_HOC"),
    FILE_ORDER("FILE_ORDER");
    private final String value;
    private final static Map<String, OrderType> CONSTANTS = new HashMap<String, OrderType>();

    static {
        for (OrderType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private OrderType(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static OrderType fromValue(String value) {
        OrderType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
