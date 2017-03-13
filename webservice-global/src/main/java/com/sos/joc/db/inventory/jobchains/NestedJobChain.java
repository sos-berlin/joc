package com.sos.joc.db.inventory.jobchains;

public class NestedJobChain {

    private String outerJobChain;
    private String innerJobChain;

    public NestedJobChain(String outerJobChain, String innerJobChain) {
        this.outerJobChain = outerJobChain;
        this.innerJobChain = innerJobChain;
    }

    public String getOuterJobChain() {
        return outerJobChain;
    }
    
    public String getInnerJobChain() {
        return innerJobChain;
    }
}
