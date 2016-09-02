
package com.sos.joc.model.event;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * register, unregister or collect event
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class RegisterObjectSchema {

    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    private com.sos.joc.model.event.Object object;

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The object
     */
    public com.sos.joc.model.event.Object getObject() {
        return object;
    }

    /**
     * 
     * (Required)
     * 
     * @param object
     *     The object
     */
    public void setObject(com.sos.joc.model.event.Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(object).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RegisterObjectSchema) == false) {
            return false;
        }
        RegisterObjectSchema rhs = ((RegisterObjectSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(object, rhs.object).isEquals();
    }

}
