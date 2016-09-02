
package com.sos.joc.model.common;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobScheduler objects filter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobSchedulerObjectFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * collection of JobScheduler object with path and type
     * 
     */
    private List<com.sos.joc.model.common.Object> objects = new ArrayList<com.sos.joc.model.common.Object>();
    /**
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    private String regex;

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
     * collection of JobScheduler object with path and type
     * 
     * @return
     *     The objects
     */
    public List<com.sos.joc.model.common.Object> getObjects() {
        return objects;
    }

    /**
     * collection of JobScheduler object with path and type
     * 
     * @param objects
     *     The objects
     */
    public void setObjects(List<com.sos.joc.model.common.Object> objects) {
        this.objects = objects;
    }

    /**
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @return
     *     The regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @param regex
     *     The regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(objects).append(regex).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobSchedulerObjectFilterSchema) == false) {
            return false;
        }
        JobSchedulerObjectFilterSchema rhs = ((JobSchedulerObjectFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(objects, rhs.objects).append(regex, rhs.regex).isEquals();
    }

}
