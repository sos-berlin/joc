package com.sos.joc.classes.audit;

import java.util.Date;

public interface IAuditLog {
    
    public String getComment();
    
    public String getFolder();
    
    public String getJob();
    
    public String getJobChain();
    public String getJobStream();
    
    public String getOrderId();
    
    public String getJobschedulerId();
    
    public Integer getTimeSpent();
    
    public String getTicketLink();
    
    public String getCalendar();
    
    public Date getStartTime();
}
