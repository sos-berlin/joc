package com.sos.joc.jobchain.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JOCXmlJobChainCommand;
import com.sos.joc.classes.jobchains.JobChainVolatileJson;
import com.sos.joc.classes.jobchains.JobChainsVCallable;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchain.resource.IJobChainResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.jobChain.JobChainV200;

@Path("job_chain")
public class JobChainResourceImpl extends JOCResourceImpl implements IJobChainResource {

    private static final String API_CALL = "./job_chain";

    @Override
    public JOCDefaultResponse postJobChain(String xAccessToken, String accessToken, JobChainFilter jobChainFilter) throws Exception {
        return postJobChain(getAccessToken(xAccessToken, accessToken), jobChainFilter);
    }

    public JOCDefaultResponse postJobChain(String accessToken, JobChainFilter jobChainFilter) throws Exception {
        SOSHibernateSession session = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainFilter, accessToken, jobChainFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobChainFilter.getJobschedulerId(), accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JobChainV200 entity = new JobChainV200();
            checkRequiredParameter("jobChain", jobChainFilter.getJobChain());

            boolean getJobChainFromXMLCommand = Globals.rollbackJobChainWithJSON;
            if (!getJobChainFromXMLCommand) {
                getJobChainFromXMLCommand = versionIsOlderThan("1.12.10");
            }

            String jobChainPath = normalizePath(jobChainFilter.getJobChain());

            if (getJobChainFromXMLCommand) {
                JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(this, accessToken);

                if (jobChainFilter.getCompact() != Boolean.TRUE) {
                    session = Globals.createSosHibernateStatelessConnection(API_CALL);
                    DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(session);
                    jocXmlCommand.setOrderDocumentations(dbDocLayer.getDocumentationPaths(jobChainFilter.getJobschedulerId(),
                            JobSchedulerObjectType.ORDER));
                    jocXmlCommand.setJobDocumentations(dbDocLayer.getDocumentationPaths(jobChainFilter.getJobschedulerId(),
                            JobSchedulerObjectType.JOB));
                    jocXmlCommand.setJobChainDocumentations(dbDocLayer.getDocumentationPaths(jobChainFilter.getJobschedulerId(),
                            JobSchedulerObjectType.JOBCHAIN));
                }
                
                entity.setJobChain(jocXmlCommand.getJobChain(jobChainPath, jobChainFilter.getCompact(), jobChainFilter.getCompactView(), jobChainFilter
                        .getMaxOrders()));
                entity.setNestedJobChains(jocXmlCommand.getNestedJobChains(jobChainFilter.getCompactView()));

            } else {
                Map<String, String> orderDocs = null;
                Map<String, String> jobDocs = null;
                Map<String, String> jobChainDocs = null;
                if (jobChainFilter.getCompact() != Boolean.TRUE) {
                    session = Globals.createSosHibernateStatelessConnection(API_CALL);
                    DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(session);
                    orderDocs = dbDocLayer.getDocumentationPaths(jobChainFilter.getJobschedulerId(), JobSchedulerObjectType.ORDER);
                    jobDocs = dbDocLayer.getDocumentationPaths(jobChainFilter.getJobschedulerId(), JobSchedulerObjectType.JOB);
                    jobChainDocs = dbDocLayer.getDocumentationPaths(jobChainFilter.getJobschedulerId(), JobSchedulerObjectType.JOBCHAIN);
                }
                
                jobChainFilter.setJobChain(jobChainPath);
                JobChainVolatileJson jobChain = new JobChainsVCallable(jobChainFilter, new JOCJsonCommand(this), accessToken, jobDocs, jobChainDocs,
                        orderDocs).getJobChain();
                entity.setJobChain(jobChain);

                Set<String> nestedJobChains = jobChain.getNestedJobChains();
                if (nestedJobChains == null || nestedJobChains.isEmpty()) {
                    entity.setNestedJobChains(null);
                } else {
                    List<JobChainsVCallable> tasks = new ArrayList<JobChainsVCallable>();
                    for (String nestedJobChain : nestedJobChains) {
                        jobChainFilter.setJobChain(nestedJobChain);
                        tasks.add(new JobChainsVCallable(jobChainFilter, new JOCJsonCommand(this), accessToken, jobDocs, jobChainDocs, orderDocs));
                    }
                    Map<String, JobChainVolatileJson> listNestedJobChains = new HashMap<String, JobChainVolatileJson>();
                    if (!tasks.isEmpty()) {
                        if (tasks.size() == 1) {
                            listNestedJobChains = tasks.get(0).call(); 
                        }
                        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                        try {
                            for (Future<Map<String, JobChainVolatileJson>> result : executorService.invokeAll(tasks)) {
                                try {
                                    listNestedJobChains.putAll(result.get());
                                } catch (ExecutionException e) {
                                    if (e.getCause() instanceof JocException) {
                                        throw (JocException) e.getCause();
                                    } else {
                                        throw (Exception) e.getCause();
                                    }
                                }
                            }
                        } finally {
                            executorService.shutdown();
                        }
                    }
                    entity.setNestedJobChains(new ArrayList<JobChainV>(listNestedJobChains.values()));
                }
            }

            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
        }
    }

}