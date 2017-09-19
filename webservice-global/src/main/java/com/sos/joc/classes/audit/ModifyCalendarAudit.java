package com.sos.joc.classes.audit;

import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.calendar.CalendarFilter;


public class ModifyCalendarAudit extends CalendarFilter implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public ModifyCalendarAudit(String calendarPath, AuditParams auditParams) {
        setPath(calendarPath);
        setAuditParams(auditParams);
        if (calendarPath != null) {
            this.folder = Paths.get(calendarPath).getParent().toString().replace('\\', '/');
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
    public String getJobschedulerId() {
        return "";
    }
}
