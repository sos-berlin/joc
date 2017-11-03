
package com.sos.joc.model.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "successful",
    "failed"
})
public class JobsHistoricSummary {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("successful")
    private Integer successful;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("failed")
    private Integer failed;

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The successful
     */
    @JsonProperty("successful")
    public Integer getSuccessful() {
        return successful;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param successful
     *     The successful
     */
    @JsonProperty("successful")
    public void setSuccessful(Integer successful) {
        this.successful = successful;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The failed
     */
    @JsonProperty("failed")
    public Integer getFailed() {
        return failed;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param failed
     *     The failed
     */
    @JsonProperty("failed")
    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(successful).append(failed).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobsHistoricSummary) == false) {
            return false;
        }
        JobsHistoricSummary rhs = ((JobsHistoricSummary) other);
        return new EqualsBuilder().append(successful, rhs.successful).append(failed, rhs.failed).isEquals();
    }

}
