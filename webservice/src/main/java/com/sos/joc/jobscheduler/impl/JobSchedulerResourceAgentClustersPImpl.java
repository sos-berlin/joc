package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.db.inventory.agents.AgentClusterMember;
import com.sos.joc.db.inventory.agents.AgentClusterPermanent;
import com.sos.joc.db.inventory.agents.InventoryAgentsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP;
import com.sos.joc.model.jobscheduler.AgentCluster;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterPath;
import com.sos.joc.model.jobscheduler.AgentClusterState;
import com.sos.joc.model.jobscheduler.AgentClusterStateText;
import com.sos.joc.model.jobscheduler.AgentClusters;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.NumOfAgentsInCluster;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClustersP {

    private static final String API_CALL = "./jobscheduler/agent_clusters/p";

    @Override
    public JOCDefaultResponse postJobschedulerAgentClustersP(String accessToken, AgentClusterFilter jobSchedulerAgentClustersBody) {
        
        
        SOSHibernateConnection connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            initLogging(API_CALL, jobSchedulerAgentClustersBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerAgentClustersBody.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            Set<String> agentClusterPaths = new HashSet<String>();
            if (jobSchedulerAgentClustersBody.getAgentClusters() != null) {
                for (AgentClusterPath agentCluster : jobSchedulerAgentClustersBody.getAgentClusters()) {
                    checkRequiredParameter("agentCluster", agentCluster.getAgentCluster());
                    agentClusterPaths.add(agentCluster.getAgentCluster());
                }
            }
            Globals.beginTransaction(connection);
            InventoryAgentsDBLayer agentLayer = new InventoryAgentsDBLayer(connection);
            //List<AgentClusterMember> agentClusterMembers = agentLayer.getInventoryAgentClusterMembers(dbItemInventoryInstance.getId(), null);
            List<AgentClusterPermanent> agentClusters = agentLayer.getInventoryAgentClusters(dbItemInventoryInstance.getId(), agentClusterPaths);
            
            ArrayList<AgentCluster> listOfAgentClusters = new ArrayList<AgentCluster>();
            for (AgentClusterPermanent agentCluster : agentClusters) {
                if (!FilterAfterResponse.matchRegex(jobSchedulerAgentClustersBody.getRegex(), agentCluster.getPath())) {
                    continue;
                }
                List<AgentClusterMember> agentClusterMembers = agentLayer.getInventoryAgentClusterMembersById(dbItemInventoryInstance.getId(), agentCluster.getAgentClusterId());
                int countRunningAgents = 0;
                for (AgentClusterMember agentClusterMember : agentClusterMembers) {
                    if (agentClusterMember.getState().getSeverity() == 0) {
                        countRunningAgents++;
                    }
                }
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
                if (jobSchedulerAgentClustersBody.getCompact() == null || !jobSchedulerAgentClustersBody.getCompact()) {
                    agentCluster.setAgents(new ArrayList<AgentOfCluster>(agentClusterMembers));
                }
                listOfAgentClusters.add(agentCluster);
            }
            
            AgentClusters entity = new AgentClusters();
            entity.setAgentClusters(listOfAgentClusters);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);;
        }
    }
}