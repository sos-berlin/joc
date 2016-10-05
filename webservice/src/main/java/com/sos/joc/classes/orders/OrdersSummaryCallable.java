package com.sos.joc.classes.orders;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.jobchain.JobChainV;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.jobChain.OrdersSummary;
import com.sos.joc.model.order.SnapshotSchema;

public class OrdersSummaryCallable implements Callable<Map<String,JobChainV>> {
    private final JobChainV jobChain;
    private final URI uri;
    
    public OrdersSummaryCallable(URI uri) {
        JobChainV jobC = new JobChainV();
        jobC.setPath("/");
        this.jobChain = jobC;
        this.uri = uri;
    }
    
    public OrdersSummaryCallable(JobChainV jobChain, URI uri) {
        this.jobChain = jobChain;
        this.uri = uri;
    }
    
    @Override
    public Map<String,JobChainV> call() throws Exception {
        return getOrdersSummary(jobChain, uri);
    }
    
    public OrdersSummary getOrdersSummary() throws Exception {
        return getOrdersSummary(new JOCJsonCommand().getJsonObjectFromResponse(uri, getServiceBody(jobChain.getPath())));
    }
    
    public SnapshotSchema getOrdersSnapshot(SnapshotSchema snapshot) throws Exception {
        JsonObject json = new JOCJsonCommand().getJsonObjectFromResponse(uri, getServiceBody(jobChain.getPath()));
        snapshot.setOrders(getOrdersSnapshot(json));
        snapshot.setSurveyDate(JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue()));
        return snapshot;
    }
    
    private Map<String,JobChainV> getOrdersSummary(JobChainV jobChain, URI uri) throws Exception {
        Map<String,JobChainV> summaryMap = new HashMap<String,JobChainV>();
        jobChain.setOrdersSummary(getOrdersSummary(new JOCJsonCommand().getJsonObjectFromResponse(uri, getServiceBody(jobChain.getPath()))));
        summaryMap.put(jobChain.getPath(), jobChain);
        return summaryMap;
    }
    
    private OrdersSummary getOrdersSummary(JsonObject json) throws JobSchedulerInvalidResponseDataException {
        OrdersSummary summary = new OrdersSummary();
        summary.setBlacklist(json.getInt("blacklisted", 0));
        summary.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
        summary.setRunning(json.getInt("inProcess", 0));
        summary.setSetback(json.getInt("setback", 0));
        summary.setSuspended(json.getInt("suspended", 0));
        summary.setWaitingForResource(json.getInt("total", 0) - summary.getBlacklist() - summary.getPending() - summary.getRunning() - summary.getSetback() - summary.getSuspended());
        return summary;
    }
    
    private com.sos.joc.model.order.Orders getOrdersSnapshot(JsonObject json) throws JobSchedulerInvalidResponseDataException {
        com.sos.joc.model.order.Orders summary = new com.sos.joc.model.order.Orders();
        summary.setBlacklist(json.getInt("blacklisted", 0));
        summary.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
        summary.setRunning(json.getInt("inProcess", 0));
        summary.setSetback(json.getInt("setback", 0));
        summary.setSuspended(json.getInt("suspended", 0));
        summary.setWaitingForResource(json.getInt("total", 0) - summary.getBlacklist() - summary.getPending() - summary.getRunning() - summary.getSetback() - summary.getSuspended());
        return summary;
    }
    
    private String getServiceBody(String jobChain) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", jobChain);
        return builder.build().toString();
    }
}
