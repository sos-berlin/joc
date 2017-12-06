package com.sos.joc.calendars.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsImportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarImportFilter;

@Path("calendars")
public class CalendarsImportResourceImpl extends JOCResourceImpl implements ICalendarsImportResource {

    private static final String API_CALL = "./calendars/import";
//    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarsImportResourceImpl.class);

    @Override
    // public JOCDefaultResponse importCalendars(String accessToken, InputStream uploadedInputStream, FormDataContentDisposition fileDetail,
    // String jobschedulerId) throws Exception {
    public JOCDefaultResponse importCalendars(String accessToken, CalendarImportFilter calendarImportFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, calendarImportFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getCalendar().getEdit().isCreate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
//            if (uploadedInputStream == null || fileDetail == null) {
//                throw new JocMissingRequiredParameterException("calendar import file is required");
//            }
//            LOGGER.info(fileDetail.getName());
//            LOGGER.info(fileDetail.getType());
//            LOGGER.info(fileDetail.getSize() + "");

//            Calendars calendars = null;
//            try {
//                ObjectMapper objMapper = new ObjectMapper();
//                calendars = objMapper.readValue(uploadedInputStream, Calendars.class);
//                LOGGER.info(objMapper.writeValueAsString(calendars));
//            } catch (Exception e) {
//                throw new JobSchedulerInvalidResponseDataException("calendar import file is invalid", e);
//            }
            List<Calendar> calendars = calendarImportFilter.getCalendars();
//            if (calendars != null && calendars.getCalendars() != null && !calendars.getCalendars().isEmpty()) {
            if (calendars != null && !calendars.isEmpty()) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
                for (Calendar calendar : calendars) {
                    if (calendar.getBasedOn() == null || calendar.getBasedOn().isEmpty()) { // Calendar
                        DBItemCalendar dbItemCalendar = dbLayer.getCalendar(dbItemInventoryInstance.getId(), calendar.getPath());
                        dbItemCalendar = dbLayer.saveOrUpdateCalendar(dbItemInventoryInstance.getId(), dbItemCalendar, calendar);
                    }
                }
                CalendarUsageDBLayer dbUsageLayer = new CalendarUsageDBLayer(connection);
                for (Calendar calendar : calendars) {
                    if (calendar.getBasedOn() != null && !calendar.getBasedOn().isEmpty()) { // CalendarUsage
                        // TODO
                    }
                }
            }

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
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