package com.sos.joc.classes.orders;

import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.exceptions.JocException;

public class OrdersSnapshotCallable implements Callable<OrdersSnapshotEvent> {
    private final String path;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;
    
    public OrdersSnapshotCallable(JOCJsonCommand jocJsonCommand, String accessToken) {
        this.path = "/";
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }
    
    public OrdersSnapshotCallable(String path, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.path = path;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }
    
    @Override
    public OrdersSnapshotEvent call() throws JocException  {
        return getOrdersSnapshot(path, jocJsonCommand, accessToken); 
    }
    
    public OrdersSnapshotEvent getOrdersSnapshot() throws JocException {
        return getOrdersSnapshot(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(path), accessToken));
    }
    
    public OrdersSnapshotEvent getOrdersSnapshot(String path, JOCJsonCommand jocJsonCommand, String accessToken) throws JocException {
        JsonObject json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(path), accessToken);
        return getOrdersSnapshot(json);
    }
    
    private OrdersSnapshotEvent getOrdersSnapshot(JsonObject json) {
        OrdersSnapshotEvent summary = new OrdersSnapshotEvent();
        JsonNumber eventId = json.getJsonNumber("eventId");
        if (eventId != null) {
            summary.setEventId(eventId.longValue()); 
        }
        summary.setBlacklist(json.getInt("blacklisted", 0));
        summary.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
        summary.setRunning(json.getInt("inTaskProcess", 0) + json.getInt("occupiedByClusterMember", 0));
        summary.setSetback(json.getInt("setback", 0));
        summary.setSuspended(json.getInt("suspended", 0));
        summary.setWaitingForResource(json.getInt("waitingForResource", 0) + json.getInt("due", 0) + json.getInt("inTask", 0) - json.getInt("inTaskProcess", 0));
        return summary;
    }
    
    private String getServiceBody(String path) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", path);
        return builder.build().toString();
    }
}
