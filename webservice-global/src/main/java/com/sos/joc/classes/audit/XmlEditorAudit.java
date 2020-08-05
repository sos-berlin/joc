package com.sos.joc.classes.audit;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.deploy.DeployConfiguration;
import com.sos.joc.model.xmleditor.read.ReadConfiguration;
import com.sos.joc.model.xmleditor.store.StoreConfiguration;
import com.sos.joc.model.xmleditor.validate.ValidateConfiguration;

public class XmlEditorAudit implements IAuditLog {

    @JsonProperty("objectType")
    private ObjectType objectType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("schemaLocation")
    private String schemaLocation;

    private String jobschedulerId;

    @JsonIgnore
    private String folder;

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;

    @JsonIgnore
    private String ticketLink;

    @JsonIgnore
    private Date startTime;

    public XmlEditorAudit(DeployConfiguration in) {
        jobschedulerId = in.getJobschedulerId();
        objectType = in.getObjectType();
        name = JocXmlEditor.getConfigurationName(objectType);
        schemaLocation = JocXmlEditor.getStandardRelativeSchemaLocation(objectType);
        folder = JobSchedulerXmlEditor.getNormalizedLiveFolder(objectType);
        setAuditParams(in.getAuditLog());
    }

    public XmlEditorAudit(ReadConfiguration in) {
        jobschedulerId = in.getJobschedulerId();
        objectType = in.getObjectType();
        name = String.valueOf(in.getId());
        folder = JobSchedulerXmlEditor.getNormalizedLiveFolder(objectType);
    }

    public XmlEditorAudit(StoreConfiguration in, String schemaPath) {
        jobschedulerId = in.getJobschedulerId();
        objectType = in.getObjectType();
        name = in.getName();
        schemaLocation = schemaPath;
        folder = JobSchedulerXmlEditor.getNormalizedLiveFolder(objectType);
    }

    public XmlEditorAudit(ValidateConfiguration in, String schemaPath) {
        jobschedulerId = in.getJobschedulerId();
        objectType = in.getObjectType();
        schemaLocation = schemaPath;
        folder = JobSchedulerXmlEditor.getNormalizedLiveFolder(objectType);
    }

    private void setAuditParams(AuditParams auditParams) {
        if (auditParams != null) {
            comment = auditParams.getComment();
            timeSpent = auditParams.getTimeSpent();
            ticketLink = auditParams.getTicketLink();
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
        return startTime;
    }

    public void setStartTime(Date val) {
        startTime = val;
    }

    @Override
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    @Override
    @JsonIgnore
    public String getJobStream() {
        return null;
    }
}
