package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.jobscheduler.AgentClusterVolatile;
import com.sos.joc.classes.jobscheduler.AgentVCallable;
import com.sos.joc.db.inventory.agents.AgentClusterMember;
import com.sos.joc.db.inventory.agents.AgentClusterPermanent;
import com.sos.joc.db.inventory.agents.InventoryAgentsDBLayer;
import com.sos.joc.exceptions.JobSchedulerConnectionRefusedException;
import com.sos.joc.exceptions.JobSchedulerNoResponseException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobscheduler.AgentCluster;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterPath;
import com.sos.joc.model.jobscheduler.AgentClusterState;
import com.sos.joc.model.jobscheduler.AgentClusterStateText;
import com.sos.joc.model.jobscheduler.AgentClusters;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.NumOfAgentsInCluster;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersImpl extends JOCResourceImpl
		implements IJobSchedulerResourceAgentClusters {

	private static final String API_CALL = "./jobscheduler/agent_clusters";

	@Override
	public JOCDefaultResponse postJobschedulerAgentClusters(String xAccessToken, String accessToken,
			AgentClusterFilter jobSchedulerAgentClustersBody) {
		return postJobschedulerAgentClusters(getAccessToken(xAccessToken, accessToken), jobSchedulerAgentClustersBody);
	}

	public JOCDefaultResponse postJobschedulerAgentClusters(String accessToken,
			AgentClusterFilter jobSchedulerAgentClustersBody) {

		SOSHibernateSession connection = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerAgentClustersBody, accessToken,
					jobSchedulerAgentClustersBody.getJobschedulerId(),
					getPermissonsJocCockpit(jobSchedulerAgentClustersBody.getJobschedulerId(), accessToken)
							.getJobschedulerUniversalAgent().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			JOCJsonCommand command = new JOCJsonCommand(this);
			command.setUriBuilderForProcessClasses();
			// always false otherwise no agent info
			// command.addProcessClassCompactQuery(jobSchedulerAgentClustersBody.getCompact());
			command.addProcessClassCompactQuery(false);

			AgentClusters entity = new AgentClusters();
			boolean volatileResponseIsAvailable = true;

			Set<String> agentClusterPaths = new HashSet<String>();
			if (jobSchedulerAgentClustersBody.getAgentClusters() != null) {
				for (AgentClusterPath agentClusterPath : jobSchedulerAgentClustersBody.getAgentClusters()) {
					checkRequiredParameter("agentCluster", agentClusterPath.getAgentCluster());
					agentClusterPaths.add(agentClusterPath.getAgentCluster());
				}
			}
			Set<String> agentSet = new HashSet<String>();
            Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
			Set<AgentClusterVolatile> listAgentCluster = new HashSet<AgentClusterVolatile>();
			InventoryAgentsDBLayer agentLayer = null;

			try {
				JsonObject jsonObjectFromPost = command.getJsonObjectFromPostWithRetry(getServiceBody(null),
						accessToken);
				Date surveyDate = JobSchedulerDate
						.getDateFromEventId(jsonObjectFromPost.getJsonNumber("eventId").longValue());
				for (JsonObject processClass : jsonObjectFromPost.getJsonArray("elements")
						.getValuesAs(JsonObject.class)) {
					JsonArray agents = processClass.getJsonArray("agents");
                    if (agents == null || agents.isEmpty()) { // consider only process classes with agents
						continue;
					}
					AgentClusterVolatile agentClusterV = new AgentClusterVolatile(processClass, surveyDate);
					agentClusterV.setAgentClusterPath();
                    if (!canAdd(agentClusterV.getPath(), permittedFolders)) {
                        continue;
                    }
					if (!agentClusterPaths.isEmpty() && !agentClusterPaths.contains(agentClusterV.getPath())) {
						continue;
					}
					if (!FilterAfterResponse.matchRegex(jobSchedulerAgentClustersBody.getRegex(),
							agentClusterV.getPath())) {
						continue;
					}
                    if (!isInFolders(agentClusterV.getPath(), jobSchedulerAgentClustersBody.getFolders())) {
						continue;
					}
					agentSet.addAll(agentClusterV.getAgentSet());
					listAgentCluster.add(agentClusterV);
				}
			} catch (JobSchedulerConnectionRefusedException | JobSchedulerNoResponseException e1) {
				volatileResponseIsAvailable = false;
			}

			if (volatileResponseIsAvailable) {
				List<AgentVCallable> tasks = new ArrayList<AgentVCallable>();
				for (String agent : agentSet) {
					tasks.add(new AgentVCallable(agent, new JOCJsonCommand(this), accessToken));
				}
				Map<String, AgentOfCluster> mapOfAgents = new HashMap<String, AgentOfCluster>();
				Set<String> unreachableAgents = new HashSet<String>();
				if (!tasks.isEmpty()) {
					ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
					try {
						for (Future<AgentOfCluster> result : executorService.invokeAll(tasks)) {
							try {
								AgentOfCluster a = result.get();
								if (a.getState().get_text() == JobSchedulerStateText.UNREACHABLE) {
								    unreachableAgents.add(a.getUrl());
								}
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
				if (!unreachableAgents.isEmpty()) {
				    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
				    agentLayer = new InventoryAgentsDBLayer(connection); 
				    List<AgentClusterMember> unreachableAgentClusterMembers = agentLayer.getInventoryAgentClusterMembersByUrls(dbItemInventoryInstance.getId(), unreachableAgents);
				    if(unreachableAgentClusterMembers != null) {
				        for (AgentClusterMember unreachableAgentClusterMember : unreachableAgentClusterMembers) {
				            mapOfAgents.put(unreachableAgentClusterMember.getUrl(), unreachableAgentClusterMember);
				        }
				    }
				}
				List<AgentCluster> listOfAgentClusters = new ArrayList<AgentCluster>();
				for (AgentClusterVolatile agentClusterV : listAgentCluster) {
					agentClusterV.setAgentsListAndState(mapOfAgents, jobSchedulerAgentClustersBody.getCompact());
					if (jobSchedulerAgentClustersBody.getState() != null
							&& agentClusterV.getState().getSeverity() != jobSchedulerAgentClustersBody.getState()) {
						continue;
					}
					agentClusterV.setFields(mapOfAgents, jobSchedulerAgentClustersBody.getCompact());
					listOfAgentClusters.add(agentClusterV);
				}

				entity.setAgentClusters(listOfAgentClusters);

			} else {
			    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
				agentLayer = new InventoryAgentsDBLayer(connection);
				List<AgentClusterPermanent> agentClusters = agentLayer
						.getInventoryAgentClusters(dbItemInventoryInstance.getId(), agentClusterPaths);

				List<AgentCluster> listOfAgentClusters = new ArrayList<AgentCluster>();
				for (AgentClusterPermanent agentCluster : agentClusters) {
                    if (!canAdd(agentCluster.getPath(), permittedFolders)) {
                        continue;
                    }
                    if (!FilterAfterResponse.matchRegex(jobSchedulerAgentClustersBody.getRegex(), agentCluster.getPath())) {
                        continue;
                    }
                    if (!isInFolders(agentCluster.getPath(), jobSchedulerAgentClustersBody.getFolders())) {
                        continue;
                    }
                    List<AgentClusterMember> agentClusterMembers = agentLayer.getInventoryAgentClusterMembersById(dbItemInventoryInstance.getId(),
                            agentCluster.getAgentClusterId());
                    int countRunningAgents = agentClusterMembers.stream().filter(p -> p.getState().getSeverity() == 0).mapToInt(e -> 1).sum();
                    
                    NumOfAgentsInCluster numOfAgents = agentCluster.getNumOfAgents();
                    numOfAgents.setRunning(countRunningAgents);
                    AgentClusterState state = new AgentClusterState();
                    if (numOfAgents.getAny() == numOfAgents.getRunning()) {
                        state.setSeverity(0);
                        state.set_text(AgentClusterStateText.ALL_AGENTS_ARE_RUNNING);
                    } else if (0 == numOfAgents.getRunning()) {
                        state.setSeverity(2);
                        state.set_text(AgentClusterStateText.ALL_AGENTS_ARE_UNREACHABLE);
                    } else {
                        state.setSeverity(1);
                        state.set_text(AgentClusterStateText.ONLY_SOME_AGENTS_ARE_RUNNING);
                    }
                    if (jobSchedulerAgentClustersBody.getState() != null && jobSchedulerAgentClustersBody.getState() != state.getSeverity()) {
                        continue;
                    }
                    agentCluster.setState(state);
                    if (jobSchedulerAgentClustersBody.getCompact() != null && !jobSchedulerAgentClustersBody.getCompact()) {
                        agentCluster.setAgents(new ArrayList<AgentOfCluster>(agentClusterMembers));
                    }
                    listOfAgentClusters.add(agentCluster);
                }
                entity.setAgentClusters(listOfAgentClusters);
            }

			entity.setDeliveryDate(Date.from(Instant.now()));

			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}

	private boolean isInFolders(String objPath, List<Folder> folders) {
		if (folders == null || folders.isEmpty()) {
			return true;
		}
		String parent = getParent(objPath);
		if (parent == null) {
			return false;
		}
		for (Folder folder : folders) {
			String f = normalizeFolder(folder.getFolder());
            if (parent.equals(f)) {
                return true;
            }
            if (folder.getRecursive() == null || folder.getRecursive()) {
                if ("/".equals(f)) {
                    return true;
                }
                if (parent.startsWith(f + "/")) {
                    return true;
                }
			}
		}
		return false;
	}

	private String getServiceBody(String agentCluster) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (agentCluster == null) {
			agentCluster = "/";
		} else {
			agentCluster = ("/" + agentCluster.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
			if (agentCluster == "/(default)") {
				agentCluster = "";
			}
		}
		builder.add("path", agentCluster);
		return builder.build().toString();
	}
}
