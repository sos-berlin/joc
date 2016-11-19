package com.sos.joc.classes.orders;

import java.net.URI;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;

public class OrdersSnapshotCallable implements Callable<OrdersSnapshotEvent> {
    private final String path;
    private final URI uri;
    private final String accessToken;
    
    public OrdersSnapshotCallable(URI uri, String accessToken) {
        this.path = "/";
        this.uri = uri;
        this.accessToken = accessToken;
    }
    
    public OrdersSnapshotCallable(String path, URI uri, String accessToken) {
        this.path = path;
        this.uri = uri;
        this.accessToken = accessToken;
    }
    
    @Override
    public OrdersSnapshotEvent call() throws Exception {
        return getOrdersSnapshot(path, uri, accessToken); 
    }
    
    public OrdersSnapshotEvent getOrdersSnapshot() throws Exception {
        return getOrdersSnapshot(new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(path), accessToken));
    }
    
    public OrdersSnapshotEvent getOrdersSnapshot(String path, URI uri, String accessToken) throws Exception {
        JsonObject json = new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(path), accessToken);
        return getOrdersSnapshot(json);
    }
    
    private OrdersSnapshotEvent getOrdersSnapshot(JsonObject json) {
        OrdersSnapshotEvent summary = new OrdersSnapshotEvent();
        summary.setEventId(json.getJsonNumber("eventId").longValue());
        summary.setBlacklist(json.getInt("blacklisted", 0));
        summary.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
        summary.setRunning(json.getInt("inProcess", 0));
        summary.setSetback(json.getInt("setback", 0));
        summary.setSuspended(json.getInt("suspended", 0));
        summary.setWaitingForResource(json.getInt("total", 0) - summary.getBlacklist() - summary.getPending() - summary.getRunning() - summary.getSetback() - summary.getSuspended());
        return summary;
    }
    
    private String getServiceBody(String path) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", path);
        return builder.build().toString();
    }
}
