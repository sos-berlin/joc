
package com.sos.joc.model.order;

import java.util.Date;
import javax.annotation.Generated;
import com.sos.joc.model.job.OrderQueue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order with delivery date (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Order200VSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * order (volatile part)
     * <p>
     * 
     * (Required)
     * 
     */
    private OrderQueue order;

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
     * order (volatile part)
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The order
     */
    public OrderQueue getOrder() {
        return order;
    }

    /**
     * order (volatile part)
     * <p>
     * 
     * (Required)
     * 
     * @param order
     *     The order
     */
    public void setOrder(OrderQueue order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(order).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Order200VSchema) == false) {
            return false;
        }
        Order200VSchema rhs = ((Order200VSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(order, rhs.order).isEquals();
    }

}
