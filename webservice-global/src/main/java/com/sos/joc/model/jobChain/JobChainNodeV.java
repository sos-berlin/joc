
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.order.OrderV;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChainNode (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "state",
    "job",
    "level",
    "jobChain",
    "numOfOrders",
    "orders"
})
public class JobChainNodeV {

    @JsonProperty("name")
    private String name;
    /**
     * jobChainNode state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private JobChainNodeState state;
    /**
     * 
     */
    @JsonProperty("job")
    private JobChainNodeJobV job;
    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    @JsonProperty("level")
    private Integer level;
    /**
     * job chain object is included in nestedJobChains collection
     * 
     */
    @JsonProperty("jobChain")
    private JobChainNodeJobChainV jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfOrders")
    private Integer numOfOrders;
    @JsonProperty("orders")
    private List<OrderV> orders = new ArrayList<OrderV>();

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * jobChainNode state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public JobChainNodeState getState() {
        return state;
    }

    /**
     * jobChainNode state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(JobChainNodeState state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public JobChainNodeJobV getJob() {
        return job;
    }

    /**
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(JobChainNodeJobV job) {
        this.job = job;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     * @return
     *     The level
     */
    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     * @param level
     *     The level
     */
    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * job chain object is included in nestedJobChains collection
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public JobChainNodeJobChainV getJobChain() {
        return jobChain;
    }

    /**
     * job chain object is included in nestedJobChains collection
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(JobChainNodeJobChainV jobChain) {
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
    @JsonProperty("numOfOrders")
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
    @JsonProperty("numOfOrders")
    public void setNumOfOrders(Integer numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    /**
     * 
     * @return
     *     The orders
     */
    @JsonProperty("orders")
    public List<OrderV> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    @JsonProperty("orders")
    public void setOrders(List<OrderV> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(state).append(job).append(level).append(jobChain).append(numOfOrders).append(orders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainNodeV) == false) {
            return false;
        }
        JobChainNodeV rhs = ((JobChainNodeV) other);
        return new EqualsBuilder().append(name, rhs.name).append(state, rhs.state).append(job, rhs.job).append(level, rhs.level).append(jobChain, rhs.jobChain).append(numOfOrders, rhs.numOfOrders).append(orders, rhs.orders).isEquals();
    }

}
