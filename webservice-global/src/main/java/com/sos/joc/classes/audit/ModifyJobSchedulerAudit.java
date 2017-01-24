package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;
import com.sos.joc.model.jobscheduler.TimeoutParameter;


public class ModifyJobSchedulerAudit extends HostPortTimeOutParameter implements IAuditLog {
    
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

    public ModifyJobSchedulerAudit(HostPortTimeOutParameter hostPortTimeoutParamSchema) {
        this.comment = hostPortTimeoutParamSchema.getComment();
        this.folder = null;
        this.job = null;
        this.jobChain = null;
        this.orderId = null;
        setHost(hostPortTimeoutParamSchema.getHost());
        setPort(hostPortTimeoutParamSchema.getPort());
        setTimeout(hostPortTimeoutParamSchema.getTimeout());
        setJobschedulerId(hostPortTimeoutParamSchema.getJobschedulerId());
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
