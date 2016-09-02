
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler agent (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentPSchema {

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
     * url can be different against host/port if agent behind a proxy
     * (Required)
     * 
     */
    private String url;
    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     */
    private Os os;
    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     */
    private State state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date startedAt;
    /**
     * Collection of process class' paths
     * (Required)
     * 
     */
    private List<String> clusters = new ArrayList<String>();

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
     * url can be different against host/port if agent behind a proxy
     * (Required)
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * url can be different against host/port if agent behind a proxy
     * (Required)
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
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
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    public void setState(State state) {
        this.state = state;
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
     * Collection of process class' paths
     * (Required)
     * 
     * @return
     *     The clusters
     */
    public List<String> getClusters() {
        return clusters;
    }

    /**
     * Collection of process class' paths
     * (Required)
     * 
     * @param clusters
     *     The clusters
     */
    public void setClusters(List<String> clusters) {
        this.clusters = clusters;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(version).append(host).append(port).append(url).append(os).append(state).append(startedAt).append(clusters).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentPSchema) == false) {
            return false;
        }
        AgentPSchema rhs = ((AgentPSchema) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(version, rhs.version).append(host, rhs.host).append(port, rhs.port).append(url, rhs.url).append(os, rhs.os).append(state, rhs.state).append(startedAt, rhs.startedAt).append(clusters, rhs.clusters).isEquals();
    }

}
