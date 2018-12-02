package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.order.OrderDocuFilter;

public class AssignmentOrderDocuAudit extends OrderDocuFilter implements IAuditLog {

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    @JsonIgnore
    private String documentation;
    
    @JsonIgnore
    private String folder;
    
    public AssignmentOrderDocuAudit(OrderDocuFilter orderDocuFilter) {
        setAuditParams(orderDocuFilter.getAuditLog());
        setJobschedulerId(orderDocuFilter.getJobschedulerId());
        setDocumentation(orderDocuFilter.getDocumentation());
        setJobChain(orderDocuFilter.getJobChain());
        setOrderId(orderDocuFilter.getOrderId());
        if (orderDocuFilter.getJobChain() != null) {
            Path p = Paths.get(orderDocuFilter.getJobChain());
            this.folder = p.getParent().toString().replace('\\', '/');
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
    public String getCalendar() {
        return null;
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

}
