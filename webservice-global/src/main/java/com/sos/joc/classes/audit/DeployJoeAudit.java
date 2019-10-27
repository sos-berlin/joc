package com.sos.joc.classes.audit;

import java.nio.file.Paths;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.joe.common.FilterDeploy;

public class DeployJoeAudit extends FilterDeploy implements IAuditLog {

    @JsonIgnore
    private String folder;

    @JsonIgnore
    private String job;

    @JsonIgnore
    private String jobChain;

    @JsonIgnore
    private String order;

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;

    @JsonIgnore
    private String ticketLink;

    private Boolean toDelete;

    public DeployJoeAudit(DBItemJoeObject joeObject, FilterDeploy filter) {
        setJobschedulerId(filter.getJobschedulerId());
        setObjectType(filter.getObjectType());
        setAuditParams(filter.getAuditLog());
        setToDelete(joeObject.operationIsDelete());
        switch (joeObject.getObjectType()) {
        case "JOB":
            this.job = joeObject.getPath();
            this.folder = Paths.get(joeObject.getPath()).getParent().toString().replace('\\', '/');
            break;
        case "JOBCHAIN":
            this.jobChain = joeObject.getPath();
            this.folder = Paths.get(joeObject.getPath()).getParent().toString().replace('\\', '/');
            break;
        case "ORDER":
            this.order = joeObject.getPath();
            this.folder = Paths.get(joeObject.getPath()).getParent().toString().replace('\\', '/');
            break;
        case "FOLDER":
            this.folder = joeObject.getPath();
            break;
        default:
            this.folder = Paths.get(joeObject.getPath()).getParent().toString().replace('\\', '/');
            break;
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
    public String getJob() {
        return job;
    }

    @Override
    public String getJobChain() {
        return jobChain;
    }

    @Override
    public String getOrderId() {
        return order;
    }

    public Boolean getToDelete() {
        return toDelete;
    }

    public void setToDelete(Boolean toDelete) {
        this.toDelete = toDelete;
    }
}
