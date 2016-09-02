
package com.sos.joc.model.common;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * dateFrom and dateTo filter
 * <p>
 * dates in ISO 8601 format
 * 
 */
@Generated("org.jsonschema2pojo")
public class DateFilterSchema {

    private Date dateFrom;
    private Date dateTo;

    /**
     * 
     * @return
     *     The dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * 
     * @param dateFrom
     *     The dateFrom
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * 
     * @return
     *     The dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * 
     * @param dateTo
     *     The dateTo
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(dateFrom).append(dateTo).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DateFilterSchema) == false) {
            return false;
        }
        DateFilterSchema rhs = ((DateFilterSchema) other);
        return new EqualsBuilder().append(dateFrom, rhs.dateFrom).append(dateTo, rhs.dateTo).isEquals();
    }

}
