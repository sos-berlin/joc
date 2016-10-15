
package com.sos.joc.model.plan;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * plan filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "regex",
    "state",
    "late",
    "dateFrom",
    "dateTo",
    "timeZone"
})
public class PlanFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    @JsonProperty("regex")
    private String regex;
    /**
     * plan state text
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private PlanStateText state;
    @JsonProperty("late")
    private Boolean late;
    @JsonProperty("dateFrom")
    private String dateFrom;
    @JsonProperty("dateTo")
    private String dateTo;
    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     */
    @JsonProperty("timeZone")
    private String timeZone;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @return
     *     The regex
     */
    @JsonProperty("regex")
    public String getRegex() {
        return regex;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @param regex
     *     The regex
     */
    @JsonProperty("regex")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * plan state text
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public PlanStateText getState() {
        return state;
    }

    /**
     * plan state text
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(PlanStateText state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The late
     */
    @JsonProperty("late")
    public Boolean getLate() {
        return late;
    }

    /**
     * 
     * @param late
     *     The late
     */
    @JsonProperty("late")
    public void setLate(Boolean late) {
        this.late = late;
    }

    /**
     * 
     * @return
     *     The dateFrom
     */
    @JsonProperty("dateFrom")
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * 
     * @param dateFrom
     *     The dateFrom
     */
    @JsonProperty("dateFrom")
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * 
     * @return
     *     The dateTo
     */
    @JsonProperty("dateTo")
    public String getDateTo() {
        return dateTo;
    }

    /**
     * 
     * @param dateTo
     *     The dateTo
     */
    @JsonProperty("dateTo")
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     * @return
     *     The timeZone
     */
    @JsonProperty("timeZone")
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     * @param timeZone
     *     The timeZone
     */
    @JsonProperty("timeZone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(regex).append(state).append(late).append(dateFrom).append(dateTo).append(timeZone).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlanFilter) == false) {
            return false;
        }
        PlanFilter rhs = ((PlanFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(regex, rhs.regex).append(state, rhs.state).append(late, rhs.late).append(dateFrom, rhs.dateFrom).append(dateTo, rhs.dateTo).append(timeZone, rhs.timeZone).isEquals();
    }

}
