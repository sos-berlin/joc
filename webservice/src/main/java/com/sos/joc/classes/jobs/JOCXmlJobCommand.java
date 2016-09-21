package com.sos.joc.classes.jobs;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.Job__;
import com.sos.joc.model.job.JobsFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowJob;
import com.sos.scheduler.model.commands.JSCmdShowState;


public class JOCXmlJobCommand extends JOCXmlCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlJobCommand.class);
    private URI uriForJsonCommand = null;
    
    public JOCXmlJobCommand(String url) {
        super(url);
    }
    
    public URI getUriForJsonCommand() {
        return uriForJsonCommand;
    }

    public void setUriForJsonCommand(URI uriForJsonCommand) {
        this.uriForJsonCommand = uriForJsonCommand;
    }
    
    public void setUriForJsonCommand(String jsonUrl, boolean compact) {
        JOCJsonCommand command = new JOCJsonCommand(jsonUrl);
        command.addCompactQuery(compact);
        this.uriForJsonCommand = command.getURI();
    }
    
    public Job_ getJob(String job, Boolean compact) throws Exception {
        return getJob(job, compact, false);
    }
    
    public Job_ getJobWithOrderQueue(String job, Boolean compact) throws Exception {
        return getJob(job, compact, true);
    }
    
    public Job_ getJob(String job, Boolean compact, Boolean withOrderQueue) throws Exception {
        excutePost(createShowJobPostCommand(job, compact));
        throwJobSchedulerError();
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job");
        JobV jobV = new JobV(jobElem, this, withOrderQueue);
        jobV.setFields(compact);
        return jobV;
    }
    
    public List<Job_> getJobsFromShowJob(List<Job__> jobs, JobsFilterSchema jobsFilterSchema) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (Job__ job : jobs) {
            if (job.getJob() == null || job.getJob().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined job");
            }
            s.append(createShowJobPostCommand(job.getJob(),jobsFilterSchema.getCompact())); 
        }
        s.append("</commands>");
        return getJobs(s.toString(), jobsFilterSchema, "/spooler/answer/job");
    }
    
    public List<Job_> getJobsFromShowState(List<FoldersSchema> folders, JobsFilterSchema jobsFilterSchema) throws Exception {
        StringBuilder s = new StringBuilder();
        s.append("<commands>");
        for (FoldersSchema folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            s.append(createShowStatePostCommand(folder.getFolder(),folder.getRecursive(), jobsFilterSchema.getCompact())); 
        }
        s.append("</commands>");
        return getJobs(s.toString(), jobsFilterSchema);
    }
    
    public List<Job_> getJobsFromShowState(JobsFilterSchema jobsFilterSchema) throws Exception {
        String s = createShowStatePostCommand("/",true, jobsFilterSchema.getCompact()); 
        return getJobs(s, jobsFilterSchema);
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
    
    private List<Job_> getJobs(String command, JobsFilterSchema jobsFilterSchema) throws Exception {
        return getJobs(command, jobsFilterSchema, "/spooler/answer//jobs/job");
    }
    
    private List<Job_> getJobs(String command, JobsFilterSchema jobsFilterSchema, String xPath) throws Exception {
        excutePost(command);
        throwJobSchedulerError();
        StringBuilder x = new StringBuilder();
        x.append(xPath);
        if (jobsFilterSchema.getIsOrderJob() != null) {
            LOGGER.info(String.format("...consider filtering by 'isOrderJob=%1$b'", jobsFilterSchema.getIsOrderJob()));
            if (jobsFilterSchema.getIsOrderJob()) {
                x.append("[@order='yes']"); 
            } else {
                x.append("[not(@order) or @order='no']"); 
            }
        }
        NodeList jobNodes = getSosxml().selectNodeList(x.toString());
        LOGGER.info("..." + jobNodes.getLength() + " jobs found");
        Map<String, Job_> jobMap = new HashMap<String,Job_>();
        for (int i= 0; i < jobNodes.getLength(); i++) {
           Element jobElem = (Element) jobNodes.item(i);
           JobV jobV = new JobV(jobElem, this);
           jobV.setPath();
           if (!FilterAfterResponse.matchReqex(jobsFilterSchema.getRegex(), jobV.getPath())) {
               LOGGER.info("...processing skipped caused by 'regex=" + jobsFilterSchema.getRegex() + "'");
               continue; 
           }
           jobV.setState();
           if (!FilterAfterResponse.filterStatehasState(jobsFilterSchema.getState(), jobV.getState().getText())) {
               LOGGER.info(String.format("...processing skipped because job's state '%1$s' doesn't contain in state filter '%2$s'", jobV.getState().getText().name(),jobsFilterSchema.getState().toString()));
               continue; 
           }
           jobV.setFields(jobsFilterSchema.getCompact());
           jobMap.put(jobV.getPath(), jobV);
        }
        //LOGGER.info("..." + jobMap.size() + " jobs processed");
        return new ArrayList<Job_>(jobMap.values());
    }
    
}
