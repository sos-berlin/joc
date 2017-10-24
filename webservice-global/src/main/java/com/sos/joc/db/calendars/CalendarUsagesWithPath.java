package com.sos.joc.db.calendars;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;

public class CalendarUsagesWithPath {

    private DBItemInventoryCalendarUsage dBItemInventoryCalendarUsage = null;
    private String calendarPath = null;

    public CalendarUsagesWithPath(DBItemInventoryCalendarUsage dBItemInventoryCalendarUsage, String calendarPath) {
        this.setdBItemInventoryCalendarUsage(dBItemInventoryCalendarUsage);
        this.setCalendarPath(calendarPath);
    }

    public String getCalendarPath() {
        return calendarPath;
    }

    public void setCalendarPath(String calendarPath) {
        this.calendarPath = calendarPath;
    }

    public DBItemInventoryCalendarUsage getdBItemInventoryCalendarUsage() {
        return dBItemInventoryCalendarUsage;
    }

    public void setdBItemInventoryCalendarUsage(DBItemInventoryCalendarUsage dBItemInventoryCalendarUsage) {
        this.dBItemInventoryCalendarUsage = dBItemInventoryCalendarUsage;
    }

    
}
