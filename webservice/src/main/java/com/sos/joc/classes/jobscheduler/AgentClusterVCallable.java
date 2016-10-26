package com.sos.joc.classes.jobscheduler;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterV;
import com.sos.joc.model.jobscheduler.AgentV;

public class AgentClusterVCallable implements Callable<AgentClusterV> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentClusterVCallable.class);
    private final String masterUrl;
    private final String agentCluster;
    private final AgentClusterFilter agentClusterBody;
    private final URI uri;

    public AgentClusterVCallable(String agentCluster, AgentClusterFilter agentClusterBody, URI uri, String masterUrl) {
        this.agentCluster = agentCluster;
        this.agentClusterBody = agentClusterBody;
        this.uri = uri;
        this.masterUrl = masterUrl;
    }
    
    public AgentClusterVCallable(AgentClusterFilter agentClusterBody, URI uri, String masterUrl) {
        this.agentCluster = null;
        this.agentClusterBody = agentClusterBody;
        this.uri = uri;
        this.masterUrl = masterUrl;
    }

    @Override
    public AgentClusterV call() throws Exception {
        return getAgentClusters(agentCluster, agentClusterBody, uri);
    }
    
    public List<AgentClusterV> getAgentCluster() throws Exception {
        JsonObject jsonObjectFromPost = new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(agentCluster));
        String regex = agentClusterBody.getRegex();
        Set<String> agentSet = new HashSet<String>();
        Set<AgentClusterVolatile> listAgentCluster = new HashSet<AgentClusterVolatile>();
        Date surveyDate = JobSchedulerDate.getDateFromEventId(jsonObjectFromPost.getJsonNumber("eventId").longValue());
        for (JsonObject processClass : jsonObjectFromPost.getJsonArray("elements").getValuesAs(JsonObject.class)) {
            JsonArray agents = processClass.getJsonArray("agents");
            if (agents == null || agents.isEmpty()) {
                continue;
            }
            AgentClusterVolatile agentClusterV = new AgentClusterVolatile(processClass, surveyDate);
            agentClusterV.setAgentClusterPath();
            if (!FilterAfterResponse.matchRegex(regex, agentClusterV.getPath())) {
                LOGGER.debug("...processing skipped caused by 'regex=" + regex + "'");
                continue;
            }
            agentSet.addAll(agentClusterV.getAgentSet());
            listAgentCluster.add(agentClusterV);
        }
        
        List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();
        for (String agent : agentSet) {
            tasks.add(new AgentVCallable(agent, masterUrl));
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Map<String,AgentV> mapOfAgents = new HashMap<String,AgentV>();
        for (Future<AgentV> result : executorService.invokeAll(tasks)) {
            try {
                AgentV a = result.get();
                mapOfAgents.put(a.getUrl(), a);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof JocException) {
                    throw (JocException) e.getCause();
                } else {
                    throw (Exception) e.getCause();
                }
            }
        }
        List<AgentClusterV> listAgentClusterV = new ArrayList<AgentClusterV>();
        for (AgentClusterVolatile agentClusterV : listAgentCluster) {
            agentClusterV.setAgentsListAndState(mapOfAgents, agentClusterBody.getCompact());
            if (agentClusterBody.getState() != null && agentClusterV.getState().getSeverity() != agentClusterBody.getState()) {
                continue;
            }
            agentClusterV.setFields(mapOfAgents, agentClusterBody.getCompact());
            listAgentClusterV.add(agentClusterV);
        }
        return listAgentClusterV;
    }

    private AgentClusterV getAgentClusters(String agentCluster, AgentClusterFilter agentClusterBody, URI uri) throws Exception {
        JsonObject jsonObjectFromPost = new JOCJsonCommand().getJsonObjectFromPost(uri, getServiceBody(agentCluster));
        Date surveyDate = JobSchedulerDate.getDateFromEventId(jsonObjectFromPost.getJsonNumber("eventId").longValue());
        JsonArray agents = jsonObjectFromPost.getJsonArray("agents");
        if (agents == null || agents.isEmpty()) {
            throw new JobSchedulerBadRequestException(String.format("%1$s is not an AgentCluster", agentCluster));
        }
        AgentClusterVolatile agentClusterV = new AgentClusterVolatile(jsonObjectFromPost, surveyDate);
        agentClusterV.setAgentClusterPath();
        List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();
        for (String agent :  agentClusterV.getAgentSet()) {
            tasks.add(new AgentVCallable(agent, masterUrl));
        }
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Map<String,AgentV> mapOfAgents = new HashMap<String,AgentV>();
        for (Future<AgentV> result : executorService.invokeAll(tasks)) {
            try {
                AgentV a = result.get();
                mapOfAgents.put(a.getUrl(), a);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof JocException) {
                    throw (JocException) e.getCause();
                } else {
                    throw (Exception) e.getCause();
                }
            }
        }
        agentClusterV.setFields(mapOfAgents, agentClusterBody.getCompact());
        return agentClusterV;
    }

    private String getServiceBody(String agentCluster) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (agentCluster == null) {
            agentCluster = "/";
        } else {
            agentCluster = ("/"+agentCluster.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
            if (agentCluster == "/(default)") {
                agentCluster = "";
            }
        }
        builder.add("path", agentCluster);
        return builder.build().toString();
    }
}
