package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;


public class ModifyJobChainAudit extends ModifyJobChains implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String jobChain;
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public ModifyJobChainAudit(ModifyJobChain modifyJobChain, ModifyJobChains modifyJobChains) {
        if (modifyJobChain != null) {
            getJobChains().add(modifyJobChain);
            if (modifyJobChain.getJobChain() != null) {
                Path p = Paths.get(modifyJobChain.getJobChain());
                this.folder = p.getParent().toString().replace('\\', '/');
                this.jobChain = p.getFileName().toString();
            }
        }
        if (modifyJobChains != null) {
            setAuditParams(modifyJobChains.getAuditLog());
            setJobschedulerId(modifyJobChains.getJobschedulerId());            
        }
    }
    
    private void setAuditParams(AuditParams auditParams) {
        if (auditParams != null) {
            this.comment = auditParams.getComment();
            this.timeSpent = auditParams.getTimeSpent();
            this.ticketLink = auditParams.getTicketLink(); 
        }
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return comment;
    }

    @Override
    @JsonIgnore
    public Integer getTimeSpent() {
        return timeSpent;
    }

    @Override
    @JsonIgnore
    public String getTicketLink() {
        return ticketLink;
    }
    
    @Override
    @JsonIgnore
    public String getFolder() {
        return folder;
    }

    @Override
    @JsonIgnore
    public String getJob() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getJobChain() {
        return jobChain;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return null;
    }
}
