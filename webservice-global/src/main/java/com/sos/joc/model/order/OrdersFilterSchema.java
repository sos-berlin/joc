
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.FoldersSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * orders filter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class OrdersFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    private List<Order_> orders = new ArrayList<Order_>();
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
    private List<ProcessingState> processingState = new ArrayList<ProcessingState>();
    private List<Type> type = new ArrayList<Type>();
    private String dateFrom;
    private String dateTo;
    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     */
    private String timeZone;
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
     *     The orders
     */
    public List<Order_> getOrders() {
        return orders;
    }

    /**
     * 
     * @param orders
     *     The orders
     */
    public void setOrders(List<Order_> orders) {
        this.orders = orders;
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
     * 
     * @return
     *     The processingState
     */
    public List<ProcessingState> getProcessingState() {
        return processingState;
    }

    /**
     * 
     * @param processingState
     *     The processingState
     */
    public void setProcessingState(List<ProcessingState> processingState) {
        this.processingState = processingState;
    }

    /**
     * 
     * @return
     *     The type
     */
    public List<Type> getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(List<Type> type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The dateFrom
     */
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * 
     * @param dateFrom
     *     The dateFrom
     */
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * 
     * @return
     *     The dateTo
     */
    public String getDateTo() {
        return dateTo;
    }

    /**
     * 
     * @param dateTo
     *     The dateTo
     */
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     * @return
     *     The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
     * 
     * @param timeZone
     *     The timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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
        return new HashCodeBuilder().append(jobschedulerId).append(orders).append(compact).append(regex).append(processingState).append(type).append(dateFrom).append(dateTo).append(timeZone).append(folders).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrdersFilterSchema) == false) {
            return false;
        }
        OrdersFilterSchema rhs = ((OrdersFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(orders, rhs.orders).append(compact, rhs.compact).append(regex, rhs.regex).append(processingState, rhs.processingState).append(type, rhs.type).append(dateFrom, rhs.dateFrom).append(dateTo, rhs.dateTo).append(timeZone, rhs.timeZone).append(folders, rhs.folders).isEquals();
    }

}
