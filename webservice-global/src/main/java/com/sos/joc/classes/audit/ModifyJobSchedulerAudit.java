package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;


public class ModifyJobSchedulerAudit extends HostPortTimeOutParameter implements IAuditLog {
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public ModifyJobSchedulerAudit(HostPortTimeOutParameter hostPortTimeoutParamSchema) {
        if (hostPortTimeoutParamSchema != null) {
            setAuditParams(hostPortTimeoutParamSchema.getAuditLog());
            setHost(hostPortTimeoutParamSchema.getHost());
            setPort(hostPortTimeoutParamSchema.getPort());
            setTimeout(hostPortTimeoutParamSchema.getTimeout());
            setJobschedulerId(hostPortTimeoutParamSchema.getJobschedulerId()); 
        }
    }
    
    public ModifyJobSchedulerAudit(HostPortParameter hostPortParamSchema) {
        if (hostPortParamSchema != null) {
            setAuditParams(hostPortParamSchema.getAuditLog());
            setHost(hostPortParamSchema.getHost());
            setPort(hostPortParamSchema.getPort());
            setJobschedulerId(hostPortParamSchema.getJobschedulerId()); 
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
        return null;
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
