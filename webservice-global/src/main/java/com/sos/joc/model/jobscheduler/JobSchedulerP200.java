
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler with delivry date (permanent part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "jobscheduler"
})
public class JobSchedulerP200 {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * jobscheduler (permanent part)
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobscheduler")
    private JobSchedulerP jobscheduler;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
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
    @JsonProperty("deliveryDate")
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
    @JsonProperty("jobscheduler")
    public JobSchedulerP getJobscheduler() {
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
    @JsonProperty("jobscheduler")
    public void setJobscheduler(JobSchedulerP jobscheduler) {
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
        if ((other instanceof JobSchedulerP200) == false) {
            return false;
        }
        JobSchedulerP200 rhs = ((JobSchedulerP200) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobscheduler, rhs.jobscheduler).isEquals();
    }

}
