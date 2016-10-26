package com.sos.joc.jobscheduler.impl;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.jobscheduler.AgentClusterVCallable;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterPath;
import com.sos.joc.model.jobscheduler.AgentClusterV;
import com.sos.joc.model.jobscheduler.AgentClustersV;
import com.sos.joc.model.processClass.Process;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClusters {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersImpl.class);
    private static final String API_CALL = "./jobscheduler/agent_clusters";
    
    @Override
    public JOCDefaultResponse postJobschedulerAgentClusters(String accessToken, AgentClusterFilter jobSchedulerAgentClustersBody) {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerAgentClustersBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            JOCJsonCommand command = new JOCJsonCommand();
            command.setUriBuilderForProcessClasses(dbItemInventoryInstance.getUrl());
            //always false otherwise no agent info command.addProcessClassCompactQuery(jobSchedulerAgentClustersBody.getCompact());
            command.addProcessClassCompactQuery(false);
            URI uri = command.getURI();
            
            AgentClustersV entity = new AgentClustersV();
            List<AgentClusterV> listAgentClusters = new ArrayList<AgentClusterV>();
            List<AgentClusterVCallable> tasks = new ArrayList<AgentClusterVCallable>();
            Set<AgentClusterPath> agentClusters = new HashSet<AgentClusterPath>(jobSchedulerAgentClustersBody.getAgentClusters());
            
            if (!agentClusters.isEmpty()) {
                for (AgentClusterPath agentCluster : agentClusters) {
                    checkRequiredParameter("agentCluster", agentCluster.getAgentCluster());
                    tasks.add(new AgentClusterVCallable(agentCluster.getAgentCluster(), jobSchedulerAgentClustersBody, uri, dbItemInventoryInstance.getUrl()));
                }
                Map<String,List<Process>> mapOfProcesses = new HashMap<String, List<Process>>();
                if (!jobSchedulerAgentClustersBody.getCompact()) {
                    mapOfProcesses = getMapOfProcesses();
                }
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                for (Future<AgentClusterV> result : executorService.invokeAll(tasks)) {
                    try {
                        AgentClusterV a = result.get();
                        if (!jobSchedulerAgentClustersBody.getCompact()) {
                            List<Process> processes = mapOfProcesses.get(a.getPath());
                            if (processes != null) {
                                a.setProcesses(processes);
                                a.setNumOfProcesses(processes.size());
                            }
                        }
                        listAgentClusters.add(a);
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof JocException) {
                            throw (JocException) e.getCause();
                        } else {
                            throw (Exception) e.getCause();
                        }
                    }
                }
                entity.setAgentClusters(listAgentClusters);
                
            } else {
                AgentClusterVCallable callable = new AgentClusterVCallable(jobSchedulerAgentClustersBody, uri, dbItemInventoryInstance.getUrl());
                List<AgentClusterV> AgentClusterVSet = callable.getAgentCluster();
                if (!jobSchedulerAgentClustersBody.getCompact()) {
                    Map<String, List<Process>> mapOfProcesses = getMapOfProcesses();
                    for (AgentClusterV a : AgentClusterVSet) {
                        List<Process> processes = mapOfProcesses.get(a.getPath());
                        if (processes != null) {
                            a.setProcesses(processes);
                            a.setNumOfProcesses(processes.size());
                        }
                    }
                }
                entity.setAgentClusters(AgentClusterVSet);
            }
            
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobSchedulerAgentClustersBody));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobSchedulerAgentClustersBody));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }
    
    private Map<String, List<Process>> getMapOfProcesses() throws Exception {
        Map<String, List<Process>> mapOfProcesses = new HashMap<String, List<Process>>();
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
        JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
        jsCmdShowState.setSubsystems("folder process_class");
        jsCmdShowState.setWhat("folders");
        String xml = jsCmdShowState.toXMLString();
        jocXmlCommand.executePostWithThrowBadRequest(xml);
        NodeList processClasses = jocXmlCommand.getSosxml().selectNodeList("//process_classes/process_class");
        for (int i = 0; i < processClasses.getLength(); i++) {
            Element processClass = (Element) processClasses.item(i);
            List<Process> listOfProcesses = new ArrayList<Process>();
            NodeList processes = jocXmlCommand.getSosxml().selectNodeList(processClass, "processes/process");
            for (int j = 0; j < processes.getLength(); j++) {
                Element processElem = (Element) processes.item(j);
                Process process = new Process();
                process.setJob(jocXmlCommand.getAttributeValue(processElem, "job", null));
                process.setPid(Integer.parseInt(jocXmlCommand.getAttributeValue(processElem, "pid", "0")));
                process.setRunningSince(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(processElem, "running_since",
                        null)));
                process.setTaskId(jocXmlCommand.getAttributeValue(processElem, "task_id", null));
                process.setAgent(jocXmlCommand.getAttributeValue(processElem, "remote_scheduler", null));
                listOfProcesses.add(process);
            }
            mapOfProcesses.put(processClass.getAttribute("path"), listOfProcesses);
        }
        return mapOfProcesses;
    }
}
