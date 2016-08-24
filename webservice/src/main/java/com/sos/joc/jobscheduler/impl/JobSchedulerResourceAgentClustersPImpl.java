package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP;
import com.sos.joc.model.jobscheduler.Agent;
import com.sos.joc.model.jobscheduler.AgentClusterPSchema;
import com.sos.joc.model.jobscheduler.AgentClustersPSchema;
import com.sos.joc.model.jobscheduler.NumOfAgents;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.model.jobscheduler.State_;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClustersP {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResourceAgentClustersPImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentClustersP(String accessToken, JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody) {
        LOGGER.debug("init JobschedulerAgentClustersP");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentClustersPSchema entity = new AgentClustersPSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            ArrayList<AgentClusterPSchema> listOfAgentClusters = new ArrayList<AgentClusterPSchema>();

            AgentClusterPSchema agentClusterPSchema = new AgentClusterPSchema();
            agentClusterPSchema.setMaxProcesses(-1);
            agentClusterPSchema.setName("myName");
            agentClusterPSchema.setPath("myPath");
            agentClusterPSchema.setType(AgentClusterPSchema.Type.round_robin);
            agentClusterPSchema.setSurveyDate(new Date());

            ArrayList<Agent> listOfAgents = new ArrayList<Agent>();
            Agent agent1 = new Agent();
            agent1.setHost("myHost");
            Os os = new Os();
            os.setArchitecture("64");
            os.setDistribution("myDistribution");
            os.setName("myName");
            agent1.setOs(os);
            agent1.setPort(4444);
            agent1.setStartedAt(new Date());
            State state1 = new State();
            state1.setSeverity(1);
            state1.setText(Text.terminating);
            agent1.setState(state1);
            agent1.setSurveyDate(new Date());
            agent1.setUrl("myUrl");
            agent1.setVersion("myVersion");
            agentClusterPSchema.setAgents(listOfAgents);

            listOfAgents.add(agent1);

            Agent agent2 = new Agent();
            agent2.setHost("myHost");
            Os os2 = new Os();
            os2.setArchitecture("32");
            os2.setDistribution("myDistribution");
            os2.setName("myName");
            agent2.setOs(os);
            agent2.setPort(4444);
            agent2.setStartedAt(new Date());
            State state2 = new State();
            state2.setSeverity(3);
            state2.setText(Text.terminating);
            agent2.setState(state1);
            agent2.setSurveyDate(new Date());
            agent2.setUrl("myUrl");
            agent2.setVersion("myVersion");
            listOfAgents.add(agent2);

            agentClusterPSchema.setAgents(listOfAgents);
            NumOfAgents numOfAgents = new NumOfAgents();
            numOfAgents.setAny(-1);
            numOfAgents.setRunning(-1);
            agentClusterPSchema.setNumOfAgents(numOfAgents);

            State_ state = new State_();
            state.setSeverity(2);
            state.setText(State_.Text.all_agents_are_unreachable);
            agentClusterPSchema.setState(state);

            listOfAgentClusters.add(agentClusterPSchema);
            entity.setAgentClusters(listOfAgentClusters);

            // TODO get a list of agents and set the data.

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
