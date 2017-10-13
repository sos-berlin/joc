package com.sos.joc.classes.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Dates;

public class CalendarInRuntimes {

    public static void delete(Long calendarId, SOSHibernateSession connection, String accessToken) throws Exception {
        exec(calendarId, connection, accessToken, null, false);
    }

    public static void update(DBItemCalendar calendarDbItem, Dates newDates, SOSHibernateSession connection, String accessToken) throws Exception {
        if (calendarDbItem != null) {
            List<String> dates = new ArrayList<String>();
            if (newDates != null) {
                dates = newDates.getDates(); 
            }
            exec(calendarDbItem.getId(), connection, accessToken, dates, true);
        }
    }

    private static void exec(Long calendarId, SOSHibernateSession connection, String accessToken, List<String> dates, boolean update)
            throws Exception {
        if (calendarId != null) {
            CalendarUsageDBLayer calendarUsageDbLayer = new CalendarUsageDBLayer(connection);
            List<CalendarUsagesAndInstance> calendarUsageInstances = calendarUsageDbLayer.getInstancesFormCalendar(calendarId);

            if (calendarUsageInstances != null && !calendarUsageInstances.isEmpty()) {
                List<JobSchedulerCalendarCallable> tasks = new ArrayList<JobSchedulerCalendarCallable>();

                for (CalendarUsagesAndInstance calendarUsageInstance : calendarUsageInstances) {
                    calendarUsageInstance.setCalendarUsages(calendarUsageDbLayer.getCalendarUsagesOfAnInstance(calendarUsageInstance.getInstance()
                            .getId()));
                    if (dates != null) {
                        calendarUsageInstance.setDates(dates);
                    }
                    tasks.add(new JobSchedulerCalendarCallable(calendarUsageInstance, accessToken));
                }

                if (tasks != null && !tasks.isEmpty()) {
                    int threadPoolSize = Math.min(10, calendarUsageInstances.size());
                    ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
                    try {
                        for (Future<List<DBItemInventoryCalendarUsage>> result : executorService.invokeAll(tasks)) {
                            try {
                                calendarUsageDbLayer.updateEditFlag(result.get(), update);
                            } catch (ExecutionException e) {
                                if (e.getCause() instanceof JocException) {
                                    throw (JocException) e.getCause();
                                } else {
                                    throw (Exception) e.getCause();
                                }
                            }
                        }
                    } finally {
                        executorService.shutdown();
                    }
                }
            }
        }
    }
}
