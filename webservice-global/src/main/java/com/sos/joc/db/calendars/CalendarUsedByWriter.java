package com.sos.joc.db.calendars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.model.calendar.Calendar;

import sos.xml.SOSXMLXPath;

public class CalendarUsedByWriter {

    private String schedulerId;
    private String path;
    private CalendarObjectType objectType;
    private String command;
    private SOSHibernateSession sosHibernateSession;
    private Map<String, Calendar> calendars = new HashMap<String, Calendar>();
    private boolean calendarEventIsNecessary = false;

    public CalendarUsedByWriter(SOSHibernateSession sosHibernateSession, String schedulerId, CalendarObjectType objectType, String path,
            String command) {
        super();
        this.sosHibernateSession = sosHibernateSession;
        this.schedulerId = schedulerId;
        this.objectType = objectType;
        this.path = path;
        this.command = command;
    }

    public CalendarUsedByWriter(SOSHibernateSession sosHibernateSession, String schedulerId, CalendarObjectType objectType, String path, String command,
            List<Calendar> calendars) {
        super();
        this.sosHibernateSession = sosHibernateSession;
        this.schedulerId = schedulerId;
        this.objectType = objectType;
        this.path = path;
        this.command = command;
        if (calendars != null) {
            for (Calendar calendar : calendars) {
                if (calendar.getBasedOn() != null) {
                    this.calendars.put(calendar.getBasedOn(), calendar);
                }
            }
        }
    }

    public void updateUsedBy() throws Exception {
        SOSXMLXPath sosxml = new SOSXMLXPath(new StringBuffer(command));
        Set<String> calendarPaths = new HashSet<String>();
        NodeList calendarNodes = sosxml.selectNodeList("//date/@calendar|//holiday/@calendar");
        ObjectMapper om = new ObjectMapper();

        try {
            sosHibernateSession.beginTransaction();
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(this.sosHibernateSession);
            CalendarsDBLayer calendarsDBLayer = new CalendarsDBLayer(this.sosHibernateSession);
            List<DBItemInventoryClusterCalendarUsage> dbCalendarUsage = calendarUsageDBLayer.getCalendarUsagesOfAnObject(schedulerId, objectType.name(),
                    path);
            calendarEventIsNecessary = dbCalendarUsage.size() + calendars.size() > 0;
            DBItemInventoryClusterCalendarUsage calendarUsageDbItem = new DBItemInventoryClusterCalendarUsage();
            calendarUsageDbItem.setSchedulerId(schedulerId);
            calendarUsageDbItem.setObjectType(objectType.name());
            calendarUsageDbItem.setEdited(false);
            calendarUsageDbItem.setPath(path);

            for (int i = 0; i < calendarNodes.getLength(); i++) {
                String calendarPath = calendarNodes.item(i).getNodeValue();
                if (calendarPath != null && !calendarPaths.contains(calendarPath)) {
                    calendarPaths.add(calendarPath);
                    DBItemInventoryClusterCalendar calendarDbItem = calendarsDBLayer.getCalendar(schedulerId, calendarPath);
                    if (calendarDbItem != null) {
                        calendarUsageDbItem.setCalendarId(calendarDbItem.getId());
                        Calendar calendar = calendars.get(calendarPath);
                        if (calendar != null) {
                            calendar.setBasedOn(null);
                            calendar.setType(null);
                            //check if periods exist for working day caöendar
                            calendarUsageDbItem.setConfiguration(om.writeValueAsString(calendar));
                        }
                        int index = dbCalendarUsage.indexOf(calendarUsageDbItem);
                        if (index == -1) {
                            calendarUsageDBLayer.saveCalendarUsage(calendarUsageDbItem);
                        } else {
                            DBItemInventoryClusterCalendarUsage dbItem = dbCalendarUsage.remove(index);
                            dbItem.setEdited(false);
                            dbItem.setConfiguration(calendarUsageDbItem.getConfiguration());
                            calendarUsageDBLayer.updateCalendarUsage(dbItem);
                        }
                    }
                }
            }
            for (DBItemInventoryClusterCalendarUsage dbItem : dbCalendarUsage) {
                calendarUsageDBLayer.deleteCalendarUsage(dbItem);
            }
            sosHibernateSession.commit();
        } catch (Exception e) {
            sosHibernateSession.rollback();
            throw e;
        }
    }

    public String getPublishEventCommand() throws JsonProcessingException {
        CalendarEvent calEvt = new CalendarEvent();
        calEvt.setKey("CalendarUsageUpdated");
        CalendarVariables calEvtVars = new CalendarVariables();
        calEvtVars.setPath(path);
        calEvtVars.setObjectType(objectType);
        calEvt.setVariables(calEvtVars);
        String xmlCommand = new ObjectMapper().writeValueAsString(calEvt);
        xmlCommand = "<publish_event>" + xmlCommand + "</publish_event>";
        return xmlCommand;
    }
    
    public CalendarEvent getCalendarEvent() throws JsonProcessingException {
        if (!calendarEventIsNecessary) {
            return null;
        }
        CalendarEvent calEvt = new CalendarEvent();
        calEvt.setKey("CalendarUsageUpdated");
        CalendarVariables calEvtVars = new CalendarVariables();
        calEvtVars.setPath(path);
        calEvtVars.setObjectType(objectType);
        calEvt.setVariables(calEvtVars);
        return calEvt;
    }

}
