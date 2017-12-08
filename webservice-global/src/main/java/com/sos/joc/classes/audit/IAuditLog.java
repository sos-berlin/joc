package com.sos.joc.classes.audit;

public interface IAuditLog {
    
    public String getComment();
    
    public String getFolder();
    
    public String getJob();
    
    public String getJobChain();
    
    public String getOrderId();
    
    public String getJobschedulerId();
    
    public Integer getTimeSpent();
    
    public String getTicketLink();
    
    public String getCalendar();
}
