package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;


public class ModifyJobChainAudit extends ModifyJobChains implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    @JsonIgnore
    private String jobChain;
    
    public ModifyJobChainAudit(ModifyJobChain modifyJobChain, ModifyJobChains modifyJobChains) {
        if (modifyJobChain != null) {
            getJobChains().add(modifyJobChain);
            if (modifyJobChain.getJobChain() != null) {
                Path p = Paths.get(modifyJobChain.getJobChain());
                this.folder = p.getParent().toString().replace('\\', '/');
                this.jobChain = p.getFileName().toString();
            }
        }
        if (modifyJobChains != null) {
            setComment(modifyJobChains.getComment());
            setJobschedulerId(modifyJobChains.getJobschedulerId());            
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
