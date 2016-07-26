
package com.sos.joc.model.order;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order object in grouped history collection
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "path",
    "orderId",
    "jobChain",
    "numOfSuccessful",
    "numOfFailed"
})
public class GroupedHistorySchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private Pattern path;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    private Pattern jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("numOfSuccessful")
    private Integer numOfSuccessful;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("numOfFailed")
    private Integer numOfFailed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public Pattern getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(Pattern path) {
        this.path = path;
    }

    /**
     * 
     * (Required)
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
     * (Required)
     * 
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public Pattern getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(Pattern jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The numOfSuccessful
     */
    @JsonProperty("numOfSuccessful")
    public Integer getNumOfSuccessful() {
        return numOfSuccessful;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param numOfSuccessful
     *     The numOfSuccessful
     */
    @JsonProperty("numOfSuccessful")
    public void setNumOfSuccessful(Integer numOfSuccessful) {
        this.numOfSuccessful = numOfSuccessful;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The numOfFailed
     */
    @JsonProperty("numOfFailed")
    public Integer getNumOfFailed() {
        return numOfFailed;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param numOfFailed
     *     The numOfFailed
     */
    @JsonProperty("numOfFailed")
    public void setNumOfFailed(Integer numOfFailed) {
        this.numOfFailed = numOfFailed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(orderId).append(jobChain).append(numOfSuccessful).append(numOfFailed).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GroupedHistorySchema) == false) {
            return false;
        }
        GroupedHistorySchema rhs = ((GroupedHistorySchema) other);
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(numOfSuccessful, rhs.numOfSuccessful).append(numOfFailed, rhs.numOfFailed).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
