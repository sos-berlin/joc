package com.sos.joc.classes.audit;

import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.calendar.CalendarId;


public class ModifyCalendarAudit extends CalendarId implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    @JsonIgnore
    private String calendar;
    
    
    public ModifyCalendarAudit(Long calendarId, String calendarPath, AuditParams auditParams, String jobschedulerId) {
        setId(calendarId);
        setPath(calendarPath);
        setAuditParams(auditParams);
        setJobschedulerId(jobschedulerId);
        if (calendarPath != null) {
            this.folder = Paths.get(calendarPath).getParent().toString().replace('\\', '/');
            this.calendar = calendarPath;
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
        return calendar;
    }
}
