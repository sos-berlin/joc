
package com.sos.joc.model.yade;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum TransferStateText {

    SUCCESSFUL("SUCCESSFUL"),
    FAILED("FAILED"),
    INCOMPLETE("INCOMPLETE");
    private final String value;
    private final static Map<String, TransferStateText> CONSTANTS = new HashMap<String, TransferStateText>();

    static {
        for (TransferStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private TransferStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static TransferStateText fromValue(String value) {
        TransferStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
