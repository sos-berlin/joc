package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterState;
import com.sos.joc.model.jobscheduler.AgentClusterStateText;
import com.sos.joc.model.jobscheduler.AgentClusterV;
import com.sos.joc.model.jobscheduler.AgentClustersV;
import com.sos.joc.model.jobscheduler.AgentV;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.NumOfAgentsInCluster;
import com.sos.joc.model.processClass.Process;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClusters {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentClusters(String accessToken, AgentClusterFilter jobSchedulerAgentClustersBody) {
        LOGGER.debug("init jobscheduler/agent/clusters");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentClustersV entity = new AgentClustersV();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());
            ArrayList<AgentClusterV> listOfAgentClusters = new ArrayList<AgentClusterV>();

            AgentClusterV agentClusterVSchema = new AgentClusterV();

            ArrayList<AgentV> listOfAgents = new ArrayList<AgentV>();
            AgentV agent1 = new AgentV();
            agent1.setRunningTasks(-1);
            agent1.setStartedAt(new Date());
            JobSchedulerState state1 = new JobSchedulerState();
            state1.setSeverity(0);
            state1.set_text(JobSchedulerStateText.TERMINATING);
            agent1.setState(state1);
            listOfAgents.add(agent1);
            AgentV agent2 = new AgentV();
            agent2.setRunningTasks(-1);
            agent2.setStartedAt(new Date());
            JobSchedulerState state2 = new JobSchedulerState();
            state2.setSeverity(2);
            state2.set_text(JobSchedulerStateText.DEAD);
            agent2.setState(state2);
            listOfAgents.add(agent2);

            agentClusterVSchema.setAgents(listOfAgents);
            NumOfAgentsInCluster numOfAgents = new NumOfAgentsInCluster();
            numOfAgents.setAny(-1);
            numOfAgents.setRunning(-1);
            agentClusterVSchema.setNumOfAgents(numOfAgents);

            agentClusterVSchema.setNumOfProcesses(-1);
            agentClusterVSchema.setPath("myPath");

            Process process = new Process();
            process.setAgent("myAgent");
            process.setJob("myJob");
            process.setPid(-1);
            process.setRunningSince(new Date());
            process.setTaskId("-1");
            List<Process> processes = new ArrayList<Process>();
            processes.add(process);
            agentClusterVSchema.setProcesses(processes);

            AgentClusterState state = new AgentClusterState();
            state.setSeverity(2);
            state.set_text(AgentClusterStateText.ALL_AGENTS_ARE_RUNNING);
            agentClusterVSchema.setState(state);

            agentClusterVSchema.setSurveyDate(new Date());

            listOfAgentClusters.add(agentClusterVSchema);
            entity.setAgentClusters(listOfAgentClusters);

            // TODO get a list of agents and set the data.

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {

            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
