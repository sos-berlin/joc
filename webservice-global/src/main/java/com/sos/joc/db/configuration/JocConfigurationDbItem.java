package com.sos.joc.db.configuration;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import com.sos.hibernate.classes.DbItem;
import com.sos.jitl.reporting.db.DBLayer;

@Entity
@Table(name = DBLayer.TABLE_JOC_CONFIGURATIONS)
@SequenceGenerator(name = DBLayer.TABLE_JOC_CONFIGURATIONS_SEQUENCE, sequenceName = DBLayer.TABLE_JOC_CONFIGURATIONS_SEQUENCE, allocationSize = 1)
public class JocConfigurationDbItem extends DbItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Primary key */
    private Long id;

    /** Foreign key INVENTORY_INSTANCES.ID */
    private Long instanceId;

    /** Others */
    private String account;
    private String objectType;
    private String configurationType;
    private String name;
    private Boolean shared;
    private String configurationItem;
    private Date modified;
    private String schedulerId;

    public JocConfigurationDbItem() {
    }

    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_JOC_CONFIGURATIONS_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    public Long getId() {
        return this.id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_JOC_CONFIGURATIONS_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    public void setId(Long val) {
        this.id = val;
    }

    /** Foreign key INVENTORY_INSTANCES.ID */
    @Column(name = "`INSTANCE_ID`", nullable = false)
    public Long getInstanceId() {
        return this.instanceId;
    }

    @Column(name = "`INSTANCE_ID`", nullable = false)
    public void setInstanceId(Long val) {
        this.instanceId = val;
    }

    /** Others */
    @Column(name = "`CONFIGURATION_ITEM`", nullable = false)
    public void setConfigurationItem(String configurationItem) {
        this.configurationItem = configurationItem;
    }

    @Column(name = "`CONFIGURATION_ITEM`", nullable = false)
    public String getConfigurationItem() {
        return this.configurationItem;
    }

    @Column(name = "`ACCOUNT`", nullable = false)
    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "`ACCOUNT`", nullable = false)
    public String getAccount() {
        return this.account;
    }

    @Column(name = "`OBJECT_TYPE`", nullable = false)
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Column(name = "`OBJECT_TYPE`", nullable = false)
    public String getObjectType() {
        return this.objectType;
    }

    @Column(name = "`CONFIGURATION_TYPE`", nullable = false)
    public void setConfigurationType(String configurationType) {
        this.configurationType = configurationType;
    }

    @Column(name = "`CONFIGURATION_TYPE`", nullable = false)
    public String getConfigurationType() {
        return this.configurationType;
    }

    @Column(name = "`NAME`", nullable = true)
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "`NAME`", nullable = true)
    public String getName() {
        return this.name;
    }

    @Column(name = "`SHARED`", nullable = false)
    @Type(type = "numeric_boolean")
    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    @Column(name = "`SHARED`", nullable = false)
    @Type(type = "numeric_boolean")
    public Boolean getShared() {
        return this.shared;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`MODIFIED`", nullable = false)
    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`MODIFIED`", nullable = false)
    public Date getModified() {
        return this.modified;
    }

    @Column(name = "`SCHEDULER_ID`", nullable = true)
    public void setSchedulerId(String jobschedulerId) {
        this.schedulerId = jobschedulerId;
    }

    @Column(name = "`SCHEDULER_ID`", nullable = true)
    public String getSchedulerId() {
        return this.schedulerId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(instanceId).append(account).append(objectType).append(configurationType).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        // always compare on unique constraint
        if (other == this) {
            return true;
        }
        if (!(other instanceof JocConfigurationDbItem)) {
            return false;
        }
        JocConfigurationDbItem rhs = ((JocConfigurationDbItem) other);
        return new EqualsBuilder().append(instanceId, rhs.instanceId).append(account, rhs.account).append(objectType, rhs.objectType).append(
                configurationType, rhs.configurationType).append(name, rhs.name).isEquals();
    }

}