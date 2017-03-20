package com.sos.joc.classes.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sos.joc.model.event.EventSnapshot;

public class Events {
    
    private Map<String,EventSnapshot> events = new HashMap<String,EventSnapshot>();
    private List<EventSnapshot> notifications = new ArrayList<EventSnapshot>();
    
    public Events() {
    }
    
    public Map<String, EventSnapshot> getEvents() {
        return events;
    }
    
    public void setEvents(Map<String, EventSnapshot> events) {
        this.events = events;
    }
    
    public List<EventSnapshot> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<EventSnapshot> notifications) {
        this.notifications = notifications;
    }

    public void putAll(Events events) {
        this.events.putAll(events.getEvents());
        this.notifications.addAll(events.getNotifications());
    }
    
    public void put(EventSnapshot event) {
        this.events.put(event.getPath() + "." + event.getEventType(), event);
    }
    
    public void add(EventSnapshot event) {
        this.notifications.add(event);
    }
    
    public boolean isEmpty() {
        return this.events.isEmpty() && this.notifications.isEmpty();
    }
    
    public List<EventSnapshot> values() {
        List<EventSnapshot> eventSnapshots = this.notifications;
        eventSnapshots.addAll(0, events.values());
        return eventSnapshots;
    }
    
    public List<EventSnapshot> values(Set<String> removedObjects) {
        List<EventSnapshot> eventSnapshots = this.notifications;
        List<EventSnapshot> eventSnapshotsForUpdate = new ArrayList<EventSnapshot>();
        for (String eventKey : events.keySet()) {
            EventSnapshot e = events.get(eventKey);
            if (!removedObjects.contains(eventKey)) {
                eventSnapshotsForUpdate.add(e);  
            } else if (e.getEventType().startsWith("FileBased")) {
                eventSnapshotsForUpdate.add(e);
            }
        }
        eventSnapshots.addAll(0, eventSnapshotsForUpdate);
        return eventSnapshots;
    }
}
