package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.event.custom.ModifyEvent;


public class ModifyEventAudit extends ModifyEvent implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    
    public ModifyEventAudit(ModifyEvent modifyEvent) {
        if (modifyEvent != null) {
            setAuditParams(modifyEvent.getAuditLog());
            setJobschedulerId(modifyEvent.getJobschedulerId());
            setJob(modifyEvent.getJob());
            setJobChain(modifyEvent.getJobChain());
            setOrderId(modifyEvent.getOrderId());
            if (modifyEvent.getJobChain() != null) {
                Path p = Paths.get(modifyEvent.getJobChain());
                this.folder = p.getParent().toString().replace('\\', '/');
            } else if (modifyEvent.getJob() != null) {
                Path p = Paths.get(modifyEvent.getJob());
                this.folder = p.getParent().toString().replace('\\', '/');
            }
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
    public String getFolder() {
        return folder;
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
    public String getCalendar() {
        return null;
    }
}
