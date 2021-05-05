package com.sos.joc.classes.audit;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.classes.JobSchedulerCommandFactory;
import com.sos.joc.model.commands.JobschedulerCommands;

public class JobSchedulerCommandAudit extends JobschedulerCommands implements IAuditLog {

    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String job;
    
    @JsonIgnore
    private String jobChain;
    
    @JsonIgnore
    private String orderId;
    
    @JsonIgnore
    private String xml;
    
    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    public JobSchedulerCommandAudit(String xml, JobschedulerCommands jobSchedulerCommands) {
        this.xml = xml;
        if (jobSchedulerCommands != null) {
            this.comment = jobSchedulerCommands.getComment();
            this.ticketLink = jobSchedulerCommands.getTicketLink();
            this.timeSpent = jobSchedulerCommands.getTimeSpent();
            setJobschedulerId(jobSchedulerCommands.getJobschedulerId());
            setUrl(jobSchedulerCommands.getUrl());
            getAddOrderOrCheckFoldersOrKillTask().addAll(jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask());
        }
    }
    
    public JobSchedulerCommandAudit(String xml, JobschedulerCommands jobSchedulerCommands, JobSchedulerCommandFactory commandFactory) {
        this.xml = xml;
        if (jobSchedulerCommands != null) {
            this.comment = jobSchedulerCommands.getComment();
            this.ticketLink = jobSchedulerCommands.getTicketLink();
            this.timeSpent = jobSchedulerCommands.getTimeSpent();
            setJobschedulerId(jobSchedulerCommands.getJobschedulerId());
            setUrl(jobSchedulerCommands.getUrl());
            getAddOrderOrCheckFoldersOrKillTask().addAll(jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask());
            setObject(commandFactory);
        }
    }
    
    private void setObject(JobSchedulerCommandFactory commandFactory) {
        this.folder = commandFactory.getFolder();
        this.job = commandFactory.getJob();
        this.jobChain = commandFactory.getJobChain();
        this.orderId = commandFactory.getOrderId();
    }

    @Override
    public String toString() {
        if (xml == null) {
            return null;
        }
        String startTag = String.format("<jobscheduler_commands jobschedulerId=\"%1$s\" url=\"%2$s\">", getJobschedulerId(), getUrl());
        String endTag = "</jobscheduler_commands>";
        if (xml.startsWith("<commands>")) {
            return xml.replaceFirst("^<commands>", startTag).replaceFirst("</commands>$", endTag);
        }
        return startTag + xml + endTag;
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

    @Override
    @JsonIgnore
    public String getJob() {
        return job;
    }

    @Override
    @JsonIgnore
    public String getJobChain() {
        return jobChain;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return orderId;
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
