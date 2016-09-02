
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.OrderQueue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChainNode (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Node__ {

    private String name;
    private State_ state;
    /**
     * job (volatile part)
     * <p>
     * 
     * 
     */
    private Job_ job;
    /**
     * nested job chain (volatile part)
     * <p>
     * 
     * 
     */
    private JobChain___ jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfOrders;
    private List<OrderQueue> orders = new ArrayList<OrderQueue>();

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The state
     */
    public State_ getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(State_ state) {
        this.state = state;
    }

    /**
     * job (volatile part)
     * <p>
     * 
     * 
     * @return
     *     The job
     */
    public Job_ getJob() {
        return job;
    }

    /**
     * job (volatile part)
     * <p>
     * 
     * 
     * @param job
     *     The job
     */
    public void setJob(Job_ job) {
        this.job = job;
    }

    /**
     * nested job chain (volatile part)
     * <p>
     * 
     * 
     * @return
     *     The jobChain
     */
    public JobChain___ getJobChain() {
        return jobChain;
    }

    /**
     * nested job chain (volatile part)
     * <p>
     * 
     * 
     * @param jobChain
     *     The jobChain
     */
    public void setJobChain(JobChain___ jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfOrders
     */
    public Integer getNumOfOrders() {
        return numOfOrders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfOrders
     *     The numOfOrders
     */
    public void setNumOfOrders(Integer numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    /**
     * 
     * @return
     *     The orders
     */
    public List<OrderQueue> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<OrderQueue> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(state).append(job).append(jobChain).append(numOfOrders).append(orders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Node__) == false) {
            return false;
        }
        Node__ rhs = ((Node__) other);
        return new EqualsBuilder().append(name, rhs.name).append(state, rhs.state).append(job, rhs.job).append(jobChain, rhs.jobChain).append(numOfOrders, rhs.numOfOrders).append(orders, rhs.orders).isEquals();
    }

}
