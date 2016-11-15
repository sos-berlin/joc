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
import com.sos.scheduler.model.commands.JSCmdCommands;
import com.sos.scheduler.model.commands.JSCmdShowJob;
import com.sos.scheduler.model.commands.JSCmdShowState;


public class JOCXmlJobCommand extends JOCXmlCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlJobCommand.class);
    private String accessToken;
    
    public JOCXmlJobCommand(String url, String accessToken) {
        super(url);
        this.accessToken = accessToken;
    }
    
    public JobV getJob(String job, Boolean compact) throws Exception {
        return getJob(job, compact, false);
    }
    
    public JobV getJobWithOrderQueue(String job, Boolean compact) throws Exception {
        return getJob(job, compact, true);
    }
    
    public JobV getJob(String job, Boolean compact, Boolean withOrderQueue) throws Exception {
        JSCmdShowJob j = createShowJobPostCommand(job, compact);
        executePostWithThrowBadRequest(j.toXMLString(), accessToken);
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job");
        JobVolatile jobV = new JobVolatile(jobElem, this, withOrderQueue);
        jobV.setFields(compact, accessToken);
        return jobV;
    }
    
    public List<JobV> getJobsFromShowJob(List<JobPath> jobs, JobsFilter jobsFilter) throws Exception {
        JSCmdCommands commands = Globals.schedulerObjectFactory.createCmdCommands();
        for (JobPath job : jobs) {
            if (job.getJob() == null || job.getJob().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined job");
            }
            JSCmdShowJob j = createShowJobPostCommand(job.getJob(),jobsFilter.getCompact());
            commands.getAddJobsOrAddOrderOrCheckFolders().add(j);
        }
        return getJobs(commands.toXMLString(), jobsFilter, "/spooler/answer/job");
    }
    
    public List<JobV> getJobsFromShowState(List<Folder> folders, JobsFilter jobsFilter) throws Exception {
        JSCmdCommands commands = Globals.schedulerObjectFactory.createCmdCommands();
        for (Folder folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            JSCmdShowState s = createShowStatePostCommand(folder.getFolder(),folder.getRecursive(), jobsFilter.getCompact());
            commands.getAddJobsOrAddOrderOrCheckFolders().add(s);
        }
        return getJobs(commands.toXMLString(), jobsFilter);
    }
    
    public List<JobV> getJobsFromShowState(JobsFilter jobsFilter) throws Exception {
        JSCmdShowState s = createShowStatePostCommand("/",true, jobsFilter.getCompact()); 
        return getJobs(s.toXMLString(), jobsFilter);
    }

    private JSCmdShowState createShowStatePostCommand(String folder, Boolean recursive, Boolean compact) {
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
            showState.setPath(("/" + folder.trim()).replaceAll("//+", "/"));
        }
        return showState;
    }

    private JSCmdShowJob createShowJobPostCommand(String job, boolean compact) {
        JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
        showJob.setWhat("task_queue job_orders");
        if (!compact) {
            showJob.setWhat("job_params " + showJob.getWhat());
        }
        showJob.setJobName(("/" + job.trim()).replaceAll("//+", "/").replaceFirst("/$", ""));
        // showJob.setMaxOrders(BigInteger.valueOf(0));
        showJob.setMaxTaskHistory(BigInteger.valueOf(0));
        return showJob;
    }
    
    private List<JobV> getJobs(String command, JobsFilter jobsFilter) throws Exception {
        return getJobs(command, jobsFilter, "/spooler/answer//jobs/job");
    }
    
    private List<JobV> getJobs(String command, JobsFilter jobsFilter, String xPath) throws Exception {
        executePostWithThrowBadRequest(command, accessToken);
        StringBuilder x = new StringBuilder();
        x.append(xPath);
        if (jobsFilter.getIsOrderJob() != null) {
            LOGGER.debug(String.format("...consider filtering by 'isOrderJob=%1$b'", jobsFilter.getIsOrderJob()));
            if (jobsFilter.getIsOrderJob()) {
                x.append("[@order='yes']"); 
            } else {
                x.append("[not(@order) or @order='no']"); 
            }
        }
        NodeList jobNodes = getSosxml().selectNodeList(x.toString());
        LOGGER.debug("..." + jobNodes.getLength() + " jobs found");
        Map<String, JobV> jobMap = new HashMap<String,JobV>();
        for (int i= 0; i < jobNodes.getLength(); i++) {
           Element jobElem = (Element) jobNodes.item(i);
           JobVolatile jobV = new JobVolatile(jobElem, this);
           jobV.setPath();
           if (!FilterAfterResponse.matchRegex(jobsFilter.getRegex(), jobV.getPath())) {
               LOGGER.debug("...processing skipped caused by 'regex=" + jobsFilter.getRegex() + "'");
               continue; 
           }
           jobV.setState();
           if (!FilterAfterResponse.filterStateHasState(jobsFilter.getStates(), jobV.getState().get_text())) {
               LOGGER.debug(String.format("...processing skipped because job's state '%1$s' doesn't contain in state filter '%2$s'", jobV.getState().get_text().name(),jobsFilter.getStates().toString()));
               continue; 
           }
           jobV.setFields(jobsFilter.getCompact(), accessToken);
           jobMap.put(jobV.getPath(), jobV);
        }
        //LOGGER.debug("..." + jobMap.size() + " jobs processed");
        return new ArrayList<JobV>(jobMap.values());
    }
    
}
