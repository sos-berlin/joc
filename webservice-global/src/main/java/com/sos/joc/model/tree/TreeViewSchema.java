
package com.sos.joc.model.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.job.Job;
import com.sos.joc.model.jobChain.JobChain;
import com.sos.joc.model.lock.LockPSchema;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.processClass.ProcessClassPSchema;
import com.sos.joc.model.schedule.Schedule;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * treeView
 * <p>
 * collections of JobScheduler objects besides folder tree structure.
 * 
 */
@Generated("org.jsonschema2pojo")
public class TreeViewSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    private List<TreeSchema> folders = new ArrayList<TreeSchema>();
    private List<Job> jobs = new ArrayList<Job>();
    private List<JobChain> jobChains = new ArrayList<JobChain>();
    private List<Order> orders = new ArrayList<Order>();
    private List<ProcessClassPSchema> processClasses = new ArrayList<ProcessClassPSchema>();
    private List<LockPSchema> locks = new ArrayList<LockPSchema>();
    private List<Schedule> schedules = new ArrayList<Schedule>();

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
     * 
     * @return
     *     The folders
     */
    public List<TreeSchema> getFolders() {
        return folders;
    }

    /**
     * 
     * @param folders
     *     The folders
     */
    public void setFolders(List<TreeSchema> folders) {
        this.folders = folders;
    }

    /**
     * 
     * @return
     *     The jobs
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * @return
     *     The jobChains
     */
    public List<JobChain> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    public void setJobChains(List<JobChain> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * 
     * @return
     *     The processClasses
     */
    public List<ProcessClassPSchema> getProcessClasses() {
        return processClasses;
    }

    /**
     * 
     * @param processClasses
     *     The processClasses
     */
    public void setProcessClasses(List<ProcessClassPSchema> processClasses) {
        this.processClasses = processClasses;
    }

    /**
     * 
     * @return
     *     The locks
     */
    public List<LockPSchema> getLocks() {
        return locks;
    }

    /**
     * 
     * @param locks
     *     The locks
     */
    public void setLocks(List<LockPSchema> locks) {
        this.locks = locks;
    }

    /**
     * 
     * @return
     *     The schedules
     */
    public List<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * 
     * @param schedules
     *     The schedules
     */
    public void setSchedules(List<Schedule> schedules) {
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
        if ((other instanceof TreeViewSchema) == false) {
            return false;
        }
        TreeViewSchema rhs = ((TreeViewSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(folders, rhs.folders).append(jobs, rhs.jobs).append(jobChains, rhs.jobChains).append(orders, rhs.orders).append(processClasses, rhs.processClasses).append(locks, rhs.locks).append(schedules, rhs.schedules).isEquals();
    }

}
