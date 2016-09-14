
package com.sos.joc.model.plan;

import java.util.Date;
import javax.annotation.Generated;
import com.sos.joc.model.common.ErrorSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * dailyPlanItem
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class PlanItem {

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
    private String job;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String jobChain;
    private String orderId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date plannedStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date expectedEndTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date startTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date endTime;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer historyId;
    /**
     * only for orders
     * 
     */
    private String node;
    /**
     * only for standalone jobs
     * 
     */
    private String exitCode;
    /**
     * error
     * <p>
     * 
     * 
     */
    private ErrorSchema error;
    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     */
    private Integer startMode = 0;
    /**
     * undefined for startMode=0
     * 
     */
    private Period period;
    private Boolean late;
    private State state;

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
     *     The job
     */
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
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * 
     * @return
     *     The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId
     *     The orderId
     */
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
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The historyId
     */
    public Integer getHistoryId() {
        return historyId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param historyId
     *     The historyId
     */
    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    /**
     * only for orders
     * 
     * @return
     *     The node
     */
    public String getNode() {
        return node;
    }

    /**
     * only for orders
     * 
     * @param node
     *     The node
     */
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * only for standalone jobs
     * 
     * @return
     *     The exitCode
     */
    public String getExitCode() {
        return exitCode;
    }

    /**
     * only for standalone jobs
     * 
     * @param exitCode
     *     The exitCode
     */
    public void setExitCode(String exitCode) {
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
    public ErrorSchema getError() {
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
    public void setError(ErrorSchema error) {
        this.error = error;
    }

    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     * @return
     *     The startMode
     */
    public Integer getStartMode() {
        return startMode;
    }

    /**
     *  0=single_start, 1=start_start_repeat, 2=start_end_repeat
     * 
     * @param startMode
     *     The startMode
     */
    public void setStartMode(Integer startMode) {
        this.startMode = startMode;
    }

    /**
     * undefined for startMode=0
     * 
     * @return
     *     The period
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * undefined for startMode=0
     * 
     * @param period
     *     The period
     */
    public void setPeriod(Period period) {
        this.period = period;
    }

    /**
     * 
     * @return
     *     The late
     */
    public Boolean getLate() {
        return late;
    }

    /**
     * 
     * @param late
     *     The late
     */
    public void setLate(Boolean late) {
        this.late = late;
    }

    /**
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(State state) {
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
