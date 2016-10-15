package com.sos.joc.jobscheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgents;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentV;
import com.sos.joc.model.jobscheduler.AgentsV;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentsImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgents(String accessToken, AgentFilter agentFilterSchema) {
        LOGGER.debug("init jobscheduler/agents");
        try {
            JOCDefaultResponse jocDefaultResponse = init(agentFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentsV entity = new AgentsV();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            ArrayList<AgentV> listOfAgents = new ArrayList<AgentV>();

            // TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden

            for (AgentUrl agentFilter : agentFilterSchema.getAgents()) {
                AgentV agent = new AgentV();
                agent.setRunningTasks(-1);
                agent.setStartedAt(new Date());
                JobSchedulerState state = new JobSchedulerState();
                state.setSeverity(0);
                state.set_text(JobSchedulerStateText.PAUSED);
                agent.setState(state);
                agent.setUrl(agentFilter.getAgent());
                agent.setSurveyDate(new Date());
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
