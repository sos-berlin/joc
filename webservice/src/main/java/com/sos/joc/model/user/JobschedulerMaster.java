
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
    "pause",
    "continue",
    "terminate",
    "abort",
    "restart",
    "manageCategories"
})
public class JobschedulerMaster {

    @JsonProperty("view")
    private View view;
    @JsonProperty("pause")
    private Boolean pause = false;
    @JsonProperty("continue")
    private Boolean _continue = false;
    @JsonProperty("terminate")
    private Boolean terminate = false;
    @JsonProperty("abort")
    private Boolean abort = false;
    @JsonProperty("restart")
    private Restart restart;
    @JsonProperty("manageCategories")
    private Boolean manageCategories = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The view
     */
    @JsonProperty("view")
    public View getView() {
        return view;
    }

    /**
     * 
     * @param view
     *     The view
     */
    @JsonProperty("view")
    public void setView(View view) {
        this.view = view;
    }

    /**
     * 
     * @return
     *     The pause
     */
    @JsonProperty("pause")
    public Boolean getPause() {
        return pause;
    }

    /**
     * 
     * @param pause
     *     The pause
     */
    @JsonProperty("pause")
    public void setPause(Boolean pause) {
        this.pause = pause;
    }

    /**
     * 
     * @return
     *     The _continue
     */
    @JsonProperty("continue")
    public Boolean getContinue() {
        return _continue;
    }

    /**
     * 
     * @param _continue
     *     The continue
     */
    @JsonProperty("continue")
    public void setContinue(Boolean _continue) {
        this._continue = _continue;
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
    public Restart getRestart() {
        return restart;
    }

    /**
     * 
     * @param restart
     *     The restart
     */
    @JsonProperty("restart")
    public void setRestart(Restart restart) {
        this.restart = restart;
    }

    /**
     * 
     * @return
     *     The manageCategories
     */
    @JsonProperty("manageCategories")
    public Boolean getManageCategories() {
        return manageCategories;
    }

    /**
     * 
     * @param manageCategories
     *     The manageCategories
     */
    @JsonProperty("manageCategories")
    public void setManageCategories(Boolean manageCategories) {
        this.manageCategories = manageCategories;
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
        return new HashCodeBuilder().append(view).append(pause).append(_continue).append(terminate).append(abort).append(restart).append(manageCategories).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobschedulerMaster) == false) {
            return false;
        }
        JobschedulerMaster rhs = ((JobschedulerMaster) other);
        return new EqualsBuilder().append(view, rhs.view).append(pause, rhs.pause).append(_continue, rhs._continue).append(terminate, rhs.terminate).append(abort, rhs.abort).append(restart, rhs.restart).append(manageCategories, rhs.manageCategories).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
