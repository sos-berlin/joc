
package com.sos.joc.model.yade;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Err;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * save and response configuration
 * <p>
 * compact=true -> required fields + possibly profile, mandator, target
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "id",
    "parent_id",
    "profile",
    "mandator",
    "state",
    "_operation",
    "start",
    "end",
    "error",
    "source",
    "target",
    "jump",
    "numOfFiles",
    "hasIntervention",
    "jobschedulerId",
    "orderId",
    "jobChain",
    "job"
})
public class Transfer {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("parent_id")
    private Long parent_id;
    @JsonProperty("profile")
    private String profile;
    @JsonProperty("mandator")
    private String mandator;
    /**
     * transfer state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private TransferState state;
    /**
     *  yade operation
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_operation")
    private Operation _operation;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("start")
    private Date start;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("end")
    private Date end;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * (Required)
     * 
     */
    @JsonProperty("source")
    private ProtocolFragment source;
    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * 
     */
    @JsonProperty("target")
    private ProtocolFragment target;
    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * 
     */
    @JsonProperty("jump")
    private ProtocolFragment jump;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfFiles")
    private Integer numOfFiles;
    @JsonProperty("hasIntervention")
    private Boolean hasIntervention = false;
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("orderId")
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    private String job;

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
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The parent_id
     */
    @JsonProperty("parent_id")
    public Long getParent_id() {
        return parent_id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param parent_id
     *     The parent_id
     */
    @JsonProperty("parent_id")
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * 
     * @return
     *     The profile
     */
    @JsonProperty("profile")
    public String getProfile() {
        return profile;
    }

    /**
     * 
     * @param profile
     *     The profile
     */
    @JsonProperty("profile")
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * 
     * @return
     *     The mandator
     */
    @JsonProperty("mandator")
    public String getMandator() {
        return mandator;
    }

    /**
     * 
     * @param mandator
     *     The mandator
     */
    @JsonProperty("mandator")
    public void setMandator(String mandator) {
        this.mandator = mandator;
    }

    /**
     * transfer state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public TransferState getState() {
        return state;
    }

    /**
     * transfer state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(TransferState state) {
        this.state = state;
    }

    /**
     *  yade operation
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _operation
     */
    @JsonProperty("_operation")
    public Operation get_operation() {
        return _operation;
    }

    /**
     *  yade operation
     * <p>
     * 
     * (Required)
     * 
     * @param _operation
     *     The _operation
     */
    @JsonProperty("_operation")
    public void set_operation(Operation _operation) {
        this._operation = _operation;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The start
     */
    @JsonProperty("start")
    public Date getStart() {
        return start;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param start
     *     The start
     */
    @JsonProperty("start")
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The end
     */
    @JsonProperty("end")
    public Date getEnd() {
        return end;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param end
     *     The end
     */
    @JsonProperty("end")
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @return
     *     The error
     */
    @JsonProperty("error")
    public Err getError() {
        return error;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @param error
     *     The error
     */
    @JsonProperty("error")
    public void setError(Err error) {
        this.error = error;
    }

    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * (Required)
     * 
     * @return
     *     The source
     */
    @JsonProperty("source")
    public ProtocolFragment getSource() {
        return source;
    }

    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * (Required)
     * 
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(ProtocolFragment source) {
        this.source = source;
    }

    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * 
     * @return
     *     The target
     */
    @JsonProperty("target")
    public ProtocolFragment getTarget() {
        return target;
    }

    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * 
     * @param target
     *     The target
     */
    @JsonProperty("target")
    public void setTarget(ProtocolFragment target) {
        this.target = target;
    }

    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * 
     * @return
     *     The jump
     */
    @JsonProperty("jump")
    public ProtocolFragment getJump() {
        return jump;
    }

    /**
     * protocol, host, port, account
     * <p>
     * compact=true -> only required fields
     * 
     * @param jump
     *     The jump
     */
    @JsonProperty("jump")
    public void setJump(ProtocolFragment jump) {
        this.jump = jump;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfFiles
     */
    @JsonProperty("numOfFiles")
    public Integer getNumOfFiles() {
        return numOfFiles;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfFiles
     *     The numOfFiles
     */
    @JsonProperty("numOfFiles")
    public void setNumOfFiles(Integer numOfFiles) {
        this.numOfFiles = numOfFiles;
    }

    /**
     * 
     * @return
     *     The hasIntervention
     */
    @JsonProperty("hasIntervention")
    public Boolean getHasIntervention() {
        return hasIntervention;
    }

    /**
     * 
     * @param hasIntervention
     *     The hasIntervention
     */
    @JsonProperty("hasIntervention")
    public void setHasIntervention(Boolean hasIntervention) {
        this.hasIntervention = hasIntervention;
    }

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The orderId
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(id).append(parent_id).append(profile).append(mandator).append(state).append(_operation).append(start).append(end).append(error).append(source).append(target).append(jump).append(numOfFiles).append(hasIntervention).append(jobschedulerId).append(orderId).append(jobChain).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Transfer) == false) {
            return false;
        }
        Transfer rhs = ((Transfer) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(id, rhs.id).append(parent_id, rhs.parent_id).append(profile, rhs.profile).append(mandator, rhs.mandator).append(state, rhs.state).append(_operation, rhs._operation).append(start, rhs.start).append(end, rhs.end).append(error, rhs.error).append(source, rhs.source).append(target, rhs.target).append(jump, rhs.jump).append(numOfFiles, rhs.numOfFiles).append(hasIntervention, rhs.hasIntervention).append(jobschedulerId, rhs.jobschedulerId).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(job, rhs.job).isEquals();
    }

}
