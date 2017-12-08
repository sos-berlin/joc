package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SecurityAudit implements IAuditLog {

    @JsonIgnore
    private String comment;

    public SecurityAudit(String comment) {
        this.comment = comment;
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return comment;
    }
    
    @Override
    @JsonIgnore
    public Integer getTimeSpent() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getTicketLink() {
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
        return "-";
    }

    @Override
    public String toString() {
        return "{}";
    }

    @Override
    @JsonIgnore
    public String getCalendar() {
        return null;
    }
}
