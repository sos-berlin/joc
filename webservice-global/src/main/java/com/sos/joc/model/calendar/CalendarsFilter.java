
package com.sos.joc.model.calendar;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.Folder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * calendars filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "calendarIds",
    "calendars",
    "compact",
    "type",
    "categories",
    "regex",
    "folders",
    "auditLog"
})
public class CalendarsFilter {

    @JsonProperty("calendarIds")
    private List<Long> calendarIds = null;
    @JsonProperty("calendars")
    private List<String> calendars = null;
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    @JsonProperty("compact")
    private Boolean compact = false;
    @JsonProperty("type")
    private String type;
    @JsonProperty("categories")
    private List<String> categories = null;
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
    private List<Folder> folders = null;
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    private AuditParams auditLog;

    /**
     * 
     * @return
     *     The calendarIds
     */
    @JsonProperty("calendarIds")
    public List<Long> getCalendarIds() {
        return calendarIds;
    }

    /**
     * 
     * @param calendarIds
     *     The calendarIds
     */
    @JsonProperty("calendarIds")
    public void setCalendarIds(List<Long> calendarIds) {
        this.calendarIds = calendarIds;
    }

    /**
     * 
     * @return
     *     The calendars
     */
    @JsonProperty("calendars")
    public List<String> getCalendars() {
        return calendars;
    }

    /**
     * 
     * @param calendars
     *     The calendars
     */
    @JsonProperty("calendars")
    public void setCalendars(List<String> calendars) {
        this.calendars = calendars;
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
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The categories
     */
    @JsonProperty("categories")
    public List<String> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    @JsonProperty("categories")
    public void setCategories(List<String> categories) {
        this.categories = categories;
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
     * auditParams
     * <p>
     * 
     * 
     * @return
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public AuditParams getAuditLog() {
        return auditLog;
    }

    /**
     * auditParams
     * <p>
     * 
     * 
     * @param auditLog
     *     The auditLog
     */
    @JsonProperty("auditLog")
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendarIds).append(calendars).append(compact).append(type).append(categories).append(regex).append(folders).append(auditLog).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CalendarsFilter) == false) {
            return false;
        }
        CalendarsFilter rhs = ((CalendarsFilter) other);
        return new EqualsBuilder().append(calendarIds, rhs.calendarIds).append(calendars, rhs.calendars).append(compact, rhs.compact).append(type, rhs.type).append(categories, rhs.categories).append(regex, rhs.regex).append(folders, rhs.folders).append(auditLog, rhs.auditLog).isEquals();
    }

}
