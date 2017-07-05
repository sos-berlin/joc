package com.sos.joc.db.yade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sos.hibernate.classes.DbItem;
import com.sos.jitl.reporting.db.DBLayer;


@Entity
@Table(name = "YADE_FILES")
@SequenceGenerator(name = DBLayer.TABLE_YADE_FILES_SEQUENCE, sequenceName = DBLayer.TABLE_YADE_FILES_SEQUENCE, allocationSize = 1)
public class DBItemYadeFiles extends DbItem implements Serializable {

    private static final long serialVersionUID = 1L;
    /** Primary key */
    private Long id;

    /** Others */
    private Long transferId;
    private Long interventionTransferId;
    private String sourcePath;
    private String targetPath;
    private Long size;
    private Date modificationDate;
    private Long state;
    private String errorMessage;
    private String integrityHash;
    
    public DBItemYadeFiles() {
    }

    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_YADE_FILES_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    public Long getId() {
        return id;
    }
    
    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = DBLayer.TABLE_YADE_FILES_SEQUENCE)
    @Column(name = "`ID`", nullable = false)
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name = "`TRANSFER_ID`", nullable = false)
    public Long getTransferId() {
        return transferId;
    }
    
    @Column(name = "`TRANSFER_ID`", nullable = false)
    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }
    
    @Column(name = "`INTERVENTION_TRANSFER_ID`", nullable = true)
    public Long getInterventionTransferId() {
        return interventionTransferId;
    }
    
    @Column(name = "`INTERVENTION_TRANSFER_ID`", nullable = true)
    public void setInterventionTransferId(Long interventionTransferId) {
        this.interventionTransferId = interventionTransferId;
    }
    
    @Column(name = "`SOURCE_PATH`", nullable = false)
    public String getSourcePath() {
        return sourcePath;
    }
    
    @Column(name = "`SOURCE_PATH`", nullable = false)
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
    
    @Column(name = "`TARGET_PATH`", nullable = true)
    public String getTargetPath() {
        return targetPath;
    }
    
    @Column(name = "`TARGET_PATH`", nullable = true)
    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
    
    @Column(name = "`SIZE`", nullable = true)
    public Long getSize() {
        return size;
    }
    
    @Column(name = "`SIZE`", nullable = true)
    public void setSize(Long size) {
        this.size = size;
    }
    
    @Column(name = "`MODIFICATION_DATE`", nullable = true)
    public Date getModificationDate() {
        return modificationDate;
    }
    
    @Column(name = "`MODIFICATION_DATE`", nullable = true)
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
    
    @Column(name = "`STATE`", nullable = false)
    public Long getState() {
        return state;
    }
    
    @Column(name = "`STATE`", nullable = false)
    public void setState(Long state) {
        this.state = state;
    }
    
    @Column(name = "`ERROR_MESSAGE`", nullable = true)
    public String getErrorMessage() {
        return errorMessage;
    }
    
    @Column(name = "`ERROR_MESSAGE`", nullable = true)
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Column(name = "`INTEGRITY_HASH`", nullable = true)
    public String getIntegrityHash() {
        return integrityHash;
    }
    
    @Column(name = "`INTEGRITY_HASH`", nullable = true)
    public void setIntegrityHash(String integrityHash) {
        this.integrityHash = integrityHash;
    }

}