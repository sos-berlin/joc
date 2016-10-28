package com.sos.joc.jobscheduler.impl;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobscheduler.AgentClusterVCallable;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusterPath;
import com.sos.joc.model.jobscheduler.AgentClusterV;
import com.sos.joc.model.jobscheduler.AgentClustersV;

@Path("jobscheduler")
public class JobSchedulerResourceAgentClustersImpl extends JOCResourceImpl implements IJobSchedulerResourceAgentClusters {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceAgentClustersImpl.class);
    private static final String API_CALL = "./jobscheduler/agent_clusters";

    @Override
    public JOCDefaultResponse postJobschedulerAgentClusters(String accessToken, AgentClusterFilter jobSchedulerAgentClustersBody) {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerAgentClustersBody.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerUniversalAgent().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand();
            command.setUriBuilderForProcessClasses(dbItemInventoryInstance.getUrl());
            // always false otherwise no agent info
            // command.addProcessClassCompactQuery(jobSchedulerAgentClustersBody.getCompact());
            command.addProcessClassCompactQuery(false);
            URI uri = command.getURI();

            AgentClustersV entity = new AgentClustersV();
            List<AgentClusterV> listAgentClusters = new ArrayList<AgentClusterV>();
            List<AgentClusterVCallable> tasks = new ArrayList<AgentClusterVCallable>();
            Set<AgentClusterPath> agentClusters = new HashSet<AgentClusterPath>(jobSchedulerAgentClustersBody.getAgentClusters());

            if (!agentClusters.isEmpty()) {
                for (AgentClusterPath agentCluster : agentClusters) {
                    checkRequiredParameter("agentCluster", agentCluster.getAgentCluster());
                    tasks.add(new AgentClusterVCallable(agentCluster.getAgentCluster(), jobSchedulerAgentClustersBody, uri, dbItemInventoryInstance
                            .getUrl()));
                }
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                for (Future<AgentClusterV> result : executorService.invokeAll(tasks)) {
                    try {
                        listAgentClusters.add(result.get());
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
                entity.setAgentClusters(callable.getAgentCluster());
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
}
