package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.calendar.CalendarDocuFilter;

public class AssignmentCalendarDocuAudit extends CalendarDocuFilter implements IAuditLog {

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    @JsonIgnore
    private String folder;
    
    public AssignmentCalendarDocuAudit(CalendarDocuFilter calendarDocuFilter) {
        setAuditParams(calendarDocuFilter.getAuditLog());
        setJobschedulerId(calendarDocuFilter.getJobschedulerId());
        setDocumentation(calendarDocuFilter.getDocumentation());
        setCalendar(calendarDocuFilter.getCalendar());
        if (calendarDocuFilter.getCalendar() != null) {
            Path p = Paths.get(calendarDocuFilter.getCalendar());
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
    public Date getStartTime() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getJobStream() {
        return null;
    }

}
