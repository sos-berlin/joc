package com.sos.joc.db.yade;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.sos.joc.model.order.OrderPath;

public class TransferOrderPath {
    
    private OrderPath orderPath = new OrderPath();
    private String jobChainNode = null;

    public TransferOrderPath(String jobChain, String orderId, String jobChainNode) {
        orderPath.setJobChain(jobChain);
        orderPath.setOrderId(orderId);
        this.setJobChainNode(jobChainNode);
    }

    public OrderPath getOrderPath() {
        return orderPath;
    }
    
    public void setOrderPath(OrderPath orderPath) {
        this.orderPath = orderPath;
    }

    public String getJobChainNode() {
        return jobChainNode;
    }

    public void setJobChainNode(String jobChainNode) {
        this.jobChainNode = jobChainNode;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(orderPath).append(jobChainNode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TransferOrderPath) == false) {
            return false;
        }
        TransferOrderPath rhs = ((TransferOrderPath) other);
        return new EqualsBuilder().append(orderPath, rhs.orderPath).append(jobChainNode, rhs.jobChainNode).isEquals();
    }
}
