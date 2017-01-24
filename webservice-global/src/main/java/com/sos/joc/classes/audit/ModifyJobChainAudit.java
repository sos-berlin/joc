package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;


public class ModifyJobChainAudit extends ModifyJobChains implements IAuditLog {
    
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

    public ModifyJobChainAudit(ModifyJobChain modifyJobChain, String jobschedulerId) {
        Path p = Paths.get(modifyJobChain.getJobChain());
        this.comment = modifyJobChain.getComment();
        this.folder = p.getParent().toString().replace('\\', '/');
        this.job = null;
        this.jobChain = p.getFileName().toString();
        this.orderId = null;
        modifyJobChain.setComment(null);
        getJobChains().add(modifyJobChain);
        setJobschedulerId(jobschedulerId);
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
