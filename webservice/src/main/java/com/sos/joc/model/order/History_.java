
package com.sos.joc.model.order;

import java.util.ArrayList;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "historyId",
    "steps"
})
public class History_ {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("historyId")
    private Integer historyId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("steps")
    private List<Step> steps = new ArrayList<Step>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The historyId
     */
    @JsonProperty("historyId")
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
    @JsonProperty("historyId")
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
    @JsonProperty("steps")
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
    @JsonProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
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
        return new HashCodeBuilder().append(historyId).append(steps).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(historyId, rhs.historyId).append(steps, rhs.steps).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
