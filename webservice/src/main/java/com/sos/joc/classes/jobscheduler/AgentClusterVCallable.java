package com.sos.joc.classes.jobscheduler;

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
import com.sos.joc.model.jobscheduler.AgentCluster;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterPath;
import com.sos.joc.model.jobscheduler.AgentOfCluster;

public class AgentClusterVCallable implements Callable<AgentCluster> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentClusterVCallable.class);
    private final String agentCluster;
    private final AgentClusterFilter agentClusterBody;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;

    public AgentClusterVCallable(String agentCluster, AgentClusterFilter agentClusterBody, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.agentCluster = agentCluster;
        this.agentClusterBody = agentClusterBody;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }
    
    public AgentClusterVCallable(AgentClusterFilter agentClusterBody, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.agentCluster = null;
        this.agentClusterBody = agentClusterBody;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
    }

    @Override
    public AgentCluster call() throws Exception {
        return getAgentCluster(agentCluster, agentClusterBody, jocJsonCommand, accessToken);
    }
    
    public List<AgentCluster> getAgentClusters() throws Exception {
        JsonObject jsonObjectFromPost = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(agentCluster), accessToken);
        String regex = agentClusterBody.getRegex();
        Set<String> agentClusterPaths = new HashSet<String>();
        for (AgentClusterPath agentClusterPath : agentClusterBody.getAgentClusters()) {
            agentClusterPaths.add(agentClusterPath.getAgentCluster());
        }
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
            if (!agentClusterPaths.contains(agentClusterV.getPath())) {
                continue;
            }
            if (!FilterAfterResponse.matchRegex(regex, agentClusterV.getPath())) {
                LOGGER.debug("...processing skipped caused by 'regex=" + regex + "'");
                continue;
            }
            agentSet.addAll(agentClusterV.getAgentSet());
            listAgentCluster.add(agentClusterV);
        }
        
        List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();
        for (String agent : agentSet) {
            JOCJsonCommand command = new JOCJsonCommand();
            command.setJOCResourceImpl(jocJsonCommand.getJOCResourceImpl());
            command.setBasicAuthorization(jocJsonCommand.getBasicAuthorization());
            tasks.add(new AgentVCallable(agent, command, accessToken));
        }
        Map<String, AgentOfCluster> mapOfAgents = new HashMap<String, AgentOfCluster>();
        if (!tasks.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size())); // max. 10;
            try {
                for (Future<AgentOfCluster> result : executorService.invokeAll(tasks)) {
                    try {
                        AgentOfCluster a = result.get();
                        mapOfAgents.put(a.getUrl(), a);
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof JocException) {
                            throw (JocException) e.getCause();
                        } else {
                            throw (Exception) e.getCause();
                        }
                    }
                }
            } finally {
                executorService.shutdown();
            }
        }
        List<AgentCluster> listAgentClusterV = new ArrayList<AgentCluster>();
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

    private AgentCluster getAgentCluster(String agentCluster, AgentClusterFilter agentClusterBody, JOCJsonCommand jocJsonCommand, String accessToken) throws Exception {
        JsonObject jsonObjectFromPost = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(agentCluster), accessToken);
        Date surveyDate = JobSchedulerDate.getDateFromEventId(jsonObjectFromPost.getJsonNumber("eventId").longValue());
        JsonArray agents = jsonObjectFromPost.getJsonArray("agents");
        if (agents == null || agents.isEmpty()) {
            throw new JobSchedulerBadRequestException(String.format("%1$s is not an AgentCluster", agentCluster));
        }
        AgentClusterVolatile agentClusterV = new AgentClusterVolatile(jsonObjectFromPost, surveyDate);
        agentClusterV.setAgentClusterPath();
        List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();
        for (String agent :  agentClusterV.getAgentSet()) {
            tasks.add(new AgentVCallable(agent, new JOCJsonCommand(jocJsonCommand), accessToken));
        }
        Map<String,AgentOfCluster> mapOfAgents = new HashMap<String,AgentOfCluster>();
        if (!tasks.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(Math.min(5, tasks.size()));
            try {
                for (Future<AgentOfCluster> result : executorService.invokeAll(tasks)) {
                    try {
                        AgentOfCluster a = result.get();
                        mapOfAgents.put(a.getUrl(), a);
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof JocException) {
                            throw (JocException) e.getCause();
                        } else {
                            throw (Exception) e.getCause();
                        }
                    }
                }
            } finally {
                executorService.shutdown();
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
