
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * stop or unstop command
 * <p>
 * the command is part of the web servive url
 * 
 */
@Generated("org.jsonschema2pojo")
public class ModifySchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    private List<JobChain_____> jobChains = new ArrayList<JobChain_____>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The jobChains
     */
    public List<JobChain_____> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    public void setJobChains(List<JobChain_____> jobChains) {
        this.jobChains = jobChains;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(jobChains).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifySchema) == false) {
            return false;
        }
        ModifySchema rhs = ((ModifySchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobChains, rhs.jobChains).isEquals();
    }

}
