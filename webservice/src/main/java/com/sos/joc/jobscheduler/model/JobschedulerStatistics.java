
package com.sos.joc.jobscheduler.model;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")

public class JobschedulerStatistics {
    @JsonProperty("delivery_date")
    private Date deliveryDate;
    @JsonProperty("survey_date")
    private Date surveyDate;
    @JsonProperty("jobs")
    private JobschedulerStatisticsJobs jobs;
    @JsonProperty("tasks")
    private JobschedulerStatisticsTasks tasks;
    @JsonProperty("job_chains")
    private JobschedulerStatisticsJobChains jobChains;
    @JsonProperty("orders")
    private JobschedulerStatisticsOrders orders;

    @JsonProperty("delivery_date")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    @JsonProperty("delivery_date")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public JobschedulerStatistics withDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    @JsonProperty("survey_date")
    public Date getSurveyDate() {
        return surveyDate;
    }

    @JsonProperty("survey_date")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public JobschedulerStatistics withSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
        return this;
    }

    @JsonProperty("jobs")
    public JobschedulerStatisticsJobs getJobs() {
        return jobs;
    }

    @JsonProperty("jobs")
    public void setJobs(JobschedulerStatisticsJobs jobs) {
        this.jobs = jobs;
    }

    public JobschedulerStatistics withPrecedence(JobschedulerStatisticsJobs jobs) {
        this.jobs = jobs;
        return this;
    }

    @JsonProperty("tasks")
    public JobschedulerStatisticsTasks getTasks() {
        return tasks;
    }

    @JsonProperty("tasks")
    public void setTasks(JobschedulerStatisticsTasks tasks) {
        this.tasks = tasks;
    }

    public JobschedulerStatistics withTasks(JobschedulerStatisticsTasks tasks) {
        this.tasks = tasks;
        return this;
    }

    @JsonProperty("job_chains")
    public JobschedulerStatisticsJobChains getJobChains() {
        return jobChains;
    }

    @JsonProperty("job_chains")
    public void setJobChains(JobschedulerStatisticsJobChains jobChains) {
        this.jobChains = jobChains;
    }

    public JobschedulerStatistics withJobChains(JobschedulerStatisticsJobChains jobChains) {
        this.jobChains = jobChains;
        return this;
    }

    @JsonProperty("orders")
    public JobschedulerStatisticsOrders getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    public void setOrders(JobschedulerStatisticsOrders orders) {
        this.orders = orders;
    }

    public JobschedulerStatistics withOrders(JobschedulerStatisticsOrders orders) {
        this.orders = orders;
        return this;
    }

}
