
package com.sos.joc.model.plan;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * undefined for startMode=0
 * 
 */
@Generated("org.jsonschema2pojo")
public class Period {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date begin;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date end;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer repeat;

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The begin
     */
    public Date getBegin() {
        return begin;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param begin
     *     The begin
     */
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param end
     *     The end
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The repeat
     */
    public Integer getRepeat() {
        return repeat;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param repeat
     *     The repeat
     */
    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(begin).append(end).append(repeat).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Period) == false) {
            return false;
        }
        Period rhs = ((Period) other);
        return new EqualsBuilder().append(begin, rhs.begin).append(end, rhs.end).append(repeat, rhs.repeat).isEquals();
    }

}
