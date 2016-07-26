
package com.sos.joc.model.user;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * permissions
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "isAuthenticated",
    "user",
    "accessToken",
    "JobschedulerMaster",
    "JobschedulerMasterCluster",
    "JobschedulerUniversalAgent",
    "DailyPlan",
    "History",
    "Order",
    "JobChain",
    "Job",
    "ProcessClass",
    "Schedule",
    "Lock",
    "Event",
    "EventAction",
    "HolidayCalendar",
    "MaintenanceWindow",
    "SOSPermissionRoles"
})
public class PermissionsSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("isAuthenticated")
    private Boolean isAuthenticated;
    @JsonProperty("user")
    private String user;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("JobschedulerMaster")
    private JobschedulerMaster jobschedulerMaster;
    @JsonProperty("JobschedulerMasterCluster")
    private JobschedulerMasterCluster jobschedulerMasterCluster;
    @JsonProperty("JobschedulerUniversalAgent")
    private JobschedulerUniversalAgent jobschedulerUniversalAgent;
    @JsonProperty("DailyPlan")
    private DailyPlan dailyPlan;
    @JsonProperty("History")
    private History history;
    @JsonProperty("Order")
    private Order order;
    @JsonProperty("JobChain")
    private JobChain jobChain;
    @JsonProperty("Job")
    private Job job;
    @JsonProperty("ProcessClass")
    private ProcessClass processClass;
    @JsonProperty("Schedule")
    private Schedule schedule;
    @JsonProperty("Lock")
    private Lock lock;
    @JsonProperty("Event")
    private Event event;
    @JsonProperty("EventAction")
    private EventAction eventAction;
    @JsonProperty("HolidayCalendar")
    private HolidayCalendar holidayCalendar;
    @JsonProperty("MaintenanceWindow")
    private MaintenanceWindow maintenanceWindow;
    @JsonProperty("SOSPermissionRoles")
    private SOSPermissionRoles sOSPermissionRoles;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The isAuthenticated
     */
    @JsonProperty("isAuthenticated")
    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    /**
     * 
     * (Required)
     * 
     * @param isAuthenticated
     *     The isAuthenticated
     */
    @JsonProperty("isAuthenticated")
    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    /**
     * 
     * @return
     *     The user
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The accessToken
     */
    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 
     * @param accessToken
     *     The accessToken
     */
    @JsonProperty("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 
     * @return
     *     The jobschedulerMaster
     */
    @JsonProperty("JobschedulerMaster")
    public JobschedulerMaster getJobschedulerMaster() {
        return jobschedulerMaster;
    }

    /**
     * 
     * @param jobschedulerMaster
     *     The JobschedulerMaster
     */
    @JsonProperty("JobschedulerMaster")
    public void setJobschedulerMaster(JobschedulerMaster jobschedulerMaster) {
        this.jobschedulerMaster = jobschedulerMaster;
    }

    /**
     * 
     * @return
     *     The jobschedulerMasterCluster
     */
    @JsonProperty("JobschedulerMasterCluster")
    public JobschedulerMasterCluster getJobschedulerMasterCluster() {
        return jobschedulerMasterCluster;
    }

    /**
     * 
     * @param jobschedulerMasterCluster
     *     The JobschedulerMasterCluster
     */
    @JsonProperty("JobschedulerMasterCluster")
    public void setJobschedulerMasterCluster(JobschedulerMasterCluster jobschedulerMasterCluster) {
        this.jobschedulerMasterCluster = jobschedulerMasterCluster;
    }

    /**
     * 
     * @return
     *     The jobschedulerUniversalAgent
     */
    @JsonProperty("JobschedulerUniversalAgent")
    public JobschedulerUniversalAgent getJobschedulerUniversalAgent() {
        return jobschedulerUniversalAgent;
    }

    /**
     * 
     * @param jobschedulerUniversalAgent
     *     The JobschedulerUniversalAgent
     */
    @JsonProperty("JobschedulerUniversalAgent")
    public void setJobschedulerUniversalAgent(JobschedulerUniversalAgent jobschedulerUniversalAgent) {
        this.jobschedulerUniversalAgent = jobschedulerUniversalAgent;
    }

    /**
     * 
     * @return
     *     The dailyPlan
     */
    @JsonProperty("DailyPlan")
    public DailyPlan getDailyPlan() {
        return dailyPlan;
    }

    /**
     * 
     * @param dailyPlan
     *     The DailyPlan
     */
    @JsonProperty("DailyPlan")
    public void setDailyPlan(DailyPlan dailyPlan) {
        this.dailyPlan = dailyPlan;
    }

    /**
     * 
     * @return
     *     The history
     */
    @JsonProperty("History")
    public History getHistory() {
        return history;
    }

    /**
     * 
     * @param history
     *     The History
     */
    @JsonProperty("History")
    public void setHistory(History history) {
        this.history = history;
    }

    /**
     * 
     * @return
     *     The order
     */
    @JsonProperty("Order")
    public Order getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The Order
     */
    @JsonProperty("Order")
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("JobChain")
    public JobChain getJobChain() {
        return jobChain;
    }

    /**
     * 
     * @param jobChain
     *     The JobChain
     */
    @JsonProperty("JobChain")
    public void setJobChain(JobChain jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * 
     * @return
     *     The job
     */
    @JsonProperty("Job")
    public Job getJob() {
        return job;
    }

    /**
     * 
     * @param job
     *     The Job
     */
    @JsonProperty("Job")
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("ProcessClass")
    public ProcessClass getProcessClass() {
        return processClass;
    }

    /**
     * 
     * @param processClass
     *     The ProcessClass
     */
    @JsonProperty("ProcessClass")
    public void setProcessClass(ProcessClass processClass) {
        this.processClass = processClass;
    }

    /**
     * 
     * @return
     *     The schedule
     */
    @JsonProperty("Schedule")
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * 
     * @param schedule
     *     The Schedule
     */
    @JsonProperty("Schedule")
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * 
     * @return
     *     The lock
     */
    @JsonProperty("Lock")
    public Lock getLock() {
        return lock;
    }

    /**
     * 
     * @param lock
     *     The Lock
     */
    @JsonProperty("Lock")
    public void setLock(Lock lock) {
        this.lock = lock;
    }

    /**
     * 
     * @return
     *     The event
     */
    @JsonProperty("Event")
    public Event getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The Event
     */
    @JsonProperty("Event")
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * 
     * @return
     *     The eventAction
     */
    @JsonProperty("EventAction")
    public EventAction getEventAction() {
        return eventAction;
    }

    /**
     * 
     * @param eventAction
     *     The EventAction
     */
    @JsonProperty("EventAction")
    public void setEventAction(EventAction eventAction) {
        this.eventAction = eventAction;
    }

    /**
     * 
     * @return
     *     The holidayCalendar
     */
    @JsonProperty("HolidayCalendar")
    public HolidayCalendar getHolidayCalendar() {
        return holidayCalendar;
    }

    /**
     * 
     * @param holidayCalendar
     *     The HolidayCalendar
     */
    @JsonProperty("HolidayCalendar")
    public void setHolidayCalendar(HolidayCalendar holidayCalendar) {
        this.holidayCalendar = holidayCalendar;
    }

    /**
     * 
     * @return
     *     The maintenanceWindow
     */
    @JsonProperty("MaintenanceWindow")
    public MaintenanceWindow getMaintenanceWindow() {
        return maintenanceWindow;
    }

    /**
     * 
     * @param maintenanceWindow
     *     The MaintenanceWindow
     */
    @JsonProperty("MaintenanceWindow")
    public void setMaintenanceWindow(MaintenanceWindow maintenanceWindow) {
        this.maintenanceWindow = maintenanceWindow;
    }

    /**
     * 
     * @return
     *     The sOSPermissionRoles
     */
    @JsonProperty("SOSPermissionRoles")
    public SOSPermissionRoles getSOSPermissionRoles() {
        return sOSPermissionRoles;
    }

    /**
     * 
     * @param sOSPermissionRoles
     *     The SOSPermissionRoles
     */
    @JsonProperty("SOSPermissionRoles")
    public void setSOSPermissionRoles(SOSPermissionRoles sOSPermissionRoles) {
        this.sOSPermissionRoles = sOSPermissionRoles;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(isAuthenticated).append(user).append(accessToken).append(jobschedulerMaster).append(jobschedulerMasterCluster).append(jobschedulerUniversalAgent).append(dailyPlan).append(history).append(order).append(jobChain).append(job).append(processClass).append(schedule).append(lock).append(event).append(eventAction).append(holidayCalendar).append(maintenanceWindow).append(sOSPermissionRoles).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PermissionsSchema) == false) {
            return false;
        }
        PermissionsSchema rhs = ((PermissionsSchema) other);
        return new EqualsBuilder().append(isAuthenticated, rhs.isAuthenticated).append(user, rhs.user).append(accessToken, rhs.accessToken).append(jobschedulerMaster, rhs.jobschedulerMaster).append(jobschedulerMasterCluster, rhs.jobschedulerMasterCluster).append(jobschedulerUniversalAgent, rhs.jobschedulerUniversalAgent).append(dailyPlan, rhs.dailyPlan).append(history, rhs.history).append(order, rhs.order).append(jobChain, rhs.jobChain).append(job, rhs.job).append(processClass, rhs.processClass).append(schedule, rhs.schedule).append(lock, rhs.lock).append(event, rhs.event).append(eventAction, rhs.eventAction).append(holidayCalendar, rhs.holidayCalendar).append(maintenanceWindow, rhs.maintenanceWindow).append(sOSPermissionRoles, rhs.sOSPermissionRoles).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
