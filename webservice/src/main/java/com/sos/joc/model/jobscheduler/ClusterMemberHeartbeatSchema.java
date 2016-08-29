
package com.sos.joc.model.jobscheduler;

import java.util.Date;
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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * cluster member heartbeat
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "count",
    "quality",
    "lastDetected",
    "lastDetectedAge",
    "dead"
})
public class ClusterMemberHeartbeatSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    private Integer count;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("quality")
    private ClusterMemberHeartbeatSchema.Quality quality;
    @JsonProperty("lastDetected")
    private Date lastDetected;
    @JsonProperty("lastDetectedAge")
    private Integer lastDetectedAge;
    @JsonProperty("dead")
    private Boolean dead;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The count
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * (Required)
     * 
     * @param count
     *     The count
     */
    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The quality
     */
    @JsonProperty("quality")
    public ClusterMemberHeartbeatSchema.Quality getQuality() {
        return quality;
    }

    /**
     * 
     * (Required)
     * 
     * @param quality
     *     The quality
     */
    @JsonProperty("quality")
    public void setQuality(ClusterMemberHeartbeatSchema.Quality quality) {
        this.quality = quality;
    }

    /**
     * 
     * @return
     *     The lastDetected
     */
    @JsonProperty("lastDetected")
    public Date getLastDetected() {
        return lastDetected;
    }

    /**
     * 
     * @param lastDetected
     *     The lastDetected
     */
    @JsonProperty("lastDetected")
    public void setLastDetected(Date lastDetected) {
        this.lastDetected = lastDetected;
    }

    /**
     * 
     * @return
     *     The lastDetectedAge
     */
    @JsonProperty("lastDetectedAge")
    public Integer getLastDetectedAge() {
        return lastDetectedAge;
    }

    /**
     * 
     * @param lastDetectedAge
     *     The lastDetectedAge
     */
    @JsonProperty("lastDetectedAge")
    public void setLastDetectedAge(Integer lastDetectedAge) {
        this.lastDetectedAge = lastDetectedAge;
    }

    /**
     * 
     * @return
     *     The dead
     */
    @JsonProperty("dead")
    public Boolean getDead() {
        return dead;
    }

    /**
     * 
     * @param dead
     *     The dead
     */
    @JsonProperty("dead")
    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(count).append(quality).append(lastDetected).append(lastDetectedAge).append(dead).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMemberHeartbeatSchema) == false) {
            return false;
        }
        ClusterMemberHeartbeatSchema rhs = ((ClusterMemberHeartbeatSchema) other);
        return new EqualsBuilder().append(count, rhs.count).append(quality, rhs.quality).append(lastDetected, rhs.lastDetected).append(lastDetectedAge, rhs.lastDetectedAge).append(dead, rhs.dead).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Quality {

        good("good"),
        late("late"),
        still_checking("still_checking");
        private final String value;
        private final static Map<String, ClusterMemberHeartbeatSchema.Quality> CONSTANTS = new HashMap<String, ClusterMemberHeartbeatSchema.Quality>();

        static {
            for (ClusterMemberHeartbeatSchema.Quality c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Quality(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static ClusterMemberHeartbeatSchema.Quality fromValue(String value) {
            ClusterMemberHeartbeatSchema.Quality constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
