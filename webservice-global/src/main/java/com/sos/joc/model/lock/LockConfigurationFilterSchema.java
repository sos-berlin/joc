
package com.sos.joc.model.lock;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * lockConfigurationFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class LockConfigurationFilterSchema {

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
    private String lock;
    private LockConfigurationFilterSchema.Mime mime = LockConfigurationFilterSchema.Mime.fromValue("XML");

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
     *     The lock
     */
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
    public void setLock(String lock) {
        this.lock = lock;
    }

    /**
     * 
     * @return
     *     The mime
     */
    public LockConfigurationFilterSchema.Mime getMime() {
        return mime;
    }

    /**
     * 
     * @param mime
     *     The mime
     */
    public void setMime(LockConfigurationFilterSchema.Mime mime) {
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
        if ((other instanceof LockConfigurationFilterSchema) == false) {
            return false;
        }
        LockConfigurationFilterSchema rhs = ((LockConfigurationFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(lock, rhs.lock).append(mime, rhs.mime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        HTML("HTML"),
        XML("XML");
        private final String value;
        private final static Map<String, LockConfigurationFilterSchema.Mime> CONSTANTS = new HashMap<String, LockConfigurationFilterSchema.Mime>();

        static {
            for (LockConfigurationFilterSchema.Mime c: values()) {
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

        public static LockConfigurationFilterSchema.Mime fromValue(String value) {
            LockConfigurationFilterSchema.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
