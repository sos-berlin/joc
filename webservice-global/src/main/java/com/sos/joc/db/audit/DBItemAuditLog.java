package com.sos.joc.db.audit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.sos.hibernate.classes.DbItem;
import com.sos.jitl.reporting.db.DBLayer;

public class DBItemAuditLog extends DbItem implements Serializable {

    private static final long serialVersionUID = -2054646245027196877L;
    private Long id;
    private String schedulerId;
    private String account;
    private String request;
    private String parameters;
    private String job;
    private String jobChain;
    private String orderId;
    private String folder;
    private String comment;
    private Date created;

     /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_INVENTORY_INSTANCES_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    @Transient
    public Long getId() {
        return this.id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_INVENTORY_INSTANCES_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    @Transient
    public void setId(Long val) {
        this.id = val;
    }

    /** Others */
    @Column(name = "`SCHEDULER_ID`", nullable = false)
    @Transient
    public void setSchedulerId(String val) {
        this.schedulerId = val;
    }

    @Column(name = "`SCHEDULER_ID`", nullable = false)
    @Transient
    public String getSchedulerId() {
        return this.schedulerId;
    }
    
    @Column(name = "`ACCOUNT`", nullable = false)
    @Transient
    public String getAccount() {
        return account;
    }
    
    @Column(name = "`ACCOUNT`", nullable = false)
    @Transient
    public void setAccount(String account) {
        this.account = account;
    }
    
    @Column(name = "`REQUEST`", nullable = false)
    @Transient
    public String getRequest() {
        return request;
    }
    
    @Column(name = "`REQUEST`", nullable = false)
    @Transient
    public void setRequest(String request) {
        this.request = request;
    }
    
    @Column(name = "`PARAMETERS`", nullable = true)
    @Transient
    public String getParameters() {
        return parameters;
    }
    
    @Column(name = "`PARAMETERS`", nullable = true)
    @Transient
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
    
    @Column(name = "`JOB`", nullable = true)
    @Transient
    public String getJob() {
        return job;
    }
    
    @Column(name = "`JOB`", nullable = true)
    @Transient
    public void setJob(String job) {
        this.job = job;
    }
    
    @Column(name = "`JOB_CHAIN`", nullable = true)
    @Transient
    public String getJobChain() {
        return jobChain;
    }
    
    @Column(name = "`JOB_CHAIN`", nullable = true)
    @Transient
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }
    
    @Column(name = "`ORDER_ID`", nullable = true)
    @Transient
    public String getOrderId() {
        return orderId;
    }
    
    @Column(name = "`ORDER_ID`", nullable = true)
    @Transient
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    @Column(name = "`FOLDER`", nullable = true)
    @Transient
    public String getFolder() {
        return folder;
    }
    
    @Column(name = "`FOLDER`", nullable = true)
    @Transient
    public void setFolder(String folder) {
        this.folder = folder;
    }
    
    @Column(name = "`COMMENT`", nullable = true)
    @Transient
    public String getComment() {
        return comment;
    }
    
    @Column(name = "`COMMENT`", nullable = true)
    @Transient
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`CREATED`", nullable = true)
    @Transient
    public Date getCreated() {
        return created;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "`CREATED`", nullable = true)
    @Transient
    public void setCreated(Date created) {
        this.created = created;
    }

}