
package com.sos.joc.model.jobChain;

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
import com.sos.joc.model.common.ConfigurationStatusSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "name",
    "state",
    "numOfNodes",
    "nodes",
    "fileOrderSources",
    "numOfOrders",
    "configurationStatus",
    "ordersSummary"
})
public class JobChain__ {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;
    @JsonProperty("name")
    private String name;
    @JsonProperty("state")
    private State state;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfNodes")
    private Integer numOfNodes;
    @JsonProperty("nodes")
    private List<Node__> nodes = new ArrayList<Node__>();
    @JsonProperty("fileOrderSources")
    private List<FileWatchingNodeVSchema> fileOrderSources = new ArrayList<FileWatchingNodeVSchema>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfOrders")
    private Integer numOfOrders;
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    @JsonProperty("configurationStatus")
    private ConfigurationStatusSchema configurationStatus;
    @JsonProperty("ordersSummary")
    private OrdersSummary ordersSummary;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
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
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public State getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(State state) {
        this.state = state;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfNodes
     */
    @JsonProperty("numOfNodes")
    public Integer getNumOfNodes() {
        return numOfNodes;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfNodes
     *     The numOfNodes
     */
    @JsonProperty("numOfNodes")
    public void setNumOfNodes(Integer numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    /**
     * 
     * @return
     *     The nodes
     */
    @JsonProperty("nodes")
    public List<Node__> getNodes() {
        return nodes;
    }

    /**
     * 
     * @param nodes
     *     The nodes
     */
    @JsonProperty("nodes")
    public void setNodes(List<Node__> nodes) {
        this.nodes = nodes;
    }

    /**
     * 
     * @return
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    public List<FileWatchingNodeVSchema> getFileOrderSources() {
        return fileOrderSources;
    }

    /**
     * 
     * @param fileOrderSources
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    public void setFileOrderSources(List<FileWatchingNodeVSchema> fileOrderSources) {
        this.fileOrderSources = fileOrderSources;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfOrders
     */
    @JsonProperty("numOfOrders")
    public Integer getNumOfOrders() {
        return numOfOrders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfOrders
     *     The numOfOrders
     */
    @JsonProperty("numOfOrders")
    public void setNumOfOrders(Integer numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @return
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public ConfigurationStatusSchema getConfigurationStatus() {
        return configurationStatus;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @param configurationStatus
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public void setConfigurationStatus(ConfigurationStatusSchema configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    /**
     * 
     * @return
     *     The ordersSummary
     */
    @JsonProperty("ordersSummary")
    public OrdersSummary getOrdersSummary() {
        return ordersSummary;
    }

    /**
     * 
     * @param ordersSummary
     *     The ordersSummary
     */
    @JsonProperty("ordersSummary")
    public void setOrdersSummary(OrdersSummary ordersSummary) {
        this.ordersSummary = ordersSummary;
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
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(state).append(numOfNodes).append(nodes).append(fileOrderSources).append(numOfOrders).append(configurationStatus).append(ordersSummary).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChain__) == false) {
            return false;
        }
        JobChain__ rhs = ((JobChain__) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(state, rhs.state).append(numOfNodes, rhs.numOfNodes).append(nodes, rhs.nodes).append(fileOrderSources, rhs.fileOrderSources).append(numOfOrders, rhs.numOfOrders).append(configurationStatus, rhs.configurationStatus).append(ordersSummary, rhs.ordersSummary).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
