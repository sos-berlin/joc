
package com.sos.joc.model.lock;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * lockUse
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class LockUseSchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    private Boolean available;
    /**
     * 
     * (Required)
     * 
     */
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
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The available
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * 
     * @param available
     *     The available
     */
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
        if ((other instanceof LockUseSchema) == false) {
            return false;
        }
        LockUseSchema rhs = ((LockUseSchema) other);
        return new EqualsBuilder().append(path, rhs.path).append(available, rhs.available).append(exclusive, rhs.exclusive).isEquals();
    }

}
