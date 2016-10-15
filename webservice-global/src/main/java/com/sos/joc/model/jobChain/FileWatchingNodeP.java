
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
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
    "regex"
})
public class FileWatchingNodeP {

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(directory).append(nextNode).append(regex).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileWatchingNodeP) == false) {
            return false;
        }
        FileWatchingNodeP rhs = ((FileWatchingNodeP) other);
        return new EqualsBuilder().append(directory, rhs.directory).append(nextNode, rhs.nextNode).append(regex, rhs.regex).isEquals();
    }

}
