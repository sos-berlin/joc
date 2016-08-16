
package com.sos.joc.model.event;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.MasterSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * register, unregister or collect event
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobscheduler",
    "master",
    "supervisor",
    "agent"
})
public class RegisterJobSchedulerSchema {

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     */
    @JsonProperty("jobscheduler")
    private MasterSchema jobscheduler;
    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     */
    @JsonProperty("master")
    private MasterSchema master;
    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     */
    @JsonProperty("supervisor")
    private MasterSchema supervisor;
    /**
     * agent's url
     * 
     */
    @JsonProperty("agent")
    private String agent;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @return
     *     The jobscheduler
     */
    @JsonProperty("jobscheduler")
    public MasterSchema getJobscheduler() {
        return jobscheduler;
    }

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @param jobscheduler
     *     The jobscheduler
     */
    @JsonProperty("jobscheduler")
    public void setJobscheduler(MasterSchema jobscheduler) {
        this.jobscheduler = jobscheduler;
    }

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @return
     *     The master
     */
    @JsonProperty("master")
    public MasterSchema getMaster() {
        return master;
    }

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @param master
     *     The master
     */
    @JsonProperty("master")
    public void setMaster(MasterSchema master) {
        this.master = master;
    }

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @return
     *     The supervisor
     */
    @JsonProperty("supervisor")
    public MasterSchema getSupervisor() {
        return supervisor;
    }

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @param supervisor
     *     The supervisor
     */
    @JsonProperty("supervisor")
    public void setSupervisor(MasterSchema supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * agent's url
     * 
     * @return
     *     The agent
     */
    @JsonProperty("agent")
    public String getAgent() {
        return agent;
    }

    /**
     * agent's url
     * 
     * @param agent
     *     The agent
     */
    @JsonProperty("agent")
    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, java.lang.Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobscheduler).append(master).append(supervisor).append(agent).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RegisterJobSchedulerSchema) == false) {
            return false;
        }
        RegisterJobSchedulerSchema rhs = ((RegisterJobSchedulerSchema) other);
        return new EqualsBuilder().append(jobscheduler, rhs.jobscheduler).append(master, rhs.master).append(supervisor, rhs.supervisor).append(agent, rhs.agent).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
