
package com.sos.joc.model.report;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agentsFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerIds",
    "agents",
    "dateFrom",
    "dateTo",
    "timeZone"
})
public class AgentsFilter {

    @JsonProperty("jobschedulerIds")
    private List<String> jobschedulerIds = new ArrayList<String>();
    @JsonProperty("agents")
    private List<String> agents = new ArrayList<String>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dateFrom")
    private String dateFrom;
    /**
     * 
     * (Required)
     * 
     */
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
     * @return
     *     The jobschedulerIds
     */
    @JsonProperty("jobschedulerIds")
    public List<String> getJobschedulerIds() {
        return jobschedulerIds;
    }

    /**
     * 
     * @param jobschedulerIds
     *     The jobschedulerIds
     */
    @JsonProperty("jobschedulerIds")
    public void setJobschedulerIds(List<String> jobschedulerIds) {
        this.jobschedulerIds = jobschedulerIds;
    }

    /**
     * 
     * @return
     *     The agents
     */
    @JsonProperty("agents")
    public List<String> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    @JsonProperty("agents")
    public void setAgents(List<String> agents) {
        this.agents = agents;
    }

    /**
     * 
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
        return new HashCodeBuilder().append(jobschedulerIds).append(agents).append(dateFrom).append(dateTo).append(timeZone).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentsFilter) == false) {
            return false;
        }
        AgentsFilter rhs = ((AgentsFilter) other);
        return new EqualsBuilder().append(jobschedulerIds, rhs.jobschedulerIds).append(agents, rhs.agents).append(dateFrom, rhs.dateFrom).append(dateTo, rhs.dateTo).append(timeZone, rhs.timeZone).isEquals();
    }

}
