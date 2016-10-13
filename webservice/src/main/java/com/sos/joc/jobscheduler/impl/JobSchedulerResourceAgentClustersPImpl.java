package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryAgentCluster;
import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.InventoryAgentsDBLayer;
import com.sos.joc.db.inventory.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP;
import com.sos.joc.model.jobscheduler.Agent;
import com.sos.joc.model.jobscheduler.AgentCluster;
import com.sos.joc.model.jobscheduler.AgentClusterFilterSchema;
import com.sos.joc.model.jobscheduler.AgentClusterPSchema;
import com.sos.joc.model.jobscheduler.AgentClustersPSchema;
import com.sos.joc.model.jobscheduler.NumOfAgents;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.model.jobscheduler.State_;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClustersP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersPImpl.class);
    private Boolean compact;
    private List<AgentCluster> agentClusters;
    private Integer state;
    private String regex;

    @Override
    public JOCDefaultResponse postJobschedulerAgentClustersP(String accessToken, AgentClusterFilterSchema jobSchedulerAgentClustersBody) {
        LOGGER.debug("init jobscheduler/agent/clusters/P");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AgentClustersPSchema entity = new AgentClustersPSchema();
            entity.setDeliveryDate(new Date());
            ArrayList<AgentClusterPSchema> listOfAgentClusters = new ArrayList<AgentClusterPSchema>();
            /** FILTERS:
             * compact
             * agentClusters (array of processClasses)
             * state
             * regex
             */
            // filters
            compact = jobSchedulerAgentClustersBody.getCompact();
            agentClusters = jobSchedulerAgentClustersBody.getAgentClusters();
            state = jobSchedulerAgentClustersBody.getState();
            regex = jobSchedulerAgentClustersBody.getRegex();

            InventoryAgentsDBLayer agentLayer = new InventoryAgentsDBLayer(Globals.sosHibernateConnection);
            AgentClusterPSchema agentClusterPSchema = new AgentClusterPSchema();
            if(agentClusters != null && !agentClusters.isEmpty()) {
                for (AgentCluster agentCluster : agentClusters) {
                    agentClusterPSchema = processAgentClusterByClusterName(agentLayer, agentCluster.getAgentCluster());
                    listOfAgentClusters.add(agentClusterPSchema);
                }
            } else if (state != null && regex != null) {
                List<AgentClusterPSchema> agentClusterPSchemas = processAgentClusterByRegexAndState(agentLayer, regex, state);
                if(agentClusterPSchemas != null) {
                    listOfAgentClusters.addAll(agentClusterPSchemas);
                }
            } else if (state != null) {
                List<AgentClusterPSchema> agentClusterPSchemas = processAgentClusterByState(agentLayer, state);
                if (agentClusterPSchemas != null) {
                    listOfAgentClusters.addAll(agentClusterPSchemas);
                }
            } else if (regex != null) {
                List<AgentClusterPSchema> agentClusterPSchemas = processAgentClusterByRegex(agentLayer, regex);
                if(agentClusterPSchemas != null) {
                    listOfAgentClusters.addAll(agentClusterPSchemas);
                }
            } else {
                List<DBItemInventoryAgentCluster> agentClusters = agentLayer.getAgentClusters();
                if(agentClusters != null) {
                    for(DBItemInventoryAgentCluster agentCluster : agentClusters) {
                        DBItemInventoryProcessClass processClass = agentLayer.getInventoryProcessClassById(agentCluster.getProcessClassId());
                        agentClusterPSchema = processAgentCluster(agentLayer, processClass, agentCluster);
                        if (agentClusterPSchema != null) {
                            listOfAgentClusters.add(agentClusterPSchema);
                        }
                    }
                }
            }
            entity.setAgentClusters(listOfAgentClusters);

            // TODO get a list of agents and set the data.

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

    private AgentClusterPSchema processAgentClusterByClusterName(InventoryAgentsDBLayer agentLayer, String agentClusterName) throws Exception {
        DBItemInventoryProcessClass processClass = agentLayer.getInventoryClusterProcessClass(agentClusterName);
        if (processClass != null) {
            DBItemInventoryAgentCluster agentCluster = agentLayer.getInventoryClusterByProcessClassId(processClass.getId());
            return processAgentCluster(agentLayer, processClass, agentCluster);
        }
        return null;
    }

    private List<AgentClusterPSchema> processAgentClusterByRegexAndState(InventoryAgentsDBLayer agentLayer, String regex, Integer state)
            throws Exception {
        List<AgentClusterPSchema> schemas = new ArrayList<AgentClusterPSchema>();
        List<DBItemInventoryProcessClass> processClasses = agentLayer.getInventoryProcessClassByRegex(regex);
        if (processClasses != null) {
            for (DBItemInventoryProcessClass processClass : processClasses) {
                DBItemInventoryAgentCluster agentCluster = agentLayer.getInventoryClusterByProcessClassId(processClass.getId());
                if (agentCluster != null) {
                    AgentClusterPSchema agentClusterPSchema = processAgentCluster(agentLayer, processClass, agentCluster);
                    if(state != null && state == agentClusterPSchema.getState().getSeverity()) {
                        schemas.add(agentClusterPSchema);
                    } else if (state == null) {
                        schemas.add(agentClusterPSchema);
                    }
                }
            }
        }
        return schemas.isEmpty() ? null : schemas;
    }

    private List<AgentClusterPSchema> processAgentClusterByState(InventoryAgentsDBLayer agentLayer, Integer state) throws Exception {
        List<AgentClusterPSchema> schemas = new ArrayList<AgentClusterPSchema>();
        List<DBItemInventoryProcessClass> processClasses = agentLayer.getInventoryProcessClassByState(state);
        if(processClasses != null) {
            for (DBItemInventoryProcessClass processClass : processClasses) {
                DBItemInventoryAgentCluster agentCluster = agentLayer.getInventoryClusterByProcessClassId(processClass.getId());
                schemas.add(processAgentCluster(agentLayer, processClass, agentCluster));
            }
        }
        return schemas.isEmpty() ? null : schemas;
    }

    private List<AgentClusterPSchema> processAgentClusterByRegex(InventoryAgentsDBLayer agentLayer, String regex) throws Exception {
        return processAgentClusterByRegexAndState(agentLayer, regex, null);
    }

    private AgentClusterPSchema processAgentCluster(InventoryAgentsDBLayer agentLayer, DBItemInventoryProcessClass processClass,
            DBItemInventoryAgentCluster agentCluster) throws Exception {
        AgentClusterPSchema agentClusterPSchema = new AgentClusterPSchema();
        agentClusterPSchema.setSurveyDate(new Date());
        agentClusterPSchema.setMaxProcesses(processClass.getMaxProcesses());
        agentClusterPSchema.setName(processClass.getBasename());
        String path = agentLayer.getPathForProcessClass(processClass.getFileId());
        if(path != null) {
            agentClusterPSchema.setPath(path);
        }
        if(agentCluster != null) {
            switch(agentCluster.getSchedulingType()) {
            case "first":
                agentClusterPSchema.setType(AgentClusterPSchema.Type.FIX_PRIORITY);
                break;
            case "next":
                agentClusterPSchema.setType(AgentClusterPSchema.Type.ROUND_ROBIN);
                break;
            case "single":
                agentClusterPSchema.setType(AgentClusterPSchema.Type.SINGLE_AGENT);
                break;
            }
        }
        NumOfAgents numOfAgents = new NumOfAgents();
        numOfAgents.setAny(agentCluster.getNumberOfAgents());
        List<DBItemInventoryAgentInstance> agents = agentLayer.getInventoryAgentInstancesByClusterId(agentCluster.getId());
        int countRunning = 0;
        for (DBItemInventoryAgentInstance agent : agents) {
            if (agent.getState() == 0) {
                countRunning++;
            }
        }
        numOfAgents.setRunning(countRunning);
        agentClusterPSchema.setNumOfAgents(numOfAgents);
        State_ state = new State_();
        int diff = numOfAgents.getAny() - numOfAgents.getRunning();
        if(diff == 0) {
            state.setSeverity(0);
            state.setText(State_.Text.ALL_AGENTS_ARE_RUNNING);
        } else if (diff == numOfAgents.getAny()) {
            state.setSeverity(2);
            state.setText(State_.Text.ALL_AGENTS_ARE_RUNNING);
        } else {
            state.setSeverity(1);
            state.setText(State_.Text.ONLY_SOME_AGENTS_ARE_RUNNING);
        }
        agentClusterPSchema.setState(state);
        if(compact == null || !compact) {
            ArrayList<Agent> listOfAgents = new ArrayList<Agent>();
            for(DBItemInventoryAgentInstance agentFromDb : agents) {
                Agent agent = new Agent();
                agent.setHost(agentFromDb.getHostname());
                InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
                DBItemInventoryOperatingSystem osFromDb = osLayer.getInventoryOperatingSystem(agentFromDb.getOsId());
                if(osFromDb != null) {
                    Os os = new Os();
                    os.setArchitecture(osFromDb.getArchitecture());
                    os.setDistribution(osFromDb.getDistribution());
                    os.setName(osFromDb.getName());
                    agent.setOs(os);
                }
                agent.setStartedAt(agentFromDb.getStartedAt());
                State outputState = new State();
                switch(agentFromDb.getState()) {
                case 0:
                    outputState.setSeverity(0);
                    outputState.setText(Text.RUNNING);
                    break;
                case 1:
                    outputState.setSeverity(2);
                    outputState.setText(Text.UNREACHABLE);
                    break;
                }
                agent.setState(outputState);
                agent.setSurveyDate(new Date());
                agent.setUrl(agentFromDb.getUrl());
                agent.setVersion(agentFromDb.getVersion());
                listOfAgents.add(agent);
            }
            agentClusterPSchema.setAgents(listOfAgents);
        }
        return agentClusterPSchema;
    }
    
}