package com.sos.joc.jobchains.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.SearchStringHelper;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JOCXmlJobChainCommand;
import com.sos.joc.classes.jobchains.JobChainVolatileJson;
import com.sos.joc.classes.jobchains.JobChainsVCallable;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.jobchains.resource.IJobChainsResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsV;
import com.sos.schema.JsonValidator;

@Path("job_chains")
public class JobChainsResourceImpl extends JOCResourceImpl implements IJobChainsResource {

    private static final String API_CALL = "./job_chains";

    @Override
    public JOCDefaultResponse postJobChains(String accessToken, byte[] jobChainsFilterBytes) {
        SOSHibernateSession session = null;
        try {
            JsonValidator.validateFailFast(jobChainsFilterBytes, JobChainsFilter.class);
            JobChainsFilter jobChainsFilter = Globals.objectMapper.readValue(jobChainsFilterBytes, JobChainsFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainsFilter, accessToken, jobChainsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(jobChainsFilter.getJobschedulerId(), accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobChainsV entity = new JobChainsV();

            if (jobChainsFilter.getJob() != null) {
                jobChainsFilter.getJob().setRegex(SearchStringHelper.getRegexValue(jobChainsFilter.getJob().getRegex()));
            }
            jobChainsFilter.setRegex(SearchStringHelper.getRegexValue(jobChainsFilter.getRegex()));

            boolean getJobChainFromXMLCommand = Globals.rollbackJobChainWithJSON;
            if (!getJobChainFromXMLCommand) {
                getJobChainFromXMLCommand = versionIsOlderThan("1.12.10");
            }

            List<JobChainPath> jobChains = jobChainsFilter.getJobChains();
            boolean withFolderFilter = jobChainsFilter.getFolders() != null && !jobChainsFilter.getFolders().isEmpty();
            Set<Folder> folders = addPermittedFolders(jobChainsFilter.getFolders());
            List<JobChainV> listOfJobChains = null;

            if (getJobChainFromXMLCommand) {
                JOCXmlJobChainCommand jocXmlCommand = new JOCXmlJobChainCommand(this, accessToken);
                if (jobChainsFilter.getCompact() != Boolean.TRUE) {
                    session = Globals.createSosHibernateStatelessConnection(API_CALL);
                    DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(session);
                    jocXmlCommand.setOrderDocumentations(dbDocLayer.getDocumentationPaths(jobChainsFilter.getJobschedulerId(),
                            JobSchedulerObjectType.ORDER));
                    jocXmlCommand.setJobDocumentations(dbDocLayer.getDocumentationPaths(jobChainsFilter.getJobschedulerId(),
                            JobSchedulerObjectType.JOB));
                    jocXmlCommand.setJobChainDocumentations(dbDocLayer.getDocumentationPaths(jobChainsFilter.getJobschedulerId(),
                            JobSchedulerObjectType.JOBCHAIN));
                }

                if (jobChains != null && !jobChains.isEmpty()) {
                    List<JobChainPath> permittedJobChains = new ArrayList<JobChainPath>();
                    Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                    for (JobChainPath jobChain : jobChains) {
                        if (jobChain != null && canAdd(jobChain.getJobChain(), permittedFolders)) {
                            permittedJobChains.add(jobChain);
                        }
                    }
                    if (!permittedJobChains.isEmpty()) {
                        listOfJobChains = jocXmlCommand.getJobChainsFromShowJobChain(permittedJobChains, jobChainsFilter);
                    }
                } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                    // no permission
                } else if (folders != null && !folders.isEmpty()) {
                    listOfJobChains = jocXmlCommand.getJobChainsFromShowState(folders, jobChainsFilter);
                } else {
                    listOfJobChains = jocXmlCommand.getJobChainsFromShowState(jobChainsFilter);
                }
                entity.setJobChains(listOfJobChains);
                entity.setNestedJobChains(jocXmlCommand.getNestedJobChains(jobChainsFilter.getCompactView()));

            } else {

                Map<String, String> orderDocs = null;
                Map<String, String> jobDocs = null;
                Map<String, String> jobChainDocs = null;
                Map<String, List<OrderVolatile>> orders = null;
                
                if (jobChainsFilter.getCompact() != Boolean.TRUE) {
                    session = Globals.createSosHibernateStatelessConnection(API_CALL);
                    DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(session);
                
                    jobDocs = dbDocLayer.getDocumentationPaths(jobChainsFilter.getJobschedulerId(), JobSchedulerObjectType.JOB);
                    jobChainDocs = dbDocLayer.getDocumentationPaths(jobChainsFilter.getJobschedulerId(), JobSchedulerObjectType.JOBCHAIN);
                    orderDocs = dbDocLayer.getDocumentationPaths(jobChainsFilter.getJobschedulerId(), JobSchedulerObjectType.ORDER);
                }
                
                if (jobChainsFilter.getCompact() != Boolean.TRUE || jobChainsFilter.getCompactView() != Boolean.TRUE) {

                    Map<String, OrderVolatile> listOrders = new HashMap<String, OrderVolatile>();
                    List<OrdersVCallable> orderTasks = new ArrayList<OrdersVCallable>();
                    JOCJsonCommand jsonOrdersCommand = new JOCJsonCommand(this);
                    jsonOrdersCommand.setUriBuilderForOrders();
                    jsonOrdersCommand.addOrderCompactQuery(false);

                    if (jobChains != null && !jobChains.isEmpty()) {
                        //GUI wants a folder but asks often with job chains array from ./job_chains/p answer
                        //for a better performance: orders are requested for the longest common folder
                        Folder commonFolder = GetLongestCommonFolder(jobChains);
                        if (commonFolder != null) {
                            listOrders = new OrdersVCallable(commonFolder, new JOCJsonCommand(jsonOrdersCommand), orderDocs, accessToken).call(); 
                        }
                    } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                        throw new JocFolderPermissionsException(jobChainsFilter.getFolders().get(0).getFolder());
                    } else if (folders != null && !folders.isEmpty()) {
                        for (Folder folder : folders) {
                            orderTasks.add(new OrdersVCallable(folder, new JOCJsonCommand(jsonOrdersCommand), orderDocs, accessToken));
                        }
                    } else {
                        Folder folder = new Folder();
                        folder.setFolder("/");
                        folder.setRecursive(true);
                        listOrders = new OrdersVCallable(folder, new JOCJsonCommand(jsonOrdersCommand), orderDocs, accessToken).call();
                    }

                    if (!orderTasks.isEmpty()) {
                        if (orderTasks.size() == 1) {
                            listOrders = orderTasks.get(0).call();
                        } else {
                            ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, orderTasks.size()));
                            try {
                                for (Future<Map<String, OrderVolatile>> result : executorService.invokeAll(orderTasks)) {
                                    try {
                                        listOrders.putAll(result.get());
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
                    }
                    orders = listOrders.values().stream().filter(o -> o.getJobChain() != null).collect(Collectors.groupingBy(
                            OrderVolatile::getJobChain, Collectors.toList()));
                }

                Map<String, JobChainVolatileJson> listJobChains = new HashMap<String, JobChainVolatileJson>();
                List<JobChainsVCallable> tasks = new ArrayList<JobChainsVCallable>();

                if (jobChains != null && !jobChains.isEmpty()) {
                    Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                    String unpermittedObject = null;
                    for (JobChainPath jobChain : jobChains) {
                        if (jobChain != null) {
                            if (canAdd(jobChain.getJobChain(), permittedFolders)) {
                                tasks.add(new JobChainsVCallable(jobChain.getJobChain(), jobChainsFilter, new JOCJsonCommand(this), accessToken,
                                        jobDocs, jobChainDocs, orders));
                            } else {
                                unpermittedObject = jobChain.getJobChain();
                            }
                        }
                    }
                    if (tasks.isEmpty() && unpermittedObject != null) {
                        throw new JocFolderPermissionsException(getParent(unpermittedObject));
                    }
                } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                    throw new JocFolderPermissionsException(jobChainsFilter.getFolders().get(0).getFolder());
                } else if (folders != null && !folders.isEmpty()) {
                    for (Folder folder : folders) {
                        tasks.add(new JobChainsVCallable(folder, jobChainsFilter, new JOCJsonCommand(this), accessToken, jobDocs, jobChainDocs,
                                orders));
                    }
                } else {
                    Folder folder = new Folder();
                    folder.setFolder("/");
                    folder.setRecursive(true);
                    listJobChains = new JobChainsVCallable(folder, jobChainsFilter, new JOCJsonCommand(this), accessToken, jobDocs, jobChainDocs,
                            orders).call();
                }

                if (!tasks.isEmpty()) {
                    if (tasks.size() == 1) {
                        listJobChains = tasks.get(0).call();
                    } else {
                        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                        try {
                            for (Future<Map<String, JobChainVolatileJson>> result : executorService.invokeAll(tasks)) {
                                try {
                                    listJobChains.putAll(result.get());
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
                }
                entity.setJobChains(new ArrayList<JobChainV>(listJobChains.values()));

                Set<String> nestedJobChains = listJobChains.values().stream().flatMap(j -> j.getNestedJobChains().stream()).collect(Collectors
                        .toSet());
                if (nestedJobChains == null || nestedJobChains.isEmpty()) {
                    entity.setNestedJobChains(null);
                } else {
                    List<JobChainsVCallable> nestedTasks = new ArrayList<JobChainsVCallable>();
                    Map<String, JobChainVolatileJson> listNestedJobChains = new HashMap<String, JobChainVolatileJson>();
                    jobChainsFilter.setFolders(null);
                    jobChainsFilter.setJob(null);
                    jobChainsFilter.setStates(null);
                    jobChainsFilter.setRegex(null);
                    jobChainsFilter.setJobChains(null);
                    for (String nestedJobChain : nestedJobChains) {
                        if (listJobChains.containsKey(nestedJobChain)) {
                            listNestedJobChains.put(nestedJobChain, listJobChains.get(nestedJobChain));
                        } else {
                            nestedTasks.add(new JobChainsVCallable(nestedJobChain, jobChainsFilter, new JOCJsonCommand(this), accessToken, jobDocs,
                                    jobChainDocs, null, orderDocs));
                        }
                    }
                    if (!nestedTasks.isEmpty()) {
                        if (nestedTasks.size() == 1) { // ExecutorService needs ~200ms around. Is only worth it if you have two or more job chains
                            listNestedJobChains = nestedTasks.get(0).call();
                        } else {
                            ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, nestedTasks.size()));
                            try {
                                for (Future<Map<String, JobChainVolatileJson>> result : executorService.invokeAll(nestedTasks)) {
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
    
    private Folder GetLongestCommonFolder(List<JobChainPath> jobChains)
    {
        if (jobChains == null || jobChains.isEmpty()) {
            return null;
        }
        Folder f = new Folder();
        f.setRecursive(false);
        f.setFolder("/");
        Iterator<JobChainPath> iter = jobChains.iterator();
        java.nio.file.Path p = Paths.get(iter.next().getJobChain()).getParent();
        if (p == null) {
            while (iter.hasNext()) {
                java.nio.file.Path p1 = Paths.get(iter.next().getJobChain()).getParent();
                if (p1 != null && p1.getNameCount() > 0) {
                    f.setRecursive(true);
                    break;
                }
            }
            return f;
        }
        while (iter.hasNext()) {
            java.nio.file.Path p1 = Paths.get(iter.next().getJobChain());
            if (p1.startsWith(p)) {
                if (!f.getRecursive() && p1.getParent().getNameCount() > p.getNameCount()) {
                    f.setRecursive(true);
                }
                continue;
            }
            f.setRecursive(true);
            p = p.getParent();
            while (p != null) {
                if (p1.startsWith(p)) {
                    break;
                }
                p = p.getParent();
            }
            if (p == null) {
                break;
            }
        }
        if (p == null) {
            return f;
        }
        f.setFolder(p.toString().replace('\\', '/'));
        return f;
    }
}
