package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.commands.JobschedulerCommands;

public class JobSchedulerCommandAudit extends JobschedulerCommands implements IAuditLog {

    @JsonIgnore
    private String xml;
    
    @JsonIgnore
    private String comment;

    public JobSchedulerCommandAudit(String xml, JobschedulerCommands jobSchedulerCommands) {
        this.xml = xml;
        if (jobSchedulerCommands != null) {
            this.comment = jobSchedulerCommands.getComment();
            setJobschedulerId(jobSchedulerCommands.getJobschedulerId());
            setUrl(jobSchedulerCommands.getUrl());
            getAddOrderOrCheckFoldersOrKillTask().addAll(jobSchedulerCommands.getAddOrderOrCheckFoldersOrKillTask());
        }
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
}
