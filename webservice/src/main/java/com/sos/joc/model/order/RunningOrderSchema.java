
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
 * running order
 * <p>
 * order object which is running
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "historyId",
    "taskId",
    "startedAt",
    "inProcessSince",
    "processingState",
    "processedBy"
})
public class RunningOrderSchema {

    /**
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
    @JsonProperty("taskId")
    private Integer taskId;
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("inProcessSince")
    private Date inProcessSince;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("processingState")
    private ProcessingState___ processingState;
    /**
     * Only specified when order is running. Host/Port of an active cluster member or path of a JobScheduler Agent CLuster
     * 
     */
    @JsonProperty("processedBy")
    private String processedBy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
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
     *     The taskId
     */
    @JsonProperty("taskId")
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 
     * (Required)
     * 
     * @param taskId
     *     The taskId
     */
    @JsonProperty("taskId")
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The inProcessSince
     */
    @JsonProperty("inProcessSince")
    public Date getInProcessSince() {
        return inProcessSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param inProcessSince
     *     The inProcessSince
     */
    @JsonProperty("inProcessSince")
    public void setInProcessSince(Date inProcessSince) {
        this.inProcessSince = inProcessSince;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The processingState
     */
    @JsonProperty("processingState")
    public ProcessingState___ getProcessingState() {
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
    public void setProcessingState(ProcessingState___ processingState) {
        this.processingState = processingState;
    }

    /**
     * Only specified when order is running. Host/Port of an active cluster member or path of a JobScheduler Agent CLuster
     * 
     * @return
     *     The processedBy
     */
    @JsonProperty("processedBy")
    public String getProcessedBy() {
        return processedBy;
    }

    /**
     * Only specified when order is running. Host/Port of an active cluster member or path of a JobScheduler Agent CLuster
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
        return new HashCodeBuilder().append(historyId).append(taskId).append(startedAt).append(inProcessSince).append(processingState).append(processedBy).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RunningOrderSchema) == false) {
            return false;
        }
        RunningOrderSchema rhs = ((RunningOrderSchema) other);
        return new EqualsBuilder().append(historyId, rhs.historyId).append(taskId, rhs.taskId).append(startedAt, rhs.startedAt).append(inProcessSince, rhs.inProcessSince).append(processingState, rhs.processingState).append(processedBy, rhs.processedBy).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
