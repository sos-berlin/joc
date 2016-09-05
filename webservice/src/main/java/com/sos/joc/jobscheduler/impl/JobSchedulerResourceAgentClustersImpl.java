package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters;
import com.sos.joc.model.jobscheduler.AgentClusterFilterSchema;
import com.sos.joc.model.jobscheduler.AgentClusterVSchema;
import com.sos.joc.model.jobscheduler.AgentClustersVSchema;
import com.sos.joc.model.jobscheduler.AgentVSchema;
import com.sos.joc.model.jobscheduler.NumOfAgents_;
import com.sos.joc.model.jobscheduler.Processes;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.model.jobscheduler.State_;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClusters {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentClusters(String accessToken, AgentClusterFilterSchema jobSchedulerAgentClustersBody) {
        LOGGER.debug("init JobschedulerAgentClusters");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentClustersVSchema entity = new AgentClustersVSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());
            ArrayList<AgentClusterVSchema> listOfAgentClusters = new ArrayList<AgentClusterVSchema>();

            AgentClusterVSchema agentClusterVSchema = new AgentClusterVSchema();

            ArrayList<AgentVSchema> listOfAgents = new ArrayList<AgentVSchema>();
            AgentVSchema agent1 = new AgentVSchema();
            agent1.setRunningTasks(-1);
            agent1.setStartedAt(new Date());
            State state1 = new State();
            state1.setSeverity(0);
            state1.setText(Text.TERMINATING);
            agent1.setState(state1);
            listOfAgents.add(agent1);
            AgentVSchema agent2 = new AgentVSchema();
            agent2.setRunningTasks(-1);
            agent2.setStartedAt(new Date());
            State state2 = new State();
            state2.setSeverity(2);
            state2.setText(Text.DEAD);
            agent2.setState(state2);
            listOfAgents.add(agent2);

            agentClusterVSchema.setAgents(listOfAgents);
            NumOfAgents_ numOfAgents = new NumOfAgents_();
            numOfAgents.setAny(-1);
            numOfAgents.setRunning(-1);
            agentClusterVSchema.setNumOfAgents(numOfAgents);

            agentClusterVSchema.setNumOfProcesses(-1);
            agentClusterVSchema.setPath("myPath");

            Processes processes = new Processes();
            processes.setAgent("myAgent");
            processes.setJob("myJob");
            processes.setPid(-1);
            processes.setRunningSince(new Date());
            processes.setTaskId(-1);
            agentClusterVSchema.setProcesses(processes);

            State_ state = new State_();
            state.setSeverity(2);
            state.setText(State_.Text.ALL_AGENTS_ARE_RUNNING);
            agentClusterVSchema.setState(state);

            agentClusterVSchema.setSurveyDate(new Date());

            listOfAgentClusters.add(agentClusterVSchema);
            entity.setAgentClusters(listOfAgentClusters);

            // TODO get a list of agents and set the data.

            return JOCDefaultResponse.responseStatus200(entity);

        } catch (Exception e) {

            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
