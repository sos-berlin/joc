
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobChain {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
    private String title;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer maxOrders;
    private Boolean distributed;
    private String processClass;
    private String fileWatchingProcessClass;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfNodes;
    private List<Node> nodes = new ArrayList<Node>();
    private List<FileWatchingNodePSchema> fileOrderSources = new ArrayList<FileWatchingNodePSchema>();
    /**
     * real end nodes or file sink nodes
     * 
     */
    private List<EndNodeSchema> endNodes = new ArrayList<EndNodeSchema>();
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date configurationDate;

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxOrders
     */
    public Integer getMaxOrders() {
        return maxOrders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxOrders
     *     The maxOrders
     */
    public void setMaxOrders(Integer maxOrders) {
        this.maxOrders = maxOrders;
    }

    /**
     * 
     * @return
     *     The distributed
     */
    public Boolean getDistributed() {
        return distributed;
    }

    /**
     * 
     * @param distributed
     *     The distributed
     */
    public void setDistributed(Boolean distributed) {
        this.distributed = distributed;
    }

    /**
     * 
     * @return
     *     The processClass
     */
    public String getProcessClass() {
        return processClass;
    }

    /**
     * 
     * @param processClass
     *     The processClass
     */
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * 
     * @return
     *     The fileWatchingProcessClass
     */
    public String getFileWatchingProcessClass() {
        return fileWatchingProcessClass;
    }

    /**
     * 
     * @param fileWatchingProcessClass
     *     The fileWatchingProcessClass
     */
    public void setFileWatchingProcessClass(String fileWatchingProcessClass) {
        this.fileWatchingProcessClass = fileWatchingProcessClass;
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
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * 
     * @param nodes
     *     The nodes
     */
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * 
     * @return
     *     The fileOrderSources
     */
    public List<FileWatchingNodePSchema> getFileOrderSources() {
        return fileOrderSources;
    }

    /**
     * 
     * @param fileOrderSources
     *     The fileOrderSources
     */
    public void setFileOrderSources(List<FileWatchingNodePSchema> fileOrderSources) {
        this.fileOrderSources = fileOrderSources;
    }

    /**
     * real end nodes or file sink nodes
     * 
     * @return
     *     The endNodes
     */
    public List<EndNodeSchema> getEndNodes() {
        return endNodes;
    }

    /**
     * real end nodes or file sink nodes
     * 
     * @param endNodes
     *     The endNodes
     */
    public void setEndNodes(List<EndNodeSchema> endNodes) {
        this.endNodes = endNodes;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The configurationDate
     */
    public Date getConfigurationDate() {
        return configurationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param configurationDate
     *     The configurationDate
     */
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(title).append(maxOrders).append(distributed).append(processClass).append(fileWatchingProcessClass).append(numOfNodes).append(nodes).append(fileOrderSources).append(endNodes).append(configurationDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChain) == false) {
            return false;
        }
        JobChain rhs = ((JobChain) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(title, rhs.title).append(maxOrders, rhs.maxOrders).append(distributed, rhs.distributed).append(processClass, rhs.processClass).append(fileWatchingProcessClass, rhs.fileWatchingProcessClass).append(numOfNodes, rhs.numOfNodes).append(nodes, rhs.nodes).append(fileOrderSources, rhs.fileOrderSources).append(endNodes, rhs.endNodes).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
