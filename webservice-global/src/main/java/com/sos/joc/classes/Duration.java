package com.sos.joc.classes;

import java.util.Date;


public class Duration {

    private Date startTime;
    private Date endTime;
    private Long durationInMillis;
    
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public Long getDurationInMillis() {
        return durationInMillis;
    }
    
    public void initDuration(){
        if(startTime != null && endTime != null) {
            durationInMillis = endTime.getTime() - startTime.getTime();
        }
    }
    
}
