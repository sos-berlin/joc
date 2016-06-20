
package com.sos.joc.jobscheduler.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "type",
    "precedence"
})
public class ClusterType {

    /**
     * JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * 
     */
    @JsonProperty("type")
    private ClusterType.Type type;
    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     */
    @JsonProperty("precedence")
    private Integer precedence;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public ClusterType.Type getType() {
        return type;
    }

    /**
     * JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(ClusterType.Type type) {
        this.type = type;
    }

    public ClusterType withType(ClusterType.Type type) {
        this.type = type;
        return this;
    }

    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     * @return
     *     The precedence
     */
    @JsonProperty("precedence")
    public Integer getPrecedence() {
        return precedence;
    }

    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     * @param precedence
     *     The precedence
     */
    @JsonProperty("precedence")
    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public ClusterType withPrecedence(Integer precedence) {
        this.precedence = precedence;
        return this;
    }

  

    @Generated("org.jsonschema2pojo")
    public static enum Type {

        ACTIVE("active"),
        PASSIVE("passive"),
        STANDALONE("standalone");
        private final String value;
        private static Map<String, ClusterType.Type> constants = new HashMap<String, ClusterType.Type>();

        static {
            for (ClusterType.Type c: values()) {
                constants.put(c.value, c);
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
        public static ClusterType.Type fromValue(String value) {
            ClusterType.Type constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
