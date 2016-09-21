
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler with delivry date (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Jobscheduler200VSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * jobscheduler (volatile part)
     * <p>
     * 
     * (Required)
     * 
     */
    private Jobscheduler_ jobscheduler;

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
     * jobscheduler (volatile part)
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The jobscheduler
     */
    public Jobscheduler_ getJobscheduler() {
        return jobscheduler;
    }

    /**
     * jobscheduler (volatile part)
     * <p>
     * 
     * (Required)
     * 
     * @param jobscheduler
     *     The jobscheduler
     */
    public void setJobscheduler(Jobscheduler_ jobscheduler) {
        this.jobscheduler = jobscheduler;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobscheduler).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jobscheduler200VSchema) == false) {
            return false;
        }
        Jobscheduler200VSchema rhs = ((Jobscheduler200VSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobscheduler, rhs.jobscheduler).isEquals();
    }

}
