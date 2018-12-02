package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.jobChain.JobChainDocuFilter;

public class AssignmentJobChainDocuAudit extends JobChainDocuFilter implements IAuditLog {

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    @JsonIgnore
    private String folder;
    
    public AssignmentJobChainDocuAudit(JobChainDocuFilter jobChainDocuFilter) {
        setAuditParams(jobChainDocuFilter.getAuditLog());
        setJobschedulerId(jobChainDocuFilter.getJobschedulerId());
        setDocumentation(jobChainDocuFilter.getDocumentation());
        setJobChain(jobChainDocuFilter.getJobChain());
        if (jobChainDocuFilter.getJobChain() != null) {
            Path p = Paths.get(jobChainDocuFilter.getJobChain());
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
    public String getOrderId() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getCalendar() {
        return null;
    }

}
