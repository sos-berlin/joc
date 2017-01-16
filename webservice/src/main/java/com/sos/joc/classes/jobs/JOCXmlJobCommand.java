package com.sos.joc.classes.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobStateFilter;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.JobsFilter;


public class JOCXmlJobCommand extends JOCXmlCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCXmlJobCommand.class);
    private String accessToken;
    
    public JOCXmlJobCommand(String url, String accessToken) {
        super(url);
        this.accessToken = accessToken;
    }
    
    public JOCXmlJobCommand(DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance);
        this.accessToken = accessToken;
    }
    
    public JOCXmlJobCommand(JOCResourceImpl jocResourceImpl, String accessToken) {
        super(jocResourceImpl);
        this.accessToken = accessToken;
    }
    
    public JobV getJob(String job, Boolean compact) throws Exception {
        return getJob(job, compact, false);
    }
    
    public JobV getJobWithOrderQueue(String job, Boolean compact) throws Exception {
        return getJob(job, compact, true);
    }
    
    public JobV getJob(String job, Boolean compact, Boolean withOrderQueue) throws Exception {
        executePostWithThrowBadRequestAfterRetry(createShowJobPostCommand(job, compact), accessToken);
        Element jobElem = (Element) getSosxml().selectSingleNode("/spooler/answer/job");
        JobVolatile jobV = new JobVolatile(jobElem, this, withOrderQueue);
        jobV.setFields(compact, accessToken);
        return jobV;
    }
    
    public List<JobV> getJobsFromShowJob(List<JobPath> jobs, JobsFilter jobsFilter) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<commands>");
        for (JobPath job : jobs) {
            if (job.getJob() == null || job.getJob().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined job");
            }
            xml.append(createShowJobPostCommand(job.getJob(),jobsFilter.getCompact()));
        }
        xml.append("</commands>");
        return getJobs(xml.toString(), jobsFilter, "/spooler/answer/job");
    }
    
    public List<JobV> getJobsFromShowState(List<Folder> folders, JobsFilter jobsFilter) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<commands>");
        for (Folder folder : folders) {
            if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                throw new JocMissingRequiredParameterException("undefined folder");
            }
            xml.append(createShowStatePostCommand(folder.getFolder(),folder.getRecursive(), jobsFilter.getCompact()));
        }
        xml.append("</commands>");
        return getJobs(xml.toString(), jobsFilter);
    }
    
    public List<JobV> getJobsFromShowState(JobsFilter jobsFilter) throws Exception {
        return getJobs(createShowStatePostCommand("/",true, jobsFilter.getCompact()), jobsFilter);
    }

    private String createShowStatePostCommand(String folder, Boolean recursive, Boolean compact) {
        String subsystems = "folder job";
        String what = "folders task_queue job_orders";
        String path = null;
        if (!compact) {
            what += " job_params";
        }
        if (!recursive) {
            what += " no_subfolders";
        }
        if (folder != null) {
            path = ("/" + folder.trim()).replaceAll("//+", "/");
        }
        return getShowStateCommand(subsystems, what, path);
    }

    private String createShowJobPostCommand(String job, boolean compact) {
        job = ("/" + job.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        String what = "task_queue job_orders";
        //Integer maxOrders = 0;
        //Integer maxTaskHistory = 0;
        if (!compact) {
            what += " job_params";
        }
        return getShowJobCommand(job, what);
    }
    
    private List<JobV> getJobs(String command, JobsFilter jobsFilter) throws Exception {
        return getJobs(command, jobsFilter, "/spooler/answer//jobs/job");
    }
    
    private List<JobV> getJobs(String command, JobsFilter jobsFilter, String xPath) throws Exception {
        executePostWithThrowBadRequestAfterRetry(command, accessToken);
        List<JobStateText> filterStates = new ArrayList<JobStateText>();
        for (JobStateFilter filterState : jobsFilter.getStates()) {
            switch (filterState) {
            case RUNNING: 
                filterStates.add(JobStateText.RUNNING);
                filterStates.add(JobStateText.STOPPING);
                break;
            case PENDING: 
                filterStates.add(JobStateText.PENDING);
                break;
            case STOPPED: 
                filterStates.add(JobStateText.STOPPED);
                break;
            case QUEUED:
            case WAITINGFORRESOURCE: 
                filterStates.add(JobStateText.NOT_IN_PERIOD);
                filterStates.add(JobStateText.WAITING_FOR_AGENT);
                filterStates.add(JobStateText.WAITING_FOR_LOCK);
                filterStates.add(JobStateText.WAITING_FOR_PROCESS);
                filterStates.add(JobStateText.WAITING_FOR_TASK);
                break;
            }
        }
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
           if ("/scheduler_file_order_sink".equals(jobV.getPath())) {
              continue; 
           }
           if (!FilterAfterResponse.matchRegex(jobsFilter.getRegex(), jobV.getPath())) {
               LOGGER.debug("...processing skipped caused by 'regex=" + jobsFilter.getRegex() + "'");
               continue; 
           }
           jobV.setState();
           if (!FilterAfterResponse.filterStateHasState(filterStates, jobV.getState().get_text())) {
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
