package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.AgentVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgents;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentV;
import com.sos.joc.model.jobscheduler.AgentsV;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {

    private static final String API_CALL = "./jobscheduler/agents";
    private static final String AGENTS_API_LIST_PATH = "/jobscheduler/master/api/agent/";

    @Override
    public JOCDefaultResponse postJobschedulerAgents(String accessToken, AgentFilter agentFilter) {
        try {
            initLogging(API_CALL, agentFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, agentFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            List<AgentV> listOfAgents = new ArrayList<AgentV>();
            List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();

            if (agentFilter.getAgents() != null && agentFilter.getAgents().size() > 0) {
                Set<AgentUrl> agentUris = new HashSet<AgentUrl>(agentFilter.getAgents());
                for (AgentUrl agentUri : agentUris) {
                    tasks.add(new AgentVCallable(agentUri.getAgent(), dbItemInventoryInstance.getUrl(), accessToken));
                }
            } else {
                JOCJsonCommand jocJsonCommand = new JOCJsonCommand(dbItemInventoryInstance.getUrl(), AGENTS_API_LIST_PATH);
                JsonObject json = jocJsonCommand.getJsonObjectFromGet(accessToken);
                JsonArray agentUris = json.getJsonArray("elements");
                for (JsonString agentUri : agentUris.getValuesAs(JsonString.class)) {
                    tasks.add(new AgentVCallable(agentUri.getString(), dbItemInventoryInstance.getUrl(), accessToken));
                }
            }

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<AgentV> result : executorService.invokeAll(tasks)) {
                try {
                    listOfAgents.add(result.get());
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof JocException) {
                        throw (JocException) e.getCause();
                    } else {
                        throw (Exception) e.getCause();
                    }
                }
            }

            AgentsV entity = new AgentsV();
            entity.setAgents(listOfAgents);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
