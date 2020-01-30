package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.FilterDeploy;

public class DeployJoeAudit extends FilterDeploy implements IAuditLog {

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

    @JsonProperty("toDelete")
    private Boolean toDelete;

    public DeployJoeAudit(FilterDeploy filter, boolean operationIsDelete) {
        setJobschedulerId(filter.getJobschedulerId());
        setObjectType(filter.getObjectType());
        setObjectName(filter.getObjectName());
        setFolder(filter.getFolder());
        setAuditParams(filter.getAuditLog());
        setToDelete(operationIsDelete);
        switch (filter.getObjectType()) {
        case JOB:
            this.job = (filter.getFolder() + "/" + filter.getObjectName()).replaceAll("//+", "/");
            break;
        case JOBCHAIN:
            this.jobChain = (filter.getFolder() + "/" + filter.getObjectName()).replaceAll("//+", "/");
            break;
        case ORDER:
            this.order = (filter.getFolder() + "/" + filter.getObjectName()).replaceAll("//+", "/");
            break;
        default:
            break;
        }
    }
    
    public DeployJoeAudit(DBItemJoeObject joeObject, FilterDeploy filter) {
        setJobschedulerId(filter.getJobschedulerId());
        try {
            setObjectType(JobSchedulerObjectType.fromValue(joeObject.getObjectType()));
        } catch (Exception e) {
            setObjectType(filter.getObjectType());
        }
        setAuditParams(filter.getAuditLog());
        setToDelete(joeObject.operationIsDelete());
        Path path = Paths.get(joeObject.getPath());
        switch (joeObject.getObjectType()) {
        case "JOB":
            this.job = joeObject.getPath();
            setFolder(joeObject.getFolder());
            setObjectName(path.getFileName().toString());
            break;
        case "JOBCHAIN":
            this.jobChain = joeObject.getPath();
            setFolder(joeObject.getFolder());
            setObjectName(path.getFileName().toString());
            break;
        case "ORDER":
            this.order = joeObject.getPath();
            setFolder(joeObject.getFolder());
            setObjectName(path.getFileName().toString());
            break;
        case "FOLDER":
            setFolder(joeObject.getPath());
            setObjectName(null);
            break;
        default:
            setFolder(joeObject.getFolder());
            setObjectName(path.getFileName().toString());
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

    @JsonProperty("toDelete")
    public Boolean getToDelete() {
        return toDelete;
    }

    @JsonProperty("toDelete")
    public void setToDelete(Boolean toDelete) {
        this.toDelete = toDelete;
    }
}
