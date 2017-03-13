package com.sos.joc.classes.orders;

import java.util.ArrayList;
import java.util.List;

public class OrdersPerJobChain {

    private String jobChain;
    private boolean isOuterJobChain = false;
    private List<String> orders = new ArrayList<String>();

    public OrdersPerJobChain() {
    }

    public String getJobChain() {
        return jobChain;
    }

    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }
    
    public boolean isOuterJobChain() {
        return isOuterJobChain;
    }

    public void setIsOuterJobChain(boolean isOuterJobChain) {
        this.isOuterJobChain = isOuterJobChain;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
    
    public void addOrder(String order) {
        if (order != null && !order.isEmpty()) {
            orders.add(order);
        }
    }
    
    public boolean containsOrder(String order) {
        return orders.contains(order);
    }
}
