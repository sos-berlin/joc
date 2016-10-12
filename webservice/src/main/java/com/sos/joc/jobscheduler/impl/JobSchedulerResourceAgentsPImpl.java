package com.sos.joc.jobscheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryAgentCluster;
import com.sos.jitl.reporting.db.DBItemInventoryAgentClusterMember;
import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.InventoryAgentsDBLayer;
import com.sos.joc.db.inventory.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.JocException;
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
            JOCDefaultResponse jocDefaultResponse = init(agentFilterSchema.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AgentsPSchema entity = new AgentsPSchema();
            entity.setDeliveryDate(new Date());
            List<AgentPSchema> listOfAgents = new ArrayList<AgentPSchema>();
            InventoryAgentsDBLayer agentLayer = new InventoryAgentsDBLayer(Globals.sosHibernateConnection);
            if(!agentFilterSchema.getAgents().isEmpty()) {
                for (Agent_ agentFilter : agentFilterSchema.getAgents()) {
                    DBItemInventoryAgentInstance agentInstance = agentLayer.getInventoryAgentInstances(agentFilter.getAgent());
                    listOfAgents.add(processAgent(agentLayer, agentInstance));
                }
            } else {
                List<DBItemInventoryAgentInstance> agentInstances = agentLayer.getInventoryAgentInstances();
                for(DBItemInventoryAgentInstance agentInstance : agentInstances){
                    AgentPSchema agentSchema = processAgent(agentLayer, agentInstance);
                    if(agentSchema != null) {
                        listOfAgents.add(agentSchema);
                    }
                }
            }
            entity.setAgents(listOfAgents);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private AgentPSchema processAgent(InventoryAgentsDBLayer agentLayer, DBItemInventoryAgentInstance agentInstance) throws Exception {
        AgentPSchema agent = new AgentPSchema();
        if(agentInstance.getStartedAt() != null) {
            agent.setStartedAt(agentInstance.getStartedAt());
        }
        State state = new State();
        switch(agentInstance.getState()) {
        case 0:
            state.setSeverity(0);
            state.setText(Text.RUNNING);
            break;
        case 1:
            state.setSeverity(2);
            state.setText(Text.UNREACHABLE);
            break;
        }
        agent.setState(state);
        agent.setSurveyDate(new Date());
        agent.setHost(agentInstance.getHostname());
        agent.setUrl(agentInstance.getUrl());
        agent.setVersion(agentInstance.getVersion());
        InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryOperatingSystem osInstance = osLayer.getInventoryOperatingSystem(agentInstance.getOsId());
        if(osInstance != null) {
            Os os = new Os();
            os.setArchitecture(osInstance.getArchitecture());
            os.setDistribution(osInstance.getDistribution());
            os.setName(osInstance.getName());
            agent.setOs(os);
        }
        List<String> listOfClusters = new ArrayList<String>();
        List<String> listFromDb = agentLayer.getProcessClassesFromAgentCluster(agentInstance.getId());
        if(listFromDb != null && !listFromDb.isEmpty()) {
            listOfClusters.addAll(listFromDb);
        }
        if(!listOfClusters.isEmpty()) {
            agent.setClusters(listOfClusters);
        }
        return agent;
    }
    
}