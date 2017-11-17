package com.sos.joc.db.calendars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.model.calendar.Calendar;

public class CalendarUsagesAndInstance {

    private Set<CalendarUsagesWithPath> calendarUsages = null;
    private DBItemInventoryInstance instance = null;
    private Calendar baseCalendar = null;
    private List<String> dates = new ArrayList<String>();
    private Map<String, Exception> exceptions = new HashMap<String, Exception>();

    public CalendarUsagesAndInstance(DBItemInventoryInstance instance) {
        this.instance = setMappedUrl(instance);
    }

    public CalendarUsagesAndInstance(DBItemInventoryInstance instance, boolean withUrlMapping) {
        if (withUrlMapping) {
            this.instance = setMappedUrl(instance);
        } else {
            this.instance = instance;
        }
    }

    public Set<CalendarUsagesWithPath> getCalendarUsages() {
        return calendarUsages;
    }

    public void setCalendarUsages(List<CalendarUsagesWithPath> calendarUsages) {
        if (calendarUsages != null && !calendarUsages.isEmpty()) {
            this.calendarUsages = new HashSet<CalendarUsagesWithPath>(calendarUsages);
        }
    }

    public DBItemInventoryInstance getInstance() {
        return instance;
    }

    public Calendar getBaseCalendar() {
        return baseCalendar;
    }

    public void setBaseCalendar(Calendar baseCalendar) {
        this.baseCalendar = baseCalendar;
    }

    public List<String> getDates() {
        if (dates == null) {
            dates = new ArrayList<String>();
        }
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public void setAllEdited(Exception e) {
        if (this.calendarUsages != null) {
            for (CalendarUsagesWithPath item : this.calendarUsages) {
                if (!item.getDBItemInventoryCalendarUsage().getEdited()) {
                    item.getDBItemInventoryCalendarUsage().setEdited(true);
                }
                String key = String.format("%1$s: %2$s on %3$s:%4$d", item.getDBItemInventoryCalendarUsage().getObjectType(), item.getDBItemInventoryCalendarUsage().getPath(), instance.getHostname(), instance.getPort());
                exceptions.put(key, e);
            }
        }
    }

    private DBItemInventoryInstance setMappedUrl(DBItemInventoryInstance instance) {
        if (Globals.jocConfigurationProperties != null) {
            return Globals.jocConfigurationProperties.setUrlMapping(instance, false);
        }
        return instance;
    }

    public Map<String, Exception> getExceptions() {
        return exceptions;
    }

    public void setExceptions(Map<String, Exception> exceptions) {
        this.exceptions = exceptions;
    }
    
    public void putException(DBItemInventoryCalendarUsage item, Exception exception) {
        String key = String.format("%1$s: %2$s on %3$s:%4$d", item.getObjectType(), item.getPath(), instance.getHostname(), instance.getPort());
        this.exceptions.put(key, exception);
    }

}
