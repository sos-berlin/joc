
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Err419;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * start task response
 * <p>
 * if ok=true then tasks collection is required
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "tasks",
    "ok",
    "errors"
})
public class StartedTasks {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    @JsonProperty("tasks")
    private List<TaskPath200> tasks = new ArrayList<TaskPath200>();
    @JsonProperty("ok")
    private Boolean ok;
    @JsonProperty("errors")
    private List<Err419> errors = new ArrayList<Err419>();

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
     * 
     * @return
     *     The tasks
     */
    @JsonProperty("tasks")
    public List<TaskPath200> getTasks() {
        return tasks;
    }

    /**
     * 
     * @param tasks
     *     The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(List<TaskPath200> tasks) {
        this.tasks = tasks;
    }

    /**
     * 
     * @return
     *     The ok
     */
    @JsonProperty("ok")
    public Boolean getOk() {
        return ok;
    }

    /**
     * 
     * @param ok
     *     The ok
     */
    @JsonProperty("ok")
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    /**
     * 
     * @return
     *     The errors
     */
    @JsonProperty("errors")
    public List<Err419> getErrors() {
        return errors;
    }

    /**
     * 
     * @param errors
     *     The errors
     */
    @JsonProperty("errors")
    public void setErrors(List<Err419> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(tasks).append(ok).append(errors).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartedTasks) == false) {
            return false;
        }
        StartedTasks rhs = ((StartedTasks) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(tasks, rhs.tasks).append(ok, rhs.ok).append(errors, rhs.errors).isEquals();
    }

}
