
package com.sos.joc.model.user;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum SOSPermissionRole {

    APPLICATION_MANAGER("application_manager"),
    IT_OPERATOR("it_operator"),
    INCIDENT_MANAGER("incident_manager"),
    BUSINESS_USER("business_user");
    private final String value;
    private final static Map<String, SOSPermissionRole> CONSTANTS = new HashMap<String, SOSPermissionRole>();

    static {
        for (SOSPermissionRole c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private SOSPermissionRole(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static SOSPermissionRole fromValue(String value) {
        SOSPermissionRole constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
