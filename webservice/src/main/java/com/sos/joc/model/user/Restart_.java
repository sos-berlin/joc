
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
    "terminate",
    "abort"
})
public class Restart_ {

    @JsonProperty("terminate")
    private Boolean terminate = false;
    @JsonProperty("abort")
    private Boolean abort = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The terminate
     */
    @JsonProperty("terminate")
    public Boolean getTerminate() {
        return terminate;
    }

    /**
     * 
     * @param terminate
     *     The terminate
     */
    @JsonProperty("terminate")
    public void setTerminate(Boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * 
     * @return
     *     The abort
     */
    @JsonProperty("abort")
    public Boolean getAbort() {
        return abort;
    }

    /**
     * 
     * @param abort
     *     The abort
     */
    @JsonProperty("abort")
    public void setAbort(Boolean abort) {
        this.abort = abort;
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
        return new HashCodeBuilder().append(terminate).append(abort).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Restart_) == false) {
            return false;
        }
        Restart_ rhs = ((Restart_) other);
        return new EqualsBuilder().append(terminate, rhs.terminate).append(abort, rhs.abort).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
