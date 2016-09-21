
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * fileOrderSource (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class FileWatchingNodeVSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String directory;
    /**
     * 
     * (Required)
     * 
     */
    private String regex;
    private List<File> files = new ArrayList<File>();
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
     *     The files
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * 
     * @param files
     *     The files
     */
    public void setFiles(List<File> files) {
        this.files = files;
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
        return new HashCodeBuilder().append(directory).append(regex).append(files).append(repeat).append(delayAfterError).append(alertWhenDirectoryMissing).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FileWatchingNodeVSchema) == false) {
            return false;
        }
        FileWatchingNodeVSchema rhs = ((FileWatchingNodeVSchema) other);
        return new EqualsBuilder().append(directory, rhs.directory).append(regex, rhs.regex).append(files, rhs.files).append(repeat, rhs.repeat).append(delayAfterError, rhs.delayAfterError).append(alertWhenDirectoryMissing, rhs.alertWhenDirectoryMissing).isEquals();
    }

}
