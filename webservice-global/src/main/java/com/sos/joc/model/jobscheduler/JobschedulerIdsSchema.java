
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobschedulerIds
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobschedulerIdsSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    private List<String> jobschedulerIds = new ArrayList<String>();
    /**
     * The Id from the 'jobschedulerIds' collection which is specified in the selected field will be used for all further calls
     * (Required)
     * 
     */
    private String selected;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerIds
     */
    public List<String> getJobschedulerIds() {
        return jobschedulerIds;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerIds
     *     The jobschedulerIds
     */
    public void setJobschedulerIds(List<String> jobschedulerIds) {
        this.jobschedulerIds = jobschedulerIds;
    }

    /**
     * The Id from the 'jobschedulerIds' collection which is specified in the selected field will be used for all further calls
     * (Required)
     * 
     * @return
     *     The selected
     */
    public String getSelected() {
        return selected;
    }

    /**
     * The Id from the 'jobschedulerIds' collection which is specified in the selected field will be used for all further calls
     * (Required)
     * 
     * @param selected
     *     The selected
     */
    public void setSelected(String selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobschedulerIds).append(selected).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobschedulerIdsSchema) == false) {
            return false;
        }
        JobschedulerIdsSchema rhs = ((JobschedulerIdsSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobschedulerIds, rhs.jobschedulerIds).append(selected, rhs.selected).isEquals();
    }

}
