package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendarUsage;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsDeleteResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.ModifyCalendarAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsageFilter;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.CalendarsFilter;
import com.sos.joc.model.common.Err419;

@Path("calendars")
public class CalendarsDeleteResourceImpl extends JOCResourceImpl implements ICalendarsDeleteResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarsDeleteResourceImpl.class);
    private static final String API_CALL = "./calendars/delete";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private Set<String> eventCommands = new HashSet<String>();

    @Override
    public JOCDefaultResponse postDeleteCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
        SOSHibernateSession connection = null;
        List<DBItemInventoryInstance> clusterMembers = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, calendarsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(calendarsFilter.getJobschedulerId(), accessToken).getCalendar().getEdit().isDelete());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(calendarsFilter.getAuditLog());
            if (calendarsFilter.getCalendarIds() == null || calendarsFilter.getCalendarIds().size() == 0) {
                if (calendarsFilter.getCalendars() == null || calendarsFilter.getCalendars().size() == 0) {
                    throw new JocMissingRequiredParameterException("undefined 'calendars' parameter");
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer calendarDbLayer = new CalendarsDBLayer(connection);
            
            if (calendarsFilter.getCalendars() == null || calendarsFilter.getCalendars().size() == 0) {
                for (Long calendarId : new HashSet<Long>(calendarsFilter.getCalendarIds())) {
                    executeDeleteCalendar(connection, calendarDbLayer.getCalendar(calendarId), calendarsFilter, calendarDbLayer, accessToken);
                }
            } else {
                for (String calendarPath : new HashSet<String>(calendarsFilter.getCalendars())) {
                    executeDeleteCalendar(connection, calendarDbLayer.getCalendar(dbItemInventoryInstance.getSchedulerId(), calendarPath), calendarsFilter,
                            calendarDbLayer, accessToken);
                }
            }
            if(eventCommands != null && !eventCommands.isEmpty() && "active".equals(dbItemInventoryInstance.getClusterType())) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
                clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(calendarsFilter.getJobschedulerId());
            }
            if(clusterMembers != null) {
                SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
            } else {
                SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
            }
            eventCommands = null;
            if (listOfErrors.size() > 0) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            try {
                if(clusterMembers != null) {
                    SendCalendarEventsUtil.sendEvent(eventCommands, clusterMembers, accessToken);
                } else {
                    SendCalendarEventsUtil.sendEvent(eventCommands, dbItemInventoryInstance, accessToken);
                }
            } catch (Exception e) {
                LOGGER.error("Couldn't send calendar events", e);
            }
            Globals.disconnect(connection);
        }
    }

    private void executeDeleteCalendar(SOSHibernateSession connection, DBItemInventoryClusterCalendar calendarDbItem, CalendarsFilter calendarsFilter,
            CalendarsDBLayer calendarDbLayer, String accessToken) {
        if (calendarDbItem != null) {
            try {
                ModifyCalendarAudit calendarAudit = new ModifyCalendarAudit(calendarDbItem.getId(), calendarDbItem.getName(), calendarsFilter
                        .getAuditLog(), calendarsFilter.getJobschedulerId());
                logAuditMessage(calendarAudit);

                calendarDbLayer.deleteCalendar(calendarDbItem);
                sendEvent(calendarDbItem, accessToken);

                CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(calendarDbLayer.getSession());
                CalendarUsageFilter calUsageFilter = new CalendarUsageFilter();
                calUsageFilter.setCalendarId(calendarDbItem.getId());
                List<DBItemInventoryClusterCalendarUsage> usages = calendarUsageDbLayer.getCalendarUsages(calUsageFilter);
                if (usages != null) {
                    for (DBItemInventoryClusterCalendarUsage usage : usages) {
                        calendarUsageDbLayer.deleteCalendarUsage(usage);
                        eventCommands.add(SendCalendarEventsUtil.addCalUsageEvent(usage.getPath(), usage.getObjectType(), "CalendarUsageUpdated"));
                    }
                }

                storeAuditLogEntry(calendarAudit);
            } catch (JocException e) {
                listOfErrors.add(new BulkError().get(e, getJocError(), calendarDbItem.getName()));
            } catch (Exception e) {
                listOfErrors.add(new BulkError().get(e, getJocError(), calendarDbItem.getName()));
            }
        }
    }

    private void sendEvent(DBItemInventoryClusterCalendar calendarDbItem, String accessToken) throws JsonProcessingException, JocException {
        CalendarEvent calEvt = new CalendarEvent();
        calEvt.setKey("CalendarDeleted");
        CalendarVariables calEvtVars = new CalendarVariables();
        calEvtVars.setPath(calendarDbItem.getName());
        if (CalendarType.NON_WORKING_DAYS.name().equals(calendarDbItem.getType())) {
            calEvtVars.setObjectType(CalendarObjectType.NONWORKINGDAYSCALENDAR);
        } else {
            calEvtVars.setObjectType(CalendarObjectType.WORKINGDAYSCALENDAR);
        }
        calEvt.setVariables(calEvtVars);
        eventCommands.add(SendCalendarEventsUtil.addEvent(calEvt));
    }

}