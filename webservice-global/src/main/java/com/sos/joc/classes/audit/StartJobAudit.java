package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.job.StartJob;
import com.sos.joc.model.job.StartJobs;


public class StartJobAudit extends StartJobs implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String job;
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public StartJobAudit(StartJob startJob, StartJobs startJobs) {
        if (startJob != null) {
            getJobs().add(startJob);
            if (startJob.getJob() != null) {
                Path p = Paths.get(startJob.getJob());
                this.folder = p.getParent().toString().replace('\\', '/');
                this.job = p.getFileName().toString();
            }
        }
        setAuditParams(startJobs.getAuditLog());
        setJobschedulerId(startJobs.getJobschedulerId());
    }

    private void setAuditParams(AuditParams auditParams) {
        this.comment = auditParams.getComment();
        this.timeSpent = auditParams.getTimeSpent();
        this.ticketLink = auditParams.getTicketLink();
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return comment;
    }

    @Override
    @JsonIgnore
    public Long getTimeSpent() {
        if (timeSpent == null) {
            return null;
        }
        return timeSpent.longValue();
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
        return job;
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
}
