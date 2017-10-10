package com.sos.joc.db.calendars;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.NodeList;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;

import sos.xml.SOSXMLXPath;

public class CalendarUsedByWriter {
    private Long instanceId;
    private String path;
    private String objectType;
    private String command;
    private SOSHibernateSession sosHibernateSession;

    public CalendarUsedByWriter(SOSHibernateSession sosHibernateSession, Long instanceId, String objectType, String path, String command) {
        super();
        this.sosHibernateSession = sosHibernateSession;
        this.instanceId = instanceId;
        this.objectType = objectType;
        this.path = path;
        this.command = command;
    }
    
    private Long getCalendarId(String calendarPath) throws DBConnectionRefusedException, DBInvalidDataException {
        CalendarsDBLayer calendarsDBLayer = new CalendarsDBLayer(sosHibernateSession);
        DBItemCalendar dbItemCalendar = calendarsDBLayer.getCalendar(calendarPath);
        if (dbItemCalendar == null) {
            return null;
        }
        return dbItemCalendar.getId();
    }

    public void deleteUsedBy() throws DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException {
        CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(this.sosHibernateSession);
        CalendarUsageFilter calendarUsageFilter = new CalendarUsageFilter();
        calendarUsageFilter.setInstanceId(instanceId);
        calendarUsageFilter.setObjectType(objectType);
        calendarUsageFilter.setPath(path);
        sosHibernateSession.beginTransaction();
        calendarUsageDBLayer.deleteCalendarUsage(calendarUsageFilter);
        sosHibernateSession.commit();
    }

    public void updateUsedBy() throws Exception {
        SOSXMLXPath sosxml = new SOSXMLXPath(new StringBuffer(command));
        Set<String> calendarPaths = new HashSet<String>();
        NodeList calendars = sosxml.selectNodeList("//date/@calendar|//holiday/@calendar");
        
        sosHibernateSession.beginTransaction();
        CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(this.sosHibernateSession);
        List<DBItemInventoryCalendarUsage> dbCalendars = calendarUsageDBLayer.getCalendarUsagesOfAnObject(instanceId, objectType, path);
        DBItemInventoryCalendarUsage calendarUsageDbItem = new DBItemInventoryCalendarUsage();
        calendarUsageDbItem.setCreated(new Date());
        calendarUsageDbItem.setInstanceId(instanceId);
        calendarUsageDbItem.setObjectType(objectType);
        calendarUsageDbItem.setPath(path);
        
        for (int i = 0; i < calendars.getLength(); i++) {
            String calendarPath = calendars.item(i).getNodeValue();
            if (!calendarPaths.contains(calendarPath)) {
                calendarPaths.add(calendarPath);
                Long calendarId = getCalendarId(calendarPath);
                calendarId = i+1L;
                if (calendarId != null) {
                    calendarUsageDbItem.setCalendarId(calendarId);
                    if (dbCalendars.contains(calendarUsageDbItem)) {
                        dbCalendars.remove(calendarUsageDbItem);
                    } else {
                        calendarUsageDBLayer.saveCalendarUsage(calendarUsageDbItem);
                    }
                }
            }
        }
        for (DBItemInventoryCalendarUsage dbItem : dbCalendars) {
            calendarUsageDBLayer.deleteCalendarUsage(dbItem);
        }
        sosHibernateSession.commit();
    }

}
