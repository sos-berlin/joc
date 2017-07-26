
package com.sos.joc.model.event.custom;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.NameValuePair;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify custom event
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "eventjobChain",
    "orderId",
    "jobChain",
    "job",
    "jobschedulerId",
    "eventClass",
    "eventId",
    "exitCode",
    "expires",
    "expirationPeriod",
    "expirationCycle",
    "params"
})
public class ModifyEvent {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("eventjobChain")
    private String eventjobChain;
    @JsonProperty("orderId")
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    private String job;
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("eventClass")
    private String eventClass;
    @JsonProperty("eventId")
    private String eventId;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    private Integer exitCode;
    @JsonProperty("expires")
    private String expires;
    @JsonProperty("expirationPeriod")
    private String expirationPeriod;
    @JsonProperty("expirationCycle")
    private String expirationCycle;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The eventjobChain
     */
    @JsonProperty("eventjobChain")
    public String getEventjobChain() {
        return eventjobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param eventjobChain
     *     The eventjobChain
     */
    @JsonProperty("eventjobChain")
    public void setEventjobChain(String eventjobChain) {
        this.eventjobChain = eventjobChain;
    }

    /**
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
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
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
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    /**
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
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The eventClass
     */
    @JsonProperty("eventClass")
    public String getEventClass() {
        return eventClass;
    }

    /**
     * 
     * @param eventClass
     *     The eventClass
     */
    @JsonProperty("eventClass")
    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    /**
     * 
     * @return
     *     The eventId
     */
    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
    }

    /**
     * 
     * @param eventId
     *     The eventId
     */
    @JsonProperty("eventId")
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The exitCode
     */
    @JsonProperty("exitCode")
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param exitCode
     *     The exitCode
     */
    @JsonProperty("exitCode")
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * 
     * @return
     *     The expires
     */
    @JsonProperty("expires")
    public String getExpires() {
        return expires;
    }

    /**
     * 
     * @param expires
     *     The expires
     */
    @JsonProperty("expires")
    public void setExpires(String expires) {
        this.expires = expires;
    }

    /**
     * 
     * @return
     *     The expirationPeriod
     */
    @JsonProperty("expirationPeriod")
    public String getExpirationPeriod() {
        return expirationPeriod;
    }

    /**
     * 
     * @param expirationPeriod
     *     The expirationPeriod
     */
    @JsonProperty("expirationPeriod")
    public void setExpirationPeriod(String expirationPeriod) {
        this.expirationPeriod = expirationPeriod;
    }

    /**
     * 
     * @return
     *     The expirationCycle
     */
    @JsonProperty("expirationCycle")
    public String getExpirationCycle() {
        return expirationCycle;
    }

    /**
     * 
     * @param expirationCycle
     *     The expirationCycle
     */
    @JsonProperty("expirationCycle")
    public void setExpirationCycle(String expirationCycle) {
        this.expirationCycle = expirationCycle;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @return
     *     The params
     */
    @JsonProperty("params")
    public List<NameValuePair> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @param params
     *     The params
     */
    @JsonProperty("params")
    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventjobChain).append(orderId).append(jobChain).append(job).append(jobschedulerId).append(eventClass).append(eventId).append(exitCode).append(expires).append(expirationPeriod).append(expirationCycle).append(params).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyEvent) == false) {
            return false;
        }
        ModifyEvent rhs = ((ModifyEvent) other);
        return new EqualsBuilder().append(eventjobChain, rhs.eventjobChain).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(job, rhs.job).append(jobschedulerId, rhs.jobschedulerId).append(eventClass, rhs.eventClass).append(eventId, rhs.eventId).append(exitCode, rhs.exitCode).append(expires, rhs.expires).append(expirationPeriod, rhs.expirationPeriod).append(expirationCycle, rhs.expirationCycle).append(params, rhs.params).isEquals();
    }

}
