package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;


public class ModifyJobChainNodeAudit extends ModifyJobChainNodes implements IAuditLog {
    
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

    public ModifyJobChainNodeAudit(ModifyJobChainNode modifyJobChainNode, String jobschedulerId) {
        Path p = Paths.get(modifyJobChainNode.getJobChain());
        this.comment = modifyJobChainNode.getComment();
        this.folder = p.getParent().toString().replace('\\', '/');
        this.job = null;
        this.jobChain = p.getFileName().toString();
        this.orderId = null;
        modifyJobChainNode.setComment(null);
        getNodes().add(modifyJobChainNode);
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
