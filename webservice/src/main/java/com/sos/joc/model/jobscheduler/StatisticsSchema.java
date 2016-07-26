
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler statistics
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "surveyDate",
    "jobs",
    "tasks",
    "jobChains",
    "orders"
})
public class StatisticsSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobs")
    private Jobs jobs;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tasks")
    private Tasks tasks;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChains")
    private JobChains jobChains;
    /**
     * TODO here we need in addition: setback, waitingForRessource, running, blacklist
     * (Required)
     * 
     */
    @JsonProperty("orders")
    private Orders orders;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * 
     * (Required)
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public Jobs getJobs() {
        return jobs;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(Jobs jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The tasks
     */
    @JsonProperty("tasks")
    public Tasks getTasks() {
        return tasks;
    }

    /**
     * 
     * (Required)
     * 
     * @param tasks
     *     The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public JobChains getJobChains() {
        return jobChains;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(JobChains jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * TODO here we need in addition: setback, waitingForRessource, running, blacklist
     * (Required)
     * 
     * @return
     *     The orders
     */
    @JsonProperty("orders")
    public Orders getOrders() {
        return orders;
    }

    /**
     * TODO here we need in addition: setback, waitingForRessource, running, blacklist
     * (Required)
     * 
     * @param orders
     *     The orders
     */
    @JsonProperty("orders")
    public void setOrders(Orders orders) {
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
        return new HashCodeBuilder().append(deliveryDate).append(surveyDate).append(jobs).append(tasks).append(jobChains).append(orders).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StatisticsSchema) == false) {
            return false;
        }
        StatisticsSchema rhs = ((StatisticsSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(surveyDate, rhs.surveyDate).append(jobs, rhs.jobs).append(tasks, rhs.tasks).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
