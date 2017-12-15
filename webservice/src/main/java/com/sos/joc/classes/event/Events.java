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
        if ("AuditLogChanged".equals(event.getEventType())) {
            this.events.putIfAbsent(event.getEventType() + "." + event.getPath() + "." + event.getObjectType().name(), event); 
        } else if (event.getEventType().startsWith("YADE")) {
            this.events.putIfAbsent(event.getEventType() + "." + event.getPath() + "." + event.getObjectType().name(), event); 
        } else if (event.getEventType().startsWith("CustomEvent")) {
            this.events.putIfAbsent(event.getEventType() + "." + event.getPath() + "." + event.getObjectType().name(), event); 
        } else if (event.getEventType().startsWith("Calendar")) {
            this.events.putIfAbsent(event.getEventType() + "." + event.getPath() + "." + event.getObjectType().name(), event); 
        } else {
            this.events.putIfAbsent(event.getPath() + "." + event.getObjectType().name(), event);
        }
    }
    
    public void add(EventSnapshot event) {
        this.notifications.add(event);
    }
    
    public boolean isEmpty() {
        return this.events.isEmpty() && this.notifications.isEmpty();
    }
    
    public List<EventSnapshot> values() {
        List<EventSnapshot> eventSnapshots = new ArrayList<EventSnapshot>();
        eventSnapshots.addAll(this.notifications);
        eventSnapshots.addAll(events.values());
        return eventSnapshots;
    }
    
    public List<EventSnapshot> values(Set<String> removedObjects) {
        List<EventSnapshot> eventSnapshots = new ArrayList<EventSnapshot>();
        for (String eventKey : events.keySet()) {
            EventSnapshot e = events.get(eventKey);
            if (!removedObjects.contains(eventKey)) {
                eventSnapshots.add(e);  
            } else if (e.getEventType().startsWith("FileBased")) {
                eventSnapshots.add(e);
            }
        }
        for (EventSnapshot e : notifications) {
            if (!removedObjects.contains(e.getPath()+"."+e.getObjectType().name())) {
                eventSnapshots.add(e);  
            } else if (e.getEventType().startsWith("FileBased")) {
                eventSnapshots.add(e);
            }
        }
        return eventSnapshots;
    }
}
