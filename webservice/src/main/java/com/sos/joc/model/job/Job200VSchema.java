
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job with delivery date (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Job200VSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date deliveryDate;
    /**
     * job (volatile part)
     * <p>
     * 
     * 
     */
    private Job_ job;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * job (volatile part)
     * <p>
     * 
     * 
     * @return
     *     The job
     */
    public Job_ getJob() {
        return job;
    }

    /**
     * job (volatile part)
     * <p>
     * 
     * 
     * @param job
     *     The job
     */
    public void setJob(Job_ job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job200VSchema) == false) {
            return false;
        }
        Job200VSchema rhs = ((Job200VSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(job, rhs.job).isEquals();
    }

}
