
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task history
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "task"
})
public class TaskHistoryItem200 {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * task in history collection
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("task")
    private TaskHistoryItem task;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * task in history collection
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The task
     */
    @JsonProperty("task")
    public TaskHistoryItem getTask() {
        return task;
    }

    /**
     * task in history collection
     * <p>
     * 
     * (Required)
     * 
     * @param task
     *     The task
     */
    @JsonProperty("task")
    public void setTask(TaskHistoryItem task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(task).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskHistoryItem200) == false) {
            return false;
        }
        TaskHistoryItem200 rhs = ((TaskHistoryItem200) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(task, rhs.task).isEquals();
    }

}