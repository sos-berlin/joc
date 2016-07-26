
package com.sos.joc.model.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "SOSPermissionRole"
})
public class SOSPermissionRoles {

    @JsonProperty("SOSPermissionRole")
    private List<SOSPermissionRole> sOSPermissionRole = new ArrayList<SOSPermissionRole>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The sOSPermissionRole
     */
    @JsonProperty("SOSPermissionRole")
    public List<SOSPermissionRole> getSOSPermissionRole() {
        return sOSPermissionRole;
    }

    /**
     * 
     * @param sOSPermissionRole
     *     The SOSPermissionRole
     */
    @JsonProperty("SOSPermissionRole")
    public void setSOSPermissionRole(List<SOSPermissionRole> sOSPermissionRole) {
        this.sOSPermissionRole = sOSPermissionRole;
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
        return new HashCodeBuilder().append(sOSPermissionRole).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SOSPermissionRoles) == false) {
            return false;
        }
        SOSPermissionRoles rhs = ((SOSPermissionRoles) other);
        return new EqualsBuilder().append(sOSPermissionRole, rhs.sOSPermissionRole).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
