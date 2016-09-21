
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
public class JobChain__ {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String path;
    private String name;
    private State state;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfNodes;
    private List<Node__> nodes = new ArrayList<Node__>();
    private List<FileWatchingNodeVSchema> fileOrderSources = new ArrayList<FileWatchingNodeVSchema>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfOrders;
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    private ConfigurationStatusSchema configurationStatus;
    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     */
    private OrdersSummary ordersSummary;

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
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
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
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
    public void setNumOfNodes(Integer numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    /**
     * 
     * @return
     *     The nodes
     */
    public List<Node__> getNodes() {
        return nodes;
    }

    /**
     * 
     * @param nodes
     *     The nodes
     */
    public void setNodes(List<Node__> nodes) {
        this.nodes = nodes;
    }

    /**
     * 
     * @return
     *     The fileOrderSources
     */
    public List<FileWatchingNodeVSchema> getFileOrderSources() {
        return fileOrderSources;
    }

    /**
     * 
     * @param fileOrderSources
     *     The fileOrderSources
     */
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
    public void setConfigurationStatus(ConfigurationStatusSchema configurationStatus) {
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
    public void setOrdersSummary(OrdersSummary ordersSummary) {
        this.ordersSummary = ordersSummary;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(state).append(numOfNodes).append(nodes).append(fileOrderSources).append(numOfOrders).append(configurationStatus).append(ordersSummary).toHashCode();
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
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(state, rhs.state).append(numOfNodes, rhs.numOfNodes).append(nodes, rhs.nodes).append(fileOrderSources, rhs.fileOrderSources).append(numOfOrders, rhs.numOfOrders).append(configurationStatus, rhs.configurationStatus).append(ordersSummary, rhs.ordersSummary).isEquals();
    }

}
