package com.sos.joc.jobscheduler.impl;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgent;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentsBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgents;
import com.sos.joc.model.jobscheduler.AgentVSchema;
import com.sos.joc.model.jobscheduler.AgentsVSchema;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsImpl extends JOCResourceImpl implements IJobSchedulerResourceAgents {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResourceAgentsImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgents(String accessToken, JobSchedulerAgentsBody jobSchedulerAgentsBody) {
        LOGGER.debug("init JobschedulerAgents");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentsBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentsVSchema entity = new AgentsVSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            ArrayList<AgentVSchema> listOfAgents = new ArrayList<AgentVSchema>();

            // TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden

            for (JobSchedulerAgent agentFilter : jobSchedulerAgentsBody.getAgents()) {
                AgentVSchema agent = new AgentVSchema();
                agent.setRunningTasks(-1);
                agent.setStartedAt(new Date());
                State state = new State();
                state.setSeverity(0);
                state.setText(Text.paused);
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
