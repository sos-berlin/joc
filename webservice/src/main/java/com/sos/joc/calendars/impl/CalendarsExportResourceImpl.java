package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsExportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.calendar.CalendarsFilter;

@Path("calendars")
public class CalendarsExportResourceImpl extends JOCResourceImpl implements ICalendarsExportResource {

    private static final String API_CALL = "./calendars/export";

    @Override
    public JOCDefaultResponse exportCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, calendarsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Long instanceId = dbItemInventoryInstance.getId();
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            CalendarUsageDBLayer usageDBLayer = new CalendarUsageDBLayer(connection);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            Set<String> calendarsToExport = new HashSet<String>(calendarsFilter.getCalendars());
            List<DBItemCalendar> calendarsFromDb = new ArrayList<DBItemCalendar>();
            if (calendarsToExport != null) {
                calendarsFromDb = dbLayer.getCalendarsFromPaths(instanceId, calendarsToExport);
            }
            List<Calendar> calendarList = new ArrayList<Calendar>();
            if (calendarsFromDb != null && !calendarsFromDb.isEmpty()) {
                for (DBItemCalendar dbCalendar : calendarsFromDb) {
                    if (dbCalendar.getConfiguration() != null) {
                        Calendar calendar = objectMapper.readValue(dbCalendar.getConfiguration(), Calendar.class);
                        calendar.setPath(dbCalendar.getName());
                        calendarList.add(calendar);
                        List<DBItemInventoryCalendarUsage> dbUsages = usageDBLayer.getCalendarUsages(dbCalendar.getId());
                        if (dbUsages != null && !dbUsages.isEmpty()) {
                            for (DBItemInventoryCalendarUsage dbUsage : dbUsages) {
                                if (dbUsage.getConfiguration() != null) {
                                    Calendar usage = objectMapper.readValue(dbUsage.getConfiguration(), Calendar.class);
                                    switch (dbUsage.getObjectType()) {
                                    case "JOB":
                                        usage.setType(CalendarType.JOB);
                                        break;
                                    case "ORDER":
                                        usage.setType(CalendarType.ORDER);
                                        break;
                                    case "SCHEDULE":
                                        usage.setType(CalendarType.SCHEDULE);
                                        break;
                                    }
                                    usage.setPath(dbUsage.getPath());
                                    calendarList.add(usage);
                                }
                            }
                        }
                    }
                }
            }
            
            Calendars entity = new Calendars();
            entity.setCalendars(calendarList);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(objectMapper.writeValueAsString(entity), "calendars.json");
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}