
package com.sos.joc.model.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.job.JobP;
import com.sos.joc.model.jobChain.JobChainP;
import com.sos.joc.model.lock.LockP;
import com.sos.joc.model.order.OrderP;
import com.sos.joc.model.processClass.ProcessClassP;
import com.sos.joc.model.schedule.ScheduleP;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * treeView
 * <p>
 * collections of JobScheduler objects besides folder tree structure.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "folders",
    "jobs",
    "jobChains",
    "orders",
    "processClasses",
    "locks",
    "schedules"
})
public class TreeView {

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
     * 
     * (Required)
     * 
     */
    @JsonProperty("folders")
    private List<Tree> folders = new ArrayList<Tree>();
    @JsonProperty("jobs")
    private List<JobP> jobs = new ArrayList<JobP>();
    @JsonProperty("jobChains")
    private List<JobChainP> jobChains = new ArrayList<JobChainP>();
    @JsonProperty("orders")
    private List<OrderP> orders = new ArrayList<OrderP>();
    @JsonProperty("processClasses")
    private List<ProcessClassP> processClasses = new ArrayList<ProcessClassP>();
    @JsonProperty("locks")
    private List<LockP> locks = new ArrayList<LockP>();
    @JsonProperty("schedules")
    private List<ScheduleP> schedules = new ArrayList<ScheduleP>();

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
     * 
     * (Required)
     * 
     * @return
     *     The folders
     */
    @JsonProperty("folders")
    public List<Tree> getFolders() {
        return folders;
    }

    /**
     * 
     * (Required)
     * 
     * @param folders
     *     The folders
     */
    @JsonProperty("folders")
    public void setFolders(List<Tree> folders) {
        this.folders = folders;
    }

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public List<JobP> getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(List<JobP> jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public List<JobChainP> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(List<JobChainP> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The orders
     */
    @JsonProperty("orders")
    public List<OrderP> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    @JsonProperty("orders")
    public void setOrders(List<OrderP> orders) {
        this.orders = orders;
    }

    /**
     * 
     * @return
     *     The processClasses
     */
    @JsonProperty("processClasses")
    public List<ProcessClassP> getProcessClasses() {
        return processClasses;
    }

    /**
     * 
     * @param processClasses
     *     The processClasses
     */
    @JsonProperty("processClasses")
    public void setProcessClasses(List<ProcessClassP> processClasses) {
        this.processClasses = processClasses;
    }

    /**
     * 
     * @return
     *     The locks
     */
    @JsonProperty("locks")
    public List<LockP> getLocks() {
        return locks;
    }

    /**
     * 
     * @param locks
     *     The locks
     */
    @JsonProperty("locks")
    public void setLocks(List<LockP> locks) {
        this.locks = locks;
    }

    /**
     * 
     * @return
     *     The schedules
     */
    @JsonProperty("schedules")
    public List<ScheduleP> getSchedules() {
        return schedules;
    }

    /**
     * 
     * @param schedules
     *     The schedules
     */
    @JsonProperty("schedules")
    public void setSchedules(List<ScheduleP> schedules) {
        this.schedules = schedules;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(folders).append(jobs).append(jobChains).append(orders).append(processClasses).append(locks).append(schedules).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TreeView) == false) {
            return false;
        }
        TreeView rhs = ((TreeView) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(folders, rhs.folders).append(jobs, rhs.jobs).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(processClasses, rhs.processClasses).append(locks, rhs.locks).append(schedules, rhs.schedules).isEquals();
    }

}
