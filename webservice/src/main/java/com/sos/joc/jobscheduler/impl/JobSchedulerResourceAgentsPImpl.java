package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.agents.InventoryAgentsDBLayer;
import com.sos.joc.db.inventory.os.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentsP;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentP;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentsP;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.OperatingSystem;

@Path("jobscheduler")
public class JobSchedulerResourceAgentsPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentsP {

    private static final String API_CALL = "./jobscheduler/agents/p";

    @Override
    public JOCDefaultResponse postJobschedulerAgentsP(String accessToken, AgentFilter agentFilter) {
        try {
            initLogging(API_CALL, agentFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, agentFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken)
                    .getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AgentsP entity = new AgentsP();
            List<AgentP> listOfAgents = new ArrayList<AgentP>();
            Long instanceId = dbItemInventoryInstance.getId();
            InventoryAgentsDBLayer agentLayer = new InventoryAgentsDBLayer(Globals.sosHibernateConnection);
            if (!agentFilter.getAgents().isEmpty()) {
                for (AgentUrl agentUrl : agentFilter.getAgents()) {
                    DBItemInventoryAgentInstance agentInstance = agentLayer.getInventoryAgentInstances(agentUrl.getAgent(), instanceId);
                    listOfAgents.add(processAgent(agentLayer, agentInstance));
                }
            } else {
                List<DBItemInventoryAgentInstance> agentInstances = agentLayer.getInventoryAgentInstances(instanceId);
                for (DBItemInventoryAgentInstance agentInstance : agentInstances) {
                    AgentP agentSchema = processAgent(agentLayer, agentInstance);
                    if (agentSchema != null) {
                        listOfAgents.add(agentSchema);
                    }
                }
            }
            entity.setAgents(listOfAgents);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private AgentP processAgent(InventoryAgentsDBLayer agentLayer, DBItemInventoryAgentInstance agentInstance) throws Exception {
        AgentP agent = new AgentP();
        if (agentInstance.getStartedAt() != null) {
            agent.setStartedAt(agentInstance.getStartedAt());
        }
        JobSchedulerState state = new JobSchedulerState();
        switch (agentInstance.getState()) {
        case 0:
            state.setSeverity(0);
            state.set_text(JobSchedulerStateText.RUNNING);
            break;
        case 1:
            state.setSeverity(2);
            state.set_text(JobSchedulerStateText.UNREACHABLE);
            break;
        }
        agent.setState(state);
        agent.setSurveyDate(new Date());
        agent.setHost(agentInstance.getHostname());
        agent.setUrl(agentInstance.getUrl());
        agent.setVersion(agentInstance.getVersion());
        InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryOperatingSystem osInstance = osLayer.getInventoryOperatingSystem(agentInstance.getOsId());
        if (osInstance != null) {
            OperatingSystem os = new OperatingSystem();
            os.setArchitecture(osInstance.getArchitecture());
            os.setDistribution(osInstance.getDistribution());
            os.setName(osInstance.getName());
            agent.setOs(os);
        }
        List<String> listOfClusters = new ArrayList<String>();
        List<String> listFromDb = agentLayer.getProcessClassesFromAgentCluster(agentInstance.getId(), dbItemInventoryInstance.getId());
        if (listFromDb != null && !listFromDb.isEmpty()) {
            listOfClusters.addAll(listFromDb);
        }
        if (!listOfClusters.isEmpty()) {
            agent.setClusters(listOfClusters);
        }
        return agent;
    }

}