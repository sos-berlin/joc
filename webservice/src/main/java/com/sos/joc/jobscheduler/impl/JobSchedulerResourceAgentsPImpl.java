package com.sos.joc.jobscheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentsP;
import com.sos.joc.model.jobscheduler.AgentFilterSchema;
import com.sos.joc.model.jobscheduler.AgentPSchema;
import com.sos.joc.model.jobscheduler.Agent_;
import com.sos.joc.model.jobscheduler.AgentsPSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentsP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentsP(String accessToken, AgentFilterSchema agentFilterSchema) {
        LOGGER.debug("init jobscheduler/agents/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(agentFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerUniversalAgent().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentsPSchema entity = new AgentsPSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            // TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden
            ArrayList<AgentPSchema> listOfAgents = new ArrayList<AgentPSchema>();

            for (Agent_ agentFilter : agentFilterSchema.getAgents()) {
                AgentPSchema agent = new AgentPSchema();
                agent.setStartedAt(new Date());
                State state = new State();
                state.setSeverity(0);
                state.setText(Text.PAUSED);
                agent.setState(state);
                agent.setSurveyDate(new Date());
                agent.setHost(dbItemInventoryInstance.getHostname());
                agent.setUrl(agentFilter.getAgent());
                Os os = new Os();
                os.setArchitecture("32");
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

            // TODO get a list of agents and set the data.

            entity.setAgents(listOfAgents);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
