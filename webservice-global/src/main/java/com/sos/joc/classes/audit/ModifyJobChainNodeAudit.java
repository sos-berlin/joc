package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;


public class ModifyJobChainNodeAudit extends ModifyJobChainNodes implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
    @JsonIgnore
    private String jobChain;
    
    @JsonIgnore
    private String comment;
    
    public ModifyJobChainNodeAudit(ModifyJobChainNode modifyJobChainNode, ModifyJobChainNodes jobChainNodes) {
        if (modifyJobChainNode != null) {
            getNodes().add(modifyJobChainNode);
            if (modifyJobChainNode.getJobChain() != null) {
                Path p = Paths.get(modifyJobChainNode.getJobChain());
                this.folder = p.getParent().toString().replace('\\', '/');
                this.jobChain = p.getFileName().toString();
            }
        }
        if (jobChainNodes != null) {
            this.comment = jobChainNodes.getComment(); 
            setJobschedulerId(jobChainNodes.getJobschedulerId());            
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
        return jobChain;
    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return null;
    }
}
