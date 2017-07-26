
package com.sos.joc.model.event.custom;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * check custom event
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "eventClass",
    "eventId",
    "exitCode",
    "xPath"
})
public class CheckEvent {

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
    @JsonProperty("xPath")
    private String xPath;

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
     *     The xPath
     */
    @JsonProperty("xPath")
    public String getXPath() {
        return xPath;
    }

    /**
     * 
     * @param xPath
     *     The xPath
     */
    @JsonProperty("xPath")
    public void setXPath(String xPath) {
        this.xPath = xPath;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(eventClass).append(eventId).append(exitCode).append(xPath).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CheckEvent) == false) {
            return false;
        }
        CheckEvent rhs = ((CheckEvent) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(eventClass, rhs.eventClass).append(eventId, rhs.eventId).append(exitCode, rhs.exitCode).append(xPath, rhs.xPath).isEquals();
    }

}
