package com.sos.joc.classes.jobchains;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.classes.orders.OrdersSummaryCallable;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.jobChain.JobChainsFilter;


public class JOCXmlJobChainCommand extends JOCXmlCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlJobChainCommand.class);
    private Set<String> nestedJobChains = new HashSet<String>();
    private String accessToken;
    
    public JOCXmlJobChainCommand(JOCResourceImpl jocResourceImpl, String accessToken) {
        super(jocResourceImpl);
        this.accessToken = accessToken;
    }
    
    public JobChainV getJobChain(String jobChain, Boolean compact, Boolean compactView, Integer maxOrders) throws Exception {
        executePostWithThrowBadRequestAfterRetry(createShowJobChainPostCommand(jobChain, compact), accessToken);
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job_chain");
        JobChainVolatile jobChainV = new JobChainVolatile(jobElem, this, compactView);
        jobChainV.setFields(compact);
        nestedJobChains.addAll(jobChainV.getNestedJobChains());
        if (((compact == null || !compact) && jobChainV.getNumOfOrders() > 0) || jobChainV.hasJobChainNodes()) {
            OrdersVCallable ordersVCallable = new OrdersVCallable(jobChainV, setUriForOrdersJsonCommand(), accessToken);
            Map<String, OrderVolatile> orders = ordersVCallable.call();
            if (jobChainV.hasJobChainNodes()) {
                jobChainV.setOuterOrdersAndSummary(orders, maxOrders, compact);
            } else if (orders != null && orders.size() > 0) {
                jobChainV.setOrders(orders, maxOrders);
            }
        }
        if (!jobChainV.hasJobChainNodes() && compactView != Boolean.TRUE) {
            jobChainV.setOrdersSummary(new OrdersSummaryCallable(jobChainV, setUriForOrdersSummaryJsonCommand(), accessToken).getOrdersSummary()); 
        }
        return jobChainV;
    }
    
    public List<JobChainV> getNestedJobChains(Boolean compactView) throws Exception {
        if (nestedJobChains.size() == 0) {
            return null;
        }
        JobChainsFilter jobChainsFilter = new JobChainsFilter();
        jobChainsFilter.setCompactView(compactView);
        StringBuilder xml = new StringBuilder();
        xml.append("<commands>");
        for (String jobChainPath : nestedJobChains) {
            if (jobChainPath != null) {
                xml.append(createShowJobChainPostCommand(jobChainPath,jobChainsFilter.getCompact()));
            }
        }
        xml.append("</commands>");
        return getJobChains(xml.toString(), jobChainsFilter, "/spooler/answer/job_chain");
    }
    
    public List<JobChainV> getJobChainsFromShowJobChain(List<JobChainPath> jobChains, JobChainsFilter jobChainsFilter) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<commands>");
        for (JobChainPath jobChain : jobChains) {
            if (jobChain.getJobChain() == null || jobChain.getJobChain().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined jobChain");
            }
            xml.append(createShowJobChainPostCommand(jobChain.getJobChain(),jobChainsFilter.getCompact()));
        }
        xml.append("</commands>");
        return getJobChains(xml.toString(), jobChainsFilter, "/spooler/answer/job_chain");
    }
    
    public List<JobChainV> getJobChainsFromShowState(List<Folder> folders, JobChainsFilter jobChainsFilter) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<commands>");
        for (Folder folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            xml.append(createShowStatePostCommand(folder.getFolder(), folder.getRecursive(), jobChainsFilter.getCompact()));
        }
        xml.append("</commands>");
        return getJobChains(xml.toString(), jobChainsFilter);
    }
    
    public List<JobChainV> getJobChainsFromShowState(JobChainsFilter jobChainsFilter) throws Exception {
        return getJobChains(createShowStatePostCommand("/", true, jobChainsFilter.getCompact()), jobChainsFilter);
    }

    private String createShowStatePostCommand(String folder, Boolean recursive, Boolean compact) {
        String subsystems = "folder order";
        String what = "job_chains folders order_source_files blacklist";
        String path = null;
        Integer maxOrders = null;
        Integer maxOrderHistory = 0;
        if (compact) {
            maxOrders = 0;
        } else {
            what += " job_chain_jobs";
        }
        if (!recursive) {
            what += " no_subfolders";
        }
        if (folder != null) {
            path = ("/" + folder.trim()).replaceAll("//+", "/");
        }
        return getShowStateCommand(subsystems, what, path, maxOrders, maxOrderHistory);
    }

    private String createShowJobChainPostCommand(String jobChain, boolean compact) {
        jobChain = ("/" + jobChain.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        String what = "order_source_files blacklist"; // blacklisted orders are not counted in <order_queue length="..."/>
        Integer maxOrders = null;
        if (compact) {
            maxOrders = 0;
        } else {
            what += " job_chain_jobs";
        }
        return getShowJobChainCommand(jobChain, what, maxOrders);
    }
    
    private List<JobChainV> getJobChains(String command, JobChainsFilter jobChainsFilter) throws Exception {
        return getJobChains(command, jobChainsFilter, "/spooler/answer//job_chains/job_chain");
    }
    
    private List<JobChainV> getJobChains(String command, JobChainsFilter jobChainsFilter, String xPath) throws Exception {
        executePostWithThrowBadRequestAfterRetry(command, accessToken);
        NodeList jobChainNodes = getSosxml().selectNodeList(xPath);
        LOGGER.debug("..." + jobChainNodes.getLength() + " job chains found");
        
        Map<String, JobChainVolatile> jobChainMap = new HashMap<String, JobChainVolatile>();
        List<OrdersSummaryCallable> summaryTasks = new ArrayList<OrdersSummaryCallable>();
        List<OrdersVCallable> orderTasks = new ArrayList<OrdersVCallable>();
        
        Pattern regex = null;
        Pattern jobRegex = null;
        if (jobChainsFilter.getRegex() != null && !jobChainsFilter.getRegex().isEmpty()) {
            regex = Pattern.compile(jobChainsFilter.getRegex());
        }
        if (jobChainsFilter.getJob() != null) {
            if (jobChainsFilter.getJob().getRegex() != null && !jobChainsFilter.getJob().getRegex().isEmpty()) {
                jobRegex = Pattern.compile(jobChainsFilter.getJob().getRegex());
            } 
        }
        
        for (int i= 0; i < jobChainNodes.getLength(); i++) {
           Element jobChainElem = (Element) jobChainNodes.item(i);
           JobChainVolatile jobChainV = new JobChainVolatile(jobChainElem, this, jobChainsFilter.getCompactView());
           jobChainV.setPath();
           if (!FilterAfterResponse.matchRegex(regex, jobChainV.getPath())) {
               LOGGER.debug("...processing skipped caused by 'regex=" + jobChainsFilter.getRegex() + "'");
               continue; 
           }
           jobChainV.setState();
           if (!FilterAfterResponse.filterStateHasState(jobChainsFilter.getStates(), jobChainV.getState().get_text())) {
               LOGGER.debug(String.format("...processing skipped because jobChain's state '%1$s' doesn't contain in state filter '%2$s'", jobChainV.getState().get_text().name(),jobChainsFilter.getStates().toString()));
               continue; 
           }
           if (jobChainsFilter.getJob() != null) {
               if (jobChainV.getJobPaths() == null || jobChainV.getJobPaths().isEmpty()) {
                   continue;
               }
               if (!FilterAfterResponse.matchRegex(jobRegex, jobChainV.getJobPaths())) {
                   LOGGER.debug("...processing skipped caused by 'jobRegex=" + jobChainsFilter.getJob().getRegex() + "'");
                   continue; 
               }
               if (jobChainsFilter.getJob().getFolders() != null && !jobChainsFilter.getJob().getFolders().isEmpty()) {
                   boolean folderFound = false;
                   for (String jobPathStr : jobChainV.getJobPaths()) {
                       Path jobPath = Paths.get(jobPathStr);
                       for (Folder f : jobChainsFilter.getJob().getFolders()) {
                           folderFound = FilterAfterResponse.folderContainsObject(f, jobPath);
                           if (folderFound) {
                               break;
                           }
                       }
                       if (folderFound) {
                          break; 
                       } 
                   }
                   if (!folderFound) {
                       continue;
                   }
               }
           }
           jobChainV.setFields(jobChainsFilter.getCompact());
           nestedJobChains.addAll(jobChainV.getNestedJobChains());
           jobChainMap.put(jobChainV.getPath(), jobChainV);
           if (jobChainsFilter.getCompactView() == null || !jobChainsFilter.getCompactView()) {
               summaryTasks.add(new OrdersSummaryCallable(jobChainV, setUriForOrdersSummaryJsonCommand(), accessToken));
           }
           if (!jobChainsFilter.getCompact() || jobChainV.hasJobChainNodes()) {
               orderTasks.add(new OrdersVCallable(jobChainV, setUriForOrdersJsonCommand(), accessToken)); 
           }
        }
        
        int threadPoolSize = Math.min(10, Math.max(summaryTasks.size(), orderTasks.size())); //max. 10
        if (threadPoolSize > 0) {
            ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            try {
                if (!summaryTasks.isEmpty()) {
                    for (Future<Map<String, JobChainVolatile>> result : executorService.invokeAll(summaryTasks)) {
                        try {
                            jobChainMap.putAll(result.get());
                        } catch (ExecutionException e) {
                            if (e.getCause() instanceof JocException) {
                                throw (JocException) e.getCause();
                            } else {
                                throw (Exception) e.getCause();
                            }
                        }
                    }
                }
                if (!orderTasks.isEmpty()) {
                    for (Future<Map<String, OrderVolatile>> result : executorService.invokeAll(orderTasks)) {
                        try {
                            Map<String, OrderVolatile> orders = result.get();
                            if (orders.size() > 0) {
                                JobChainVolatile j = jobChainMap.get(orders.values().iterator().next().origJobChain());
                                if (j != null) {
                                    if (j.hasJobChainNodes()) {
                                        j.setOuterOrdersAndSummary(orders, jobChainsFilter.getMaxOrders(), jobChainsFilter.getCompact(), jobChainsFilter.getCompactView());
                                    } else {
                                        j.setOrders(orders, jobChainsFilter.getMaxOrders());
                                    }
                                }
                            }
                        } catch (ExecutionException e) {
                            if (e.getCause() instanceof JocException) {
                                throw (JocException) e.getCause();
                            } else {
                                throw (Exception) e.getCause();
                            }
                        }
                    }
                }
            } finally {
                executorService.shutdown();
            }
        }
        //LOGGER.debug("..." + jobChainMap.size() + " jobChains processed");
        return new ArrayList<JobChainV>(jobChainMap.values());
    }
    
    private JOCJsonCommand setUriForOrdersSummaryJsonCommand() {
        JOCJsonCommand jsonSummaryCommand = new JOCJsonCommand(getJOCResourceImpl());
        jsonSummaryCommand.setUriBuilderForOrders();
        jsonSummaryCommand.addOrderStatisticsQuery();
        return jsonSummaryCommand;
    }
    
    private JOCJsonCommand setUriForOrdersJsonCommand() {
        JOCJsonCommand jsonOrdersCommand = new JOCJsonCommand(getJOCResourceImpl());
        jsonOrdersCommand.setUriBuilderForOrders();
        jsonOrdersCommand.addOrderCompactQuery(false);
        return jsonOrdersCommand;
    }
}
