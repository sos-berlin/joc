
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
    "terminateFailSafe",
    "restart"
})
public class JobschedulerMasterCluster {

    @JsonProperty("view")
    private View_ view;
    @JsonProperty("terminate")
    private Boolean terminate = false;
    @JsonProperty("terminateFailSafe")
    private Boolean terminateFailSafe = false;
    @JsonProperty("restart")
    private Boolean restart = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The view
     */
    @JsonProperty("view")
    public View_ getView() {
        return view;
    }

    /**
     * 
     * @param view
     *     The view
     */
    @JsonProperty("view")
    public void setView(View_ view) {
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
     *     The terminateFailSafe
     */
    @JsonProperty("terminateFailSafe")
    public Boolean getTerminateFailSafe() {
        return terminateFailSafe;
    }

    /**
     * 
     * @param terminateFailSafe
     *     The terminateFailSafe
     */
    @JsonProperty("terminateFailSafe")
    public void setTerminateFailSafe(Boolean terminateFailSafe) {
        this.terminateFailSafe = terminateFailSafe;
    }

    /**
     * 
     * @return
     *     The restart
     */
    @JsonProperty("restart")
    public Boolean getRestart() {
        return restart;
    }

    /**
     * 
     * @param restart
     *     The restart
     */
    @JsonProperty("restart")
    public void setRestart(Boolean restart) {
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
        return new HashCodeBuilder().append(view).append(terminate).append(terminateFailSafe).append(restart).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobschedulerMasterCluster) == false) {
            return false;
        }
        JobschedulerMasterCluster rhs = ((JobschedulerMasterCluster) other);
        return new EqualsBuilder().append(view, rhs.view).append(terminate, rhs.terminate).append(terminateFailSafe, rhs.terminateFailSafe).append(restart, rhs.restart).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
