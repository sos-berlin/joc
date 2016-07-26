
package com.sos.joc.model.user;

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
    "clusterStatus"
})
public class View_ {

    @JsonProperty("clusterStatus")
    private Boolean clusterStatus = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The clusterStatus
     */
    @JsonProperty("clusterStatus")
    public Boolean getClusterStatus() {
        return clusterStatus;
    }

    /**
     * 
     * @param clusterStatus
     *     The clusterStatus
     */
    @JsonProperty("clusterStatus")
    public void setClusterStatus(Boolean clusterStatus) {
        this.clusterStatus = clusterStatus;
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
        return new HashCodeBuilder().append(clusterStatus).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof View_) == false) {
            return false;
        }
        View_ rhs = ((View_) other);
        return new EqualsBuilder().append(clusterStatus, rhs.clusterStatus).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
