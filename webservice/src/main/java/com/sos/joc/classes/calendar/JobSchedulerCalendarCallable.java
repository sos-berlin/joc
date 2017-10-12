package com.sos.joc.classes.calendar;

import java.util.List;
import java.util.concurrent.Callable;

import org.w3c.dom.Element;

import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.db.calendars.CalendarUsagesAndInstance;
import com.sos.joc.exceptions.JocException;

public class JobSchedulerCalendarCallable implements Callable<List<DBItemInventoryCalendarUsage>> {

    private final CalendarUsagesAndInstance calendarUsageAndInstance;
    private final String accessToken;

    public JobSchedulerCalendarCallable(CalendarUsagesAndInstance calendarUsageAndInstance, String accessToken) {
        this.calendarUsageAndInstance = calendarUsageAndInstance;
        this.accessToken = accessToken;
    }

    @Override
    public List<DBItemInventoryCalendarUsage> call() {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(calendarUsageAndInstance.getInstance());
        try {
            String xmlCommand = getJobSchedulerCommand(jocXmlCommand);
            if (!xmlCommand.isEmpty()) {
                jocXmlCommand.executePostWithThrowBadRequest(xmlCommand, accessToken);
                xmlCommand = modifyJobSchedulerObjects(jocXmlCommand);
                if (!xmlCommand.isEmpty()) {
                    jocXmlCommand.executePostWithThrowBadRequest(xmlCommand, accessToken);
                }
            }
        } catch (JocException e) {
            calendarUsageAndInstance.setAllEdited();
        }
        return calendarUsageAndInstance.getCalendarUsages();
    }

    private String getJobSchedulerCommand(JOCXmlCommand jocXmlCommand) {
        String commandForSchedules = "";
        StringBuilder forJobsAndOrders = new StringBuilder();
        boolean scheduleFound = false;
        for (DBItemInventoryCalendarUsage item : calendarUsageAndInstance.getCalendarUsages()) {
            String objectType = item.getObjectType();
            if (objectType == null) {
                // TODO remove in DB but not here
            } else {
                switch (item.getObjectType().toUpperCase()) {
                case "ORDER":
                    String[] orderPath = item.getPath().split(",", 2);
                    forJobsAndOrders.append(jocXmlCommand.getShowOrderCommand(orderPath[0], orderPath[1], "source"));
                    break;
                case "JOB":
                    forJobsAndOrders.append(jocXmlCommand.getShowJobCommand(item.getPath(), "source"));
                    break;
                case "SCHEDULE":
                    if (!scheduleFound) {
                        commandForSchedules = jocXmlCommand.getShowStateCommand("folder schedule", "folders source", null);
                    }
                    break;
                default:
                    break;
                }
            }
        }

        String commands = commandForSchedules + forJobsAndOrders.toString();
        if (!commands.isEmpty()) {
            commands = "<commands>" + commands + "</commands>";
        }
        return commands;
    }

    private String modifyJobSchedulerObjects(JOCXmlCommand jocXmlCommand) {
        StringBuilder commandForHotFolder = new StringBuilder();
        for (DBItemInventoryCalendarUsage item : calendarUsageAndInstance.getCalendarUsages()) {
            try {
                String objectType = item.getObjectType();
                if (objectType == null) {
                    // TODO remove in DB but not here
                } else {
                    List<String> dates = calendarUsageAndInstance.getDates();
                    Element jobSchedulerObjectElement = jocXmlCommand.updateCalendarInRuntimes(dates, item.getObjectType(), item.getPath(), item
                            .getCalendarId());
                    if (jobSchedulerObjectElement != null) {
                        commandForHotFolder.append(jocXmlCommand.getModifyHotFolderCommand(item.getPath(), jobSchedulerObjectElement));
                    }
                }
                item.setEdited(false);
            } catch (Exception e) {
                item.setEdited(true);
            }
        }
        String commands = commandForHotFolder.toString();
        if (!commands.isEmpty()) {
            commands = "<commands>" + commands + "</commands>";
        }
        return commands;
    }

}
