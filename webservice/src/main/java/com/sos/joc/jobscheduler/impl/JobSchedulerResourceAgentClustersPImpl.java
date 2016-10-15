package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterP;
import com.sos.joc.model.jobscheduler.AgentClusterState;
import com.sos.joc.model.jobscheduler.AgentClusterStateText;
import com.sos.joc.model.jobscheduler.AgentClusterType;
import com.sos.joc.model.jobscheduler.AgentClustersP;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.NumOfAgentsInCluster;
import com.sos.joc.model.jobscheduler.OperatingSystem;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClustersP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersPImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentClustersP(String accessToken, AgentClusterFilter jobSchedulerAgentClustersBody) {
        LOGGER.debug("init jobscheduler/agent/clusters/P");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentClustersP entity = new AgentClustersP();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            ArrayList<AgentClusterP> listOfAgentClusters = new ArrayList<AgentClusterP>();

            AgentClusterP agentClusterPSchema = new AgentClusterP();
            agentClusterPSchema.setMaxProcesses(-1);
            agentClusterPSchema.setName("myName");
            agentClusterPSchema.setPath("myPath");
            agentClusterPSchema.set_type(AgentClusterType.ROUND_ROBIN);
            agentClusterPSchema.setSurveyDate(new Date());

            ArrayList<AgentOfCluster> listOfAgents = new ArrayList<AgentOfCluster>();
            AgentOfCluster agent1 = new AgentOfCluster();
            agent1.setHost("myHost");
            OperatingSystem os = new OperatingSystem();
            os.setArchitecture("64");
            os.setDistribution("myDistribution");
            os.setName("myName");
            agent1.setOs(os);
            agent1.setStartedAt(new Date());
            JobSchedulerState state1 = new JobSchedulerState();
            state1.setSeverity(1);
            state1.set_text(JobSchedulerStateText.TERMINATING);
            agent1.setState(state1);
            agent1.setSurveyDate(new Date());
            agent1.setUrl("myUrl");
            agent1.setVersion("myVersion");
            agentClusterPSchema.setAgents(listOfAgents);

            listOfAgents.add(agent1);

            AgentOfCluster agent2 = new AgentOfCluster();
            agent2.setHost("myHost");
            OperatingSystem os2 = new OperatingSystem();
            os2.setArchitecture("32");
            os2.setDistribution("myDistribution");
            os2.setName("myName");
            agent2.setOs(os);
            agent2.setStartedAt(new Date());
            JobSchedulerState state2 = new JobSchedulerState();
            state2.setSeverity(3);
            state2.set_text(JobSchedulerStateText.TERMINATING);
            agent2.setState(state1);
            agent2.setSurveyDate(new Date());
            agent2.setUrl("myUrl");
            agent2.setVersion("myVersion");
            listOfAgents.add(agent2);

            agentClusterPSchema.setAgents(listOfAgents);
            NumOfAgentsInCluster numOfAgents = new NumOfAgentsInCluster();
            numOfAgents.setAny(-1);
            numOfAgents.setRunning(-1);
            agentClusterPSchema.setNumOfAgents(numOfAgents);

            AgentClusterState state = new AgentClusterState();
            state.setSeverity(2);
            state.set_text(AgentClusterStateText.ALL_AGENTS_ARE_UNREACHABLE);
            agentClusterPSchema.setState(state);

            listOfAgentClusters.add(agentClusterPSchema);
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
