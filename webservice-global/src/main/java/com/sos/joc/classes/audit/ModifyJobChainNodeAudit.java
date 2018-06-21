package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.jobChain.ModifyJobChainNode;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;


public class ModifyJobChainNodeAudit extends ModifyJobChainNode implements IAuditLog {
    
    @JsonIgnore
    private String folder;
    
//    //@JsonIgnore
//    private String jobChain;
    
    @JsonIgnore
    private String comment;
    
    @JsonIgnore
    private Integer timeSpent;
    
    @JsonIgnore
    private String ticketLink;
    
    //@JsonIgnore
    private String jobschedulerId;
    
    public ModifyJobChainNodeAudit(ModifyJobChainNode modifyJobChainNode, ModifyJobChainNodes jobChainNodes) {
        if (modifyJobChainNode != null) {
            setJobChain(modifyJobChainNode.getJobChain());
            setNode(modifyJobChainNode.getNode());
            if (modifyJobChainNode.getJobChain() != null) {
                Path p = Paths.get(modifyJobChainNode.getJobChain());
                this.folder = p.getParent().toString().replace('\\', '/');
                //this.jobChain = p.toString().replace('\\', '/');
            }
        }
        if (jobChainNodes != null) {
            setAuditParams(jobChainNodes.getAuditLog());
            this.jobschedulerId = jobChainNodes.getJobschedulerId();
        }
    }

    private void setAuditParams(AuditParams auditParams) {
        if (auditParams != null) {
            this.comment = auditParams.getComment();
            this.timeSpent = auditParams.getTimeSpent();
            this.ticketLink = auditParams.getTicketLink(); 
        }
    }

    @Override
    @JsonIgnore
    public String getComment() {
        return comment;
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
    public String getFolder() {
        return folder;
    }

    @Override
    @JsonIgnore
    public String getJob() {
        return null;
    }

//    @Override
//    //@JsonIgnore
//    public String getJobChain() {
//        return jobChain;
//    }

    @Override
    @JsonIgnore
    public String getOrderId() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getCalendar() {
        return null;
    }

    @Override
    //@JsonIgnore
    public String getJobschedulerId() {
        return jobschedulerId;
    }
}
