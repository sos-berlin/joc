
package com.sos.joc.model.job;

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
 * job object (permanent part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "isOrderJob",
    "name",
    "title",
    "estimatedDuration",
    "processClass",
    "maxTasks",
    "locks",
    "usedInJobChains",
    "jobChains",
    "hasDescription",
    "configurationDate"
})
public class JobP {

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
    @JsonProperty("isOrderJob")
    private Boolean isOrderJob;
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
    @JsonProperty("estimatedDuration")
    private Integer estimatedDuration;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("processClass")
    private String processClass;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxTasks")
    private Integer maxTasks;
    /**
     * job locks (permanent)
     * <p>
     * 
     * 
     */
    @JsonProperty("locks")
    private List<LockUseP> locks = new ArrayList<LockUseP>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("usedInJobChains")
    private Integer usedInJobChains;
    /**
     * Only relevant for order jobs when called /jobs/p/... or job/p/...
     * 
     */
    @JsonProperty("jobChains")
    private List<String> jobChains = new ArrayList<String>();
    @JsonProperty("hasDescription")
    private Boolean hasDescription;
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
     *     The isOrderJob
     */
    @JsonProperty("isOrderJob")
    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    /**
     * 
     * @param isOrderJob
     *     The isOrderJob
     */
    @JsonProperty("isOrderJob")
    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
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
     *     The estimatedDuration
     */
    @JsonProperty("estimatedDuration")
    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param estimatedDuration
     *     The estimatedDuration
     */
    @JsonProperty("estimatedDuration")
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("processClass")
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param processClass
     *     The processClass
     */
    @JsonProperty("processClass")
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxTasks
     */
    @JsonProperty("maxTasks")
    public Integer getMaxTasks() {
        return maxTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxTasks
     *     The maxTasks
     */
    @JsonProperty("maxTasks")
    public void setMaxTasks(Integer maxTasks) {
        this.maxTasks = maxTasks;
    }

    /**
     * job locks (permanent)
     * <p>
     * 
     * 
     * @return
     *     The locks
     */
    @JsonProperty("locks")
    public List<LockUseP> getLocks() {
        return locks;
    }

    /**
     * job locks (permanent)
     * <p>
     * 
     * 
     * @param locks
     *     The locks
     */
    @JsonProperty("locks")
    public void setLocks(List<LockUseP> locks) {
        this.locks = locks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The usedInJobChains
     */
    @JsonProperty("usedInJobChains")
    public Integer getUsedInJobChains() {
        return usedInJobChains;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param usedInJobChains
     *     The usedInJobChains
     */
    @JsonProperty("usedInJobChains")
    public void setUsedInJobChains(Integer usedInJobChains) {
        this.usedInJobChains = usedInJobChains;
    }

    /**
     * Only relevant for order jobs when called /jobs/p/... or job/p/...
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public List<String> getJobChains() {
        return jobChains;
    }

    /**
     * Only relevant for order jobs when called /jobs/p/... or job/p/...
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(List<String> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The hasDescription
     */
    @JsonProperty("hasDescription")
    public Boolean getHasDescription() {
        return hasDescription;
    }

    /**
     * 
     * @param hasDescription
     *     The hasDescription
     */
    @JsonProperty("hasDescription")
    public void setHasDescription(Boolean hasDescription) {
        this.hasDescription = hasDescription;
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
        return new HashCodeBuilder().append(surveyDate).append(path).append(isOrderJob).append(name).append(title).append(estimatedDuration).append(processClass).append(maxTasks).append(locks).append(usedInJobChains).append(jobChains).append(hasDescription).append(configurationDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobP) == false) {
            return false;
        }
        JobP rhs = ((JobP) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(isOrderJob, rhs.isOrderJob).append(name, rhs.name).append(title, rhs.title).append(estimatedDuration, rhs.estimatedDuration).append(processClass, rhs.processClass).append(maxTasks, rhs.maxTasks).append(locks, rhs.locks).append(usedInJobChains, rhs.usedInJobChains).append(jobChains, rhs.jobChains).append(hasDescription, rhs.hasDescription).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
