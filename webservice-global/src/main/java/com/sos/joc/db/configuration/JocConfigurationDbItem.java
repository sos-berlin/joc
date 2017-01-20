package com.sos.joc.db.configuration;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.sos.hibernate.classes.DbItem;

@Entity
@Table(name = "JOC_CONFIGURATIONS")
public class JocConfigurationDbItem extends DbItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Primary key */
    private Long id;

    /** Foreign key INVENTORY_INSTANCES.ID */
    private Long instanceId;

    /** Others */
    private String owner;
    private String objectType;
    private String objectSource;
    private String name;
    private Boolean shared;
    private byte[] configurationItem;
    private Date created;
    private Date modified;

    public JocConfigurationDbItem() {
    }

    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "`ID`", nullable = false)
    public Long getId() {
        return this.id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "`OWNER`", nullable = false)
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Column(name = "`OWNER`", nullable = false)
    public String getOwner() {
        return this.owner;
    }

    @Column(name = "`OBJECT_TYPE`", nullable = false)
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Column(name = "`OBJECT_TYPE`", nullable = false)
    public String getObjectType() {
        return this.objectType;
    }

    @Column(name = "`OBJECT_SOURCE`", nullable = false)
    public void setObjectSource(String objectType) {
        this.objectType = objectType;
    }

    @Column(name = "`OBJECT_SOURCE`", nullable = false)
    public String getObjectSource() {
        return this.objectType;
    }

    @Column(name = "`NAME`", nullable = false)
    public void setName(String name) {
        this.name=name;
    }

    @Column(name = "`NAME`", nullable = false)
    public String getName() {
        return this.name;
    }

    @Column(name = "`SHARED`", nullable = false)
    public void setShared(Boolean shared) {
        this.shared=shared;
    }

    @Column(name = "`SHARED`", nullable = false)
    public Boolean getShared() {
        return this.shared;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`CREATED`", nullable = false)
    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`CREATED`", nullable = false)
    public Date getCreated() {
        return this.created;
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(instanceId).append(owner).append(objectType).append(objectSource).append(name).toHashCode();
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
        return new EqualsBuilder().append(instanceId, rhs.instanceId).append(owner, rhs.owner).append(objectType, rhs.objectType).append(objectSource, rhs.objectSource).append(name, rhs.name).isEquals();
    }

}