package com.sos.joc.db.calendars;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        HashMap<String, DBItemInventoryCalendarUsage> listOfusedBy = new HashMap<String, DBItemInventoryCalendarUsage>();
        NodeList calendars = sosxml.selectNodeList("//date/@calendar");
        for (int i = 0; i < calendars.getLength(); i++) {
            DBItemInventoryCalendarUsage calendarUsageDbItem = new DBItemInventoryCalendarUsage();
            Long calendarId = getCalendarId(calendars.item(i).getNodeValue());
//            calendarUsageDbItem.setCalendarId(Long.valueOf(calendars.item(i).getNodeValue()));
            calendarUsageDbItem.setCalendarId(calendarId);
            calendarUsageDbItem.setCreated(new Date());
            calendarUsageDbItem.setInstanceId(instanceId);
            calendarUsageDbItem.setObjectType(objectType);
            calendarUsageDbItem.setPath(path);
            listOfusedBy.put(calendars.item(i).getNodeValue(), calendarUsageDbItem);
            System.out.println(calendars.item(i).getNodeValue());
        }

        calendars = sosxml.selectNodeList("//holiday/@calendar");
        for (int i = 0; i < calendars.getLength(); i++) {
            DBItemInventoryCalendarUsage calendarUsageDbItem = new DBItemInventoryCalendarUsage();
            Long calendarId = getCalendarId(calendars.item(i).getNodeValue());
//          calendarUsageDbItem.setCalendarId(Long.valueOf(calendars.item(i).getNodeValue()));
          calendarUsageDbItem.setCalendarId(calendarId);
            calendarUsageDbItem.setCreated(new Date());
            calendarUsageDbItem.setInstanceId(instanceId);
            calendarUsageDbItem.setObjectType(objectType);
            calendarUsageDbItem.setPath(path);
            listOfusedBy.put(calendars.item(i).getNodeValue(), calendarUsageDbItem);
            System.out.println(calendars.item(i).getNodeValue());
        }

        sosHibernateSession.beginTransaction();
        CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(this.sosHibernateSession);
        CalendarUsageFilter calendarUsageFilter = new CalendarUsageFilter();
        for (DBItemInventoryCalendarUsage calendarUsageDbItem : listOfusedBy.values()) {
            calendarUsageFilter.setCalendarId(calendarUsageDbItem.getCalendarId());
            calendarUsageFilter.setInstanceId(calendarUsageDbItem.getInstanceId());
            calendarUsageFilter.setObjectType(calendarUsageDbItem.getObjectType());
            calendarUsageFilter.setPath(calendarUsageDbItem.getPath());
            calendarUsageDBLayer.deleteCalendarUsage(calendarUsageFilter);
            calendarUsageDBLayer.saveCalendarUsage(calendarUsageDbItem);
        }
        sosHibernateSession.commit();
    }

}
