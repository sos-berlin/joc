package com.sos.joc.classes.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Dates;

public class CalendarInRuntimes {

    public static void delete(Long calendarId, SOSHibernateSession connection, String accessToken) throws Exception {
        exec(calendarId, connection, accessToken, null, false);
    }

    public static void update(DBItemCalendar calendarDbItem, Calendar calendar, SOSHibernateSession connection, String accessToken) throws Exception {
        if (calendarDbItem != null) {
            FrequencyResolver fr = new FrequencyResolver();
            Dates newDates;
            Dates oldDates;
            try {
                newDates = fr.resolveFromToday(calendar);
                oldDates = fr.resolveFromToday(new ObjectMapper().readValue(calendarDbItem.getConfiguration(), Calendar.class));
            } catch (SOSMissingDataException e) {
                throw new JocMissingRequiredParameterException(e);
            } catch (SOSInvalidDataException e) {
                throw new JobSchedulerInvalidResponseDataException(e);
            }
            if (newDates.getDates().equals(oldDates.getDates())) {
                // nothing to do
            } else {
                exec(calendarDbItem.getId(), connection, accessToken, newDates.getDates(), true);
            }
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
                    int threadPoolSize = Math.max(10, calendarUsageInstances.size());
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
