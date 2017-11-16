package com.sos.joc.classes.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Dates;

public class CalendarInRuntimes {

    public static Map<String, Exception> delete(DBItemInventoryInstance dbItemInventoryInstance, Long calendarId, SOSHibernateSession connection,
            String accessToken) throws Exception {
        return exec(dbItemInventoryInstance, calendarId, connection, accessToken, null, null, false);
    }

    public static Map<String, Exception> update(DBItemInventoryInstance dbItemInventoryInstance, DBItemCalendar calendarDbItem, Calendar baseCalendar,
            Dates newDates, SOSHibernateSession connection, String accessToken) throws Exception {
        if (calendarDbItem != null) {
            List<String> dates = new ArrayList<String>();
            if (newDates != null) {
                dates = newDates.getDates();
            }
            return exec(dbItemInventoryInstance, calendarDbItem.getId(), connection, accessToken, baseCalendar, dates, true);
        }
        return new HashMap<String, Exception>();
    }

    private static Map<String, Exception> exec(DBItemInventoryInstance dbItemInventoryInstance, Long calendarId, SOSHibernateSession connection,
            String accessToken, Calendar baseCalendar, List<String> dates, boolean update) throws Exception {
        Map<String, Exception> exceptions = new HashMap<String, Exception>();
        if (calendarId != null) {
            CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);

            CalendarUsagesAndInstance calendarUsageInstance = new CalendarUsagesAndInstance(dbItemInventoryInstance, false);
            calendarUsageInstance.setCalendarUsages(calendarUsageDbLayer.getCalendarUsagesOfAnInstance(dbItemInventoryInstance.getId(), calendarId));
            calendarUsageInstance.setBaseCalendar(baseCalendar);
            if (dates != null) {
                calendarUsageInstance.setDates(dates);
            }
            JobSchedulerCalendarCallable callable = new JobSchedulerCalendarCallable(calendarUsageInstance, accessToken);
            CalendarUsagesAndInstance c = callable.call();
            calendarUsageDbLayer.updateEditFlag(c.getCalendarUsages(), update);
            exceptions.putAll(c.getExceptions());

            // List<CalendarUsagesAndInstance> calendarUsageInstances = calendarUsageDbLayer.getInstancesFromCalendar(calendarId);
            //
            // if (calendarUsageInstances != null && !calendarUsageInstances.isEmpty()) {
            // List<JobSchedulerCalendarCallable> tasks = new ArrayList<JobSchedulerCalendarCallable>();
            //
            // for (CalendarUsagesAndInstance calendarUsageInstance : calendarUsageInstances) {
            // calendarUsageInstance.setCalendarUsages(calendarUsageDbLayer.getCalendarUsagesOfAnInstance(calendarUsageInstance.getInstance()
            // .getId(), calendarId));
            // if (dates != null) {
            // calendarUsageInstance.setDates(dates);
            // }
            // tasks.add(new JobSchedulerCalendarCallable(calendarUsageInstance, accessToken));
            // }
            //
            // if (tasks != null && !tasks.isEmpty()) {
            // int threadPoolSize = Math.min(10, calendarUsageInstances.size());
            // ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            // try {
            // for (Future<CalendarUsagesAndInstance> result : executorService.invokeAll(tasks)) {
            // try {
            // CalendarUsagesAndInstance c = result.get();
            // calendarUsageDbLayer.updateEditFlag(c.getCalendarUsages(), update);
            // exceptions.putAll(c.getExceptions());
            // } catch (ExecutionException e) {
            // if (e.getCause() instanceof JocException) {
            // throw (JocException) e.getCause();
            // } else {
            // throw (Exception) e.getCause();
            // }
            // }
            // }
            // } finally {
            // executorService.shutdown();
            // }
            // }
            // }
        }
        return exceptions;
    }
}
