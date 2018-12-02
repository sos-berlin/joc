package com.sos.joc.classes.audit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.docu.DocumentationsFilter;


public class DeleteDocumentationAudit extends DocumentationsFilter implements IAuditLog {
    
    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    @JsonIgnore
    private String folder;
    
    
    public DeleteDocumentationAudit(DocumentationsFilter documentationsFilter, String path, String folder) {
        setAuditParams(documentationsFilter.getAuditLog());
        setJobschedulerId(documentationsFilter.getJobschedulerId());
        List<String> documentations = new ArrayList<String>();
        documentations.add(path);
        setDocumentations(documentations);
        this.folder = folder;
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
        return null;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getCalendar() {
        return null;
    }
    
}
