
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum Type {

    permanent("permanent"),
    ad_hoc("ad_hoc"),
    file_order("file_order");
    private final String value;
    private final static Map<String, Type> CONSTANTS = new HashMap<String, Type>();

    static {
        for (Type c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Type(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static Type fromValue(String value) {
        Type constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
