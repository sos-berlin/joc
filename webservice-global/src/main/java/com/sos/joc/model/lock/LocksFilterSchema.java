
package com.sos.joc.model.lock;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.FoldersSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * locksFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class LocksFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    private List<Lock> locks = new ArrayList<Lock>();
    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    private String regex;
    /**
     * folders
     * <p>
     * 
     * 
     */
    private List<FoldersSchema> folders = new ArrayList<FoldersSchema>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The locks
     */
    public List<Lock> getLocks() {
        return locks;
    }

    /**
     * 
     * @param locks
     *     The locks
     */
    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @return
     *     The regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @param regex
     *     The regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * folders
     * <p>
     * 
     * 
     * @return
     *     The folders
     */
    public List<FoldersSchema> getFolders() {
        return folders;
    }

    /**
     * folders
     * <p>
     * 
     * 
     * @param folders
     *     The folders
     */
    public void setFolders(List<FoldersSchema> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(locks).append(regex).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LocksFilterSchema) == false) {
            return false;
        }
        LocksFilterSchema rhs = ((LocksFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(locks, rhs.locks).append(regex, rhs.regex).append(folders, rhs.folders).isEquals();
    }

}
