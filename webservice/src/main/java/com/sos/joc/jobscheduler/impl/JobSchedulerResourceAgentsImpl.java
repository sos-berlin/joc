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
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentsV;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {

	private static final String API_CALL = "./jobscheduler/agents";
	private static final String AGENTS_API_LIST_PATH = "/jobscheduler/master/api/agent/";

	@Override
	public JOCDefaultResponse postJobschedulerAgents(String xAccessToken, String accessToken, AgentFilter agentFilter) {
		return postJobschedulerAgents(getAccessToken(xAccessToken, accessToken), agentFilter);
	}

	public JOCDefaultResponse postJobschedulerAgents(String accessToken, AgentFilter agentFilter) {
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, agentFilter, accessToken,
					agentFilter.getJobschedulerId(),
					getPermissonsJocCockpit(agentFilter.getJobschedulerId(), accessToken)
							.getJobschedulerUniversalAgent().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			List<AgentOfCluster> listOfAgents = new ArrayList<AgentOfCluster>();
			List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();

			if (agentFilter.getAgents() != null && agentFilter.getAgents().size() > 0) {
				Set<AgentUrl> agentUris = new HashSet<AgentUrl>(agentFilter.getAgents());
				for (AgentUrl agentUri : agentUris) {
					tasks.add(new AgentVCallable(agentUri.getAgent(), new JOCJsonCommand(this), accessToken));
				}
			} else {
				JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this, AGENTS_API_LIST_PATH);
				JsonObject json = jocJsonCommand.getJsonObjectFromGetWithRetry(accessToken);
				JsonArray agentUris = json.getJsonArray("elements");
				for (JsonString agentUri : agentUris.getValuesAs(JsonString.class)) {
					tasks.add(new AgentVCallable(agentUri.getString(), new JOCJsonCommand(this), accessToken));
				}
			}
			if (!tasks.isEmpty()) {
				ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
				try {
					for (Future<AgentOfCluster> result : executorService.invokeAll(tasks)) {
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
				} finally {
					executorService.shutdown();
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
