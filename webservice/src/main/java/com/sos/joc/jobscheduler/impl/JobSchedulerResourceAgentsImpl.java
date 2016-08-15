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
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgents;
import com.sos.joc.model.jobscheduler.AgentVSchema;
import com.sos.joc.model.jobscheduler.AgentsVSchema;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {

    @Override
    public JobschedulerAgentsResponse postJobschedulerAgents(String accessToken, JobSchedulerAgentsBody jobSchedulerAgentsBody) throws Exception {
        JobschedulerAgentsResponse jobschedulerAgentsResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerAgentsResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerAgentsResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }
        
        if (!getPermissons().getJobschedulerUniversalAgent().getView().isStatus()){
            return JobschedulerAgentsResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerAgentsBody.getJobschedulerId() == null) {
            return JobschedulerAgentsResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerAgentsBody.getJobschedulerId()));
          
            if (dbItemInventoryInstance == null) {
                return JobschedulerAgentsResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerAgentsBody.getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }
            
            AgentsVSchema entity = new AgentsVSchema();
 
            //TODO JOC Cockpit Webservice
            
            entity.setDeliveryDate(new Date());
            
            
            ArrayList<AgentVSchema> listOfAgents = new ArrayList<AgentVSchema>();

            //TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden

            for (JobSchedulerAgent agentFilter : jobSchedulerAgentsBody.getAgents()) {
                AgentVSchema agent = new AgentVSchema();
                agent.setRunningTasks(-1);
                agent.setStartedAt(new Date());
                State state = new State();
                state.setSeverity(Severity._0);
                state.setText(Text.PAUSED);
                agent.setState(state);
                agent.setUrl(agentFilter.getAgent());
                agent.setSurveyDate(new Date());
                listOfAgents.add(agent);
            }

            //TODO get a list of agents and set the data.
            
            entity.setAgents(listOfAgents);
             
            jobschedulerAgentsResponse = JobschedulerAgentsResponse.responseStatus200(entity);

            return jobschedulerAgentsResponse;
        } catch (Exception e) {

            return JobschedulerAgentsResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

 
 
 

}
