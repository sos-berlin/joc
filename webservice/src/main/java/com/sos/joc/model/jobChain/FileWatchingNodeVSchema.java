
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(directory).append(regex).append(files).toHashCode();
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
        return new EqualsBuilder().append(directory, rhs.directory).append(regex, rhs.regex).append(files, rhs.files).isEquals();
    }

}
