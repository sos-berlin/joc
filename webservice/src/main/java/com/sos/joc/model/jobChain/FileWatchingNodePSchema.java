
package com.sos.joc.model.jobChain;

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


/**
 * fileOrderSource (permanent part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "directory",
    "nextNode",
    "regex",
    "repeat",
    "delayAfterError",
    "alertWhenDirectoryMissing"
})
public class FileWatchingNodePSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("directory")
    private String directory;
    @JsonProperty("nextNode")
    private String nextNode;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("regex")
    private String regex;
    @JsonProperty("repeat")
    private Integer repeat;
    @JsonProperty("delayAfterError")
    private Integer delayAfterError;
    @JsonProperty("alertWhenDirectoryMissing")
    private Boolean alertWhenDirectoryMissing;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The directory
     */
    @JsonProperty("directory")
    public String getDirectory() {
        return directory;
    }

    /**
     * 
     * (Required)
     * 
     * @param directory
     *     The directory
     */
    @JsonProperty("directory")
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * 
     * @return
     *     The nextNode
     */
    @JsonProperty("nextNode")
    public String getNextNode() {
        return nextNode;
    }

    /**
     * 
     * @param nextNode
     *     The nextNode
     */
    @JsonProperty("nextNode")
    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The regex
     */
    @JsonProperty("regex")
    public String getRegex() {
        return regex;
    }

    /**
     * 
     * (Required)
     * 
     * @param regex
     *     The regex
     */
    @JsonProperty("regex")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * 
     * @return
     *     The repeat
     */
    @JsonProperty("repeat")
    public Integer getRepeat() {
        return repeat;
    }

    /**
     * 
     * @param repeat
     *     The repeat
     */
    @JsonProperty("repeat")
    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    /**
     * 
     * @return
     *     The delayAfterError
     */
    @JsonProperty("delayAfterError")
    public Integer getDelayAfterError() {
        return delayAfterError;
    }

    /**
     * 
     * @param delayAfterError
     *     The delayAfterError
     */
    @JsonProperty("delayAfterError")
    public void setDelayAfterError(Integer delayAfterError) {
        this.delayAfterError = delayAfterError;
    }

    /**
     * 
     * @return
     *     The alertWhenDirectoryMissing
     */
    @JsonProperty("alertWhenDirectoryMissing")
    public Boolean getAlertWhenDirectoryMissing() {
        return alertWhenDirectoryMissing;
    }

    /**
     * 
     * @param alertWhenDirectoryMissing
     *     The alertWhenDirectoryMissing
     */
    @JsonProperty("alertWhenDirectoryMissing")
    public void setAlertWhenDirectoryMissing(Boolean alertWhenDirectoryMissing) {
        this.alertWhenDirectoryMissing = alertWhenDirectoryMissing;
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
        return new HashCodeBuilder().append(directory).append(nextNode).append(regex).append(repeat).append(delayAfterError).append(alertWhenDirectoryMissing).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileWatchingNodePSchema) == false) {
            return false;
        }
        FileWatchingNodePSchema rhs = ((FileWatchingNodePSchema) other);
        return new EqualsBuilder().append(directory, rhs.directory).append(nextNode, rhs.nextNode).append(regex, rhs.regex).append(repeat, rhs.repeat).append(delayAfterError, rhs.delayAfterError).append(alertWhenDirectoryMissing, rhs.alertWhenDirectoryMissing).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
