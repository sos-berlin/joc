
package com.sos.joc.model.event;

import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
public class RegisterJobSchedulerSchema {

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     */
    private MasterSchema jobscheduler;
    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     */
    private MasterSchema master;
    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     */
    private MasterSchema supervisor;
    /**
     * agent's url
     * 
     */
    private String agent;

    /**
     * Master JobScheduler
     * <p>
     * 
     * 
     * @return
     *     The jobscheduler
     */
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
    public void setSupervisor(MasterSchema supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * agent's url
     * 
     * @return
     *     The agent
     */
    public String getAgent() {
        return agent;
    }

    /**
     * agent's url
     * 
     * @param agent
     *     The agent
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobscheduler).append(master).append(supervisor).append(agent).toHashCode();
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
        return new EqualsBuilder().append(jobscheduler, rhs.jobscheduler).append(master, rhs.master).append(supervisor, rhs.supervisor).append(agent, rhs.agent).isEquals();
    }

}
