package com.sos.joc.classes.calendar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.w3c.dom.Element;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;

public class JobSchedulerCalendarCallable implements Callable<CalendarUsagesAndInstance> {

    private final CalendarUsagesAndInstance calendarUsageAndInstance;
    private final String accessToken;

    public JobSchedulerCalendarCallable(CalendarUsagesAndInstance calendarUsageAndInstance, String accessToken) {
        this.calendarUsageAndInstance = calendarUsageAndInstance;
        this.accessToken = accessToken;
    }

    @Override
    public CalendarUsagesAndInstance call() {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(calendarUsageAndInstance.getInstance());
        String xmlCommand = null;
        List<String> dates = calendarUsageAndInstance.getDates();
        Set<DBItemInventoryCalendarUsage> schedules = new HashSet<DBItemInventoryCalendarUsage>();
        try {
            for (DBItemInventoryCalendarUsage item : calendarUsageAndInstance.getCalendarUsages()) {
                try {
                    String objectType = item.getObjectType();
                    Element jobSchedulerObjectElement = null;
                    if (objectType == null) {
                        throw new DBInvalidDataException("Object type is undefined in CALENDAR_USAGE");
                    } else {
                        switch (item.getObjectType().toUpperCase()) {
                        case "ORDER":
                            String[] orderPath = item.getPath().split(",", 2);
                            xmlCommand = jocXmlCommand.getShowOrderCommand(orderPath[0], orderPath[1], "source");
                            jocXmlCommand.executePost(xmlCommand, accessToken);
                            jobSchedulerObjectElement = jocXmlCommand.updateCalendarInRuntimes(dates, item.getObjectType(), item.getPath(), item
                                    .getCalendarId());
                            if (jobSchedulerObjectElement != null) {
                                xmlCommand = jocXmlCommand.getModifyHotFolderCommand(item.getPath(), jobSchedulerObjectElement);
                                jocXmlCommand.executePost(xmlCommand, accessToken);
                            }
                            break;
                        case "JOB":
                            xmlCommand = jocXmlCommand.getShowJobCommand(item.getPath(), "source");
                            jocXmlCommand.executePost(xmlCommand, accessToken);
                            jobSchedulerObjectElement = jocXmlCommand.updateCalendarInRuntimes(dates, item.getObjectType(), item.getPath(), item
                                    .getCalendarId());
                            if (jobSchedulerObjectElement != null) {
                                xmlCommand = jocXmlCommand.getModifyHotFolderCommand(item.getPath(), jobSchedulerObjectElement);
                                jocXmlCommand.executePost(xmlCommand, accessToken);
                            }
                            break;
                        case "SCHEDULE":
                            schedules.add(item);
                            break;
                        default:
                            break;
                        }
                    }
                    item.setEdited(false);
                } catch (JobSchedulerObjectNotExistException e) {
                    item.setEdited(null);
                } catch (JobSchedulerNoResponseException e) {
                    throw e;
                } catch (JobSchedulerConnectionRefusedException e) {
                    throw e;
                } catch (JobSchedulerConnectionResetException e) {
                    throw e;
                } catch (Exception e) {
                    item.setEdited(true);
                    calendarUsageAndInstance.putException(item, e);
                }
            }

            if (!schedules.isEmpty()) {
                xmlCommand = jocXmlCommand.getShowStateCommand("folder schedule", "folders source", null);
                jocXmlCommand.executePost(xmlCommand, accessToken);

                for (DBItemInventoryCalendarUsage item : schedules) {
                    try {
                        Element jobSchedulerObjectElement = jocXmlCommand.updateCalendarInRuntimes(dates, item.getObjectType(), item.getPath(), item
                                .getCalendarId());
                        if (jobSchedulerObjectElement != null) {
                            xmlCommand = jocXmlCommand.getModifyHotFolderCommand(item.getPath(), jobSchedulerObjectElement);
                            jocXmlCommand.executePost(xmlCommand, accessToken);
                        }
                        item.setEdited(false);
                    } catch (JobSchedulerObjectNotExistException e) {
                        item.setEdited(null);
                    } catch (JobSchedulerNoResponseException e) {
                        throw e;
                    } catch (JobSchedulerConnectionRefusedException e) {
                        throw e;
                    } catch (JobSchedulerConnectionResetException e) {
                        throw e;
                    } catch (Exception e) {
                        item.setEdited(true);
                        calendarUsageAndInstance.putException(item, e);
                    }
                }
            }

        } catch (Exception e) {
            calendarUsageAndInstance.setAllEdited(e);
        }
        return calendarUsageAndInstance;
    }

}
