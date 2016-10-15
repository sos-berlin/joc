
package com.sos.joc.model.order;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order with delivray date (permanent part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "order"
})
public class OrderP200 {

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
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded, title is optional
     * (Required)
     * 
     */
    @JsonProperty("order")
    private OrderP order;

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
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded, title is optional
     * (Required)
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    public OrderP getOrder() {
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
    @JsonProperty("order")
    public void setOrder(OrderP order) {
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
        if ((other instanceof OrderP200) == false) {
            return false;
        }
        OrderP200 rhs = ((OrderP200) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(order, rhs.order).isEquals();
    }

}
