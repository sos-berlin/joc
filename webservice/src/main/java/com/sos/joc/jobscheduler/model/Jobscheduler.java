
package com.sos.joc.jobscheduler.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
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
    "time_zone",
    "cluster_type",
    "started_at"
})
public class Jobscheduler {

    /**
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
    @JsonProperty("time_zone")
    private String timeZone;
    /**
     * 
     */
    @JsonProperty("cluster_type")
    private ClusterType clusterType;
    /**
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("started_at")
    private String startedAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("survey_date")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The survey_date
     */
    @JsonProperty("survey_date")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public Jobscheduler withSurveyDate(Date surveyDate) {
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

    public Jobscheduler withJobschedulerId(String jobschedulerId) {
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

    public Jobscheduler withVersion(String version) {
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

    public Jobscheduler withHost(String host) {
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

    public Jobscheduler withPort(Integer port) {
        this.port = port;
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

    public Jobscheduler withTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    /**
     * 
     * @return
     *     The clusterType
     */
    @JsonProperty("cluster_type")
    public ClusterType getClusterType() {
        return clusterType;
    }

    /**
     * 
     * @param clusterType
     *     The cluster_type
     */
    @JsonProperty("cluster_type")
    public void setClusterType(ClusterType clusterType) {
        this.clusterType = clusterType;
    }

    public Jobscheduler withClusterType(ClusterType clusterType) {
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

    public Jobscheduler withStartedAt(String startedAt) {
        this.startedAt = startedAt;
        return this;
    }



}
