
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobschedulerIds
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "jobschedulerIds",
    "selected"
})
public class JobschedulerIdsSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerIds")
    private List<String> jobschedulerIds = new ArrayList<String>();
    /**
     * The Id from the 'jobschedulerIds' collection which is specified in the selected field will be used for all further calls
     * (Required)
     * 
     */
    @JsonProperty("selected")
    private String selected;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
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
    @JsonProperty("deliveryDate")
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
    @JsonProperty("jobschedulerIds")
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
    @JsonProperty("jobschedulerIds")
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
    @JsonProperty("selected")
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
    @JsonProperty("selected")
    public void setSelected(String selected) {
        this.selected = selected;
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
        return new HashCodeBuilder().append(deliveryDate).append(jobschedulerIds).append(selected).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobschedulerIds, rhs.jobschedulerIds).append(selected, rhs.selected).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
