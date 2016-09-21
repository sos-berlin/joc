
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent cluster (permant part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentClusterPSchema {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
    private String name;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer maxProcesses;
    private AgentClusterPSchema.Type type;
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
    private NumOfAgents numOfAgents;
    private List<Agent> agents = new ArrayList<Agent>();

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
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
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
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
    public void setMaxProcesses(Integer maxProcesses) {
        this.maxProcesses = maxProcesses;
    }

    /**
     * 
     * @return
     *     The type
     */
    public AgentClusterPSchema.Type getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The _type
     */
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
    public NumOfAgents getNumOfAgents() {
        return numOfAgents;
    }

    /**
     * 
     * @param numOfAgents
     *     The numOfAgents
     */
    public void setNumOfAgents(NumOfAgents numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    /**
     * 
     * @return
     *     The agents
     */
    public List<Agent> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(maxProcesses).append(type).append(state).append(numOfAgents).append(agents).toHashCode();
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
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(maxProcesses, rhs.maxProcesses).append(type, rhs.type).append(state, rhs.state).append(numOfAgents, rhs.numOfAgents).append(agents, rhs.agents).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        SINGLE_AGENT("SINGLE_AGENT"),
        FIX_PRIORITY("FIX_PRIORITY"),
        ROUND_ROBIN("ROUND_ROBIN");
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

        @Override
        public String toString() {
            return this.value;
        }

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
