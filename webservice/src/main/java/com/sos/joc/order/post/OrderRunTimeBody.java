package com.sos.joc.order.post;

public class OrderRunTimeBody {

    private String jobschedulerId;
    private String jobChain;
    private String orderId;

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public String getJobChain() {
        return jobChain;
    }

    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
//
}
