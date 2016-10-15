
package com.sos.joc.model.lock;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationMime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * lockConfigurationFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "lock",
    "mime"
})
public class LockConfigurationFilter {

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
    @JsonProperty("lock")
    private String lock;
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
     *     The lock
     */
    @JsonProperty("lock")
    public String getLock() {
        return lock;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param lock
     *     The lock
     */
    @JsonProperty("lock")
    public void setLock(String lock) {
        this.lock = lock;
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
        return new HashCodeBuilder().append(jobschedulerId).append(lock).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LockConfigurationFilter) == false) {
            return false;
        }
        LockConfigurationFilter rhs = ((LockConfigurationFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(lock, rhs.lock).append(mime, rhs.mime).isEquals();
    }

}
