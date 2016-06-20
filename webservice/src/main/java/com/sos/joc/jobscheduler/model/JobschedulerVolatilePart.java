
package com.sos.joc.jobscheduler.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * jobscheduler (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "delivery_date",
    "jobscheduler"
})
public class JobschedulerVolatilePart {

    /**
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("delivery_date")
    private Date deliveryDate;
    @JsonProperty("jobscheduler")
    private Jobscheduler jobscheduler;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("delivery_date")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param deliveryDate
     *     The delivery_date
     */
    @JsonProperty("delivery_date")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public JobschedulerVolatilePart withDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    /**
     * 
     * @return
     *     The jobscheduler
     */
    @JsonProperty("jobscheduler")
    public Jobscheduler getJobscheduler() {
        return jobscheduler;
    }

    /**
     * 
     * @param jobscheduler
     *     The jobscheduler
     */
    @JsonProperty("jobscheduler")
    public void setJobscheduler(Jobscheduler jobscheduler) {
        this.jobscheduler = jobscheduler;
    }

    public JobschedulerVolatilePart withJobscheduler(Jobscheduler jobscheduler) {
        this.jobscheduler = jobscheduler;
        return this;
    }

 

}
