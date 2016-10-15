
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent cluster (permant part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "name",
    "maxProcesses",
    "_type",
    "state",
    "numOfAgents",
    "agents"
})
public class AgentClusterP {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
    @JsonProperty("name")
    private String name;
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
    private List<AgentOfCluster> agents = new ArrayList<AgentOfCluster>();

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
    public List<AgentOfCluster> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    @JsonProperty("agents")
    public void setAgents(List<AgentOfCluster> agents) {
        this.agents = agents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(maxProcesses).append(_type).append(state).append(numOfAgents).append(agents).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterP) == false) {
            return false;
        }
        AgentClusterP rhs = ((AgentClusterP) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(maxProcesses, rhs.maxProcesses).append(_type, rhs._type).append(state, rhs.state).append(numOfAgents, rhs.numOfAgents).append(agents, rhs.agents).isEquals();
    }

}
