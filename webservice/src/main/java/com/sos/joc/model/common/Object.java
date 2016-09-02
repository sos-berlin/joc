
package com.sos.joc.model.common;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Object {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    private com.sos.joc.model.common.Configuration.Type type;

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
     * JobScheduler object type
     * <p>
     * 
     * 
     * @return
     *     The type
     */
    public com.sos.joc.model.common.Configuration.Type getType() {
        return type;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @param type
     *     The type
     */
    public void setType(com.sos.joc.model.common.Configuration.Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(type).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Object) == false) {
            return false;
        }
        Object rhs = ((Object) other);
        return new EqualsBuilder().append(path, rhs.path).append(type, rhs.type).isEquals();
    }

}
