
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * add order commands
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AddOrdersSchema {

    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    private List<AddOrderSchema> orders = new ArrayList<AddOrderSchema>();

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The orders
     */
    public List<AddOrderSchema> getOrders() {
        return orders;
    }

    /**
     * 
     * (Required)
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<AddOrderSchema> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(orders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AddOrdersSchema) == false) {
            return false;
        }
        AddOrdersSchema rhs = ((AddOrdersSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(orders, rhs.orders).isEquals();
    }

}
