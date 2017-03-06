package com.sos.joc.classes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.JsonObject;

import org.apache.shiro.session.Session;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;

public class EventCallableOfCurrentJobScheduler extends EventCallable implements Callable<JobSchedulerEvent> {

    private final String accessToken;
    private final JobSchedulerEvent jobSchedulerEvent;
    private final JOCJsonCommand command;
    private final SOSShiroCurrentUser shiroUser;
    
    public EventCallableOfCurrentJobScheduler(JOCJsonCommand command, JobSchedulerEvent jobSchedulerEvent, String accessToken, Session session,
            Integer eventTimeout, SOSShiroCurrentUser shiroUser) {
        super(command, jobSchedulerEvent, accessToken, session, eventTimeout);
        this.accessToken = accessToken;
        this.command = command;
        this.jobSchedulerEvent = jobSchedulerEvent;
        this.shiroUser = shiroUser;
    }

    @Override
    public JobSchedulerEvent call() throws JocException {
        return super.call();
    }

    private void removeSavedInventoryInstance() {
        if (Globals.urlFromJobSchedulerId.containsKey(jobSchedulerEvent.getJobschedulerId())) {
            DBItemInventoryInstance instance = Globals.urlFromJobSchedulerId.get(jobSchedulerEvent.getJobschedulerId());
            if (instance != null && !"standalone".equals(instance.getClusterType())) {
                Globals.urlFromJobSchedulerId.remove(jobSchedulerEvent.getJobschedulerId());
                try {
                    shiroUser.removeSchedulerInstanceDBItem(jobSchedulerEvent.getJobschedulerId());
                } catch (Exception e) {
                }
            }
        }
    }
    
    private Map<String,EventSnapshot> createJobChainEvent(EventSnapshot orderEventSnapshot) {
        Map<String,EventSnapshot> jobChainEventSnapshots = new HashMap<String,EventSnapshot>();
        String[] pathParts = orderEventSnapshot.getPath().split(",", 2);
        String jobChain = pathParts[0];
        EventSnapshot jobChainEventSnapshot = new EventSnapshot();
//        jobChainEventSnapshot.setEventId(orderEventSnapshot.getEventId());
        jobChainEventSnapshot.setEventType("JobChainStateChanged");
        jobChainEventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
        jobChainEventSnapshot.setPath(jobChain);
        jobChainEventSnapshots.put(jobChain, jobChainEventSnapshot);
        return jobChainEventSnapshots;
    }

    @Override
    protected List<EventSnapshot> getEventSnapshots(String eventId) throws JocException {
        return new ArrayList<EventSnapshot>(getEventSnapshotsMap(eventId).values());
    }
    
    private Map<String,EventSnapshot> nonEmptyEvent(Long newEventId, JsonObject json) {
        Map<String,EventSnapshot> eventSnapshots = new HashMap<String,EventSnapshot>();
        jobSchedulerEvent.setEventId(newEventId.toString());
        for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
            EventSnapshot eventSnapshot = new EventSnapshot();
            String eventType = event.getString("TYPE", null);
            eventSnapshot.setEventType(eventType);
            if (eventType.startsWith("File")) {
                continue;
            }
            if (eventType.startsWith("Task")) {
                eventSnapshot.setEventType("JobStateChanged");
                JsonObject eventKeyO = event.getJsonObject("key");
                String jobPath = eventKeyO.getString("jobPath", null);
                if (jobPath == null || "/scheduler_file_order_sink".equals(jobPath)) {
                    continue;
                }
                eventSnapshot.setPath(jobPath);
                eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
            } else {
                String eventKey = event.getString("key", null);
                if (eventType.equals("VariablesCustomEvent")) {
                    JsonObject variables = event.getJsonObject("variables");
                    boolean isInventoryEvent = false;
                    if (variables != null) {
                        isInventoryEvent = variables.getString("InventoryEventUpdateFinished", null) != null; 
                    }
                    if (isInventoryEvent) {
                        eventSnapshot.setEventType("FileBasedActivated");
                        String[] eventKeyParts = eventKey.split(":", 2);
                        eventSnapshot.setPath(eventKeyParts[1]);
                        eventSnapshot.setObjectType(JobSchedulerObjectType.fromValue(eventKeyParts[0].toUpperCase().replaceAll("_", ""))); 
                    }
                } else {
                    eventSnapshot.setPath(eventKey);
                    if (eventType.startsWith("JobState")) {
                        eventSnapshot.setEventType("JobStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOB);
                    } else if (eventType.startsWith("JobChain")) {
                        eventSnapshot.setEventType("JobChainStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOBCHAIN);
                    } else if (eventType.startsWith("Order")) {
                        eventSnapshot.setEventType("OrderStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.ORDER);
                        eventSnapshots.putAll(createJobChainEvent(eventSnapshot));
                    } else if (eventType.startsWith("Scheduler")) {
                        eventSnapshot.setEventType("SchedulerStateChanged");
                        eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
                        String state = event.getString("state", null);
                        eventSnapshot.setPath(command.getSchemeAndAuthority());
                        if (state!= null && (state.contains("stop") || state.contains("waiting"))) {
                            removeSavedInventoryInstance();
                        }
                    }
                }
            }
            eventSnapshots.put(eventSnapshot.getPath(),eventSnapshot);
        }
        try {
            json.clear();
        } catch (Exception e) {
        } finally {
            json = null;
        }
        return eventSnapshots;
    }
    
    private Map<String,EventSnapshot> getEventSnapshotsMap(String eventId) throws JocException {
        Map<String,EventSnapshot> eventSnapshots = new HashMap<String,EventSnapshot>();
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
                    try { //collect further events after 2sec to minimize the number of responses 
                        int delay = Math.min(2000, new Long(getSessionTimeout()).intValue());
                        Thread.sleep(delay);
                    } catch (InterruptedException e1) {
                    }
                    eventSnapshots.putAll(getEventSnapshotsMapFromNextResponse(newEventId.toString()));
                }
                break;
            case "Torn":
                eventSnapshots.putAll(getEventSnapshotsMap(newEventId.toString()));
                break;
            }
        } catch (JobSchedulerNoResponseException | JobSchedulerConnectionRefusedException e) {
            // if current Jobscheduler down then retry after 15sec
            removeSavedInventoryInstance();
            try {
                int delay = Math.min(15000, new Long(getSessionTimeout()).intValue());
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
            }
            SOSHibernateConnection connection = null;
            DBItemInventoryInstance inventoryInstance = null;
            InventoryInstancesDBLayer instanceLayer = null;
            try {
                connection = Globals.createSosHibernateStatelessConnection("eventCallable"+jobSchedulerEvent.getJobschedulerId());
                Globals.beginTransaction(connection);
                instanceLayer = new InventoryInstancesDBLayer(connection);
                inventoryInstance = instanceLayer.getInventoryInstanceBySchedulerId(jobSchedulerEvent.getJobschedulerId(), accessToken);
            } catch (Exception e1) {
                throw e1;
            } finally {
                Globals.disconnect(connection);
            }
            if (inventoryInstance != null) {
                Globals.urlFromJobSchedulerId.put(jobSchedulerEvent.getJobschedulerId(), inventoryInstance);
                command.setUriBuilderForEvents(inventoryInstance.getUrl());
                command.setBasicAuthorization(inventoryInstance.getAuth());
                command.createHttpClient();
                eventSnapshots.putAll(getEventSnapshotsMap(eventId));   
            } else {
                throw e;
            }
        }
        return eventSnapshots;
    }

    private Map<String, EventSnapshot> getEventSnapshotsMapFromNextResponse(String eventId) throws JocException {
        Map<String, EventSnapshot> eventSnapshots = new HashMap<String, EventSnapshot>();
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
