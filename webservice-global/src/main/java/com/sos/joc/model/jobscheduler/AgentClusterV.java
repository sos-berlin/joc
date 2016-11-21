
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.processClass.Process;
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
    "processes",
    "maxProcesses",
    "_type",
    "configurationStatus"
})
public class AgentClusterV {

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
    private AgentClusterState state;
    /**
     * num of agents
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfAgents")
    private NumOfAgentsInCluster numOfAgents;
    @JsonProperty("agents")
    private List<AgentV> agents = new ArrayList<AgentV>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfProcesses")
    private Integer numOfProcesses;
    @JsonProperty("processes")
    private List<Process> processes = new ArrayList<Process>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxProcesses")
    private Integer maxProcesses;
    /**
     * agent cluster type
     * <p>
     * the type of agent cluster
     * 
     */
    @JsonProperty("_type")
    private AgentClusterType _type;
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    @JsonProperty("configurationStatus")
    private ConfigurationState configurationStatus;

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
    public AgentClusterState getState() {
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
    public void setState(AgentClusterState state) {
        this.state = state;
    }

    /**
     * num of agents
     * <p>
     * 
     * 
     * @return
     *     The numOfAgents
     */
    @JsonProperty("numOfAgents")
    public NumOfAgentsInCluster getNumOfAgents() {
        return numOfAgents;
    }

    /**
     * num of agents
     * <p>
     * 
     * 
     * @param numOfAgents
     *     The numOfAgents
     */
    @JsonProperty("numOfAgents")
    public void setNumOfAgents(NumOfAgentsInCluster numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    /**
     * 
     * @return
     *     The agents
     */
    @JsonProperty("agents")
    public List<AgentV> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    @JsonProperty("agents")
    public void setAgents(List<AgentV> agents) {
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
    public List<Process> getProcesses() {
        return processes;
    }

    /**
     * 
     * @param processes
     *     The processes
     */
    @JsonProperty("processes")
    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxProcesses
     */
    @JsonProperty("maxProcesses")
    public Integer getMaxProcesses() {
        return maxProcesses;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxProcesses
     *     The maxProcesses
     */
    @JsonProperty("maxProcesses")
    public void setMaxProcesses(Integer maxProcesses) {
        this.maxProcesses = maxProcesses;
    }

    /**
     * agent cluster type
     * <p>
     * the type of agent cluster
     * 
     * @return
     *     The _type
     */
    @JsonProperty("_type")
    public AgentClusterType get_type() {
        return _type;
    }

    /**
     * agent cluster type
     * <p>
     * the type of agent cluster
     * 
     * @param _type
     *     The _type
     */
    @JsonProperty("_type")
    public void set_type(AgentClusterType _type) {
        this._type = _type;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @return
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public ConfigurationState getConfigurationStatus() {
        return configurationStatus;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @param configurationStatus
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public void setConfigurationStatus(ConfigurationState configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(state).append(numOfAgents).append(agents).append(numOfProcesses).append(processes).append(maxProcesses).append(_type).append(configurationStatus).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterV) == false) {
            return false;
        }
        AgentClusterV rhs = ((AgentClusterV) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(state, rhs.state).append(numOfAgents, rhs.numOfAgents).append(agents, rhs.agents).append(numOfProcesses, rhs.numOfProcesses).append(processes, rhs.processes).append(maxProcesses, rhs.maxProcesses).append(_type, rhs._type).append(configurationStatus, rhs.configurationStatus).isEquals();
    }

}
