
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
    "role",
    "permissions",
    "folders"
})
public class SecurityConfigurationRole {

    @JsonProperty("role")
    private String role;
    @JsonProperty("permissions")
    private List<SecurityConfigurationPermission> permissions = new ArrayList<SecurityConfigurationPermission>();
    @JsonProperty("folders")
    private List<SecurityConfigurationFolder> folders = new ArrayList<SecurityConfigurationFolder>();

    /**
     * 
     * @return
     *     The role
     */
    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    /**
     * 
     * @param role
     *     The role
     */
    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 
     * @return
     *     The permissions
     */
    @JsonProperty("permissions")
    public List<SecurityConfigurationPermission> getPermissions() {
        return permissions;
    }

    /**
     * 
     * @param permissions
     *     The permissions
     */
    @JsonProperty("permissions")
    public void setPermissions(List<SecurityConfigurationPermission> permissions) {
        this.permissions = permissions;
    }

    /**
     * 
     * @return
     *     The folders
     */
    @JsonProperty("folders")
    public List<SecurityConfigurationFolder> getFolders() {
        return folders;
    }

    /**
     * 
     * @param folders
     *     The folders
     */
    @JsonProperty("folders")
    public void setFolders(List<SecurityConfigurationFolder> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(role).append(permissions).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SecurityConfigurationRole) == false) {
            return false;
        }
        SecurityConfigurationRole rhs = ((SecurityConfigurationRole) other);
        return new EqualsBuilder().append(role, rhs.role).append(permissions, rhs.permissions).append(folders, rhs.folders).isEquals();
    }

}
