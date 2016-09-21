
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler with delivry date (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Jobscheduler200PSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * jobscheduler (permanent part)
     * <p>
     * 
     * (Required)
     * 
     */
    private Jobscheduler jobscheduler;

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
     * jobscheduler (permanent part)
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The jobscheduler
     */
    public Jobscheduler getJobscheduler() {
        return jobscheduler;
    }

    /**
     * jobscheduler (permanent part)
     * <p>
     * 
     * (Required)
     * 
     * @param jobscheduler
     *     The jobscheduler
     */
    public void setJobscheduler(Jobscheduler jobscheduler) {
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
        if ((other instanceof Jobscheduler200PSchema) == false) {
            return false;
        }
        Jobscheduler200PSchema rhs = ((Jobscheduler200PSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobscheduler, rhs.jobscheduler).isEquals();
    }

}
