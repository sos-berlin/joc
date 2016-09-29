
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.NameValuePairsSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify order command
 * <p>
 * NOTE: orderId is required too except for add order
 * 
 */
@Generated("org.jsonschema2pojo")
public class ModifyOrderSchema {

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
     * the name of the node
     * 
     */
    private String state;
    /**
     * the name of the end node
     * 
     */
    private String endState;
    /**
     * Field to comment this action which can be logged.
     * 
     */
    private String comment;
    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     */
    private String at;
    /**
     * only useful when changing order state of suspended orders
     * 
     */
    private Boolean resume;
    /**
     * only useful when order has a setback
     * 
     */
    private Boolean removeSetback;
    private String title;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer priority;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    private List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     */
    private String runTime;

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
     * the name of the node
     * 
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     * the name of the node
     * 
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * the name of the end node
     * 
     * @return
     *     The endState
     */
    public String getEndState() {
        return endState;
    }

    /**
     * the name of the end node
     * 
     * @param endState
     *     The endState
     */
    public void setEndState(String endState) {
        this.endState = endState;
    }

    /**
     * Field to comment this action which can be logged.
     * 
     * @return
     *     The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Field to comment this action which can be logged.
     * 
     * @param comment
     *     The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     * @return
     *     The at
     */
    public String getAt() {
        return at;
    }

    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     * @param at
     *     The at
     */
    public void setAt(String at) {
        this.at = at;
    }

    /**
     * only useful when changing order state of suspended orders
     * 
     * @return
     *     The resume
     */
    public Boolean getResume() {
        return resume;
    }

    /**
     * only useful when changing order state of suspended orders
     * 
     * @param resume
     *     The resume
     */
    public void setResume(Boolean resume) {
        this.resume = resume;
    }

    /**
     * only useful when order has a setback
     * 
     * @return
     *     The removeSetback
     */
    public Boolean getRemoveSetback() {
        return removeSetback;
    }

    /**
     * only useful when order has a setback
     * 
     * @param removeSetback
     *     The removeSetback
     */
    public void setRemoveSetback(Boolean removeSetback) {
        this.removeSetback = removeSetback;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param priority
     *     The priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @return
     *     The params
     */
    public List<NameValuePairsSchema> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @param params
     *     The params
     */
    public void setParams(List<NameValuePairsSchema> params) {
        this.params = params;
    }

    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     * @return
     *     The runTime
     */
    public String getRunTime() {
        return runTime;
    }

    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     * @param runTime
     *     The runTime
     */
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(orderId).append(jobChain).append(state).append(endState).append(comment).append(at).append(resume).append(removeSetback).append(title).append(priority).append(params).append(runTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyOrderSchema) == false) {
            return false;
        }
        ModifyOrderSchema rhs = ((ModifyOrderSchema) other);
        return new EqualsBuilder().append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(state, rhs.state).append(endState, rhs.endState).append(comment, rhs.comment).append(at, rhs.at).append(resume, rhs.resume).append(removeSetback, rhs.removeSetback).append(title, rhs.title).append(priority, rhs.priority).append(params, rhs.params).append(runTime, rhs.runTime).isEquals();
    }

}