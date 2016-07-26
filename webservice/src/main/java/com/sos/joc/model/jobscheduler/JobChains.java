
package com.sos.joc.model.jobscheduler;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "any",
    "stopped"
})
public class JobChains {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("any")
    private Integer any = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("stopped")
    private Integer stopped = 0;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The any
     */
    @JsonProperty("any")
    public Integer getAny() {
        return any;
    }

    /**
     * 
     * (Required)
     * 
     * @param any
     *     The any
     */
    @JsonProperty("any")
    public void setAny(Integer any) {
        this.any = any;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The stopped
     */
    @JsonProperty("stopped")
    public Integer getStopped() {
        return stopped;
    }

    /**
     * 
     * (Required)
     * 
     * @param stopped
     *     The stopped
     */
    @JsonProperty("stopped")
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
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
        return new HashCodeBuilder().append(any).append(stopped).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChains) == false) {
            return false;
        }
        JobChains rhs = ((JobChains) other);
        return new EqualsBuilder().append(any, rhs.any).append(stopped, rhs.stopped).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
