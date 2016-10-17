package com.sos.joc.classes.orders;

import java.net.URI;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.model.order.OrdersSummary;

public class OrdersSnapshotCallable implements Callable<OrdersSummary> {
    private final String path;
    private final URI uri;
    
    public OrdersSnapshotCallable(URI uri) {
        this.path = "/";
        this.uri = uri;
    }
    
    public OrdersSnapshotCallable(String path, URI uri) {
        this.path = path;
        this.uri = uri;
    }
    
    @Override
    public OrdersSummary call() throws Exception {
        return getOrdersSnapshot(path, uri); 
    }
    
    public OrdersSummary getOrdersSnapshot() throws Exception {
        return getOrdersSnapshot(new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(path)));
    }
    
    public OrdersSummary getOrdersSnapshot(String path, URI uri) throws Exception {
        JsonObject json = new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(path));
        return getOrdersSnapshot(json);
    }
    
    private OrdersSummary getOrdersSnapshot(JsonObject json) {
        OrdersSummary summary = new OrdersSummary();
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
