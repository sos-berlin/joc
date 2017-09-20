package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.calendar.CalendarsFilter;
import com.sos.joc.model.common.Folder;

@Path("calendars")
public class CalendarsResourceImpl extends JOCResourceImpl implements ICalendarsResource {

    private static final String API_CALL = "./calendars";

    @Override
    public JOCDefaultResponse postCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken, "", getPermissonsJocCockpit(accessToken)
                    .getCalendar().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
            List<DBItemCalendar> dbCalendars = null;
            
            if (calendarsFilter.getCalendars() != null && !calendarsFilter.getCalendars().isEmpty()) {
                calendarsFilter.setRegex(null);
                dbCalendars = dbLayer.getCalendars(new HashSet<String>(calendarsFilter.getCalendars()));
            } else {
                if (calendarsFilter.getType() != null && !calendarsFilter.getType().isEmpty()) {
                    try {
                        CalendarType.fromValue(calendarsFilter.getType().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new JobSchedulerBadRequestException(String.format("Invalid value '%1$s' in 'type' parameter", calendarsFilter.getType()));
                    }
                }
                Set<String> categories = null;
                if (calendarsFilter.getCategories() != null) {
                    categories = new HashSet<String>(calendarsFilter.getCategories());
                }
                Set<String> folders = new HashSet<String>();
                Set<String> recursiveFolders = new HashSet<String>();
                if (calendarsFilter.getFolders() != null) {
                    for (Folder folder : calendarsFilter.getFolders()) {
                        folders.add(folder.getFolder());
                        if (folder.getRecursive()) {
                            recursiveFolders.add(folder.getFolder()); 
                        }
                    }
                }
                dbCalendars = dbLayer.getCalendars(calendarsFilter.getType().toUpperCase(), categories, folders, recursiveFolders);
            }
            
            List<Calendar> calendarList = new ArrayList<Calendar>();
            if (dbCalendars != null) {
                ObjectMapper om = new ObjectMapper();
                boolean compact = calendarsFilter.getCompact() != null && calendarsFilter.getCompact();
                for (DBItemCalendar dbCalendar : dbCalendars) {
                    if (FilterAfterResponse.matchRegex(calendarsFilter.getRegex(), dbCalendar.getName())) {
                        Calendar calendar = om.readValue(dbCalendar.getConfiguration(), Calendar.class);
                        calendar.setPath(dbCalendar.getName());
                        calendar.setName(dbCalendar.getBaseName());
                        if (compact) {
                            calendar.setIncludes(null);
                            calendar.setExcludes(null);
                        }
                        calendarList.add(calendar);
                    }
                }
            }
            Calendars entity = new Calendars();
            entity.setCalendars(calendarList);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
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