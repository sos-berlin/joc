
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


/**
 * security
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "user",
    "accessToken",
    "hasRole",
    "isAuthenticated",
    "isPermitted"
})
public class SecuritySchema {

    @JsonProperty("user")
    private String user;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("hasRole")
    private Boolean hasRole = false;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("isAuthenticated")
    private Boolean isAuthenticated;
    @JsonProperty("isPermitted")
    private Boolean isPermitted = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The user
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The accessToken
     */
    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The accessToken
     */
    @JsonProperty("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 
     * @return
     *     The hasRole
     */
    @JsonProperty("hasRole")
    public Boolean getHasRole() {
        return hasRole;
    }

    /**
     * 
     * @param hasRole
     *     The hasRole
     */
    @JsonProperty("hasRole")
    public void setHasRole(Boolean hasRole) {
        this.hasRole = hasRole;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The isAuthenticated
     */
    @JsonProperty("isAuthenticated")
    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    /**
     * 
     * (Required)
     * 
     * @param isAuthenticated
     *     The isAuthenticated
     */
    @JsonProperty("isAuthenticated")
    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    /**
     * 
     * @return
     *     The isPermitted
     */
    @JsonProperty("isPermitted")
    public Boolean getIsPermitted() {
        return isPermitted;
    }

    /**
     * 
     * @param isPermitted
     *     The isPermitted
     */
    @JsonProperty("isPermitted")
    public void setIsPermitted(Boolean isPermitted) {
        this.isPermitted = isPermitted;
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
        return new HashCodeBuilder().append(user).append(accessToken).append(hasRole).append(isAuthenticated).append(isPermitted).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SecuritySchema) == false) {
            return false;
        }
        SecuritySchema rhs = ((SecuritySchema) other);
        return new EqualsBuilder().append(user, rhs.user).append(accessToken, rhs.accessToken).append(hasRole, rhs.hasRole).append(isAuthenticated, rhs.isAuthenticated).append(isPermitted, rhs.isPermitted).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
