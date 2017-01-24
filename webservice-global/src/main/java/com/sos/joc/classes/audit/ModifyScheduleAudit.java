package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.schedule.ModifyRunTime;


public class ModifyScheduleAudit extends ModifyRunTime implements IAuditLog {
    
    @JsonIgnore
    private String comment;
    @JsonIgnore
    private String folder;
    @JsonIgnore
    private String job;
    @JsonIgnore
    private String jobChain;
    @JsonIgnore
    private String orderId;

    public ModifyScheduleAudit(ModifyRunTime modifyRunTime) {
        Path p = Paths.get(modifyRunTime.getSchedule());
        this.folder = p.getParent().toString().replace('\\', '/');
        this.job = null;
        this.jobChain = null;
        this.orderId = null;
        setSchedule(modifyRunTime.getSchedule());
        setRunTime(modifyRunTime.getRunTime());
        setJobschedulerId(modifyRunTime.getJobschedulerId());
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
}
