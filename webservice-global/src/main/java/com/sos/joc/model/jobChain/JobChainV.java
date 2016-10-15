
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersSummary;
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
    "blacklist",
    "numOfOrders",
    "configurationStatus",
    "ordersSummary"
})
public class JobChainV {

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
    /**
     * jobChain state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private JobChainState state;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfNodes")
    private Integer numOfNodes;
    @JsonProperty("nodes")
    private List<JobChainNodeV> nodes = new ArrayList<JobChainNodeV>();
    @JsonProperty("fileOrderSources")
    private List<FileWatchingNodeV> fileOrderSources = new ArrayList<FileWatchingNodeV>();
    @JsonProperty("blacklist")
    private List<OrderV> blacklist = new ArrayList<OrderV>();
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
    private ConfigurationState configurationStatus;
    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     */
    @JsonProperty("ordersSummary")
    private OrdersSummary ordersSummary;

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
     * jobChain state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public JobChainState getState() {
        return state;
    }

    /**
     * jobChain state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(JobChainState state) {
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
    public List<JobChainNodeV> getNodes() {
        return nodes;
    }

    /**
     * 
     * @param nodes
     *     The nodes
     */
    @JsonProperty("nodes")
    public void setNodes(List<JobChainNodeV> nodes) {
        this.nodes = nodes;
    }

    /**
     * 
     * @return
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    public List<FileWatchingNodeV> getFileOrderSources() {
        return fileOrderSources;
    }

    /**
     * 
     * @param fileOrderSources
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    public void setFileOrderSources(List<FileWatchingNodeV> fileOrderSources) {
        this.fileOrderSources = fileOrderSources;
    }

    /**
     * 
     * @return
     *     The blacklist
     */
    @JsonProperty("blacklist")
    public List<OrderV> getBlacklist() {
        return blacklist;
    }

    /**
     * 
     * @param blacklist
     *     The blacklist
     */
    @JsonProperty("blacklist")
    public void setBlacklist(List<OrderV> blacklist) {
        this.blacklist = blacklist;
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
    public ConfigurationState getConfigurationStatus() {
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
    public void setConfigurationStatus(ConfigurationState configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     * @return
     *     The ordersSummary
     */
    @JsonProperty("ordersSummary")
    public OrdersSummary getOrdersSummary() {
        return ordersSummary;
    }

    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(state).append(numOfNodes).append(nodes).append(fileOrderSources).append(blacklist).append(numOfOrders).append(configurationStatus).append(ordersSummary).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainV) == false) {
            return false;
        }
        JobChainV rhs = ((JobChainV) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(state, rhs.state).append(numOfNodes, rhs.numOfNodes).append(nodes, rhs.nodes).append(fileOrderSources, rhs.fileOrderSources).append(blacklist, rhs.blacklist).append(numOfOrders, rhs.numOfOrders).append(configurationStatus, rhs.configurationStatus).append(ordersSummary, rhs.ordersSummary).isEquals();
    }

}
