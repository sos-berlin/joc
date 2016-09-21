
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent cluster (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentClusterVSchema {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String path;
    /**
     * agent cluster state
     * <p>
     * 
     * 
     */
    private State_ state;
    /**
     * 
     */
    private NumOfAgents_ numOfAgents;
    private List<AgentVSchema> agents = new ArrayList<AgentVSchema>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfProcesses;
    /**
     * 
     */
    private Processes processes;

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * 
     * @param surveyDate
     *     The surveyDate
     */
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The path
     */
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * agent cluster state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    public State_ getState() {
        return state;
    }

    /**
     * agent cluster state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    public void setState(State_ state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The numOfAgents
     */
    public NumOfAgents_ getNumOfAgents() {
        return numOfAgents;
    }

    /**
     * 
     * @param numOfAgents
     *     The numOfAgents
     */
    public void setNumOfAgents(NumOfAgents_ numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    /**
     * 
     * @return
     *     The agents
     */
    public List<AgentVSchema> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    public void setAgents(List<AgentVSchema> agents) {
        this.agents = agents;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfProcesses
     */
    public Integer getNumOfProcesses() {
        return numOfProcesses;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfProcesses
     *     The numOfProcesses
     */
    public void setNumOfProcesses(Integer numOfProcesses) {
        this.numOfProcesses = numOfProcesses;
    }

    /**
     * 
     * @return
     *     The processes
     */
    public Processes getProcesses() {
        return processes;
    }

    /**
     * 
     * @param processes
     *     The processes
     */
    public void setProcesses(Processes processes) {
        this.processes = processes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(state).append(numOfAgents).append(agents).append(numOfProcesses).append(processes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterVSchema) == false) {
            return false;
        }
        AgentClusterVSchema rhs = ((AgentClusterVSchema) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(state, rhs.state).append(numOfAgents, rhs.numOfAgents).append(agents, rhs.agents).append(numOfProcesses, rhs.numOfProcesses).append(processes, rhs.processes).isEquals();
    }

}
