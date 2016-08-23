package com.sos.joc.orders.post.orders;

import java.util.HashMap;
import java.util.Map;

public enum ProcessingState {

    pending("pending"), running("running"), waitingForResource("waitingForResource"), suspended("suspended"), setback("setback"), blacklist("blacklist");
    private final String value;
    private final static Map<String, ProcessingState> CONSTANTS = new HashMap<String, ProcessingState>();

    static {
        for (ProcessingState c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private ProcessingState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static ProcessingState fromValue(String value) {
        ProcessingState constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}