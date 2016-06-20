
package com.sos.joc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "survey_date",
    "jobscheduler_id",
    "version",
    "host",
    "port",
    "os",
    "time_zone",
    "cluster_type",
    "started_at",
    "supervisor"
})
public class Jobscheduler_ {

    /**
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("survey_date")
    private Date surveyDate;
    @JsonProperty("jobscheduler_id")
    private String jobschedulerId;
    @JsonProperty("version")
    private String version;
    @JsonProperty("host")
    private String host;
    @JsonProperty("port")
    private Integer port;
    /**
     * 
     */
    @JsonProperty("os")
    private Os os;
    @JsonProperty("time_zone")
    private String timeZone;
    /**
     * 
     */
    @JsonProperty("cluster_type")
    private ClusterType_ clusterType;
    /**
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("started_at")
    private String startedAt;
    /**
     * undefined iff JobScheduler doesn't have a supervisor
     * 
     */
    @JsonProperty("supervisor")
    private Supervisor supervisor;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("survey_date")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The survey_date
     */
    @JsonProperty("survey_date")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public Jobscheduler_ withSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
        return this;
    }

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobscheduler_id")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobscheduler_id
     */
    @JsonProperty("jobscheduler_id")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public Jobscheduler_ withJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
        return this;
    }

    /**
     * 
     * @return
     *     The version
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public Jobscheduler_ withVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * 
     * @return
     *     The host
     */
    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    /**
     * 
     * @param host
     *     The host
     */
    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    public Jobscheduler_ withHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 
     * @return
     *     The port
     */
    @JsonProperty("port")
    public Integer getPort() {
        return port;
    }

    /**
     * 
     * @param port
     *     The port
     */
    @JsonProperty("port")
    public void setPort(Integer port) {
        this.port = port;
    }

    public Jobscheduler_ withPort(Integer port) {
        this.port = port;
        return this;
    }

    /**
     * 
     * @return
     *     The os
     */
    @JsonProperty("os")
    public Os getOs() {
        return os;
    }

    /**
     * 
     * @param os
     *     The os
     */
    @JsonProperty("os")
    public void setOs(Os os) {
        this.os = os;
    }

    public Jobscheduler_ withOs(Os os) {
        this.os = os;
        return this;
    }

    /**
     * 
     * @return
     *     The timeZone
     */
    @JsonProperty("time_zone")
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * 
     * @param timeZone
     *     The time_zone
     */
    @JsonProperty("time_zone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Jobscheduler_ withTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    /**
     * 
     * @return
     *     The clusterType
     */
    @JsonProperty("cluster_type")
    public ClusterType_ getClusterType() {
        return clusterType;
    }

    /**
     * 
     * @param clusterType
     *     The cluster_type
     */
    @JsonProperty("cluster_type")
    public void setClusterType(ClusterType_ clusterType) {
        this.clusterType = clusterType;
    }

    public Jobscheduler_ withClusterType(ClusterType_ clusterType) {
        this.clusterType = clusterType;
        return this;
    }

    /**
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The startedAt
     */
    @JsonProperty("started_at")
    public String getStartedAt() {
        return startedAt;
    }

    /**
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param startedAt
     *     The started_at
     */
    @JsonProperty("started_at")
    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public Jobscheduler_ withStartedAt(String startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    /**
     * undefined iff JobScheduler doesn't have a supervisor
     * 
     * @return
     *     The supervisor
     */
    @JsonProperty("supervisor")
    public Supervisor getSupervisor() {
        return supervisor;
    }

    /**
     * undefined iff JobScheduler doesn't have a supervisor
     * 
     * @param supervisor
     *     The supervisor
     */
    @JsonProperty("supervisor")
    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public Jobscheduler_ withSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Jobscheduler_ withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
