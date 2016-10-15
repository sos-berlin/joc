
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain (permanent part)
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
    "title",
    "maxOrders",
    "distributed",
    "processClass",
    "fileWatchingProcessClass",
    "numOfNodes",
    "nodes",
    "fileOrderSources",
    "endNodes",
    "configurationDate"
})
public class JobChainP {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
    @JsonProperty("title")
    private String title;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxOrders")
    private Integer maxOrders;
    @JsonProperty("distributed")
    private Boolean distributed;
    @JsonProperty("processClass")
    private String processClass;
    @JsonProperty("fileWatchingProcessClass")
    private String fileWatchingProcessClass;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfNodes")
    private Integer numOfNodes;
    @JsonProperty("nodes")
    private List<JobChainNodeP> nodes = new ArrayList<JobChainNodeP>();
    @JsonProperty("fileOrderSources")
    private List<FileWatchingNodeP> fileOrderSources = new ArrayList<FileWatchingNodeP>();
    /**
     * real end nodes or file sink nodes
     * 
     */
    @JsonProperty("endNodes")
    private List<EndNode> endNodes = new ArrayList<EndNode>();
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    private Date configurationDate;

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
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
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
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
    @JsonProperty("maxOrders")
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
    @JsonProperty("maxOrders")
    public void setMaxOrders(Integer maxOrders) {
        this.maxOrders = maxOrders;
    }

    /**
     * 
     * @return
     *     The distributed
     */
    @JsonProperty("distributed")
    public Boolean getDistributed() {
        return distributed;
    }

    /**
     * 
     * @param distributed
     *     The distributed
     */
    @JsonProperty("distributed")
    public void setDistributed(Boolean distributed) {
        this.distributed = distributed;
    }

    /**
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("processClass")
    public String getProcessClass() {
        return processClass;
    }

    /**
     * 
     * @param processClass
     *     The processClass
     */
    @JsonProperty("processClass")
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * 
     * @return
     *     The fileWatchingProcessClass
     */
    @JsonProperty("fileWatchingProcessClass")
    public String getFileWatchingProcessClass() {
        return fileWatchingProcessClass;
    }

    /**
     * 
     * @param fileWatchingProcessClass
     *     The fileWatchingProcessClass
     */
    @JsonProperty("fileWatchingProcessClass")
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
    public List<JobChainNodeP> getNodes() {
        return nodes;
    }

    /**
     * 
     * @param nodes
     *     The nodes
     */
    @JsonProperty("nodes")
    public void setNodes(List<JobChainNodeP> nodes) {
        this.nodes = nodes;
    }

    /**
     * 
     * @return
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    public List<FileWatchingNodeP> getFileOrderSources() {
        return fileOrderSources;
    }

    /**
     * 
     * @param fileOrderSources
     *     The fileOrderSources
     */
    @JsonProperty("fileOrderSources")
    public void setFileOrderSources(List<FileWatchingNodeP> fileOrderSources) {
        this.fileOrderSources = fileOrderSources;
    }

    /**
     * real end nodes or file sink nodes
     * 
     * @return
     *     The endNodes
     */
    @JsonProperty("endNodes")
    public List<EndNode> getEndNodes() {
        return endNodes;
    }

    /**
     * real end nodes or file sink nodes
     * 
     * @param endNodes
     *     The endNodes
     */
    @JsonProperty("endNodes")
    public void setEndNodes(List<EndNode> endNodes) {
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
    @JsonProperty("configurationDate")
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
    @JsonProperty("configurationDate")
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
        if ((other instanceof JobChainP) == false) {
            return false;
        }
        JobChainP rhs = ((JobChainP) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(title, rhs.title).append(maxOrders, rhs.maxOrders).append(distributed, rhs.distributed).append(processClass, rhs.processClass).append(fileWatchingProcessClass, rhs.fileWatchingProcessClass).append(numOfNodes, rhs.numOfNodes).append(nodes, rhs.nodes).append(fileOrderSources, rhs.fileOrderSources).append(endNodes, rhs.endNodes).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
