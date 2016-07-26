
package com.sos.joc.model.user;

import java.util.HashMap;
import java.util.Map;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "status",
    "configuration",
    "history"
})
public class View______ {

    @JsonProperty("status")
    private Boolean status = false;
    @JsonProperty("configuration")
    private Boolean configuration = false;
    @JsonProperty("history")
    private Boolean history = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public Boolean getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    public Boolean getConfiguration() {
        return configuration;
    }

    /**
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    public void setConfiguration(Boolean configuration) {
        this.configuration = configuration;
    }

    /**
     * 
     * @return
     *     The history
     */
    @JsonProperty("history")
    public Boolean getHistory() {
        return history;
    }

    /**
     * 
     * @param history
     *     The history
     */
    @JsonProperty("history")
    public void setHistory(Boolean history) {
        this.history = history;
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
        return new HashCodeBuilder().append(status).append(configuration).append(history).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof View______) == false) {
            return false;
        }
        View______ rhs = ((View______) other);
        return new EqualsBuilder().append(status, rhs.status).append(configuration, rhs.configuration).append(history, rhs.history).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
