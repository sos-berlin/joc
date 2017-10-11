package com.sos.joc.db.calendars;

import java.util.ArrayList;
import java.util.List;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;

public class CalendarUsagesAndInstance {

    private List<DBItemInventoryCalendarUsage> calendarUsages = null;
    private DBItemInventoryInstance instance = null;
    private List<String> dates = new ArrayList<String>();

    public CalendarUsagesAndInstance(DBItemInventoryInstance instance) {
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

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public void setAllEdited() {
        if (this.calendarUsages != null) {
            for (DBItemInventoryCalendarUsage item : this.calendarUsages) {
                if (!item.getEdited()) {
                    item.setEdited(true);
                }
            }
        }
    }

    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance) {
        if (Globals.jocConfigurationProperties != null) {
            return Globals.jocConfigurationProperties.setUrlMapping(instance, false);
        }
        return instance;
    }

}
