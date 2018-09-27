package com.sos.joc.schedule.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.CalendarsOfAnObject;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.schedule.resource.IScheduleResourceCalendars;

@Path("schedule")
public class ScheduleResourceCalendarsImpl extends JOCResourceImpl implements IScheduleResourceCalendars {

    private static final String API_CALL = "./schedule/calendars";

    @Override
    public JOCDefaultResponse postScheduleCalendars(String accessToken, ScheduleFilter scheduleFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, scheduleFilter, accessToken, scheduleFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(scheduleFilter.getJobschedulerId(), accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("schedule", scheduleFilter.getSchedule());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            Calendars entity = CalendarsOfAnObject.get(connection, dbItemInventoryInstance.getId(), CalendarType.SCHEDULE, normalizePath(
                    scheduleFilter.getSchedule()), scheduleFilter.getCompact());

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