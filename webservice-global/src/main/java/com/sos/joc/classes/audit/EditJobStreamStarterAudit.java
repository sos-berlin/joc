package com.sos.joc.classes.audit;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.jobstreams.JobStreamStarter;
import com.sos.joc.model.jobstreams.JobStreamStarters;

public class EditJobStreamStarterAudit extends JobStreamStarter implements IAuditLog {

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;

    @JsonIgnore
    private String ticketLink;
    private String title;
    private String jobschedulerId;

    public EditJobStreamStarterAudit(JobStreamStarters jobStreamStarters, JobStreamStarter jobStreamStarter) {
        if (jobStreamStarter != null) {
            setJobschedulerId(jobStreamStarters.getJobschedulerId());
            setTitle(jobStreamStarter.getTitle());
            setAuditParams(jobStreamStarters.getAuditLog());
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

    @Override
    @JsonIgnore
    public Date getStartTime() {
        return null;
    }

    @Override
    public String getFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public String getJobschedulerId() {
        return jobschedulerId;
    }

}
