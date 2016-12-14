package com.sos.joc.classes.orders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.jobchains.JobChainVolatile;
import com.sos.joc.model.order.OrdersSummary;

public class OrdersSummaryCallable implements Callable<Map<String, JobChainVolatile>> {

    private final JobChainVolatile jobChain;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;

    public OrdersSummaryCallable(JOCJsonCommand jocJsonCommand, String accessToken) {
        JobChainVolatile jobC = new JobChainVolatile();
        jobC.setPath("/");
        this.jobChain = jobC;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    public OrdersSummaryCallable(JobChainVolatile jobChain, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.jobChain = jobChain;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, JobChainVolatile> call() throws Exception {
        return getOrdersSummary(jobChain, jocJsonCommand, accessToken);
    }

    public OrdersSummary getOrdersSummary() throws Exception {
        return getOrdersSummary(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobChain.getPath()), accessToken));
    }

    private Map<String, JobChainVolatile> getOrdersSummary(JobChainVolatile jobChain, JOCJsonCommand jocJsonCommand, String accessToken) throws Exception {
        Map<String, JobChainVolatile> summaryMap = new HashMap<String, JobChainVolatile>();
        jobChain.setOrdersSummary(getOrdersSummary(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobChain.getPath()), accessToken)));
        summaryMap.put(jobChain.getPath(), jobChain);
        return summaryMap;
    }

    private OrdersSummary getOrdersSummary(JsonObject json) {
        OrdersSummary summary = new OrdersSummary();
        summary.setBlacklist(json.getInt("blacklisted", 0));
        summary.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
        summary.setRunning(json.getInt("inTaskProcess", 0) + json.getInt("occupiedByClusterMember", 0));
        summary.setSetback(json.getInt("setback", 0));
        summary.setSuspended(json.getInt("suspended", 0));
        summary.setWaitingForResource(json.getInt("waitingForResource", 0) + json.getInt("due", 0) + json.getInt("inTask", 0) - json.getInt("inTaskProcess", 0));
        return summary;
    }

    private String getServiceBody(String jobChain) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", jobChain);
        return builder.build().toString();
    }
}
