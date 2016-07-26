
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "state",
    "job",
    "jobChain",
    "numOfOrders",
    "orders"
})
public class Node__ {

    @JsonProperty("name")
    private String name;
    @JsonProperty("state")
    private State_ state;
    /**
     * job (volatile part)
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    private Job_ job;
    /**
     * nested job chain (volatile part)
     * <p>
     * 
     * 
     */
    @JsonProperty("jobChain")
    private JobChain___ jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfOrders")
    private Integer numOfOrders;
    @JsonProperty("orders")
    private List<OrderQueue> orders = new ArrayList<OrderQueue>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public State_ getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
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
    @JsonProperty("job")
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
    @JsonProperty("job")
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
    @JsonProperty("jobChain")
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
    @JsonProperty("jobChain")
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
    public List<OrderQueue> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    @JsonProperty("orders")
    public void setOrders(List<OrderQueue> orders) {
        this.orders = orders;
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
        return new HashCodeBuilder().append(name).append(state).append(job).append(jobChain).append(numOfOrders).append(orders).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(name, rhs.name).append(state, rhs.state).append(job, rhs.job).append(jobChain, rhs.jobChain).append(numOfOrders, rhs.numOfOrders).append(orders, rhs.orders).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
