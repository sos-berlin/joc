package com.sos.joc.classes.jobs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.JobsFilter;
import com.sos.scheduler.model.commands.JSCmdShowJob;
import com.sos.scheduler.model.commands.JSCmdShowState;


public class JOCXmlJobCommand extends JOCXmlCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlJobCommand.class);
    
    public JOCXmlJobCommand(String url) {
        super(url);
    }
    
    public JobV getJob(String job, Boolean compact) throws Exception {
        return getJob(job, compact, false);
    }
    
    public JobV getJobWithOrderQueue(String job, Boolean compact) throws Exception {
        return getJob(job, compact, true);
    }
    
    public JobV getJob(String job, Boolean compact, Boolean withOrderQueue) throws Exception {
        excutePost(createShowJobPostCommand(job, compact));
        throwJobSchedulerError();
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job");
        JobVolatile jobV = new JobVolatile(jobElem, this, withOrderQueue);
        jobV.setFields(compact);
        return jobV;
    }
    
    public List<JobV> getJobsFromShowJob(List<JobPath> jobs, JobsFilter jobsFilter) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (JobPath job : jobs) {
            if (job.getJob() == null || job.getJob().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined job");
            }
            s.append(createShowJobPostCommand(job.getJob(),jobsFilter.getCompact())); 
        }
        s.append("</commands>");
        return getJobs(s.toString(), jobsFilter, "/spooler/answer/job");
    }
    
    public List<JobV> getJobsFromShowState(List<Folder> folders, JobsFilter jobsFilter) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (Folder folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            s.append(createShowStatePostCommand(folder.getFolder(),folder.getRecursive(), jobsFilter.getCompact())); 
        }
        s.append("</commands>");
        return getJobs(s.toString(), jobsFilter);
    }
    
    public List<JobV> getJobsFromShowState(JobsFilter jobsFilter) throws Exception {
        String s = createShowStatePostCommand("/",true, jobsFilter.getCompact()); 
        return getJobs(s, jobsFilter);
    }

    private String createShowStatePostCommand(String folder, Boolean recursive, Boolean compact) {
        JSCmdShowState showState = Globals.schedulerObjectFactory.createShowState();
        showState.setSubsystems("folder job");
        showState.setWhat("folders task_queue job_orders");
        if (!compact) {
            showState.setWhat("job_params " + showState.getWhat());
        }
        if (!recursive) {
            showState.setWhat("no_subfolders " + showState.getWhat());
        }
        if (folder != null) {
            showState.setPath(("/"+folder).replaceAll("//+", "/"));
        }
        return showState.toXMLString();
    }

    private String createShowJobPostCommand(String job, boolean compact) {
        JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
        showJob.setWhat("task_queue job_orders");
        if (!compact) {
            showJob.setWhat("job_params " + showJob.getWhat());
        }
        showJob.setJob(job);
        // showJob.setMaxOrders(BigInteger.valueOf(0));
        showJob.setMaxTaskHistory(BigInteger.valueOf(0));
        return showJob.toXMLString();
    }
    
    private List<JobV> getJobs(String command, JobsFilter jobsFilter) throws Exception {
        return getJobs(command, jobsFilter, "/spooler/answer//jobs/job");
    }
    
    private List<JobV> getJobs(String command, JobsFilter jobsFilter, String xPath) throws Exception {
        excutePost(command);
        throwJobSchedulerError();
        StringBuilder x = new StringBuilder();
        x.append(xPath);
        if (jobsFilter.getIsOrderJob() != null) {
            LOGGER.info(String.format("...consider filtering by 'isOrderJob=%1$b'", jobsFilter.getIsOrderJob()));
            if (jobsFilter.getIsOrderJob()) {
                x.append("[@order='yes']"); 
            } else {
                x.append("[not(@order) or @order='no']"); 
            }
        }
        NodeList jobNodes = getSosxml().selectNodeList(x.toString());
        LOGGER.info("..." + jobNodes.getLength() + " jobs found");
        Map<String, JobV> jobMap = new HashMap<String,JobV>();
        for (int i= 0; i < jobNodes.getLength(); i++) {
           Element jobElem = (Element) jobNodes.item(i);
           JobVolatile jobV = new JobVolatile(jobElem, this);
           jobV.setPath();
           if (!FilterAfterResponse.matchReqex(jobsFilter.getRegex(), jobV.getPath())) {
               LOGGER.info("...processing skipped caused by 'regex=" + jobsFilter.getRegex() + "'");
               continue; 
           }
           jobV.setState();
           if (!FilterAfterResponse.filterStateHasState(jobsFilter.getStates(), jobV.getState().get_text())) {
               LOGGER.info(String.format("...processing skipped because job's state '%1$s' doesn't contain in state filter '%2$s'", jobV.getState().get_text().name(),jobsFilter.getStates().toString()));
               continue; 
           }
           jobV.setFields(jobsFilter.getCompact());
           jobMap.put(jobV.getPath(), jobV);
        }
        //LOGGER.info("..." + jobMap.size() + " jobs processed");
        return new ArrayList<JobV>(jobMap.values());
    }
    
}
