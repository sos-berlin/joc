package com.sos.joc.classes.orders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.jobchains.JobChainVolatile;
import com.sos.joc.classes.jobchains.JobChainVolatileJson;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrdersSummary;

public class OrdersSummaryCallable implements Callable<Map<String, JobChainVolatile>> {

    private final JobChainVolatile jobChain;
    private final JobChainVolatileJson jobChainJson;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;

    public OrdersSummaryCallable(JOCJsonCommand jocJsonCommand, String accessToken) {
        JobChainVolatile jobC = new JobChainVolatile();
        jobC.setPath("/");
        this.jobChain = jobC;
        this.jobChainJson = null;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    public OrdersSummaryCallable(JobChainVolatile jobChain, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.jobChain = jobChain;
        this.jobChainJson = null;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    public OrdersSummaryCallable(JobChainVolatileJson jobChain, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.jobChain = null;
        this.jobChainJson = jobChain;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, JobChainVolatile> call() throws JocException {
        return callOrdersSummary();
    }

    public OrdersSummary getOrdersSummary() throws JocException {
        try {
            if (jobChain != null) {
                return getOrdersSummary(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobChain.getPath()), accessToken));
            } else if (jobChainJson != null) {
                return getOrdersSummary(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobChainJson.getPath()), accessToken));
            } else {
                return getOrdersSummary(null);
            }
        } catch (JobSchedulerBadRequestException e) {
            //LOGGER.warn("", e);
            return getOrdersSummary(null);
        }
    }

    private Map<String, JobChainVolatile> callOrdersSummary() throws JocException {
        Map<String, JobChainVolatile> summaryMap = new HashMap<String, JobChainVolatile>();
        try {
            jobChain.setOrdersSummary(getOrdersSummary(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobChain.getPath()), accessToken)));
        } catch (JobSchedulerBadRequestException e) {
            //LOGGER.warn("", e);
            jobChain.setOrdersSummary(getOrdersSummary(null));
        }
        summaryMap.put(jobChain.getPath(), jobChain);
        return summaryMap;
    }

    private OrdersSummary getOrdersSummary(JsonObject json) {
        OrdersSummary summary = new OrdersSummary();
        if (json != null) {
            summary.setBlacklist(json.getInt("blacklisted", 0));
            summary.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
            summary.setRunning(json.getInt("inTaskProcess", 0) + json.getInt("occupiedByClusterMember", 0));
            summary.setSetback(json.getInt("setback", 0));
            summary.setSuspended(json.getInt("suspended", 0));
            summary.setWaitingForResource(json.getInt("waitingForResource", 0) + json.getInt("due", 0) + json.getInt("inTask", 0) - json.getInt(
                    "inTaskProcess", 0));
        } else {
            return null;
//            summary.setBlacklist(0);
//            summary.setPending(0);
//            summary.setRunning(0);
//            summary.setSetback(0);
//            summary.setSuspended(0);
//            summary.setWaitingForResource(0);
        }
        return summary;
    }

    private String getServiceBody(String jobChain) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", jobChain);
        return builder.build().toString();
    }
}
