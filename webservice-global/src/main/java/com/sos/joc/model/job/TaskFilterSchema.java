
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task filter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class TaskFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Long taskId;
    private TaskFilterSchema.Mime mime = TaskFilterSchema.Mime.fromValue("PLAIN");

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
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The taskId
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param taskId
     *     The taskId
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * 
     * @return
     *     The mime
     */
    public TaskFilterSchema.Mime getMime() {
        return mime;
    }

    /**
     * 
     * @param mime
     *     The mime
     */
    public void setMime(TaskFilterSchema.Mime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(taskId).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskFilterSchema) == false) {
            return false;
        }
        TaskFilterSchema rhs = ((TaskFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(taskId, rhs.taskId).append(mime, rhs.mime).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Mime {

        PLAIN("PLAIN"),
        HTML("HTML");
        private final String value;
        private final static Map<String, TaskFilterSchema.Mime> CONSTANTS = new HashMap<String, TaskFilterSchema.Mime>();

        static {
            for (TaskFilterSchema.Mime c: values()) {
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

        public static TaskFilterSchema.Mime fromValue(String value) {
            TaskFilterSchema.Mime constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
