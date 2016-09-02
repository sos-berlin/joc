
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Jobscheduler {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date surveyDate;
    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    private String host;
    /**
     * port
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer port;
    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     */
    private Os os;
    /**
     * 
     * (Required)
     * 
     */
    private String timeZone;
    /**
     * jobscheduler cluster member type
     * <p>
     * 
     * (Required)
     * 
     */
    private ClusterMemberTypeSchema clusterType;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date startedAt;
    /**
     * undefined iff JobScheduler doesn't have a supervisor
     * 
     */
    private Supervisor supervisor;

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
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
     * (Required)
     * 
     * @param surveyDate
     *     The surveyDate
     */
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * (Required)
     * 
     * @param version
     *     The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The host
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * (Required)
     * 
     * @param host
     *     The host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * port
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * port
     * <p>
     * 
     * (Required)
     * 
     * @param port
     *     The port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The os
     */
    public Os getOs() {
        return os;
    }

    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     * @param os
     *     The os
     */
    public void setOs(Os os) {
        this.os = os;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * 
     * (Required)
     * 
     * @param timeZone
     *     The timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * jobscheduler cluster member type
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The clusterType
     */
    public ClusterMemberTypeSchema getClusterType() {
        return clusterType;
    }

    /**
     * jobscheduler cluster member type
     * <p>
     * 
     * (Required)
     * 
     * @param clusterType
     *     The clusterType
     */
    public void setClusterType(ClusterMemberTypeSchema clusterType) {
        this.clusterType = clusterType;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The startedAt
     */
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param startedAt
     *     The startedAt
     */
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * undefined iff JobScheduler doesn't have a supervisor
     * 
     * @return
     *     The supervisor
     */
    public Supervisor getSupervisor() {
        return supervisor;
    }

    /**
     * undefined iff JobScheduler doesn't have a supervisor
     * 
     * @param supervisor
     *     The supervisor
     */
    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(jobschedulerId).append(version).append(host).append(port).append(os).append(timeZone).append(clusterType).append(startedAt).append(supervisor).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jobscheduler) == false) {
            return false;
        }
        Jobscheduler rhs = ((Jobscheduler) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(jobschedulerId, rhs.jobschedulerId).append(version, rhs.version).append(host, rhs.host).append(port, rhs.port).append(os, rhs.os).append(timeZone, rhs.timeZone).append(clusterType, rhs.clusterType).append(startedAt, rhs.startedAt).append(supervisor, rhs.supervisor).isEquals();
    }

}
