
package com.sos.joc.model.audit;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * audit
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "account",
    "request",
    "created",
    "jobschedulerId",
    "comment",
    "parameters",
    "job",
    "jobChain",
    "orderId",
    "timeSpent",
    "ticketLink"
})
public class AuditLogItem {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("account")
    private String account;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("request")
    private String request;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("created")
    private Date created;
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("comment")
    private String comment;
    /**
     * JSON object as string, parameter of request
     * 
     */
    @JsonProperty("parameters")
    private String parameters;
    @JsonProperty("job")
    private String job;
    @JsonProperty("jobChain")
    private String jobChain;
    @JsonProperty("orderId")
    private String orderId;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeSpent")
    private Integer timeSpent;
    @JsonProperty("ticketLink")
    private String ticketLink;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The account
     */
    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    /**
     * 
     * (Required)
     * 
     * @param account
     *     The account
     */
    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The request
     */
    @JsonProperty("request")
    public String getRequest() {
        return request;
    }

    /**
     * 
     * (Required)
     * 
     * @param request
     *     The request
     */
    @JsonProperty("request")
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The created
     */
    @JsonProperty("created")
    public Date getCreated() {
        return created;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param created
     *     The created
     */
    @JsonProperty("created")
    public void setCreated(Date created) {
        this.created = created;
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
     *     The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * 
     * @param comment
     *     The comment
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * JSON object as string, parameter of request
     * 
     * @return
     *     The parameters
     */
    @JsonProperty("parameters")
    public String getParameters() {
        return parameters;
    }

    /**
     * JSON object as string, parameter of request
     * 
     * @param parameters
     *     The parameters
     */
    @JsonProperty("parameters")
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The timeSpent
     */
    @JsonProperty("timeSpent")
    public Integer getTimeSpent() {
        return timeSpent;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param timeSpent
     *     The timeSpent
     */
    @JsonProperty("timeSpent")
    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    /**
     * 
     * @return
     *     The ticketLink
     */
    @JsonProperty("ticketLink")
    public String getTicketLink() {
        return ticketLink;
    }

    /**
     * 
     * @param ticketLink
     *     The ticketLink
     */
    @JsonProperty("ticketLink")
    public void setTicketLink(String ticketLink) {
        this.ticketLink = ticketLink;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(account).append(request).append(created).append(jobschedulerId).append(comment).append(parameters).append(job).append(jobChain).append(orderId).append(timeSpent).append(ticketLink).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AuditLogItem) == false) {
            return false;
        }
        AuditLogItem rhs = ((AuditLogItem) other);
        return new EqualsBuilder().append(account, rhs.account).append(request, rhs.request).append(created, rhs.created).append(jobschedulerId, rhs.jobschedulerId).append(comment, rhs.comment).append(parameters, rhs.parameters).append(job, rhs.job).append(jobChain, rhs.jobChain).append(orderId, rhs.orderId).append(timeSpent, rhs.timeSpent).append(ticketLink, rhs.ticketLink).isEquals();
    }

}
