
package com.sos.joc.model.order;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order object in grouped history collection
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class GroupedHistorySchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer numOfSuccessful;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer numOfFailed;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * (Required)
     * 
     * @param orderId
     *     The orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The numOfSuccessful
     */
    public Integer getNumOfSuccessful() {
        return numOfSuccessful;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param numOfSuccessful
     *     The numOfSuccessful
     */
    public void setNumOfSuccessful(Integer numOfSuccessful) {
        this.numOfSuccessful = numOfSuccessful;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The numOfFailed
     */
    public Integer getNumOfFailed() {
        return numOfFailed;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param numOfFailed
     *     The numOfFailed
     */
    public void setNumOfFailed(Integer numOfFailed) {
        this.numOfFailed = numOfFailed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(orderId).append(jobChain).append(numOfSuccessful).append(numOfFailed).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GroupedHistorySchema) == false) {
            return false;
        }
        GroupedHistorySchema rhs = ((GroupedHistorySchema) other);
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(numOfSuccessful, rhs.numOfSuccessful).append(numOfFailed, rhs.numOfFailed).isEquals();
    }

}
