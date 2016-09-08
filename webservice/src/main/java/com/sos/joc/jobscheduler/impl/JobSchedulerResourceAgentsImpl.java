package com.sos.joc.jobscheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgents;
import com.sos.joc.model.jobscheduler.AgentFilterSchema;
import com.sos.joc.model.jobscheduler.AgentVSchema;
import com.sos.joc.model.jobscheduler.Agent_;
import com.sos.joc.model.jobscheduler.AgentsVSchema;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentsImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgents(String accessToken, AgentFilterSchema agentFilterSchema) {
        LOGGER.debug("init jobscheduler/agents");
        try {
            JOCDefaultResponse jocDefaultResponse = init(agentFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentsVSchema entity = new AgentsVSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            ArrayList<AgentVSchema> listOfAgents = new ArrayList<AgentVSchema>();

            // TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden

            for (Agent_ agentFilter : agentFilterSchema.getAgents()) {
                AgentVSchema agent = new AgentVSchema();
                agent.setRunningTasks(-1);
                agent.setStartedAt(new Date());
                State state = new State();
                state.setSeverity(0);
                state.setText(Text.PAUSED);
                agent.setState(state);
                agent.setUrl(agentFilter.getAgent());
                agent.setSurveyDate(new Date());
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
