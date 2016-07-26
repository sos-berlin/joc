
package com.sos.joc.model.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Order200PSchema {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded
     * (Required)
     * 
     */
    @JsonProperty("order")
    private Order order;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
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
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded
     * (Required)
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    public Order getOrder() {
        return order;
    }

    /**
     * order (permanent part)
     * <p>
     * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded
     * (Required)
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(order).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(order, rhs.order).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
