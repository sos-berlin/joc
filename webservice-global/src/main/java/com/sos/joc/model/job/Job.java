
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.NameValuePairsSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job object (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Job {

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
    private Boolean isOrderJob;
    private String name;
    private String title;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer estimatedDuration;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String processClass;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer maxTasks;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    private List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
    /**
     * job locks (permanent)
     * <p>
     * 
     * 
     */
    private List<Lock> locks = new ArrayList<Lock>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer usedInJobChains;
    /**
     * Only relevant for order jobs when called /jobs/p/... or job/p/...
     * 
     */
    private List<String> jobChains = new ArrayList<String>();
    private Boolean hasDescription;
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
     *     The isOrderJob
     */
    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    /**
     * 
     * @param isOrderJob
     *     The isOrderJob
     */
    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
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
     *     The estimatedDuration
     */
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
    public void setMaxTasks(Integer maxTasks) {
        this.maxTasks = maxTasks;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @return
     *     The params
     */
    public List<NameValuePairsSchema> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @param params
     *     The params
     */
    public void setParams(List<NameValuePairsSchema> params) {
        this.params = params;
    }

    /**
     * job locks (permanent)
     * <p>
     * 
     * 
     * @return
     *     The locks
     */
    public List<Lock> getLocks() {
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
    public void setLocks(List<Lock> locks) {
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
    public void setUsedInJobChains(Integer usedInJobChains) {
        this.usedInJobChains = usedInJobChains;
    }

    /**
     * Only relevant for order jobs when called /jobs/p/... or job/p/...
     * 
     * @return
     *     The jobChains
     */
    public List<String> getJobChains() {
        return jobChains;
    }

    /**
     * Only relevant for order jobs when called /jobs/p/... or job/p/...
     * 
     * @param jobChains
     *     The jobChains
     */
    public void setJobChains(List<String> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The hasDescription
     */
    public Boolean getHasDescription() {
        return hasDescription;
    }

    /**
     * 
     * @param hasDescription
     *     The hasDescription
     */
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
        return new HashCodeBuilder().append(surveyDate).append(path).append(isOrderJob).append(name).append(title).append(estimatedDuration).append(processClass).append(maxTasks).append(params).append(locks).append(usedInJobChains).append(jobChains).append(hasDescription).append(configurationDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job) == false) {
            return false;
        }
        Job rhs = ((Job) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(isOrderJob, rhs.isOrderJob).append(name, rhs.name).append(title, rhs.title).append(estimatedDuration, rhs.estimatedDuration).append(processClass, rhs.processClass).append(maxTasks, rhs.maxTasks).append(params, rhs.params).append(locks, rhs.locks).append(usedInJobChains, rhs.usedInJobChains).append(jobChains, rhs.jobChains).append(hasDescription, rhs.hasDescription).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
