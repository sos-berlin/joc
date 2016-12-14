
package com.sos.joc.model.event;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * event snapshot
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "eventId",
    "path",
    "eventType",
    "objectType",
    "nodeId",
    "fromNodeId",
    "taskId",
    "state",
    "nodeTransition"
})
public class EventSnapshot {

    /**
     * unique id of an event, monoton increasing, id/1000=milliseconds of UTC time
     * (Required)
     * 
     */
    @JsonProperty("eventId")
    private String eventId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * OrderStarted, OrderStepStarted, OrderStepEnded, OrderNodeChanged, OrderFinished, OrderSetback, OrderSuspended, OrderResumed
     * (Required)
     * 
     */
    @JsonProperty("eventType")
    private String eventType;
    /**
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("objectType")
    private JobSchedulerObjectType objectType;
    /**
     * comes with events OrderNodeChanged, OrderStepStarted, OrderFinished
     * 
     */
    @JsonProperty("nodeId")
    private String nodeId;
    /**
     * comes with event OrderNodeChanged
     * 
     */
    @JsonProperty("fromNodeId")
    private String fromNodeId;
    /**
     * comes with event OrderStepStarted
     * 
     */
    @JsonProperty("taskId")
    private String taskId;
    /**
     * comes with event ...State
     * 
     */
    @JsonProperty("state")
    private String state;
    /**
     * comes with event OrderStepEnded
     * 
     */
    @JsonProperty("nodeTransition")
    private NodeTransition nodeTransition;

    /**
     * unique id of an event, monoton increasing, id/1000=milliseconds of UTC time
     * (Required)
     * 
     * @return
     *     The eventId
     */
    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
    }

    /**
     * unique id of an event, monoton increasing, id/1000=milliseconds of UTC time
     * (Required)
     * 
     * @param eventId
     *     The eventId
     */
    @JsonProperty("eventId")
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * OrderStarted, OrderStepStarted, OrderStepEnded, OrderNodeChanged, OrderFinished, OrderSetback, OrderSuspended, OrderResumed
     * (Required)
     * 
     * @return
     *     The eventType
     */
    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    /**
     * OrderStarted, OrderStepStarted, OrderStepEnded, OrderNodeChanged, OrderFinished, OrderSetback, OrderSuspended, OrderResumed
     * (Required)
     * 
     * @param eventType
     *     The eventType
     */
    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    public JobSchedulerObjectType getObjectType() {
        return objectType;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * (Required)
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }

    /**
     * comes with events OrderNodeChanged, OrderStepStarted, OrderFinished
     * 
     * @return
     *     The nodeId
     */
    @JsonProperty("nodeId")
    public String getNodeId() {
        return nodeId;
    }

    /**
     * comes with events OrderNodeChanged, OrderStepStarted, OrderFinished
     * 
     * @param nodeId
     *     The nodeId
     */
    @JsonProperty("nodeId")
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * comes with event OrderNodeChanged
     * 
     * @return
     *     The fromNodeId
     */
    @JsonProperty("fromNodeId")
    public String getFromNodeId() {
        return fromNodeId;
    }

    /**
     * comes with event OrderNodeChanged
     * 
     * @param fromNodeId
     *     The fromNodeId
     */
    @JsonProperty("fromNodeId")
    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    /**
     * comes with event OrderStepStarted
     * 
     * @return
     *     The taskId
     */
    @JsonProperty("taskId")
    public String getTaskId() {
        return taskId;
    }

    /**
     * comes with event OrderStepStarted
     * 
     * @param taskId
     *     The taskId
     */
    @JsonProperty("taskId")
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    @JsonProperty("state")
    public String getState() {
        return state;
    }
    
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * comes with event OrderStepEnded
     * 
     * @return
     *     The nodeTransition
     */
    @JsonProperty("nodeTransition")
    public NodeTransition getNodeTransition() {
        return nodeTransition;
    }

    /**
     * comes with event OrderStepEnded
     * 
     * @param nodeTransition
     *     The nodeTransition
     */
    @JsonProperty("nodeTransition")
    public void setNodeTransition(NodeTransition nodeTransition) {
        this.nodeTransition = nodeTransition;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventId).append(path).append(eventType).append(objectType).append(nodeId).append(fromNodeId).append(taskId).append(state).append(nodeTransition).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EventSnapshot) == false) {
            return false;
        }
        EventSnapshot rhs = ((EventSnapshot) other);
        return new EqualsBuilder().append(eventId, rhs.eventId).append(path, rhs.path).append(eventType, rhs.eventType).append(objectType, rhs.objectType).append(nodeId, rhs.nodeId).append(fromNodeId, rhs.fromNodeId).append(taskId, rhs.taskId).append(state, rhs.state).append(nodeTransition, rhs.nodeTransition).isEquals();
    }

}
