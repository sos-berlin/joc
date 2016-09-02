
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * fileOrderSource (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class FileWatchingNodePSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String directory;
    private String nextNode;
    /**
     * 
     * (Required)
     * 
     */
    private String regex;
    private Integer repeat;
    private Integer delayAfterError;
    private Boolean alertWhenDirectoryMissing;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The directory
     */
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
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * 
     * @return
     *     The nextNode
     */
    public String getNextNode() {
        return nextNode;
    }

    /**
     * 
     * @param nextNode
     *     The nextNode
     */
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
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * 
     * @return
     *     The repeat
     */
    public Integer getRepeat() {
        return repeat;
    }

    /**
     * 
     * @param repeat
     *     The repeat
     */
    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    /**
     * 
     * @return
     *     The delayAfterError
     */
    public Integer getDelayAfterError() {
        return delayAfterError;
    }

    /**
     * 
     * @param delayAfterError
     *     The delayAfterError
     */
    public void setDelayAfterError(Integer delayAfterError) {
        this.delayAfterError = delayAfterError;
    }

    /**
     * 
     * @return
     *     The alertWhenDirectoryMissing
     */
    public Boolean getAlertWhenDirectoryMissing() {
        return alertWhenDirectoryMissing;
    }

    /**
     * 
     * @param alertWhenDirectoryMissing
     *     The alertWhenDirectoryMissing
     */
    public void setAlertWhenDirectoryMissing(Boolean alertWhenDirectoryMissing) {
        this.alertWhenDirectoryMissing = alertWhenDirectoryMissing;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(directory).append(nextNode).append(regex).append(repeat).append(delayAfterError).append(alertWhenDirectoryMissing).toHashCode();
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
        return new EqualsBuilder().append(directory, rhs.directory).append(nextNode, rhs.nextNode).append(regex, rhs.regex).append(repeat, rhs.repeat).append(delayAfterError, rhs.delayAfterError).append(alertWhenDirectoryMissing, rhs.alertWhenDirectoryMissing).isEquals();
    }

}
