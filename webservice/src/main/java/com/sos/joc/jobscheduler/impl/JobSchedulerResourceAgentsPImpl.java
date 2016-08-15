package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerAgent;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentsBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentsP;
import com.sos.joc.model.jobscheduler.AgentPSchema;
import com.sos.joc.model.jobscheduler.AgentsPSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Os.Architecture;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentsP {

    @Override
    public JobschedulerAgentsPResponse postJobschedulerAgentsP(String accessToken, JobSchedulerAgentsBody jobSchedulerAgentsBody) throws Exception {
        JobschedulerAgentsPResponse jobschedulerAgentsResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerAgentsPResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerAgentsPResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }
        
        if (!getPermissons().getJobschedulerUniversalAgent().getView().isStatus()){
            return JobschedulerAgentsPResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerAgentsBody.getJobschedulerId() == null) {
            return JobschedulerAgentsPResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerAgentsBody.getJobschedulerId()));
          
            if (dbItemInventoryInstance == null) {
                return JobschedulerAgentsPResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerAgentsBody.getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }
            
            AgentsPSchema entity = new AgentsPSchema();
 
            //TODO JOC Cockpit Webservice
            
            entity.setDeliveryDate(new Date());
            
            //TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden
            ArrayList<AgentPSchema> listOfAgents = new ArrayList<AgentPSchema>();
          
            for (JobSchedulerAgent agentFilter : jobSchedulerAgentsBody.getAgents()) {
                AgentPSchema agent = new AgentPSchema();
                agent.setStartedAt(new Date());
                State state = new State();
                state.setSeverity(Severity._0);
                state.setText(Text.PAUSED);
                agent.setState(state);
                agent.setSurveyDate(new Date());
                agent.setHost(dbItemInventoryInstance.getHostname());
                agent.setUrl(agentFilter.getAgent());
                Os os = new Os();
                os.setArchitecture(Architecture._32);
                os.setDistribution("myDistribution");
                os.setName("myName");
                agent.setOs(os);
                agent.setPort(dbItemInventoryInstance.getPort());
                agent.setStartedAt(dbItemInventoryInstance.getStartTime());
                ArrayList<String> listOfClusters = new ArrayList<String>();
                listOfClusters.add("cluster1");
                listOfClusters.add("cluster2");
                agent.setClusters(listOfClusters);
                listOfAgents.add(agent);
            }

            //TODO get a list of agents and set the data.
            
            entity.setAgents(listOfAgents);
             
            jobschedulerAgentsResponse = JobschedulerAgentsPResponse.responseStatus200(entity);

            return jobschedulerAgentsResponse;
        } catch (Exception e) {

            return JobschedulerAgentsPResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

 
 
 

}
