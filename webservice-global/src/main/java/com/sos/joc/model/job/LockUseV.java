
package com.sos.joc.model.job;

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
    "available",
    "exclusive"
})
public class LockUseV {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("available")
    private Boolean available;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("exclusive")
    private Boolean exclusive;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The available
     */
    @JsonProperty("available")
    public Boolean getAvailable() {
        return available;
    }

    /**
     * 
     * (Required)
     * 
     * @param available
     *     The available
     */
    @JsonProperty("available")
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The exclusive
     */
    @JsonProperty("exclusive")
    public Boolean getExclusive() {
        return exclusive;
    }

    /**
     * 
     * (Required)
     * 
     * @param exclusive
     *     The exclusive
     */
    @JsonProperty("exclusive")
    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(available).append(exclusive).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LockUseV) == false) {
            return false;
        }
        LockUseV rhs = ((LockUseV) other);
        return new EqualsBuilder().append(path, rhs.path).append(available, rhs.available).append(exclusive, rhs.exclusive).isEquals();
    }

}
