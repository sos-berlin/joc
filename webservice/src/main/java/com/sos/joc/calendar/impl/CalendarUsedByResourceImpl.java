package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarUsedByResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarId;
import com.sos.joc.model.calendar.UsedBy;

@Path("calendar")
public class CalendarUsedByResourceImpl extends JOCResourceImpl implements ICalendarUsedByResource {

    private static final String API_CALL = "./calendar/used";

    @Override
    public JOCDefaultResponse postUsedBy(String accessToken, CalendarId calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, calendarFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getCalendar().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (calendarFilter.getId() == null && (calendarFilter.getPath() == null || calendarFilter.getPath().isEmpty())) {
                throw new JocMissingRequiredParameterException("undefined 'calendar path'");
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarUsageDBLayer dbCalendarLayer = new CalendarUsageDBLayer(connection);

            List<DBItemInventoryCalendarUsage> calendarUsages = null;
            if (calendarFilter.getId() != null) {
                calendarUsages = dbCalendarLayer.getCalendarUsages(calendarFilter.getId());
            } else {
                calendarUsages = dbCalendarLayer.getCalendarUsages(dbItemInventoryInstance.getId(), normalizePath(calendarFilter.getPath()));
            }

            List<String> orders = new ArrayList<String>();
            List<String> jobs = new ArrayList<String>();
            List<String> schedules = new ArrayList<String>();
            if (calendarUsages != null) {
                for (DBItemInventoryCalendarUsage item : calendarUsages) {
                    if (item.getObjectType() == null) {
                        continue;
                    }
                    switch (item.getObjectType().toUpperCase()) {
                    case "ORDER":
                        orders.add(item.getPath());
                        break;
                    case "JOB":
                        jobs.add(item.getPath());
                        break;
                    case "SCHEDULE":
                        schedules.add(item.getPath());
                        break;
                    }
                }
            }

            UsedBy entity = new UsedBy();
            if (!orders.isEmpty()) {
                entity.setOrders(orders);
            }
            if (!jobs.isEmpty()) {
                entity.setJobs(jobs);
            }
            if (!schedules.isEmpty()) {
                entity.setSchedules(schedules);
            }
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