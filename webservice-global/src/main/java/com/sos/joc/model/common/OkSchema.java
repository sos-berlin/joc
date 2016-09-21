
package com.sos.joc.model.common;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * ok answer
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class OkSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * is also required for permanent or volatile requests
     * 
     */
    private Date surveyDate;
    /**
     * 
     * (Required)
     * 
     */
    private Boolean ok;

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
     * is also required for permanent or volatile requests
     * 
     * @return
     *     The surveyDate
     */
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * is also required for permanent or volatile requests
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
     *     The ok
     */
    public Boolean getOk() {
        return ok;
    }

    /**
     * 
     * (Required)
     * 
     * @param ok
     *     The ok
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(surveyDate).append(ok).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OkSchema) == false) {
            return false;
        }
        OkSchema rhs = ((OkSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(surveyDate, rhs.surveyDate).append(ok, rhs.ok).isEquals();
    }

}
