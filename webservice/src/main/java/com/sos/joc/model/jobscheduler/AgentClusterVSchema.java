
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
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
 * agent cluster (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "state",
    "numOfAgents",
    "agents",
    "numOfProcesses",
    "processes"
})
public class AgentClusterVSchema {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * agent cluster state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private State_ state;
    /**
     * 
     */
    @JsonProperty("numOfAgents")
    private NumOfAgents_ numOfAgents;
    @JsonProperty("agents")
    private List<AgentVSchema> agents = new ArrayList<AgentVSchema>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfProcesses")
    private Integer numOfProcesses;
    /**
     * 
     */
    @JsonProperty("processes")
    private Processes processes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
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
    @JsonProperty("surveyDate")
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
    @JsonProperty("path")
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
    @JsonProperty("path")
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
    @JsonProperty("state")
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
    @JsonProperty("state")
    public void setState(State_ state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The numOfAgents
     */
    @JsonProperty("numOfAgents")
    public NumOfAgents_ getNumOfAgents() {
        return numOfAgents;
    }

    /**
     * 
     * @param numOfAgents
     *     The numOfAgents
     */
    @JsonProperty("numOfAgents")
    public void setNumOfAgents(NumOfAgents_ numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    /**
     * 
     * @return
     *     The agents
     */
    @JsonProperty("agents")
    public List<AgentVSchema> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    @JsonProperty("agents")
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
    @JsonProperty("numOfProcesses")
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
    @JsonProperty("numOfProcesses")
    public void setNumOfProcesses(Integer numOfProcesses) {
        this.numOfProcesses = numOfProcesses;
    }

    /**
     * 
     * @return
     *     The processes
     */
    @JsonProperty("processes")
    public Processes getProcesses() {
        return processes;
    }

    /**
     * 
     * @param processes
     *     The processes
     */
    @JsonProperty("processes")
    public void setProcesses(Processes processes) {
        this.processes = processes;
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
        return new HashCodeBuilder().append(surveyDate).append(path).append(state).append(numOfAgents).append(agents).append(numOfProcesses).append(processes).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(state, rhs.state).append(numOfAgents, rhs.numOfAgents).append(agents, rhs.agents).append(numOfProcesses, rhs.numOfProcesses).append(processes, rhs.processes).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
