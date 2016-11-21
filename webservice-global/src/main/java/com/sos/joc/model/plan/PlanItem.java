
package com.sos.joc.model.plan;

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
 * dailyPlanItem
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "job",
    "jobChain",
    "orderId",
    "plannedStartTime",
    "expectedEndTime",
    "startTime",
    "endTime",
    "historyId",
    "node",
    "exitCode",
    "error",
    "startMode",
    "period",
    "late",
    "state"
})
public class PlanItem {

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
    @JsonProperty("job")
    private String job;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    @JsonProperty("orderId")
    private String orderId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStartTime")
    private Date plannedStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("expectedEndTime")
    private Date expectedEndTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startTime")
    private Date startTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("endTime")
    private Date endTime;
    @JsonProperty("historyId")
    private String historyId;
    /**
     * only for orders
     * 
     */
    @JsonProperty("node")
    private String node;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    private Integer exitCode;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     */
    @JsonProperty("startMode")
    private Integer startMode = 0;
    /**
     * undefined for startMode=0
     * 
     */
    @JsonProperty("period")
    private Period period;
    @JsonProperty("late")
    private Boolean late;
    /**
     * plan state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private PlanState state;

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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The plannedStartTime
     */
    @JsonProperty("plannedStartTime")
    public Date getPlannedStartTime() {
        return plannedStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param plannedStartTime
     *     The plannedStartTime
     */
    @JsonProperty("plannedStartTime")
    public void setPlannedStartTime(Date plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The expectedEndTime
     */
    @JsonProperty("expectedEndTime")
    public Date getExpectedEndTime() {
        return expectedEndTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param expectedEndTime
     *     The expectedEndTime
     */
    @JsonProperty("expectedEndTime")
    public void setExpectedEndTime(Date expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The startTime
     */
    @JsonProperty("startTime")
    public Date getStartTime() {
        return startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param startTime
     *     The startTime
     */
    @JsonProperty("startTime")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The endTime
     */
    @JsonProperty("endTime")
    public Date getEndTime() {
        return endTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param endTime
     *     The endTime
     */
    @JsonProperty("endTime")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     * @return
     *     The historyId
     */
    @JsonProperty("historyId")
    public String getHistoryId() {
        return historyId;
    }

    /**
     * 
     * @param historyId
     *     The historyId
     */
    @JsonProperty("historyId")
    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    /**
     * only for orders
     * 
     * @return
     *     The node
     */
    @JsonProperty("node")
    public String getNode() {
        return node;
    }

    /**
     * only for orders
     * 
     * @param node
     *     The node
     */
    @JsonProperty("node")
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The exitCode
     */
    @JsonProperty("exitCode")
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param exitCode
     *     The exitCode
     */
    @JsonProperty("exitCode")
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
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
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     * @return
     *     The startMode
     */
    @JsonProperty("startMode")
    public Integer getStartMode() {
        return startMode;
    }

    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     * @param startMode
     *     The startMode
     */
    @JsonProperty("startMode")
    public void setStartMode(Integer startMode) {
        this.startMode = startMode;
    }

    /**
     * undefined for startMode=0
     * 
     * @return
     *     The period
     */
    @JsonProperty("period")
    public Period getPeriod() {
        return period;
    }

    /**
     * undefined for startMode=0
     * 
     * @param period
     *     The period
     */
    @JsonProperty("period")
    public void setPeriod(Period period) {
        this.period = period;
    }

    /**
     * 
     * @return
     *     The late
     */
    @JsonProperty("late")
    public Boolean getLate() {
        return late;
    }

    /**
     * 
     * @param late
     *     The late
     */
    @JsonProperty("late")
    public void setLate(Boolean late) {
        this.late = late;
    }

    /**
     * plan state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public PlanState getState() {
        return state;
    }

    /**
     * plan state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(PlanState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(job).append(jobChain).append(orderId).append(plannedStartTime).append(expectedEndTime).append(startTime).append(endTime).append(historyId).append(node).append(exitCode).append(error).append(startMode).append(period).append(late).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlanItem) == false) {
            return false;
        }
        PlanItem rhs = ((PlanItem) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(job, rhs.job).append(jobChain, rhs.jobChain).append(orderId, rhs.orderId).append(plannedStartTime, rhs.plannedStartTime).append(expectedEndTime, rhs.expectedEndTime).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(historyId, rhs.historyId).append(node, rhs.node).append(exitCode, rhs.exitCode).append(error, rhs.error).append(startMode, rhs.startMode).append(period, rhs.period).append(late, rhs.late).append(state, rhs.state).isEquals();
    }

}
