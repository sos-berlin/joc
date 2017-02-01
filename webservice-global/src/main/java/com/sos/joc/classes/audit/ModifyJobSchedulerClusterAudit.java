package com.sos.joc.classes.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobscheduler.TimeoutParameter;


public class ModifyJobSchedulerClusterAudit extends TimeoutParameter implements IAuditLog {
    
    public ModifyJobSchedulerClusterAudit(TimeoutParameter timeoutParameter) {
        if (timeoutParameter != null) {
            setComment(timeoutParameter.getComment());
            setTimeout(timeoutParameter.getTimeout());
            setJobschedulerId(timeoutParameter.getJobschedulerId()); 
        }
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return super.getComment();
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
