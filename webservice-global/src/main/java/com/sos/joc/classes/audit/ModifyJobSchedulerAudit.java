package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;


public class ModifyJobSchedulerAudit extends HostPortTimeOutParameter implements IAuditLog {
    
    @JsonIgnore
    private String comment;
    
    public ModifyJobSchedulerAudit(HostPortTimeOutParameter hostPortTimeoutParamSchema) {
        if (hostPortTimeoutParamSchema != null) {
            this.comment = hostPortTimeoutParamSchema.getComment();
            setHost(hostPortTimeoutParamSchema.getHost());
            setPort(hostPortTimeoutParamSchema.getPort());
            setTimeout(hostPortTimeoutParamSchema.getTimeout());
            setJobschedulerId(hostPortTimeoutParamSchema.getJobschedulerId()); 
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
