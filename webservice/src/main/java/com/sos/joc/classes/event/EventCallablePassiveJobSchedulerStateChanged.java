package com.sos.joc.classes.event;

import java.util.List;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;

public class EventCallablePassiveJobSchedulerStateChanged extends EventCallable implements Callable<JobSchedulerEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCallablePassiveJobSchedulerStateChanged.class);

    public EventCallablePassiveJobSchedulerStateChanged(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken, Session session, Long instanceId) {
        super(command, jobSchedulerEvent, accessToken, session, instanceId);
    }

    @Override
    public JobSchedulerEvent call() throws Exception {
        return super.call();
    }

    @Override
    protected List<EventSnapshot> getEventSnapshots(String eventId) throws JocException {
        return getEventSnapshotsMap(eventId).values();
    }
    
    protected Events nonEmptyEvent(Long newEventId, JsonObject json) {
        Events eventSnapshots = new Events();
        jobSchedulerEvent.setEventId(newEventId.toString());
        for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
            EventSnapshot eventSnapshot = new EventSnapshot();
            
            String eventType = event.getString("TYPE", null);
            eventSnapshot.setEventType(eventType);
            
            if (eventType.startsWith("Scheduler")) {
                eventSnapshot.setEventType("SchedulerStateChanged");
                eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                eventSnapshot.setPath(command.getSchemeAndAuthority());
            } else {
                continue;
            }
            if (eventSnapshot != null) {
                eventSnapshots.put(eventSnapshot);
            }
        }
        try {
            json.clear();
        } catch (Exception e) {
        } finally {
            json = null;
        }
        return eventSnapshots;
    }
    
    protected Events getEventSnapshotsMap(String eventId) throws JocException {
        Events eventSnapshots = new Events();
        checkTimeout();
        try {
            JsonObject json = getJsonObject(eventId);
            Long newEventId = json.getJsonNumber("eventId").longValue();
            String type = json.getString("TYPE", "Empty");
            switch (type) {
            case "Empty":
                eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString()));
                break;
            case "NonEmpty":
                eventSnapshots.putAll(nonEmptyEvent(newEventId, json));
                if (eventSnapshots.isEmpty()) {
                    eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString())); 
                } else {
                    for (int i=0; i < 6; i++) {
                        if (!(Boolean) session.getAttribute(Globals.SESSION_KEY_FOR_SEND_EVENTS_IMMEDIATLY)) {
                            try { //collect further events after 2sec to minimize the number of responses 
                                int delay = Math.min(250, getSessionTimeout());
                                if (delay > 0) {
                                    Thread.sleep(delay);
                                }
                            } catch (InterruptedException e1) {
                            } 
                        }
                    }
                    eventSnapshots.putAll(getEventSnapshotsMapFromNextResponse(newEventId.toString()));
                    try { //a small delay because events comes earlier then the JobScheduler has update its objects in some requests 
                        int delay = Math.min(500, getSessionTimeout());
                        if (delay > 0) {
                            Thread.sleep(delay);
                        }
                    } catch (InterruptedException e1) {
                    }
                }
                break;
            case "Torn":
                eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString()));
                break;
            }
        } catch (JobSchedulerNoResponseException | JobSchedulerConnectionRefusedException e) {
            // if current Jobscheduler down then retry after 15sec
            try {
                int delay = Math.min(15000, new Long(getSessionTimeout()).intValue());
                LOGGER.debug(command.getSchemeAndAuthority() + ": connection refused; retry after " + delay + "ms");
                while (delay > 0) {
                    Thread.sleep(1000);
                    delay = delay - 1000;
                    if (command.isForcedClosingHttpClient()) {
                        throw new ForcedClosingHttpClientException(command.getSchemeAndAuthority());
                    }
                }
                if (command.isForcedClosingHttpClient()) {
                    throw new ForcedClosingHttpClientException(command.getSchemeAndAuthority());
                }
            } catch (InterruptedException e1) {
            }
            eventSnapshots.putAll(getEventSnapshotsMap(eventId));
        } catch (JobSchedulerConnectionResetException e) {
            EventSnapshot eventSnapshot = new EventSnapshot();
            eventSnapshot.setEventType("SchedulerStateChanged");
            eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
            eventSnapshot.setPath(command.getSchemeAndAuthority());
            eventSnapshots.put(eventSnapshot);
        }
        return eventSnapshots;
    }

    private Events getEventSnapshotsMapFromNextResponse(String eventId) throws JocException {
        Events eventSnapshots = new Events();
        JsonObject json = getJsonObject(eventId, 0);
        Long newEventId = json.getJsonNumber("eventId").longValue();
        String type = json.getString("TYPE", "Empty");
        switch (type) {
        case "Empty":
            break;
        case "NonEmpty":
            eventSnapshots.putAll(nonEmptyEvent(newEventId, json));
            break;
        case "Torn":
            break;
        }
        return eventSnapshots;
    }
}
