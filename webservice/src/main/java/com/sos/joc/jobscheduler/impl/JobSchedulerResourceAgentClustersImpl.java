package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters;
import com.sos.joc.model.jobscheduler.AgentClusterVSchema;
import com.sos.joc.model.jobscheduler.AgentClustersVSchema;
import com.sos.joc.model.jobscheduler.AgentVSchema;
import com.sos.joc.model.jobscheduler.NumOfAgents_;
import com.sos.joc.model.jobscheduler.Processes;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.model.jobscheduler.State_;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClusters {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResourceAgentClustersImpl.class);

    @Override
    public JobschedulerAgentClustersResponse postJobschedulerAgentClusters(String accessToken, JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody) throws Exception {
        JobschedulerAgentClustersResponse jobschedulerAgentClustersResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return JobschedulerAgentClustersResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return JobschedulerAgentClustersResponse.responseStatus440(JocCockpitResponse.getError401Schema(jobschedulerUser,e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return JobschedulerAgentClustersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

        if (!getPermissons().getJobschedulerUniversalAgent().getView().isStatus()) {
            return JobschedulerAgentClustersResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerAgentClustersBody.getJobschedulerId() == null) {
            return JobschedulerAgentClustersResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerAgentClustersBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return JobschedulerAgentClustersResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",
                        jobSchedulerAgentClustersBody.getJobschedulerId(), DBLayer.TABLE_INVENTORY_INSTANCES)));
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
            state1.setSeverity(Severity._0);
            state1.setText(Text.TERMINATING);
            agent1.setState(state1);
            listOfAgents.add(agent1);
            AgentVSchema agent2 = new AgentVSchema();
            agent2.setRunningTasks(-1);
            agent2.setStartedAt(new Date());
            State state2 = new State();
            state2.setSeverity(Severity._2);
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
            state.setSeverity(State_.Severity._2);
            state.setText(State_.Text.ALL_AGENTS_ARE_UNREACHABLE);
            agentClusterVSchema.setState(state);

            agentClusterVSchema.setSurveyDate(new Date());

            listOfAgentClusters.add(agentClusterVSchema);
            entity.setAgentClusters(listOfAgentClusters);

            // TODO get a list of agents and set the data.

            jobschedulerAgentClustersResponse = JobschedulerAgentClustersResponse.responseStatus200(entity);

            return jobschedulerAgentClustersResponse;
        } catch (Exception e) {

            return JobschedulerAgentClustersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
