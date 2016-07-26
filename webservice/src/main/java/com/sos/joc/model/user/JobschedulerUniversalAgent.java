
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
    "view",
    "terminate",
    "abort",
    "restart"
})
public class JobschedulerUniversalAgent {

    @JsonProperty("view")
    private View__ view;
    @JsonProperty("terminate")
    private Boolean terminate = false;
    @JsonProperty("abort")
    private Boolean abort = false;
    @JsonProperty("restart")
    private Restart_ restart;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The view
     */
    @JsonProperty("view")
    public View__ getView() {
        return view;
    }

    /**
     * 
     * @param view
     *     The view
     */
    @JsonProperty("view")
    public void setView(View__ view) {
        this.view = view;
    }

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

    /**
     * 
     * @return
     *     The restart
     */
    @JsonProperty("restart")
    public Restart_ getRestart() {
        return restart;
    }

    /**
     * 
     * @param restart
     *     The restart
     */
    @JsonProperty("restart")
    public void setRestart(Restart_ restart) {
        this.restart = restart;
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
        return new HashCodeBuilder().append(view).append(terminate).append(abort).append(restart).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobschedulerUniversalAgent) == false) {
            return false;
        }
        JobschedulerUniversalAgent rhs = ((JobschedulerUniversalAgent) other);
        return new EqualsBuilder().append(view, rhs.view).append(terminate, rhs.terminate).append(abort, rhs.abort).append(restart, rhs.restart).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
