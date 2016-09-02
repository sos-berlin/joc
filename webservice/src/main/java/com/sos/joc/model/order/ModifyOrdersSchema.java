
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify order commands
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ModifyOrdersSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    private List<ModifyOrderSchema> orders = new ArrayList<ModifyOrderSchema>();

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
     * 
     * (Required)
     * 
     * @return
     *     The orders
     */
    public List<ModifyOrderSchema> getOrders() {
        return orders;
    }

    /**
     * 
     * (Required)
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<ModifyOrderSchema> orders) {
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
        if ((other instanceof ModifyOrdersSchema) == false) {
            return false;
        }
        ModifyOrdersSchema rhs = ((ModifyOrdersSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(orders, rhs.orders).isEquals();
    }

}
