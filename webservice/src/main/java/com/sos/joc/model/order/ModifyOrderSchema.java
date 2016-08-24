
package com.sos.joc.model.order;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "orderId",
    "jobChain",
    "state",
    "endState",
    "comment",
    "at",
    "resume",
    "title",
    "priority",
    "params",
    "runTime"
})
public class ModifyOrderSchema {

    @JsonProperty("orderId")
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    /**
     * the name of the node
     * 
     */
    @JsonProperty("state")
    private String state;
    /**
     * the name of the end node
     * 
     */
    @JsonProperty("endState")
    private String endState;
    /**
     * Field to comment this action which can be logged.
     * 
     */
    @JsonProperty("comment")
    private String comment;
    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     */
    @JsonProperty("at")
    private String at = "now";
    /**
     * only useful when changing order state of suspended orders
     * 
     */
    @JsonProperty("resume")
    private Boolean resume = false;
    @JsonProperty("title")
    private String title;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("priority")
    private Integer priority;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     */
    @JsonProperty("runTime")
    private String runTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The orderId
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
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
    @JsonProperty("jobChain")
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
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * the name of the node
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * the name of the node
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * the name of the end node
     * 
     * @return
     *     The endState
     */
    @JsonProperty("endState")
    public String getEndState() {
        return endState;
    }

    /**
     * the name of the end node
     * 
     * @param endState
     *     The endState
     */
    @JsonProperty("endState")
    public void setEndState(String endState) {
        this.endState = endState;
    }

    /**
     * Field to comment this action which can be logged.
     * 
     * @return
     *     The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * Field to comment this action which can be logged.
     * 
     * @param comment
     *     The comment
     */
    @JsonProperty("comment")
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
    @JsonProperty("at")
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
    @JsonProperty("at")
    public void setAt(String at) {
        this.at = at;
    }

    /**
     * only useful when changing order state of suspended orders
     * 
     * @return
     *     The resume
     */
    @JsonProperty("resume")
    public Boolean getResume() {
        return resume;
    }

    /**
     * only useful when changing order state of suspended orders
     * 
     * @param resume
     *     The resume
     */
    @JsonProperty("resume")
    public void setResume(Boolean resume) {
        this.resume = resume;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
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
    @JsonProperty("priority")
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
    @JsonProperty("priority")
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
    @JsonProperty("params")
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
    @JsonProperty("params")
    public void setParams(List<NameValuePairsSchema> params) {
        this.params = params;
    }

    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     * @return
     *     The runTime
     */
    @JsonProperty("runTime")
    public String getRunTime() {
        return runTime;
    }

    /**
     * A run_time xml is expected which is specified in the <xsd:complexType name='run_time'> element of  http://www.sos-berlin.com/schema/scheduler.xsd
     * 
     * @param runTime
     *     The runTime
     */
    @JsonProperty("runTime")
    public void setRunTime(String runTime) {
        this.runTime = runTime;
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
        return new HashCodeBuilder().append(orderId).append(jobChain).append(state).append(endState).append(comment).append(at).append(resume).append(title).append(priority).append(params).append(runTime).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(state, rhs.state).append(endState, rhs.endState).append(comment, rhs.comment).append(at, rhs.at).append(resume, rhs.resume).append(title, rhs.title).append(priority, rhs.priority).append(params, rhs.params).append(runTime, rhs.runTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
