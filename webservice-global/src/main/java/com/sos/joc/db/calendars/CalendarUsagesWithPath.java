package com.sos.joc.db.calendars;

import java.util.List;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;

public class CalendarUsagesWithPath {

    private DBItemInventoryCalendarUsage dBItemInventoryCalendarUsage = null;
    private String calendarPath = null;
    private List<String> dates = null;

    public CalendarUsagesWithPath(DBItemInventoryCalendarUsage dBItemInventoryCalendarUsage, String calendarPath) {
        this.setDBItemInventoryCalendarUsage(dBItemInventoryCalendarUsage);
        this.setCalendarPath(calendarPath);
    }

    public String getCalendarPath() {
        return calendarPath;
    }

    public void setCalendarPath(String calendarPath) {
        this.calendarPath = calendarPath;
    }

    public DBItemInventoryCalendarUsage getDBItemInventoryCalendarUsage() {
        return dBItemInventoryCalendarUsage;
    }

    public void setDBItemInventoryCalendarUsage(DBItemInventoryCalendarUsage dBItemInventoryCalendarUsage) {
        this.dBItemInventoryCalendarUsage = dBItemInventoryCalendarUsage;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    
}
