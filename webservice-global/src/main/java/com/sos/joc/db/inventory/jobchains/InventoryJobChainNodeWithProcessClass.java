package com.sos.joc.db.inventory.jobchains;

import com.sos.jitl.reporting.db.DBItemInventoryJobChainNode;

public class InventoryJobChainNodeWithProcessClass {
    
    private DBItemInventoryJobChainNode dBItemInventoryJobChainNode;
    private String processClass;
    
    public InventoryJobChainNodeWithProcessClass(DBItemInventoryJobChainNode dBItemInventoryJobChainNode, String processClass) {
        this.setdBItemInventoryJobChainNode(dBItemInventoryJobChainNode);
        if (!".".equals(processClass)) {
            this.setProcessClass(processClass);
        }
    }

    public DBItemInventoryJobChainNode getdBItemInventoryJobChainNode() {
        return dBItemInventoryJobChainNode;
    }

    public void setdBItemInventoryJobChainNode(DBItemInventoryJobChainNode dBItemInventoryJobChainNode) {
        this.dBItemInventoryJobChainNode = dBItemInventoryJobChainNode;
    }

    public String getProcessClass() {
        return processClass;
    }

    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

}
