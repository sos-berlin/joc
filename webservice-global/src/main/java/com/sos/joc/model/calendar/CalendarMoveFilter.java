
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.audit.AuditParams;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * calendarFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "calendar",
    "category",
    "newCalendar",
    "newCategory",
    "auditLog"
})
public class CalendarMoveFilter {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("calendar")
    private String calendar;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("category")
    private String category;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("newCalendar")
    private String newCalendar;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("newCategory")
    private String newCategory;
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    private AuditParams auditLog;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The calendar
     */
    @JsonProperty("calendar")
    public String getCalendar() {
        return calendar;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param calendar
     *     The calendar
     */
    @JsonProperty("calendar")
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The category
     */
    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    /**
     * 
     * (Required)
     * 
     * @param category
     *     The category
     */
    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The newCalendar
     */
    @JsonProperty("newCalendar")
    public String getNewCalendar() {
        return newCalendar;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param newCalendar
     *     The newCalendar
     */
    @JsonProperty("newCalendar")
    public void setNewCalendar(String newCalendar) {
        this.newCalendar = newCalendar;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The newCategory
     */
    @JsonProperty("newCategory")
    public String getNewCategory() {
        return newCategory;
    }

    /**
     * 
     * (Required)
     * 
     * @param newCategory
     *     The newCategory
     */
    @JsonProperty("newCategory")
    public void setNewCategory(String newCategory) {
        this.newCategory = newCategory;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @return
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public AuditParams getAuditLog() {
        return auditLog;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @param auditLog
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendar).append(category).append(newCalendar).append(newCategory).append(auditLog).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CalendarMoveFilter) == false) {
            return false;
        }
        CalendarMoveFilter rhs = ((CalendarMoveFilter) other);
        return new EqualsBuilder().append(calendar, rhs.calendar).append(category, rhs.category).append(newCalendar, rhs.newCalendar).append(newCategory, rhs.newCategory).append(auditLog, rhs.auditLog).isEquals();
    }

}
