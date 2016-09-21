
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task history
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class HistoryItem200Schema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * task in history collection
     * <p>
     * 
     * (Required)
     * 
     */
    private History task;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
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
    public History getTask() {
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
    public void setTask(History task) {
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
        if ((other instanceof HistoryItem200Schema) == false) {
            return false;
        }
        HistoryItem200Schema rhs = ((HistoryItem200Schema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(task, rhs.task).isEquals();
    }

}
