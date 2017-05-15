
package com.sos.joc.model.security;

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
    "path",
    "excluded"
})
public class SecurityConfigurationPermission {

    @JsonProperty("path")
    private Object path;
    @JsonProperty("excluded")
    private Object excluded;

    /**
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public Object getPath() {
        return path;
    }

    /**
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(Object path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The excluded
     */
    @JsonProperty("excluded")
    public Object getExcluded() {
        return excluded;
    }

    /**
     * 
     * @param excluded
     *     The excluded
     */
    @JsonProperty("excluded")
    public void setExcluded(Object excluded) {
        this.excluded = excluded;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(excluded).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SecurityConfigurationPermission) == false) {
            return false;
        }
        SecurityConfigurationPermission rhs = ((SecurityConfigurationPermission) other);
        return new EqualsBuilder().append(path, rhs.path).append(excluded, rhs.excluded).isEquals();
    }

}
