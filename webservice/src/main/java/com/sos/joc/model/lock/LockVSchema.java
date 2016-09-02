
package com.sos.joc.model.lock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * lock object (volatile part)
 * <p>
 * The lock is free iff no holders specified
 * 
 */
@Generated("org.jsonschema2pojo")
public class LockVSchema {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    private String name;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer maxNonExclusive;
    /**
     * 
     */
    private Holders holders;
    /**
     * Collection of jobs which have to wait until the lock is free
     * 
     */
    private List<Queue> queue = new ArrayList<Queue>();
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    private ConfigurationStatusSchema configurationStatus;

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxNonExclusive
     */
    public Integer getMaxNonExclusive() {
        return maxNonExclusive;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxNonExclusive
     *     The maxNonExclusive
     */
    public void setMaxNonExclusive(Integer maxNonExclusive) {
        this.maxNonExclusive = maxNonExclusive;
    }

    /**
     * 
     * @return
     *     The holders
     */
    public Holders getHolders() {
        return holders;
    }

    /**
     * 
     * @param holders
     *     The holders
     */
    public void setHolders(Holders holders) {
        this.holders = holders;
    }

    /**
     * Collection of jobs which have to wait until the lock is free
     * 
     * @return
     *     The queue
     */
    public List<Queue> getQueue() {
        return queue;
    }

    /**
     * Collection of jobs which have to wait until the lock is free
     * 
     * @param queue
     *     The queue
     */
    public void setQueue(List<Queue> queue) {
        this.queue = queue;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(maxNonExclusive).append(holders).append(queue).append(configurationStatus).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LockVSchema) == false) {
            return false;
        }
        LockVSchema rhs = ((LockVSchema) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(maxNonExclusive, rhs.maxNonExclusive).append(holders, rhs.holders).append(queue, rhs.queue).append(configurationStatus, rhs.configurationStatus).isEquals();
    }

}
