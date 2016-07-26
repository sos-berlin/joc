
package com.sos.joc.model.order;

import java.util.Date;
import java.util.HashMap;
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
 * order object in the blacklist
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "historyId",
    "startedAt",
    "processingState",
    "processedBy"
})
public class BlacklistOrderSchema {

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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("startedAt")
    private Date startedAt;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("processingState")
    private ProcessingState processingState;
    /**
     * Only specified when order is running. Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     */
    @JsonProperty("processedBy")
    private String processedBy;
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The startedAt
     */
    @JsonProperty("startedAt")
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param startedAt
     *     The startedAt
     */
    @JsonProperty("startedAt")
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The processingState
     */
    @JsonProperty("processingState")
    public ProcessingState getProcessingState() {
        return processingState;
    }

    /**
     * 
     * (Required)
     * 
     * @param processingState
     *     The processingState
     */
    @JsonProperty("processingState")
    public void setProcessingState(ProcessingState processingState) {
        this.processingState = processingState;
    }

    /**
     * Only specified when order is running. Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     * @return
     *     The processedBy
     */
    @JsonProperty("processedBy")
    public String getProcessedBy() {
        return processedBy;
    }

    /**
     * Only specified when order is running. Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     * @param processedBy
     *     The processedBy
     */
    @JsonProperty("processedBy")
    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
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
        return new HashCodeBuilder().append(historyId).append(startedAt).append(processingState).append(processedBy).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BlacklistOrderSchema) == false) {
            return false;
        }
        BlacklistOrderSchema rhs = ((BlacklistOrderSchema) other);
        return new EqualsBuilder().append(historyId, rhs.historyId).append(startedAt, rhs.startedAt).append(processingState, rhs.processingState).append(processedBy, rhs.processedBy).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
