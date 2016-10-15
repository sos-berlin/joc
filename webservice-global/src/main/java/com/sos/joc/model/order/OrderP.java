
package com.sos.joc.model.order;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order (permanent part)
 * <p>
 * compact=true then ONLY surveyDate, path, id, jobChain and _type are responded, title is optional
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "orderId",
    "jobChain",
    "priority",
    "_type",
    "title",
    "initialState",
    "endState",
    "estimatedDuration",
    "configurationDate"
})
public class OrderP {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * 
     * (Required)
     * 
     */
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
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("priority")
    private Integer priority;
    /**
     * order type
     * <p>
     * the type of the order
     * 
     */
    @JsonProperty("_type")
    private OrderType _type;
    @JsonProperty("title")
    private String title;
    /**
     * the name of the start node
     * 
     */
    @JsonProperty("initialState")
    private String initialState;
    /**
     * the name of the end node
     * 
     */
    @JsonProperty("endState")
    private String endState;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("estimatedDuration")
    private Integer estimatedDuration;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    private Date configurationDate;

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param surveyDate
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
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
    @JsonProperty("path")
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
    @JsonProperty("orderId")
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
     * order type
     * <p>
     * the type of the order
     * 
     * @return
     *     The _type
     */
    @JsonProperty("_type")
    public OrderType get_type() {
        return _type;
    }

    /**
     * order type
     * <p>
     * the type of the order
     * 
     * @param _type
     *     The _type
     */
    @JsonProperty("_type")
    public void set_type(OrderType _type) {
        this._type = _type;
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
     * the name of the start node
     * 
     * @return
     *     The initialState
     */
    @JsonProperty("initialState")
    public String getInitialState() {
        return initialState;
    }

    /**
     * the name of the start node
     * 
     * @param initialState
     *     The initialState
     */
    @JsonProperty("initialState")
    public void setInitialState(String initialState) {
        this.initialState = initialState;
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
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The estimatedDuration
     */
    @JsonProperty("estimatedDuration")
    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param estimatedDuration
     *     The estimatedDuration
     */
    @JsonProperty("estimatedDuration")
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The configurationDate
     */
    @JsonProperty("configurationDate")
    public Date getConfigurationDate() {
        return configurationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param configurationDate
     *     The configurationDate
     */
    @JsonProperty("configurationDate")
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(orderId).append(jobChain).append(priority).append(_type).append(title).append(initialState).append(endState).append(estimatedDuration).append(configurationDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderP) == false) {
            return false;
        }
        OrderP rhs = ((OrderP) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(priority, rhs.priority).append(_type, rhs._type).append(title, rhs.title).append(initialState, rhs.initialState).append(endState, rhs.endState).append(estimatedDuration, rhs.estimatedDuration).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
