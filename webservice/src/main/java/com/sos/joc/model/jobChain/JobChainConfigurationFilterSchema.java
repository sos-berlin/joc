
package com.sos.joc.model.jobChain;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChainConfigurationFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobChainConfigurationFilterSchema {

    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String jobChain;
    /**
     * The configuration can have a HTML representation where the XML gets a highlighting via CSS classes.
     * 
     */
    private JobChainConfigurationFilterSchema.Mime mime = JobChainConfigurationFilterSchema.Mime.fromValue("XML");

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
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
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * The configuration can have a HTML representation where the XML gets a highlighting via CSS classes.
     * 
     * @return
     *     The mime
     */
    public JobChainConfigurationFilterSchema.Mime getMime() {
        return mime;
    }

    /**
     * The configuration can have a HTML representation where the XML gets a highlighting via CSS classes.
     * 
     * @param mime
     *     The mime
     */
    public void setMime(JobChainConfigurationFilterSchema.Mime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(jobChain).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainConfigurationFilterSchema) == false) {
            return false;
        }
        JobChainConfigurationFilterSchema rhs = ((JobChainConfigurationFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobChain, rhs.jobChain).append(mime, rhs.mime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        XML("XML"),
        HTML("HTML");
        private final String value;
        private final static Map<String, JobChainConfigurationFilterSchema.Mime> CONSTANTS = new HashMap<String, JobChainConfigurationFilterSchema.Mime>();

        static {
            for (JobChainConfigurationFilterSchema.Mime c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Mime(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static JobChainConfigurationFilterSchema.Mime fromValue(String value) {
            JobChainConfigurationFilterSchema.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
