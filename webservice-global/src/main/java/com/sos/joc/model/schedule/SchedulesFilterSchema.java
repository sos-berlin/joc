
package com.sos.joc.model.schedule;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.FoldersSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * schedulesFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class SchedulesFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    private List<Schedule__> schedules = new ArrayList<Schedule__>();
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    private Boolean compact = false;
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
    private List<State_> state = new ArrayList<State_>();

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
     *     The schedules
     */
    public List<Schedule__> getSchedules() {
        return schedules;
    }

    /**
     * 
     * @param schedules
     *     The schedules
     */
    public void setSchedules(List<Schedule__> schedules) {
        this.schedules = schedules;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @return
     *     The compact
     */
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

    /**
     * 
     * @return
     *     The state
     */
    public List<State_> getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(List<State_> state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(schedules).append(compact).append(regex).append(folders).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SchedulesFilterSchema) == false) {
            return false;
        }
        SchedulesFilterSchema rhs = ((SchedulesFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(schedules, rhs.schedules).append(compact, rhs.compact).append(regex, rhs.regex).append(folders, rhs.folders).append(state, rhs.state).isEquals();
    }

}
