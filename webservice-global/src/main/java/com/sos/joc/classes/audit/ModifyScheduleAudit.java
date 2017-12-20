package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.schedule.ModifyRunTime;


public class ModifyScheduleAudit extends ModifyRunTime implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public ModifyScheduleAudit(ModifyRunTime modifyRunTime) {
        if (modifyRunTime != null) {
            setAuditParams(modifyRunTime.getAuditLog());
            setSchedule(modifyRunTime.getSchedule());
            //setRunTime(modifyRunTime.getRunTime());
            setJobschedulerId(modifyRunTime.getJobschedulerId());
            if (modifyRunTime.getSchedule() != null) {
                Path p = Paths.get(modifyRunTime.getSchedule());
                if (p.getParent() != null) {
                    this.folder = p.getParent().toString().replace('\\', '/');
                } else {
                    this.folder = p.toString().replace('\\', '/');
                }
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
