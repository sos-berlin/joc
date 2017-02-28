package com.sos.joc.classes.audit;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

public class ModifyOrderAudit extends ModifyOrders implements IAuditLog {

    @JsonIgnore
    private String folder;

    @JsonIgnore
    private String jobChain;

    @JsonIgnore
    private String orderId;

    @JsonIgnore
    private String comment;

    @JsonIgnore
    private Integer timeSpent;

    @JsonIgnore
    private String ticketLink;

    public ModifyOrderAudit(ModifyOrder modifyOrder, ModifyOrders modifyOrders) {
        if (modifyOrder != null) {
            getOrders().add(modifyOrder);
            if (modifyOrder.getJobChain() != null) {
                Path p = Paths.get(modifyOrder.getJobChain());
                this.folder = p.getParent().toString().replace('\\', '/');
                this.jobChain = p.toString().replace('\\', '/');
                this.orderId = modifyOrder.getOrderId();
            }
        }
        if (modifyOrders != null) {
            setAuditParams(modifyOrders.getAuditLog());
            setJobschedulerId(modifyOrders.getJobschedulerId());
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
    
    @JsonIgnore
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
