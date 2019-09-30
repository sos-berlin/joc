
package com.sos.joc.model.joe.common;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.JobSchedulerObjectType;


/**
 * edit JobScheduler object configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "objectType", visible = true)
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.job.JobEdit.class, name = "JOB"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.jobchain.JobChainEdit.class, name = "JOBCHAIN"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.order.OrderEdit.class, name = "ORDER"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.processclass.ProcessClassEdit.class, name = "PROCESSCLASS"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.lock.LockEdit.class, name = "LOCK"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.schedule.ScheduleEdit.class, name = "SCHEDULE"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.other.FolderEdit.class, name = "FOLDER"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.job.MonitorEdit.class, name = "MONITOR"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.nodeparams.NodeParamsEdit.class, name = "NODEPARAMS"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.schedule.HolidaysEdit.class, name = "HOLIDAYS"),
    @JsonSubTypes.Type(value = com.sos.joc.model.joe.other.OtherEdit.class, name = "OTHER")
})


@JsonPropertyOrder({
    "deliveryDate",
    "configurationDate",
    "jobschedulerId",
    "path",
    "oldPath",
    "objectType",
    "objectVersionStatus",
    "configuration",
    "account",
    "auditLog"
})
public class JSObjectEdit {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    private Date deliveryDate;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    @JacksonXmlProperty(localName = "configuration_date", isAttribute = true)
    private Date configurationDate;
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    private String path;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    private String oldPath;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    private JobSchedulerObjectType objectType;
    /**
     * filter for requests
     * <p>
     * Describes the situation live/draft
     * 
     */
    @JsonProperty("objectVersionStatus")
    @JacksonXmlProperty(localName = "object_version_status", isAttribute = false)
    private JoeObjectStatus objectVersionStatus;
    /**
     * interface for different json representations of a configuration item
     * 
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    private IJSObject configuration;
    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    private String account;
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    private AuditParams auditLog;

    /**
     * No args constructor for use in serialization
     * 
     */
    public JSObjectEdit() {
    }

    /**
     * 
     * @param configurationDate
     * @param path
     * @param objectVersionStatus
     * @param auditLog
     * @param configuration
     * @param oldPath
     * @param deliveryDate
     * @param jobschedulerId
     * @param account
     * @param objectType
     */
    public JSObjectEdit(Date deliveryDate, Date configurationDate, String jobschedulerId, String path, String oldPath, JobSchedulerObjectType objectType, JoeObjectStatus objectVersionStatus, IJSObject configuration, String account, AuditParams auditLog) {
        this.deliveryDate = deliveryDate;
        this.configurationDate = configurationDate;
        this.jobschedulerId = jobschedulerId;
        this.path = path;
        this.oldPath = oldPath;
        this.objectType = objectType;
        this.objectVersionStatus = objectVersionStatus;
        this.configuration = configuration;
        this.account = account;
        this.auditLog = auditLog;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    @JacksonXmlProperty(localName = "delivery_date", isAttribute = true)
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The configurationDate
     */
    @JsonProperty("configurationDate")
    @JacksonXmlProperty(localName = "configuration_date", isAttribute = true)
    public Date getConfigurationDate() {
        return configurationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param configurationDate
     *     The configurationDate
     */
    @JsonProperty("configurationDate")
    @JacksonXmlProperty(localName = "configuration_date", isAttribute = true)
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobscheduler_id", isAttribute = true)
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    @JacksonXmlProperty(localName = "path", isAttribute = true)
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The oldPath
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    public String getOldPath() {
        return oldPath;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param oldPath
     *     The oldPath
     */
    @JsonProperty("oldPath")
    @JacksonXmlProperty(localName = "old_path", isAttribute = true)
    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    public JobSchedulerObjectType getObjectType() {
        return objectType;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    @JacksonXmlProperty(localName = "object_type", isAttribute = false)
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }

    /**
     * filter for requests
     * <p>
     * Describes the situation live/draft
     * 
     * @return
     *     The objectVersionStatus
     */
    @JsonProperty("objectVersionStatus")
    @JacksonXmlProperty(localName = "object_version_status", isAttribute = false)
    public JoeObjectStatus getObjectVersionStatus() {
        return objectVersionStatus;
    }

    /**
     * filter for requests
     * <p>
     * Describes the situation live/draft
     * 
     * @param objectVersionStatus
     *     The objectVersionStatus
     */
    @JsonProperty("objectVersionStatus")
    @JacksonXmlProperty(localName = "object_version_status", isAttribute = false)
    public void setObjectVersionStatus(JoeObjectStatus objectVersionStatus) {
        this.objectVersionStatus = objectVersionStatus;
    }

    /**
     * interface for different json representations of a configuration item
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public IJSObject getConfiguration() {
        return configuration;
    }

    /**
     * interface for different json representations of a configuration item
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    @JacksonXmlProperty(localName = "configuration", isAttribute = false)
    public void setConfiguration(IJSObject configuration) {
        this.configuration = configuration;
    }

    /**
     * 
     * @return
     *     The account
     */
    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    public String getAccount() {
        return account;
    }

    /**
     * 
     * @param account
     *     The account
     */
    @JsonProperty("account")
    @JacksonXmlProperty(localName = "account", isAttribute = true)
    public void setAccount(String account) {
        this.account = account;
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
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
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
    @JacksonXmlProperty(localName = "audit_log", isAttribute = false)
    public void setAuditLog(AuditParams auditLog) {
        this.auditLog = auditLog;
    }
 
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(this.objectVersionStatus.getMessage().get_messageCode()).append(configurationDate).append(jobschedulerId).append(path).append(oldPath).append(objectType).append(this.getObjectVersionStatus().getDeployed()).append(configuration).append(account).append(auditLog).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JSObjectEdit) == false) {
            return false;
        }
        JSObjectEdit rhs = ((JSObjectEdit) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(this.objectVersionStatus.getMessage().get_messageCode(), rhs.getObjectVersionStatus().getMessage().get_messageCode()).append(configurationDate, rhs.configurationDate).append(jobschedulerId, rhs.jobschedulerId).append(path, rhs.path).append(oldPath, rhs.oldPath).append(objectType, rhs.objectType).append(this.getObjectVersionStatus().getDeployed(), rhs.getObjectVersionStatus().getDeployed()).append(configuration, rhs.configuration).append(account, rhs.account).append(auditLog, rhs.auditLog).isEquals();
    }
    
    @JsonIgnore
    public <T> T cast(Class<T> clazz) throws ClassCastException {
        return clazz.cast(this);
    }
    
    @SuppressWarnings({ "unchecked" })
    @JsonIgnore
    public <T> T cast() throws ClassCastException {
        return (T) this;
    }
    
    @JsonIgnore
    public <T> Boolean isInstanceOf(Class<T> clazz) throws ClassCastException {
        return clazz.isInstance(this);
    }

}
