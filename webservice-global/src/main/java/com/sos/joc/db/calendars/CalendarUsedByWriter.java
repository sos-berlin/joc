package com.sos.joc.db.calendars;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.NodeList;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
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
    
    public void deleteUsedBy() throws DBConnectionRefusedException, DBInvalidDataException, SOSHibernateException {
        CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(this.sosHibernateSession);
        CalendarUsageFilter calendarUsageFilter = new CalendarUsageFilter();
        calendarUsageFilter.setInstanceId(instanceId);
        calendarUsageFilter.setObjectType(objectType);
        calendarUsageFilter.setPath(path);
        try {
            sosHibernateSession.beginTransaction();
            calendarUsageDBLayer.deleteCalendarUsage(calendarUsageFilter);
            sosHibernateSession.commit();
        } catch (Exception e) {
            sosHibernateSession.rollback();
            throw e;
        }
    }

    public void updateUsedBy() throws Exception {
        SOSXMLXPath sosxml = new SOSXMLXPath(new StringBuffer(command));
        Set<Long> calendarIds = new HashSet<Long>();
        NodeList calendars = sosxml.selectNodeList("//date/@calendar|//holiday/@calendar");
        
        try {
            sosHibernateSession.beginTransaction();
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(this.sosHibernateSession);
            CalendarsDBLayer calendarsDBLayer = new CalendarsDBLayer(this.sosHibernateSession);
            List<DBItemInventoryCalendarUsage> dbCalendarUsage = calendarUsageDBLayer.getCalendarUsagesOfAnObject(instanceId, objectType, path);
            DBItemInventoryCalendarUsage calendarUsageDbItem = new DBItemInventoryCalendarUsage();
            calendarUsageDbItem.setInstanceId(instanceId);
            calendarUsageDbItem.setObjectType(objectType);
            calendarUsageDbItem.setEdited(false);
            calendarUsageDbItem.setPath(path);
            
            for (int i = 0; i < calendars.getLength(); i++) {
                Long calendarId = Long.parseLong(calendars.item(i).getNodeValue());
                if (calendarId != null && !calendarIds.contains(calendarId)) {
                    calendarIds.add(calendarId);
                    if (calendarsDBLayer.getCalendar(calendarId) != null) {
                        calendarUsageDbItem.setCalendarId(calendarId);
                        if (!dbCalendarUsage.remove(calendarUsageDbItem)) {
                            calendarUsageDBLayer.saveCalendarUsage(calendarUsageDbItem);
                        } 
                    }
                }
            }
            for (DBItemInventoryCalendarUsage dbItem : dbCalendarUsage) {
                calendarUsageDBLayer.deleteCalendarUsage(dbItem);
            }
            sosHibernateSession.commit();
        } catch (Exception e) {
            sosHibernateSession.rollback();
            throw e;
        }
    }

}
