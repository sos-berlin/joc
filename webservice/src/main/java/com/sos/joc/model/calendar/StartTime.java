
package com.sos.joc.model.calendar;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * estimated time
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "time",
    "estimated"
})
public class StartTime {

    /**
     * hh:mm:ss
     * (Required)
     * 
     */
    @JsonProperty("time")
    private String time;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("estimated")
    private Boolean estimated;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * hh:mm:ss
     * (Required)
     * 
     * @return
     *     The time
     */
    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    /**
     * hh:mm:ss
     * (Required)
     * 
     * @param time
     *     The time
     */
    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The estimated
     */
    @JsonProperty("estimated")
    public Boolean getEstimated() {
        return estimated;
    }

    /**
     * 
     * (Required)
     * 
     * @param estimated
     *     The estimated
     */
    @JsonProperty("estimated")
    public void setEstimated(Boolean estimated) {
        this.estimated = estimated;
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
        return new HashCodeBuilder().append(time).append(estimated).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartTime) == false) {
            return false;
        }
        StartTime rhs = ((StartTime) other);
        return new EqualsBuilder().append(time, rhs.time).append(estimated, rhs.estimated).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
