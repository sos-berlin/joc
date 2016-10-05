package com.sos.joc.classes.jobchain;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
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
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.jobChain.JobChain__;
import com.sos.joc.model.jobChain.JobChain___;
import com.sos.joc.model.jobChain.JobChain____;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
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
    
    public JobChain__ getJobChain(String jobChain, Boolean compact) throws Exception {
        excutePost(createShowJobChainPostCommand(jobChain, compact));
        throwJobSchedulerError();
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job_chain");
        JobChainV jobChainV = new JobChainV(jobElem, this);
        jobChainV.setFields(compact);
        jobChainV.setOrdersSummary(new OrdersSummaryCallable(jobChainV, setUriForOrdersSummaryJsonCommand()).getOrdersSummary());
        return jobChainV;
    }
    
    public JobChain___ getNestedJobChain(String jobChain, Boolean compact) throws Exception {
        excutePost(createShowJobChainPostCommand(jobChain, compact));
        throwJobSchedulerError();
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job_chain");
        NestedJobChainV jobChainV = new NestedJobChainV(jobElem, this);
        jobChainV.setFields(compact);
        return jobChainV;
    }
    
    public List<JobChain__> getJobChainsFromShowJobChain(List<JobChain____> jobChains, JobChainsFilterSchema jobChainsFilterSchema) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (JobChain____ jobChain : jobChains) {
            if (jobChain.getJobChain() == null || jobChain.getJobChain().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined jobChain");
            }
            s.append(createShowJobChainPostCommand(jobChain.getJobChain(),jobChainsFilterSchema.getCompact())); 
        }
        s.append("</commands>");
        return getJobChains(s.toString(), jobChainsFilterSchema, "/spooler/answer/job_chain");
    }
    
    public List<JobChain__> getJobChainsFromShowState(List<FoldersSchema> folders, JobChainsFilterSchema jobChainsFilterSchema) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (FoldersSchema folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            s.append(createShowStatePostCommand(folder.getFolder(), folder.getRecursive(), jobChainsFilterSchema.getCompact())); 
        }
        s.append("</commands>");
        return getJobChains(s.toString(), jobChainsFilterSchema);
    }
    
    public List<JobChain__> getJobChainsFromShowState(JobChainsFilterSchema jobChainsFilterSchema) throws Exception {
        String s = createShowStatePostCommand("/", true, jobChainsFilterSchema.getCompact()); 
        return getJobChains(s, jobChainsFilterSchema);
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
    
    private List<JobChain__> getJobChains(String command, JobChainsFilterSchema jobChainsFilterSchema) throws Exception {
        return getJobChains(command, jobChainsFilterSchema, "/spooler/answer//job_chains/job_chain");
    }
    
    private List<JobChain__> getJobChains(String command, JobChainsFilterSchema jobChainsFilterSchema, String xPath) throws Exception {
        excutePost(command);
        throwJobSchedulerError();
        StringBuilder x = new StringBuilder();
        x.append(xPath);
        NodeList jobChainNodes = getSosxml().selectNodeList(x.toString());
        LOGGER.info("..." + jobChainNodes.getLength() + " jobChains found");
        
        Map<String, JobChainV> jobChainMap = new HashMap<String, JobChainV>();
        List<OrdersSummaryCallable> summaryTasks = new ArrayList<OrdersSummaryCallable>();
        List<OrdersVCallable> orderTasks = new ArrayList<OrdersVCallable>();
        URI uriForJsonSummaryCommand = setUriForOrdersSummaryJsonCommand();
        URI uriForJsonOrdersCommand = setUriForOrdersJsonCommand();
        
        for (int i= 0; i < jobChainNodes.getLength(); i++) {
           Element jobChainElem = (Element) jobChainNodes.item(i);
           JobChainV jobChainV = new JobChainV(jobChainElem, this);
           jobChainV.setPath();
           if (!FilterAfterResponse.matchReqex(jobChainsFilterSchema.getRegex(), jobChainV.getPath())) {
               LOGGER.info("...processing skipped caused by 'regex=" + jobChainsFilterSchema.getRegex() + "'");
               continue; 
           }
           jobChainV.setState();
           if (!FilterAfterResponse.filterStatehasState(jobChainsFilterSchema.getState(), jobChainV.getState().getText())) {
               LOGGER.info(String.format("...processing skipped because jobChain's state '%1$s' doesn't contain in state filter '%2$s'", jobChainV.getState().getText().name(),jobChainsFilterSchema.getState().toString()));
               continue; 
           }
           jobChainV.setFields(jobChainsFilterSchema.getCompact());
           summaryTasks.add(new OrdersSummaryCallable(jobChainV, uriForJsonSummaryCommand));
           if (!jobChainsFilterSchema.getCompact() && jobChainV.hasJobNodes()) {
               orderTasks.add(new OrdersVCallable(jobChainV, false, uriForJsonOrdersCommand)); 
           }
        }
        
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Future<Map<String, JobChainV>> result : executorService.invokeAll(summaryTasks)) {
            jobChainMap.putAll(result.get());
        }
        if (!jobChainsFilterSchema.getCompact()) {
            for (Future<Map<String, OrderQueue>> result : executorService.invokeAll(orderTasks)) {
                Map<String, OrderQueue> orders = result.get();
                if (orders.size() > 0) {
                    List<OrderQueue> o = new ArrayList<OrderQueue>(orders.values());
                    JobChainV j = jobChainMap.get(o.get(0).getJobChain());
                    j.setOrders(o);
                    // jobChainMap.put(jobChain, j);
                } 
            }
        }
        //LOGGER.info("..." + jobChainMap.size() + " jobChains processed");
        return new ArrayList<JobChain__>(jobChainMap.values());
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
