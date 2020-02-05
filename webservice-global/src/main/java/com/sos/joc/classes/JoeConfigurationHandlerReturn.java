package com.sos.joc.classes;

import java.util.Date;

import org.w3c.dom.Node;

public class JoeConfigurationHandlerReturn {
    private Node sourceNode;
    private Date lastWrite;
    
    public Node getSourceNode() {
        return sourceNode;
    }
    
    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }
    
    public Date getLastWrite() {
        return lastWrite;
    }
    
    public void setLastWrite(Date lastWrite) {
        this.lastWrite = lastWrite;
    }

}
