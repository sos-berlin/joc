package com.sos.joc.db.calendars;

import java.util.List;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;

public class CalendarUsageAndInstances {
    
    private List<DBItemInventoryCalendarUsage> calendarUsages = null;
    private DBItemInventoryInstance instance = null;
    
    public CalendarUsageAndInstances(DBItemInventoryInstance instance) {
        this.instance = setMappedUrl(instance);
    }
    
    public List<DBItemInventoryCalendarUsage> getCalendarUsages() {
        return calendarUsages;
    }
    
    public void setCalendarUsages(List<DBItemInventoryCalendarUsage> calendarUsages) {
        this.calendarUsages = calendarUsages;
    }
    
    public DBItemInventoryInstance getInstance() {
        return instance;
    }
    
    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance) {
        if (Globals.jocConfigurationProperties != null) {
            return Globals.jocConfigurationProperties.setUrlMapping(instance, false);
        }
        return instance;
    }

}
