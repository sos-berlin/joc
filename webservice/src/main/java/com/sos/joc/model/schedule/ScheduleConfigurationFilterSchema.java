
package com.sos.joc.model.schedule;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * scheduleConfigurationFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ScheduleConfigurationFilterSchema {

    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String schedule;
    private ScheduleConfigurationFilterSchema.Mime mime = ScheduleConfigurationFilterSchema.Mime.fromValue("XML");

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
     *     The schedule
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param schedule
     *     The schedule
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     * 
     * @return
     *     The mime
     */
    public ScheduleConfigurationFilterSchema.Mime getMime() {
        return mime;
    }

    /**
     * 
     * @param mime
     *     The mime
     */
    public void setMime(ScheduleConfigurationFilterSchema.Mime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(schedule).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ScheduleConfigurationFilterSchema) == false) {
            return false;
        }
        ScheduleConfigurationFilterSchema rhs = ((ScheduleConfigurationFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(schedule, rhs.schedule).append(mime, rhs.mime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        HTML("HTML"),
        XML("XML");
        private final String value;
        private final static Map<String, ScheduleConfigurationFilterSchema.Mime> CONSTANTS = new HashMap<String, ScheduleConfigurationFilterSchema.Mime>();

        static {
            for (ScheduleConfigurationFilterSchema.Mime c: values()) {
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

        public static ScheduleConfigurationFilterSchema.Mime fromValue(String value) {
            ScheduleConfigurationFilterSchema.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
