package com.sos.joc.classes.audit;

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
    private String jobChain;
    
    @JsonIgnore
    private String orderId;
    
    public AssignmentOrderDocuAudit(OrderDocuFilter orderDocuFilter) {
        setAuditParams(orderDocuFilter.getAuditLog());
        setJobschedulerId(orderDocuFilter.getJobschedulerId());
        setDocumentation(orderDocuFilter.getDocumentation());
        setJobChain(orderDocuFilter.getJobChain());
        setOrderId(orderDocuFilter.getOrderId());
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
    public String getJobChain() {
        return jobChain;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return orderId;
    }

    @Override
    @JsonIgnore
    public String getCalendar() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getFolder() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getJob() {
        return null;
    }

}
