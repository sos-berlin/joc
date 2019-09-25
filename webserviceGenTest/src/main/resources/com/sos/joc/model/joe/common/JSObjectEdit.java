
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
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.JobSchedulerObjectType;


/**
 * edit JobScheduler object configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "objectType")
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
    "deployed",
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
    private Date deliveryDate;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    private Date configurationDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * oldPath
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * used for rename/move
     * (Required)
     * 
     */
    @JsonProperty("oldPath")
    private String oldPath;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("objectType")
    private JobSchedulerObjectType objectType;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("deployed")
    private Boolean deployed;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("configuration")
    private IJSObject configuration;
    @JsonProperty("account")
    private String account;
    /**
     * auditParams
     * <p>
     * 
     * 
     */
    @JsonProperty("auditLog")
    private AuditParams auditLog;
    
    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
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
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

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
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }
    
    /**
     * oldPath
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The oldPath
     */
    @JsonProperty("oldPath")
    public String getOldPath() {
        return oldPath;
    }

    /**
     * oldPath
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param oldPath
     *     The oldPath
     */
    @JsonProperty("oldPath")
    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }
    
    /**
     * 
     * (Required)
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    public JobSchedulerObjectType getObjectType() {
        return objectType;
    }

    /**
     * 
     * (Required)
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }
    
    /**
     * 
     * (Required)
     * 
     * @return
     *     The deployed
     */
    @JsonProperty("deployed")
    public Boolean getDeployed() {
        return deployed;
    }

    /**
     * 
     * (Required)
     * 
     * @param deployed
     *     The deployed
     */
    @JsonProperty("deployed")
    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The configuration
     */
    @JsonProperty("configuration")
    public IJSObject getConfiguration() {
        return configuration;
    }

    /**
     * 
     * (Required)
     * 
     * @param configuration
     *     The configuration
     */
    @JsonProperty("configuration")
    public void setConfiguration(IJSObject configuration) {
        this.configuration = configuration;
    }
    
    /**
     * 
     * @return
     *     The account
     */
    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    /**
     * 
     * @param account
     *     The account
     */
    @JsonProperty("account")
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
        return new HashCodeBuilder().append(deliveryDate).append(configurationDate).append(jobschedulerId).append(path).append(oldPath).append(objectType).append(deployed).append(configuration).append(account).append(auditLog).toHashCode();
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
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(configurationDate, rhs.configurationDate).append(jobschedulerId, rhs.jobschedulerId).append(path, rhs.path).append(oldPath, rhs.oldPath).append(objectType, rhs.objectType).append(deployed, rhs.deployed).append(configuration, rhs.configuration).append(account, rhs.account).append(auditLog, rhs.auditLog).isEquals();
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
