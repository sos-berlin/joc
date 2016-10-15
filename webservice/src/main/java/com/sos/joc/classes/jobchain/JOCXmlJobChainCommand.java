package com.sos.joc.classes.jobchain;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.orders.OrdersSummaryCallable;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrderV;
import com.sos.scheduler.model.commands.JSCmdShowJobChain;
import com.sos.scheduler.model.commands.JSCmdShowState;


public class JOCXmlJobChainCommand extends JOCXmlCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlJobChainCommand.class);
    private String jsonUrl = null;
    
    public JOCXmlJobChainCommand(String url) {
        super(url);
    }
    
    public JOCXmlJobChainCommand(String url, String jsonUrl) {
        super(url);
        this.jsonUrl = jsonUrl;
    }
    
    public JobChainV getJobChain(String jobChain, Boolean compact) throws Exception {
        excutePost(createShowJobChainPostCommand(jobChain, compact));
        throwJobSchedulerError();
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job_chain");
        JobChainVolatile jobChainV = new JobChainVolatile(jobElem, this);
        jobChainV.setFields(compact);
        jobChainV.setOrdersSummary(new OrdersSummaryCallable(jobChainV, setUriForOrdersSummaryJsonCommand()).getOrdersSummary());
        return jobChainV;
    }
    
//    public JobChainV getNestedJobChain(String jobChain, Boolean compact) throws Exception {
//        excutePost(createShowJobChainPostCommand(jobChain, compact));
//        throwJobSchedulerError();
//        Element jobChainElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job_chain");
////        NestedJobChainV jobChainV = new NestedJobChainV(jobChainElem, this);
////        jobChainV.setFields(compact);
//        JobChain___ jobChainV = new JobChain___();
//        jobChainV.setPath(jobChainElem.getAttribute(WebserviceConstants.PATH));
//        return jobChainV;
//    }
    
    public List<JobChainV> getJobChainsFromShowJobChain(List<JobChainPath> jobChains, JobChainsFilter jobChainsFilter) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (JobChainPath jobChain : jobChains) {
            if (jobChain.getJobChain() == null || jobChain.getJobChain().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined jobChain");
            }
            s.append(createShowJobChainPostCommand(jobChain.getJobChain(),jobChainsFilter.getCompact())); 
        }
        s.append("</commands>");
        return getJobChains(s.toString(), jobChainsFilter, "/spooler/answer/job_chain");
    }
    
    public List<JobChainV> getJobChainsFromShowState(List<Folder> folders, JobChainsFilter jobChainsFilter) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (Folder folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            s.append(createShowStatePostCommand(folder.getFolder(), folder.getRecursive(), jobChainsFilter.getCompact())); 
        }
        s.append("</commands>");
        return getJobChains(s.toString(), jobChainsFilter);
    }
    
    public List<JobChainV> getJobChainsFromShowState(JobChainsFilter jobChainsFilter) throws Exception {
        String s = createShowStatePostCommand("/", true, jobChainsFilter.getCompact()); 
        return getJobChains(s, jobChainsFilter);
    }

    private String createShowStatePostCommand(String folder, Boolean recursive, Boolean compact) {
        JSCmdShowState showState = Globals.schedulerObjectFactory.createShowState();
        showState.setSubsystems("folder order");
        showState.setWhat("job_chains folders order_source_files blacklist");
        if (compact) {
            showState.setMaxOrders(BigInteger.valueOf(0));
        } else {
            showState.setWhat("job_chain_orders job_chain_jobs " + showState.getWhat());
        }
        if (!recursive) {
            showState.setWhat("no_subfolders " + showState.getWhat());
        }
        if (folder != null) {
            showState.setPath(("/"+folder).replaceAll("//+", "/"));
        }
        showState.setMaxOrderHistory(BigInteger.valueOf(0));
        return showState.toXMLString();
    }

    private String createShowJobChainPostCommand(String jobChain, boolean compact) {
        JSCmdShowJobChain showJobChain = Globals.schedulerObjectFactory.createShowJobChain();
        showJobChain.setWhat("order_source_files blacklist"); //blacklisted orders are not counted in <order_queue length="..."/>
        if (compact) {
            showJobChain.setMaxOrders(BigInteger.valueOf(0));
        } else {
            showJobChain.setWhat("job_chain_orders job_chain_jobs " + showJobChain.getWhat());
        }
        showJobChain.setJobChain(jobChain);
        showJobChain.setMaxOrderHistory(BigInteger.valueOf(0));
        return showJobChain.toXMLString();
    }
    
    private List<JobChainV> getJobChains(String command, JobChainsFilter jobChainsFilter) throws Exception {
        return getJobChains(command, jobChainsFilter, "/spooler/answer//job_chains/job_chain");
    }
    
    private List<JobChainV> getJobChains(String command, JobChainsFilter jobChainsFilter, String xPath) throws Exception {
        excutePost(command);
        throwJobSchedulerError();
        StringBuilder x = new StringBuilder();
        x.append(xPath);
        NodeList jobChainNodes = getSosxml().selectNodeList(x.toString());
        LOGGER.info("..." + jobChainNodes.getLength() + " jobChains found");
        
        Map<String, JobChainVolatile> jobChainMap = new HashMap<String, JobChainVolatile>();
        List<OrdersSummaryCallable> summaryTasks = new ArrayList<OrdersSummaryCallable>();
        List<OrdersVCallable> orderTasks = new ArrayList<OrdersVCallable>();
        URI uriForJsonSummaryCommand = setUriForOrdersSummaryJsonCommand();
        URI uriForJsonOrdersCommand = setUriForOrdersJsonCommand();
        
        for (int i= 0; i < jobChainNodes.getLength(); i++) {
           Element jobChainElem = (Element) jobChainNodes.item(i);
           JobChainVolatile jobChainV = new JobChainVolatile(jobChainElem, this);
           jobChainV.setPath();
           if (!FilterAfterResponse.matchReqex(jobChainsFilter.getRegex(), jobChainV.getPath())) {
               LOGGER.info("...processing skipped caused by 'regex=" + jobChainsFilter.getRegex() + "'");
               continue; 
           }
           jobChainV.setState();
           if (!FilterAfterResponse.filterStateHasState(jobChainsFilter.getStates(), jobChainV.getState().get_text())) {
               LOGGER.info(String.format("...processing skipped because jobChain's state '%1$s' doesn't contain in state filter '%2$s'", jobChainV.getState().get_text().name(),jobChainsFilter.getStates().toString()));
               continue; 
           }
           jobChainV.setFields(jobChainsFilter.getCompact());
           summaryTasks.add(new OrdersSummaryCallable(jobChainV, uriForJsonSummaryCommand));
           if (!jobChainsFilter.getCompact() && jobChainV.hasJobNodes()) {
               orderTasks.add(new OrdersVCallable(jobChainV, false, uriForJsonOrdersCommand)); 
           }
        }
        
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Future<Map<String, JobChainVolatile>> result : executorService.invokeAll(summaryTasks)) {
            jobChainMap.putAll(result.get());
        }
        if (!jobChainsFilter.getCompact()) {
            for (Future<Map<String, OrderV>> result : executorService.invokeAll(orderTasks)) {
                Map<String, OrderV> orders = result.get();
                if (orders.size() > 0) {
                    List<OrderV> o = new ArrayList<OrderV>(orders.values());
                    JobChainVolatile j = jobChainMap.get(o.get(0).getJobChain());
                    j.setOrders(o);
                    // jobChainMap.put(jobChain, j);
                } 
            }
        }
        //LOGGER.info("..." + jobChainMap.size() + " jobChains processed");
        return new ArrayList<JobChainV>(jobChainMap.values());
    }
    
    private URI setUriForOrdersSummaryJsonCommand() {
        JOCJsonCommand jsonSummaryCommand = new JOCJsonCommand(jsonUrl);
        jsonSummaryCommand.addOrderStatisticsQuery();
        return jsonSummaryCommand.getURI();
    }
    
    private URI setUriForOrdersJsonCommand() {
        JOCJsonCommand jsonOrdersCommand = new JOCJsonCommand(jsonUrl);
        jsonOrdersCommand.addCompactQuery(false);
        return jsonOrdersCommand.getURI();
    }
}
