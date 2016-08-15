package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP;
import com.sos.joc.model.jobscheduler.Agent;
import com.sos.joc.model.jobscheduler.AgentClusterPSchema;
import com.sos.joc.model.jobscheduler.AgentClustersPSchema;
import com.sos.joc.model.jobscheduler.NumOfAgents;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Os.Architecture;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.model.jobscheduler.State_;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClustersP {

    @Override
    public JobschedulerAgentClustersPResponse postJobschedulerAgentClusters(String accessToken, JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody) throws Exception {
        JobschedulerAgentClustersPResponse jobschedulerAgentClustersResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerAgentClustersPResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerAgentClustersPResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (!getPermissons().getJobschedulerUniversalAgent().getView().isStatus()) {
            return JobschedulerAgentClustersPResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerAgentClustersBody.getJobschedulerId() == null) {
            return JobschedulerAgentClustersPResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerAgentClustersBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return JobschedulerAgentClustersPResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerAgentClustersBody.getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }

            AgentClustersPSchema entity = new AgentClustersPSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());
            
            ArrayList<AgentClusterPSchema> listOfAgentClusters = new ArrayList<AgentClusterPSchema>();

            AgentClusterPSchema agentClusterPSchema = new AgentClusterPSchema();
            agentClusterPSchema.setMaxProcesses(-1);
            agentClusterPSchema.setName("myName");
            agentClusterPSchema.setPath("myPath");
            agentClusterPSchema.setType(AgentClusterPSchema.Type.ROUND_ROBIN);
            agentClusterPSchema.setSurveyDate(new Date());

            ArrayList<Agent> listOfAgents = new ArrayList<Agent>();
            Agent agent1 = new Agent();
            agent1.setHost("myHost");
            Os os = new Os();
            os.setArchitecture(Architecture._64);
            os.setDistribution("myDistribution");
            os.setName("myName");
            agent1.setOs(os);
            agent1.setPort(4444);
            agent1.setStartedAt(new Date());
            State state1 = new State();
            state1.setSeverity(State.Severity._1);
            state1.setText(Text.TERMINATING);
            agent1.setState(state1);
            agent1.setSurveyDate(new Date());
            agent1.setUrl("myUrl");
            agent1.setVersion("myVersion");     
            agentClusterPSchema.setAgents(listOfAgents);

  
            listOfAgents.add(agent1);
            
            Agent agent2 = new Agent();
            agent2.setHost("myHost");
            Os os2 = new Os();
            os2.setArchitecture(Architecture._32);
            os2.setDistribution("myDistribution");
            os2.setName("myName");
            agent2.setOs(os);
            agent2.setPort(4444);
            agent2.setStartedAt(new Date());
            State state2 = new State();
            state2.setSeverity(State.Severity._3);
            state2.setText(Text.TERMINATING);
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
            state.setSeverity(State_.Severity._2);
            state.setText(State_.Text.ALL_AGENTS_ARE_UNREACHABLE);
            agentClusterPSchema.setState(state);

            listOfAgentClusters.add(agentClusterPSchema);
            entity.setAgentClusters(listOfAgentClusters);

            // TODO get a list of agents and set the data.

            jobschedulerAgentClustersResponse = JobschedulerAgentClustersPResponse.responseStatus200(entity);

            return jobschedulerAgentClustersResponse;
        } catch (Exception e) {

            return JobschedulerAgentClustersPResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
