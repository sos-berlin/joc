package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.job.ModifyJob;
import com.sos.joc.model.job.ModifyJobs;


public class ModifyJobAudit extends ModifyJob implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
//    //@JsonIgnore
//    private String job;
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    //@JsonIgnore
    private String jobschedulerId;
    
    public ModifyJobAudit(ModifyJob modifyJob, ModifyJobs modifyJobs) {
        if (modifyJob != null) {
            setCalendars(modifyJob.getCalendars());
            setJob(modifyJob.getJob());
            setRunTime(null);
            if (modifyJob.getJob() != null) {
                Path p = Paths.get(modifyJob.getJob());
                this.folder = p.getParent().toString().replace('\\', '/');
                //this.job = p.toString().replace('\\', '/');
            }
        }
        if (modifyJobs != null) {
            setAuditParams(modifyJobs.getAuditLog());
            this.jobschedulerId = modifyJobs.getJobschedulerId();
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

//    @Override
//    //@JsonIgnore
//    public String getJob() {
//        return job;
//    }

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
    //@JsonIgnore
    public String getJobschedulerId() {
        return jobschedulerId;
    }
}
