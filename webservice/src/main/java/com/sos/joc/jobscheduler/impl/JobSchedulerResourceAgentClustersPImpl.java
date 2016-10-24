package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryAgentCluster;
import com.sos.jitl.reporting.db.DBItemInventoryAgentInstance;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.agents.InventoryAgentsDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.os.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP;
import com.sos.joc.model.jobscheduler.AgentClusterPath;
import com.sos.joc.model.jobscheduler.AgentClusterState;
import com.sos.joc.model.jobscheduler.AgentClusterStateText;
import com.sos.joc.model.jobscheduler.AgentClusterType;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterP;
import com.sos.joc.model.jobscheduler.AgentClustersP;
import com.sos.joc.model.jobscheduler.AgentOfCluster;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.NumOfAgentsInCluster;
import com.sos.joc.model.jobscheduler.OperatingSystem;;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersPImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClustersP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersPImpl.class);
    private Boolean compact;
    private List<AgentClusterPath> agentClusters;
    private Integer state;
    private String regex;
    private Long instanceId;

    @Override
    public JOCDefaultResponse postJobschedulerAgentClustersP(String accessToken, AgentClusterFilter jobSchedulerAgentClustersBody) {
        LOGGER.debug("init jobscheduler/agent/clusters/P");
        try {
            JOCDefaultResponse jocDefaultResponse =
                    init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView()
                            .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            AgentClustersP entity = new AgentClustersP();
            entity.setDeliveryDate(Date.from(Instant.now()));
            ArrayList<AgentClusterP> listOfAgentClusters = new ArrayList<AgentClusterP>();
            /** FILTERS:
             * compact
             * agentClusters (array of processClasses)
             * state
             * regex
             */
            compact = jobSchedulerAgentClustersBody.getCompact();
            agentClusters = jobSchedulerAgentClustersBody.getAgentClusters();
            state = jobSchedulerAgentClustersBody.getState();
            regex = jobSchedulerAgentClustersBody.getRegex();
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(jobSchedulerAgentClustersBody.getJobschedulerId());
            instanceId = instance.getId();
            InventoryAgentsDBLayer agentLayer = new InventoryAgentsDBLayer(Globals.sosHibernateConnection);
            AgentClusterP agentClusterPSchema = new AgentClusterP();
            if(agentClusters != null && !agentClusters.isEmpty()) {
                for (AgentClusterPath agentCluster : agentClusters) {
                    agentClusterPSchema = processAgentClusterByClusterName(agentLayer, agentCluster.getAgentCluster());
                    listOfAgentClusters.add(agentClusterPSchema);
                }
            } else if (state != null && regex != null) {
                List<AgentClusterP> agentClusterPSchemas = processAgentClusterByRegexAndState(agentLayer, regex, state);
                if(agentClusterPSchemas != null) {
                    listOfAgentClusters.addAll(agentClusterPSchemas);
                }
            } else if (state != null) {
                List<AgentClusterP> agentClusterPSchemas = processAgentClusterByState(agentLayer, state);
                if (agentClusterPSchemas != null) {
                    listOfAgentClusters.addAll(agentClusterPSchemas);
                }
            } else if (regex != null) {
                List<AgentClusterP> agentClusterPSchemas = processAgentClusterByRegex(agentLayer, regex);
                if(agentClusterPSchemas != null) {
                    listOfAgentClusters.addAll(agentClusterPSchemas);
                }
            } else {
                List<DBItemInventoryAgentCluster> agentClusters = agentLayer.getAgentClusters(instanceId);
                if(agentClusters != null) {
                    for(DBItemInventoryAgentCluster agentCluster : agentClusters) {
                        DBItemInventoryProcessClass processClass = 
                                agentLayer.getInventoryProcessClassById(agentCluster.getProcessClassId(), instanceId);
                        agentClusterPSchema = processAgentCluster(agentLayer, processClass, agentCluster);
                        if (agentClusterPSchema != null) {
                            listOfAgentClusters.add(agentClusterPSchema);
                        }
                    }
                }
            }
            entity.setAgentClusters(listOfAgentClusters);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private AgentClusterP processAgentClusterByClusterName(InventoryAgentsDBLayer agentLayer, String agentClusterName) throws Exception {
        DBItemInventoryProcessClass processClass = agentLayer.getInventoryClusterProcessClass(agentClusterName, instanceId);
        if (processClass != null) {
            DBItemInventoryAgentCluster agentCluster = agentLayer.getInventoryClusterByProcessClassId(processClass.getId(), instanceId);
            return processAgentCluster(agentLayer, processClass, agentCluster);
        }
        return null;
    }

    private List<AgentClusterP> processAgentClusterByRegexAndState(InventoryAgentsDBLayer agentLayer, String regex, Integer state)
            throws Exception {
        List<AgentClusterP> schemas = new ArrayList<AgentClusterP>();
        List<DBItemInventoryProcessClass> processClasses = agentLayer.getInventoryProcessClasses(instanceId);
        if (processClasses != null) {
            for (DBItemInventoryProcessClass processClass : processClasses) {
                DBItemInventoryAgentCluster agentCluster = agentLayer.getInventoryClusterByProcessClassId(processClass.getId(), instanceId);
                if (agentCluster != null) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(processClass.getName());
                    if (regExMatcher.find()) {
                        AgentClusterP agentClusterPSchema = processAgentCluster(agentLayer, processClass, agentCluster);
                        if(state != null && state == agentClusterPSchema.getState().getSeverity()) {
                            schemas.add(agentClusterPSchema);
                        } else if (state == null) {
                            schemas.add(agentClusterPSchema);
                        }
                    }
                }
            }
        }
        return schemas.isEmpty() ? null : schemas;
    }

    private List<AgentClusterP> processAgentClusterByState(InventoryAgentsDBLayer agentLayer, Integer state) throws Exception {
        List<AgentClusterP> schemas = new ArrayList<AgentClusterP>();
        List<DBItemInventoryProcessClass> processClasses = agentLayer.getInventoryProcessClassByState(state, instanceId);
        if(processClasses != null) {
            for (DBItemInventoryProcessClass processClass : processClasses) {
                DBItemInventoryAgentCluster agentCluster = agentLayer.getInventoryClusterByProcessClassId(processClass.getId(), instanceId);
                schemas.add(processAgentCluster(agentLayer, processClass, agentCluster));
            }
        }
        return schemas.isEmpty() ? null : schemas;
    }

    private List<AgentClusterP> processAgentClusterByRegex(InventoryAgentsDBLayer agentLayer, String regex) throws Exception {
        return processAgentClusterByRegexAndState(agentLayer, regex, null);
    }

    private AgentClusterP processAgentCluster(InventoryAgentsDBLayer agentLayer, DBItemInventoryProcessClass processClass,
            DBItemInventoryAgentCluster agentCluster) throws Exception {
        AgentClusterP agentClusterPSchema = new AgentClusterP();
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
                agentClusterPSchema.set_type(AgentClusterType.FIX_PRIORITY);
                break;
            case "next":
                agentClusterPSchema.set_type(AgentClusterType.ROUND_ROBIN);
                break;
            case "single":
                agentClusterPSchema.set_type(AgentClusterType.SINGLE_AGENT);
                break;
            }
        }
        NumOfAgentsInCluster numOfAgents = new NumOfAgentsInCluster();
        numOfAgents.setAny(agentCluster.getNumberOfAgents());
        List<DBItemInventoryAgentInstance> agents = agentLayer.getInventoryAgentInstancesByClusterId(agentCluster.getId(), instanceId);
        int countRunning = 0;
        for (DBItemInventoryAgentInstance agent : agents) {
            if (agent.getState() == 0) {
                countRunning++;
            }
        }
        numOfAgents.setRunning(countRunning);
        agentClusterPSchema.setNumOfAgents(numOfAgents);
        AgentClusterState state = new AgentClusterState();
        int diff = numOfAgents.getAny() - numOfAgents.getRunning();
        if(diff == 0) {
            state.setSeverity(0);
            state.set_text(AgentClusterStateText.ALL_AGENTS_ARE_RUNNING);
        } else if (diff == numOfAgents.getAny()) {
            state.setSeverity(2);
            state.set_text(AgentClusterStateText.ALL_AGENTS_ARE_RUNNING);
        } else {
            state.setSeverity(1);
            state.set_text(AgentClusterStateText.ONLY_SOME_AGENTS_ARE_RUNNING);
        }
        agentClusterPSchema.setState(state);
        if(compact == null || !compact) {
            ArrayList<AgentOfCluster> listOfAgents = new ArrayList<AgentOfCluster>();
            for(DBItemInventoryAgentInstance agentFromDb : agents) {
                AgentOfCluster agent = new AgentOfCluster();
                agent.setHost(agentFromDb.getHostname());
                InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
                DBItemInventoryOperatingSystem osFromDb = osLayer.getInventoryOperatingSystem(agentFromDb.getOsId());
                if(osFromDb != null) {
                    OperatingSystem os = new OperatingSystem();
                    os.setArchitecture(osFromDb.getArchitecture());
                    os.setDistribution(osFromDb.getDistribution());
                    os.setName(osFromDb.getName());
                    agent.setOs(os);
                }
                agent.setStartedAt(agentFromDb.getStartedAt());
                JobSchedulerState outputState = new JobSchedulerState();
                switch(agentFromDb.getState()) {
                case 0:
                    outputState.setSeverity(0);
                    outputState.set_text(JobSchedulerStateText.RUNNING);
                    break;
                case 1:
                    outputState.setSeverity(2);
                    outputState.set_text(JobSchedulerStateText.UNREACHABLE);
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