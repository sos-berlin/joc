
package com.sos.joc.model.jobChain;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain with delivery date (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobChain200VSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * job chain (volatile part)
     * <p>
     * 
     * (Required)
     * 
     */
    private JobChain__ jobChain;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * job chain (volatile part)
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The jobChain
     */
    public JobChain__ getJobChain() {
        return jobChain;
    }

    /**
     * job chain (volatile part)
     * <p>
     * 
     * (Required)
     * 
     * @param jobChain
     *     The jobChain
     */
    public void setJobChain(JobChain__ jobChain) {
        this.jobChain = jobChain;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobChain).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChain200VSchema) == false) {
            return false;
        }
        JobChain200VSchema rhs = ((JobChain200VSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobChain, rhs.jobChain).isEquals();
    }

}
