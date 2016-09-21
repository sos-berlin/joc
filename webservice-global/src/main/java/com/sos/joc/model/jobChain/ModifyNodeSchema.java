
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * modify job chain node
 * <p>
 * the command is part of the web servive url
 * 
 */
@Generated("org.jsonschema2pojo")
public class ModifyNodeSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    private List<Node____> nodes = new ArrayList<Node____>();

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
     * (Required)
     * 
     * @return
     *     The nodes
     */
    public List<Node____> getNodes() {
        return nodes;
    }

    /**
     * 
     * (Required)
     * 
     * @param nodes
     *     The nodes
     */
    public void setNodes(List<Node____> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(nodes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyNodeSchema) == false) {
            return false;
        }
        ModifyNodeSchema rhs = ((ModifyNodeSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(nodes, rhs.nodes).isEquals();
    }

}
