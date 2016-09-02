
package com.sos.joc.model.calendar;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * day
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class DaySchema {

    /**
     * YYYY-MM-DD
     * (Required)
     * 
     */
    private String date;
    /**
     * 
     * (Required)
     * 
     */
    private List<Order> orders = new ArrayList<Order>();
    /**
     * 
     * (Required)
     * 
     */
    private List<Order> jobs = new ArrayList<Order>();

    /**
     * YYYY-MM-DD
     * (Required)
     * 
     * @return
     *     The date
     */
    public String getDate() {
        return date;
    }

    /**
     * YYYY-MM-DD
     * (Required)
     * 
     * @param date
     *     The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * 
     * (Required)
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobs
     */
    public List<Order> getJobs() {
        return jobs;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobs
     *     The jobs
     */
    public void setJobs(List<Order> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(date).append(orders).append(jobs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DaySchema) == false) {
            return false;
        }
        DaySchema rhs = ((DaySchema) other);
        return new EqualsBuilder().append(date, rhs.date).append(orders, rhs.orders).append(jobs, rhs.jobs).isEquals();
    }

}
