
package com.sos.joc.model.order;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationMime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order conf filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "jobChain",
    "orderId",
    "mime"
})
public class OrderConfigurationFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    private String orderId;
    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    private ConfigurationMime mime = ConfigurationMime.fromValue("XML");

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The orderId
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * (Required)
     * 
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     * @return
     *     The mime
     */
    @JsonProperty("mime")
    public ConfigurationMime getMime() {
        return mime;
    }

    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     * @param mime
     *     The mime
     */
    @JsonProperty("mime")
    public void setMime(ConfigurationMime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(jobChain).append(orderId).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderConfigurationFilter) == false) {
            return false;
        }
        OrderConfigurationFilter rhs = ((OrderConfigurationFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobChain, rhs.jobChain).append(orderId, rhs.orderId).append(mime, rhs.mime).isEquals();
    }

}
