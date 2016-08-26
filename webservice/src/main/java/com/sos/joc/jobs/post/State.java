package com.sos.joc.jobs.post;

import java.util.HashMap;
import java.util.Map;

public enum State {

    initialized("initialized"), not_initialized("not_initialized"), loaded("loaded"), pending("pending"), running("running"), waiting_for_process(
            "waiting_for_process"), waiting_for_lock("waiting_for_lock"), waiting_for_agent("waiting_for_agent"), waiting_for_task("waiting_for_task"), not_in_period(
                    "not_in_period"), stopping("stopping"), stopped("stopped"), removed("removed"), disabled("disabled");
    private final String value;
    private final static Map<String, State> CONSTANTS = new HashMap<String, State>();

    static {
        for (State c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private State(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static State fromValue(String value) {
        State constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
