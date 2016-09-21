
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.TreeSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * orders with delivery date (permant part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class OrdersPSchema {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    private List<Order> orders = new ArrayList<Order>();
    /**
     * 
     * (Required)
     * 
     */
    private List<TreeSchema> folders = new ArrayList<TreeSchema>();

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
     * 
     * (Required)
     * 
     * @return
     *     The orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * 
     * (Required)
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The folders
     */
    public List<TreeSchema> getFolders() {
        return folders;
    }

    /**
     * 
     * (Required)
     * 
     * @param folders
     *     The folders
     */
    public void setFolders(List<TreeSchema> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(orders).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrdersPSchema) == false) {
            return false;
        }
        OrdersPSchema rhs = ((OrdersPSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(orders, rhs.orders).append(folders, rhs.folders).isEquals();
    }

}
