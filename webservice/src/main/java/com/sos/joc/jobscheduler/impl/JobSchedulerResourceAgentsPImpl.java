package com.sos.joc.jobscheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentsP;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentP;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentsP;
import com.sos.joc.model.jobscheduler.OperatingSystem;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentsP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentsP(String accessToken, AgentFilter agentFilterSchema) {
        LOGGER.debug("init jobscheduler/agents/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(agentFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerUniversalAgent().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentsP entity = new AgentsP();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            // TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden
            ArrayList<AgentP> listOfAgents = new ArrayList<AgentP>();

            for (AgentUrl agentFilter : agentFilterSchema.getAgents()) {
                AgentP agent = new AgentP();
                agent.setStartedAt(new Date());
                JobSchedulerState state = new JobSchedulerState();
                state.setSeverity(0);
                state.set_text(JobSchedulerStateText.PAUSED);
                agent.setState(state);
                agent.setSurveyDate(new Date());
                agent.setHost(dbItemInventoryInstance.getHostname());
                agent.setUrl(agentFilter.getAgent());
                OperatingSystem os = new OperatingSystem();
                os.setArchitecture("32");
                os.setDistribution("myDistribution");
                os.setName("myName");
                agent.setOs(os);
                agent.setStartedAt(dbItemInventoryInstance.getStartedAt());
                ArrayList<String> listOfClusters = new ArrayList<String>();
                listOfClusters.add("cluster1");
                listOfClusters.add("cluster2");
                agent.setClusters(listOfClusters);
                listOfAgents.add(agent);
            }

            // TODO get a list of agents and set the data.

            entity.setAgents(listOfAgents);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
