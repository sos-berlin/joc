
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
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
public class AgentClusterPSchema {

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
    @JsonProperty("_type")
    private AgentClusterPSchema.Type type;
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
    private NumOfAgents numOfAgents;
    @JsonProperty("agents")
    private List<Agent> agents = new ArrayList<Agent>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * 
     * @return
     *     The type
     */
    @JsonProperty("_type")
    public AgentClusterPSchema.Type getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The _type
     */
    @JsonProperty("_type")
    public void setType(AgentClusterPSchema.Type type) {
        this.type = type;
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
    public NumOfAgents getNumOfAgents() {
        return numOfAgents;
    }

    /**
     * 
     * @param numOfAgents
     *     The numOfAgents
     */
    @JsonProperty("numOfAgents")
    public void setNumOfAgents(NumOfAgents numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    /**
     * 
     * @return
     *     The agents
     */
    @JsonProperty("agents")
    public List<Agent> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    @JsonProperty("agents")
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
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
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(maxProcesses).append(type).append(state).append(numOfAgents).append(agents).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterPSchema) == false) {
            return false;
        }
        AgentClusterPSchema rhs = ((AgentClusterPSchema) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(maxProcesses, rhs.maxProcesses).append(type, rhs.type).append(state, rhs.state).append(numOfAgents, rhs.numOfAgents).append(agents, rhs.agents).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        single_agent("single_agent"),
        fix_priority("fix_priority"),
        round_robin("round_robin");
        private final String value;
        private final static Map<String, AgentClusterPSchema.Type> CONSTANTS = new HashMap<String, AgentClusterPSchema.Type>();

        static {
            for (AgentClusterPSchema.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static AgentClusterPSchema.Type fromValue(String value) {
            AgentClusterPSchema.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
