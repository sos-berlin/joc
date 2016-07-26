
package com.sos.joc.model.calendar;

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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * day
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "date",
    "orders",
    "jobs"
})
public class DaySchema {

    /**
     * YYYY-MM-DD
     * (Required)
     * 
     */
    @JsonProperty("date")
    private String date;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orders")
    private List<Order> orders = new ArrayList<Order>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobs")
    private List<Order> jobs = new ArrayList<Order>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * YYYY-MM-DD
     * (Required)
     * 
     * @return
     *     The date
     */
    @JsonProperty("date")
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
    @JsonProperty("date")
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
    @JsonProperty("orders")
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
    @JsonProperty("orders")
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
    @JsonProperty("jobs")
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
    @JsonProperty("jobs")
    public void setJobs(List<Order> jobs) {
        this.jobs = jobs;
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
        return new HashCodeBuilder().append(date).append(orders).append(jobs).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(date, rhs.date).append(orders, rhs.orders).append(jobs, rhs.jobs).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
