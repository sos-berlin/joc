
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler agent (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentVSchema {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date surveyDate;
    /**
     * url can be different against host/port if agent behind a proxy
     * (Required)
     * 
     */
    private String url;
    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     */
    private State state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date startedAt;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer runningTasks;

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
     * url can be different against host/port if agent behind a proxy
     * (Required)
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * url can be different against host/port if agent behind a proxy
     * (Required)
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The startedAt
     */
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param startedAt
     *     The startedAt
     */
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The runningTasks
     */
    public Integer getRunningTasks() {
        return runningTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param runningTasks
     *     The runningTasks
     */
    public void setRunningTasks(Integer runningTasks) {
        this.runningTasks = runningTasks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(url).append(state).append(startedAt).append(runningTasks).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentVSchema) == false) {
            return false;
        }
        AgentVSchema rhs = ((AgentVSchema) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(url, rhs.url).append(state, rhs.state).append(startedAt, rhs.startedAt).append(runningTasks, rhs.runningTasks).isEquals();
    }

}
