
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * fileOrderSource (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "directory",
    "regex",
    "files",
    "repeat",
    "delayAfterError",
    "alertWhenDirectoryMissing"
})
public class FileWatchingNodeV {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("directory")
    private String directory;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("regex")
    private String regex;
    @JsonProperty("files")
    private List<FileWatchingNodeFile> files = new ArrayList<FileWatchingNodeFile>();
    @JsonProperty("repeat")
    private Integer repeat;
    @JsonProperty("delayAfterError")
    private Integer delayAfterError;
    @JsonProperty("alertWhenDirectoryMissing")
    private Boolean alertWhenDirectoryMissing;

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
     *     The files
     */
    @JsonProperty("files")
    public List<FileWatchingNodeFile> getFiles() {
        return files;
    }

    /**
     * 
     * @param files
     *     The files
     */
    @JsonProperty("files")
    public void setFiles(List<FileWatchingNodeFile> files) {
        this.files = files;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(directory).append(regex).append(files).append(repeat).append(delayAfterError).append(alertWhenDirectoryMissing).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileWatchingNodeV) == false) {
            return false;
        }
        FileWatchingNodeV rhs = ((FileWatchingNodeV) other);
        return new EqualsBuilder().append(directory, rhs.directory).append(regex, rhs.regex).append(files, rhs.files).append(repeat, rhs.repeat).append(delayAfterError, rhs.delayAfterError).append(alertWhenDirectoryMissing, rhs.alertWhenDirectoryMissing).isEquals();
    }

}
