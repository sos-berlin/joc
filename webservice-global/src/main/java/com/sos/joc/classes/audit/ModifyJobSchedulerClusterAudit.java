package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobscheduler.TimeoutParameter;


public class ModifyJobSchedulerClusterAudit extends TimeoutParameter implements IAuditLog {
    
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

    public ModifyJobSchedulerClusterAudit(TimeoutParameter timeoutParameter) {
        this.comment = timeoutParameter.getComment();
        this.folder = null;
        this.job = null;
        this.jobChain = null;
        this.orderId = null;
        setTimeout(timeoutParameter.getTimeout());
        setJobschedulerId(timeoutParameter.getJobschedulerId());
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
