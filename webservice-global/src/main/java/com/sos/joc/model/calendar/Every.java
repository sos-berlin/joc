
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * every
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "from",
    "to",
    "dateEntity",
    "interval"
})
public class Every {

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("from")
    private String from;
    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("to")
    private String to;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dateEntity")
    private DateEntity dateEntity;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("interval")
    private Integer interval;

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @return
     *     The from
     */
    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @param from
     *     The from
     */
    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @return
     *     The to
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @param to
     *     The to
     */
    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The dateEntity
     */
    @JsonProperty("dateEntity")
    public DateEntity getDateEntity() {
        return dateEntity;
    }

    /**
     * 
     * (Required)
     * 
     * @param dateEntity
     *     The dateEntity
     */
    @JsonProperty("dateEntity")
    public void setDateEntity(DateEntity dateEntity) {
        this.dateEntity = dateEntity;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The interval
     */
    @JsonProperty("interval")
    public Integer getInterval() {
        return interval;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param interval
     *     The interval
     */
    @JsonProperty("interval")
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).append(to).append(dateEntity).append(interval).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Every) == false) {
            return false;
        }
        Every rhs = ((Every) other);
        return new EqualsBuilder().append(from, rhs.from).append(to, rhs.to).append(dateEntity, rhs.dateEntity).append(interval, rhs.interval).isEquals();
    }

}
