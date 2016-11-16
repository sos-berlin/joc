
package com.sos.joc.model.plan;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * undefined for startMode=0
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "begin",
    "end",
    "repeat"
})
public class Period {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("begin")
    private Date begin;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("end")
    private Date end;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("repeat")
    private Long repeat;

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The begin
     */
    @JsonProperty("begin")
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
    @JsonProperty("begin")
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
    @JsonProperty("end")
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
    @JsonProperty("end")
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
    @JsonProperty("repeat")
    public Long getRepeat() {
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
    @JsonProperty("repeat")
    public void setRepeat(Long repeat) {
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
