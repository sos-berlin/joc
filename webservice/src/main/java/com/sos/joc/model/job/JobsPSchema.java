
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.TreeSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobs (permant part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobsPSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    private List<Job> jobs = new ArrayList<Job>();
    /**
     * 
     * (Required)
     * 
     */
    private List<TreeSchema> folders = new ArrayList<TreeSchema>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobs
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobs
     *     The jobs
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The folders
     */
    public List<TreeSchema> getFolders() {
        return folders;
    }

    /**
     * 
     * (Required)
     * 
     * @param folders
     *     The folders
     */
    public void setFolders(List<TreeSchema> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobs).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobsPSchema) == false) {
            return false;
        }
        JobsPSchema rhs = ((JobsPSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobs, rhs.jobs).append(folders, rhs.folders).isEquals();
    }

}
