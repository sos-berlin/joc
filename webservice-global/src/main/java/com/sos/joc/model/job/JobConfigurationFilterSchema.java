
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobConfigurationFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobConfigurationFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String job;
    private JobConfigurationFilterSchema.Mime mime = JobConfigurationFilterSchema.Mime.fromValue("XML");

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
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
     *     The job
     */
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param job
     *     The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The mime
     */
    public JobConfigurationFilterSchema.Mime getMime() {
        return mime;
    }

    /**
     * 
     * @param mime
     *     The mime
     */
    public void setMime(JobConfigurationFilterSchema.Mime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(job).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobConfigurationFilterSchema) == false) {
            return false;
        }
        JobConfigurationFilterSchema rhs = ((JobConfigurationFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(mime, rhs.mime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        HTML("HTML"),
        XML("XML");
        private final String value;
        private final static Map<String, JobConfigurationFilterSchema.Mime> CONSTANTS = new HashMap<String, JobConfigurationFilterSchema.Mime>();

        static {
            for (JobConfigurationFilterSchema.Mime c: values()) {
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

        public static JobConfigurationFilterSchema.Mime fromValue(String value) {
            JobConfigurationFilterSchema.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
