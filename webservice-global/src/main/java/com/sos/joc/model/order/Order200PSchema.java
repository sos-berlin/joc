
package com.sos.joc.model.order;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order with delivray date (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Order200PSchema {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded, title is optional
     * (Required)
     * 
     */
    private Order order;

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded, title is optional
     * (Required)
     * 
     * @return
     *     The order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded, title is optional
     * (Required)
     * 
     * @param order
     *     The order
     */
    public void setOrder(Order order) {
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
        if ((other instanceof Order200PSchema) == false) {
            return false;
        }
        Order200PSchema rhs = ((Order200PSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(order, rhs.order).isEquals();
    }

}
