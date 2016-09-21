
package com.sos.joc.model.order;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Order_ {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String jobChain;
    /**
     * if orderId undefined or empty then all orders of specified job chain are requested
     * 
     */
    private String orderId;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The jobChain
     */
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param jobChain
     *     The jobChain
     */
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * if orderId undefined or empty then all orders of specified job chain are requested
     * 
     * @return
     *     The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * if orderId undefined or empty then all orders of specified job chain are requested
     * 
     * @param orderId
     *     The orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobChain).append(orderId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Order_) == false) {
            return false;
        }
        Order_ rhs = ((Order_) other);
        return new EqualsBuilder().append(jobChain, rhs.jobChain).append(orderId, rhs.orderId).isEquals();
    }

}
