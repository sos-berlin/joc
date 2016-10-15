
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * stop or unstop command
 * <p>
 * the command is part of the web servive url
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "jobChains"
})
public class ModifyJobChains {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("jobChains")
    private List<ModifyJobChain> jobChains = new ArrayList<ModifyJobChain>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
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
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public List<ModifyJobChain> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(List<ModifyJobChain> jobChains) {
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
        if ((other instanceof ModifyJobChains) == false) {
            return false;
        }
        ModifyJobChains rhs = ((ModifyJobChains) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobChains, rhs.jobChains).isEquals();
    }

}
