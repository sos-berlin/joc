package com.sos.joc.classes.calendar;

import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.jobscheduler.model.event.CalendarVariables;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JocException;

public abstract class SendCalendarEventsUtil {

    private static ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void sendEvent(Collection<String> xmlCommands, DBItemInventoryInstance dbItemInventoryInstance, String accessToken)
            throws JocException {
        try {
            if (xmlCommands!= null && !xmlCommands.isEmpty()) {
                String xmlCommand = "";
                if (xmlCommands.size() > 1) {
                    xmlCommand += "<commands>";
                }
                for (String command : xmlCommands) {
                    xmlCommand += command;
                }
                if (xmlCommands.size() > 1) {
                    xmlCommand += "</commands>";
                }
                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
                jocXmlCommand.executePostWithRetry(xmlCommand, accessToken);
            }
        } catch (JobSchedulerConnectionRefusedException e) {
        } catch (JobSchedulerConnectionResetException e) {
        }
    }

    public static String addCalUsageEvent(String path, String objectType, String key) throws JsonProcessingException, JocException {
        CalendarEvent calEvt = new CalendarEvent();
        calEvt.setKey(key);
        CalendarVariables calEvtVars = new CalendarVariables();
        calEvtVars.setPath(path);
        calEvtVars.setObjectType(CalendarObjectType.fromValue(objectType));
        calEvt.setVariables(calEvtVars);
        return addEvent(calEvt);
    }

    public static String addEvent(CalendarEvent calEvt) throws JocException, JsonProcessingException {
        String xmlCommand = objectMapper.writeValueAsString(calEvt);
        return "<publish_event>" + xmlCommand + "</publish_event>";
    }

    public static void sendEvent(CalendarEvent calEvt, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) throws JocException,
            JsonProcessingException {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            jocXmlCommand.executePostWithRetry(addEvent(calEvt), accessToken);
        } catch (JobSchedulerConnectionRefusedException e) {
        } catch (JobSchedulerConnectionResetException e) {
        }
    }
}
