package com.sos.joc.db.yade;

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
}
