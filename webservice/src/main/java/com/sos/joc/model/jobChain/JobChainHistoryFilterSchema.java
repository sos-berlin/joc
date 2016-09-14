
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChainFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobChainHistoryFilterSchema {

    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String jobChain;
    private List<String> orders = new ArrayList<String>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer maxLastHistoryItems;

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
     * 
     * @return
     *     The orders
     */
    public List<String> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxLastHistoryItems
     */
    public Integer getMaxLastHistoryItems() {
        return maxLastHistoryItems;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxLastHistoryItems
     *     The maxLastHistoryItems
     */
    public void setMaxLastHistoryItems(Integer maxLastHistoryItems) {
        this.maxLastHistoryItems = maxLastHistoryItems;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(jobChain).append(orders).append(maxLastHistoryItems).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainHistoryFilterSchema) == false) {
            return false;
        }
        JobChainHistoryFilterSchema rhs = ((JobChainHistoryFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobChain, rhs.jobChain).append(orders, rhs.orders).append(maxLastHistoryItems, rhs.maxLastHistoryItems).isEquals();
    }

}
