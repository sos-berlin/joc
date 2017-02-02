
package com.sos.joc.model.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * comments
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "forceCommentsForAuditLog",
    "comments"
})
public class Comments {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("forceCommentsForAuditLog")
    private Boolean forceCommentsForAuditLog = false;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("comments")
    private List<String> comments = new ArrayList<String>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
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
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The forceCommentsForAuditLog
     */
    @JsonProperty("forceCommentsForAuditLog")
    public Boolean getForceCommentsForAuditLog() {
        return forceCommentsForAuditLog;
    }

    /**
     * 
     * (Required)
     * 
     * @param forceCommentsForAuditLog
     *     The forceCommentsForAuditLog
     */
    @JsonProperty("forceCommentsForAuditLog")
    public void setForceCommentsForAuditLog(Boolean forceCommentsForAuditLog) {
        this.forceCommentsForAuditLog = forceCommentsForAuditLog;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The comments
     */
    @JsonProperty("comments")
    public List<String> getComments() {
        return comments;
    }

    /**
     * 
     * (Required)
     * 
     * @param comments
     *     The comments
     */
    @JsonProperty("comments")
    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(forceCommentsForAuditLog).append(comments).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Comments) == false) {
            return false;
        }
        Comments rhs = ((Comments) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(forceCommentsForAuditLog, rhs.forceCommentsForAuditLog).append(comments, rhs.comments).isEquals();
    }

}
