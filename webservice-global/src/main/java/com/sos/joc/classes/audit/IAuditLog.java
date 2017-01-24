package com.sos.joc.classes.audit;

public interface IAuditLog {
    
    public String getComment();
    
    public String getFolder();
    
    public String getJob();
    
    public String getJobChain();
    
    public String getOrderId();
}
