
package com.sos.joc.model.jobChain;

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
 * jobChainsFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "jobChains",
    "compact",
    "regex",
    "folders",
    "states",
    "close",
    "maxOrders"
})
public class JobChainsFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("jobChains")
    private List<JobChainPath> jobChains = new ArrayList<JobChainPath>();
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    private Boolean compact = false;
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
    @JsonProperty("states")
    private List<JobChainStateText> states = new ArrayList<JobChainStateText>();
    /**
     * concerns only events
     * 
     */
    @JsonProperty("close")
    private Boolean close = false;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("maxOrders")
    private Integer maxOrders;

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
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public List<JobChainPath> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(List<JobChainPath> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @return
     *     The compact
     */
    @JsonProperty("compact")
    public Boolean getCompact() {
        return compact;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @param compact
     *     The compact
     */
    @JsonProperty("compact")
    public void setCompact(Boolean compact) {
        this.compact = compact;
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
     * 
     * @return
     *     The states
     */
    @JsonProperty("states")
    public List<JobChainStateText> getStates() {
        return states;
    }

    /**
     * 
     * @param states
     *     The states
     */
    @JsonProperty("states")
    public void setStates(List<JobChainStateText> states) {
        this.states = states;
    }

    /**
     * concerns only events
     * 
     * @return
     *     The close
     */
    @JsonProperty("close")
    public Boolean getClose() {
        return close;
    }

    /**
     * concerns only events
     * 
     * @param close
     *     The close
     */
    @JsonProperty("close")
    public void setClose(Boolean close) {
        this.close = close;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxOrders
     */
    @JsonProperty("maxOrders")
    public Integer getMaxOrders() {
        return maxOrders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxOrders
     *     The maxOrders
     */
    @JsonProperty("maxOrders")
    public void setMaxOrders(Integer maxOrders) {
        this.maxOrders = maxOrders;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(jobChains).append(compact).append(regex).append(folders).append(states).append(close).append(maxOrders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainsFilter) == false) {
            return false;
        }
        JobChainsFilter rhs = ((JobChainsFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobChains, rhs.jobChains).append(compact, rhs.compact).append(regex, rhs.regex).append(folders, rhs.folders).append(states, rhs.states).append(close, rhs.close).append(maxOrders, rhs.maxOrders).isEquals();
    }

}
