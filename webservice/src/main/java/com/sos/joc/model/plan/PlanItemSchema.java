
package com.sos.joc.model.plan;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * dailyPlanItem
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class PlanItemSchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String job;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String jobChain;
    private String orderId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date plannedStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date expectedEndTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date startTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date endTime;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer historyId;
    /**
     * only for orders
     * 
     */
    private String node;
    private Boolean error = false;
    private State state;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The job
     */
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param job
     *     The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
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
     *     The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId
     *     The orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The plannedStartTime
     */
    public Date getPlannedStartTime() {
        return plannedStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param plannedStartTime
     *     The plannedStartTime
     */
    public void setPlannedStartTime(Date plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The expectedEndTime
     */
    public Date getExpectedEndTime() {
        return expectedEndTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param expectedEndTime
     *     The expectedEndTime
     */
    public void setExpectedEndTime(Date expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param startTime
     *     The startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param endTime
     *     The endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The historyId
     */
    public Integer getHistoryId() {
        return historyId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param historyId
     *     The historyId
     */
    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    /**
     * only for orders
     * 
     * @return
     *     The node
     */
    public String getNode() {
        return node;
    }

    /**
     * only for orders
     * 
     * @param node
     *     The node
     */
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * 
     * @return
     *     The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(jobChain).append(orderId).append(plannedStartTime).append(expectedEndTime).append(startTime).append(endTime).append(historyId).append(node).append(error).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlanItemSchema) == false) {
            return false;
        }
        PlanItemSchema rhs = ((PlanItemSchema) other);
        return new EqualsBuilder().append(job, rhs.job).append(jobChain, rhs.jobChain).append(orderId, rhs.orderId).append(plannedStartTime, rhs.plannedStartTime).append(expectedEndTime, rhs.expectedEndTime).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(historyId, rhs.historyId).append(node, rhs.node).append(error, rhs.error).append(state, rhs.state).isEquals();
    }

}
