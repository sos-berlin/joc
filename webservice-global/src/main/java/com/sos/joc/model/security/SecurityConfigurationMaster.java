
package com.sos.joc.model.security;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "master",
    "roles"
})
public class SecurityConfigurationMaster {

    @JsonProperty("master")
    private String master;
    @JsonProperty("roles")
    private List<SecurityConfigurationRole> roles = new ArrayList<SecurityConfigurationRole>();

    /**
     * 
     * @return
     *     The master
     */
    @JsonProperty("master")
    public String getMaster() {
        return master;
    }

    /**
     * 
     * @param master
     *     The master
     */
    @JsonProperty("master")
    public void setMaster(String master) {
        this.master = master;
    }

    /**
     * 
     * @return
     *     The roles
     */
    @JsonProperty("roles")
    public List<SecurityConfigurationRole> getRoles() {
        return roles;
    }

    /**
     * 
     * @param roles
     *     The roles
     */
    @JsonProperty("roles")
    public void setRoles(List<SecurityConfigurationRole> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(master).append(roles).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SecurityConfigurationMaster) == false) {
            return false;
        }
        SecurityConfigurationMaster rhs = ((SecurityConfigurationMaster) other);
        return new EqualsBuilder().append(master, rhs.master).append(roles, rhs.roles).isEquals();
    }

}
