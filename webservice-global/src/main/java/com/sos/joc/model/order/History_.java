
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class History_ {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer historyId;
    /**
     * 
     * (Required)
     * 
     */
    private List<Step> steps = new ArrayList<Step>();

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The historyId
     */
    public Integer getHistoryId() {
        return historyId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param historyId
     *     The historyId
     */
    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * 
     * (Required)
     * 
     * @param steps
     *     The steps
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(historyId).append(steps).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof History_) == false) {
            return false;
        }
        History_ rhs = ((History_) other);
        return new EqualsBuilder().append(historyId, rhs.historyId).append(steps, rhs.steps).isEquals();
    }

}
