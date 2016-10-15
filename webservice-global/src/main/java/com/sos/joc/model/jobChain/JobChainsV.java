
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chains with delivery date (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "jobChains",
    "nestedJobChains"
})
public class JobChainsV {

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
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChains")
    private List<JobChainV> jobChains = new ArrayList<JobChainV>();
    @JsonProperty("nestedJobChains")
    private List<JobChainV> nestedJobChains = new ArrayList<JobChainV>();

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
     * 
     * (Required)
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public List<JobChainV> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(List<JobChainV> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The nestedJobChains
     */
    @JsonProperty("nestedJobChains")
    public List<JobChainV> getNestedJobChains() {
        return nestedJobChains;
    }

    /**
     * 
     * @param nestedJobChains
     *     The nestedJobChains
     */
    @JsonProperty("nestedJobChains")
    public void setNestedJobChains(List<JobChainV> nestedJobChains) {
        this.nestedJobChains = nestedJobChains;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobChains).append(nestedJobChains).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainsV) == false) {
            return false;
        }
        JobChainsV rhs = ((JobChainsV) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobChains, rhs.jobChains).append(nestedJobChains, rhs.nestedJobChains).isEquals();
    }

}
