
package com.sos.joc.model.jobChain;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain with delivery date (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobChain200PSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * job chain (permanent part)
     * <p>
     * 
     * (Required)
     * 
     */
    private JobChain jobChain;

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
     * job chain (permanent part)
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The jobChain
     */
    public JobChain getJobChain() {
        return jobChain;
    }

    /**
     * job chain (permanent part)
     * <p>
     * 
     * (Required)
     * 
     * @param jobChain
     *     The jobChain
     */
    public void setJobChain(JobChain jobChain) {
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
        if ((other instanceof JobChain200PSchema) == false) {
            return false;
        }
        JobChain200PSchema rhs = ((JobChain200PSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobChain, rhs.jobChain).isEquals();
    }

}
