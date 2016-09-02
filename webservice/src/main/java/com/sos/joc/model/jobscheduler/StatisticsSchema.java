
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler statistics
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class StatisticsSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date surveyDate;
    /**
     * 
     * (Required)
     * 
     */
    private Jobs jobs;
    /**
     * 
     * (Required)
     * 
     */
    private Tasks tasks;
    /**
     * 
     * (Required)
     * 
     */
    private JobChains jobChains;
    /**
     * TODO here we need in addition: setback, waitingForResource, running, blacklist
     * (Required)
     * 
     */
    private Orders orders;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
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
    public void setJobChains(JobChains jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * TODO here we need in addition: setback, waitingForResource, running, blacklist
     * (Required)
     * 
     * @return
     *     The orders
     */
    public Orders getOrders() {
        return orders;
    }

    /**
     * TODO here we need in addition: setback, waitingForResource, running, blacklist
     * (Required)
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(surveyDate).append(jobs).append(tasks).append(jobChains).append(orders).toHashCode();
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
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(surveyDate, rhs.surveyDate).append(jobs, rhs.jobs).append(tasks, rhs.tasks).append(jobChains, rhs.jobChains).append(orders, rhs.orders).isEquals();
    }

}
