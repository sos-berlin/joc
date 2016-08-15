package com.sos.joc.orders.post;

import java.util.HashMap;
import java.util.Map;

public enum Type {

    PERMANENT("permanent"), AD_HOC("ad_hoc"), FILE_ORDER("file_order");
    private final String value;
    private final static Map<String, Type> CONSTANTS = new HashMap<String, Type>();

    static {
        for (Type c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Type(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Type fromValue(String value) {
        Type constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}