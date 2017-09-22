
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
    "repetition",
    "step"
})
public class Repetition {

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
    @JsonProperty("repetition")
    private RepetitionText repetition;
    @JsonProperty("step")
    private Integer step = 1;

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
     *     The repetition
     */
    @JsonProperty("repetition")
    public RepetitionText getRepetition() {
        return repetition;
    }

    /**
     * 
     * (Required)
     * 
     * @param repetition
     *     The repetition
     */
    @JsonProperty("repetition")
    public void setRepetition(RepetitionText repetition) {
        this.repetition = repetition;
    }

    /**
     * 
     * @return
     *     The step
     */
    @JsonProperty("step")
    public Integer getStep() {
        return step;
    }

    /**
     * 
     * @param step
     *     The step
     */
    @JsonProperty("step")
    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).append(to).append(repetition).append(step).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Repetition) == false) {
            return false;
        }
        Repetition rhs = ((Repetition) other);
        return new EqualsBuilder().append(from, rhs.from).append(to, rhs.to).append(repetition, rhs.repetition).append(step, rhs.step).isEquals();
    }

}
