package com.sos.joc.job.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobCalendarsResource;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobCalendarsResourceImpl extends JOCResourceImpl implements IJobCalendarsResource {

    private static final String API_CALL = "./job/calendars";

    @Override
    public JOCDefaultResponse postJobCalendars(String accessToken, JobFilter jobFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken, jobFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("job", jobFilter.getJob());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer dbCalendarLayer = new CalendarsDBLayer(connection);

            List<DBItemCalendar> dbCalendars = dbCalendarLayer.getCalendarsOfAnObject(dbItemInventoryInstance.getId(), "JOB", normalizePath(jobFilter
                    .getJob()));

            List<Calendar> calendarList = new ArrayList<Calendar>();
            if (dbCalendars != null) {
                ObjectMapper om = new ObjectMapper();
                for (DBItemCalendar dbCalendar : dbCalendars) {
                    Calendar calendar = om.readValue(dbCalendar.getConfiguration(), Calendar.class);
                    calendar.setId(dbCalendar.getId());
                    calendar.setPath(dbCalendar.getName());
                    calendar.setName(dbCalendar.getBaseName());
                    // calendar.setIncludes(null);
                    // calendar.setExcludes(null);
                    calendarList.add(calendar);
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
