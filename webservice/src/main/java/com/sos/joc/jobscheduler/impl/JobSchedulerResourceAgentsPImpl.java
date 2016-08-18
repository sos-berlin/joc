package com.sos.joc.jobscheduler.impl;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgent;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentsBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentsP;
import com.sos.joc.model.jobscheduler.AgentPSchema;
import com.sos.joc.model.jobscheduler.AgentsPSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.response.JOCDefaultResponse;
import com.sos.joc.response.JOCCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentsP {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerAgentsP(String accessToken, JobSchedulerAgentsBody jobSchedulerAgentsBody) {
        LOGGER.debug("init JobschedulerAgentsP");
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerAgentsBody.getJobschedulerId());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            AgentsPSchema entity = new AgentsPSchema();

            // TODO JOC Cockpit Webservice

            entity.setDeliveryDate(new Date());

            // TODO Hier muss die DB gelesen und mit dem Filter gefiltert werden
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

            return JOCDefaultResponse.responseStatus420(JOCCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
