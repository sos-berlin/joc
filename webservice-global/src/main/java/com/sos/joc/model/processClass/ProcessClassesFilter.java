
package com.sos.joc.model.processClass;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Folder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * process class filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "processClasses",
    "regex",
    "folders",
    "isAgentCluster"
})
public class ProcessClassesFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("processClasses")
    private List<ProcessClassPath> processClasses = new ArrayList<ProcessClassPath>();
    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    @JsonProperty("regex")
    private String regex;
    /**
     * folders
     * <p>
     * 
     * 
     */
    @JsonProperty("folders")
    private List<Folder> folders = new ArrayList<Folder>();
    /**
     * only relevant for volatile request
     * 
     */
    @JsonProperty("isAgentCluster")
    private Boolean isAgentCluster = false;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
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
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The processClasses
     */
    @JsonProperty("processClasses")
    public List<ProcessClassPath> getProcessClasses() {
        return processClasses;
    }

    /**
     * 
     * @param processClasses
     *     The processClasses
     */
    @JsonProperty("processClasses")
    public void setProcessClasses(List<ProcessClassPath> processClasses) {
        this.processClasses = processClasses;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @return
     *     The regex
     */
    @JsonProperty("regex")
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
    @JsonProperty("regex")
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
    @JsonProperty("folders")
    public List<Folder> getFolders() {
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
    @JsonProperty("folders")
    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    /**
     * only relevant for volatile request
     * 
     * @return
     *     The isAgentCluster
     */
    @JsonProperty("isAgentCluster")
    public Boolean getIsAgentCluster() {
        return isAgentCluster;
    }

    /**
     * only relevant for volatile request
     * 
     * @param isAgentCluster
     *     The isAgentCluster
     */
    @JsonProperty("isAgentCluster")
    public void setIsAgentCluster(Boolean isAgentCluster) {
        this.isAgentCluster = isAgentCluster;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(processClasses).append(regex).append(folders).append(isAgentCluster).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessClassesFilter) == false) {
            return false;
        }
        ProcessClassesFilter rhs = ((ProcessClassesFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(processClasses, rhs.processClasses).append(regex, rhs.regex).append(folders, rhs.folders).append(isAgentCluster, rhs.isAgentCluster).isEquals();
    }

}
