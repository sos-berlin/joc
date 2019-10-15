package com.sos.joc.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobPath;
import com.sos.joc.model.job.JobStateFilter;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.JobsFilter;

public class JobsVCallable implements Callable<Map<String, JobVolatileJson>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobsVCallable.class);
    private final String job;
    private final Collection<String> jobs;
    private final Folder folder;
    private final JobsFilter jobsBody;
    private final Boolean compact;
    private final Boolean compactView;
    private final Boolean withOrderQueue;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;
    private final Boolean suppressJobSchedulerObjectNotExistException;
    private final JsonObject summary;

    public JobsVCallable(JobFilter job, JOCJsonCommand jocJsonCommand, String accessToken, Boolean withOrderQueue, JsonObject summary) {
        this.job = job.getJob();
        this.jobs = null;
        this.folder = null;
        this.jobsBody = null;
        this.compact = job.getCompact();
        this.compactView = job.getCompactView();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = false;
        this.withOrderQueue = withOrderQueue;
        this.summary = summary;
    }
    
    public JobsVCallable(JobPath job, JobsFilter jobs, JOCJsonCommand jocJsonCommand, String accessToken, JsonObject summary) {
        this.job = job.getJob();
        this.jobs = null;
        this.folder = null;
        this.jobsBody = jobs;
        this.compact = jobs.getCompact();
        this.compactView = jobs.getCompactView();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
        this.withOrderQueue = false;
        this.summary = summary;
    }
    
    public JobsVCallable(String job, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.job = job;
        this.jobs = null;
        this.folder = null;
        this.jobsBody = null;
        this.compact = true;
        this.compactView = true;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
        this.withOrderQueue = false;
        this.summary = null;
    }
    
    public JobsVCallable(Collection<String> jobs, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.job = null;
        this.jobs = jobs;
        this.folder = null;
        this.jobsBody = null;
        this.compact = true;
        this.compactView = true;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
        this.withOrderQueue = false;
        this.summary = null;
    }
    
    public JobsVCallable(Collection<String> jobs, JobsFilter jobsFilter, JOCJsonCommand jocJsonCommand, String accessToken, JsonObject summary) {
        this.job = null;
        this.jobs = jobs;
        this.folder = null;
        this.jobsBody = jobsFilter;
        this.compact = jobsFilter.getCompact();
        this.compactView = jobsFilter.getCompactView();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
        this.withOrderQueue = false;
        this.summary = summary;
    }

    public JobsVCallable(Folder folder, JobsFilter jobs, JOCJsonCommand jocJsonCommand, String accessToken, JsonObject summary) {
        this.job = null;
        this.jobs = null;
        this.folder = folder;
        this.jobsBody = jobs;
        this.compact = jobs.getCompact();
        this.compactView = jobs.getCompactView();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.suppressJobSchedulerObjectNotExistException = true;
        this.withOrderQueue = false;
        this.summary = summary;
    }

    @Override
    public Map<String, JobVolatileJson> call() throws JocException {
        try {
            if (job != null) {
                return getJobs(job, compact, jocJsonCommand);
            } else if (jobs != null) {
                return getJobs(jobs, jobsBody, jocJsonCommand);
            } else {
                return getJobs(folder, jobsBody, jocJsonCommand);
            }
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return new HashMap<String, JobVolatileJson>();
            }
            throw e;
        }
    }

    public JobV getJob() throws JocException {
        Map<String, JobVolatileJson> jobMap;
        JobV j = new JobV();
        j.setParams(null);
        try {
            jobMap = getJobs(job, compact, jocJsonCommand);
            if (jobMap == null || jobMap.isEmpty()) {
                return j;
            }
            return jobMap.values().iterator().next();
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return j;
            }
            throw e;
        }
    }


    private Map<String, JobVolatileJson> getJobs(String job, boolean compact, JOCJsonCommand jocJsonCommand) throws JocException {
        JsonObject json = null;
        try {
            json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(job), accessToken);
        } catch (JobSchedulerObjectNotExistException e) {
            throw e;
        }
        return getJobs(json, compact, null, null, null);
    }
    
    private Map<String, JobVolatileJson> getJobs(Collection<String> jobs, JobsFilter jobsBody, JOCJsonCommand jocJsonCommand) throws JocException {
        if (jobsBody == null) {
            return getJobs(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobs), accessToken), compact, null, null, null);
        } else {
            return getJobs(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobs), accessToken), jobsBody.getCompact(), jobsBody
                    .getRegex(), jobsBody.getStates(), jobsBody.getIsOrderJob());
        }
    }

    private Map<String, JobVolatileJson> getJobs(Folder folder, JobsFilter jobsBody, JOCJsonCommand jocJsonCommand) throws JocException {
        return getJobs(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(folder), accessToken), jobsBody.getCompact(),
                jobsBody.getRegex(), jobsBody.getStates(), jobsBody.getIsOrderJob());
    }

    private Map<String, JobVolatileJson> getJobs(JsonObject json, boolean compact, String regex, List<JobStateFilter> processingStates,
            Boolean isOrderJob) throws JocException {
        Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
        Map<String, JobVolatileJson> listJobQueue = new HashMap<String, JobVolatileJson>();
        JsonArray elements = json.getJsonArray("elements");
        if (elements == null) {
            JobVolatileJson job = new JobVolatileJson(json, compactView, withOrderQueue);
            job.setRunTimeIsTemporary(false);
            job.setSurveyDate(surveyDate);
            job.setFields(compact, jocJsonCommand.getJOCResourceImpl(), accessToken);
            listJobQueue.put(job.getPath(), job);
        } else {
            List<JobStateText> filterStates = getFilterStates(processingStates);
            for (JsonObject jobsItem : json.getJsonArray("elements").getValuesAs(JsonObject.class)) {
                
                JobVolatileJson job = new JobVolatileJson(jobsItem, compactView, summary);
                job.setPath();
                if (folder != null && "/scheduler_file_order_sink".equals(job.getPath())) {
                    continue; 
                }
                if (folder != null && "/scheduler_service_forwarder".equals(job.getPath())) {
                    continue; 
                }
                if (isOrderJob != null && isOrderJob != job.isOrderJob()) {
                    continue;
                }
                if (!FilterAfterResponse.matchRegex(regex, job.getPath())) {
                    LOGGER.debug("...processing skipped caused by 'regex=" + regex + "'");
                    continue;
                }
                job.setState();
                if (!FilterAfterResponse.filterStateHasState(filterStates, job.getState().get_text())) {
                    LOGGER.debug(String.format("...processing skipped because job's state '%1$s' doesn't contain in state filter '%2$s'", job.getState().get_text().name(), processingStates.toString()));
                    continue; 
                }
                job.setRunTimeIsTemporary(false);
                job.setSurveyDate(surveyDate);
                job.setFields(compact, jocJsonCommand.getJOCResourceImpl(), accessToken);
                listJobQueue.put(job.getPath(), job);
            } 
        }
        return listJobQueue;
    }
    

//    private boolean checkSuspendedOrdersWithFilter(OrderStateFilter processingState, List<OrderStateFilter> processingStates) {
//        if (processingState == null || processingStates == null) {
//            return true;
//        }
//        return FilterAfterResponse.filterStateHasState(processingStates, processingState);
//    }

    private String getServiceBody(String job) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", job);

        return builder.build().toString();
    }
    
    private String getServiceBody(Collection<String> jobs) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder jobsArr = Json.createArrayBuilder();
        for(String job : jobs) {
            jobsArr.add(job);
        }
        builder.add("paths", jobsArr);

        return builder.build().toString();
    }

    private String getServiceBody(Folder folder) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        // add folder path from response body
        String path = folder.getFolder();
        if (path != null && !path.isEmpty()) {
            path = ("/" + path.trim() + "/").replaceAll("//+", "/");
            if (!folder.getRecursive()) {
                path += "*";
                LOGGER.debug(String.format("...consider 'recursive=%1$b' for folder '%2$s'", folder.getRecursive(), folder.getFolder()));
            }
            builder.add("path", path);
        }

        return builder.build().toString();
    }
    
    private List<JobStateText> getFilterStates(List<JobStateFilter> jobStateFilter) {
        if (jobStateFilter == null) {
            return null;
        }
        List<JobStateText> filterStates = new ArrayList<JobStateText>();
        for (JobStateFilter filterState : jobStateFilter) {
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
                filterStates.add(JobStateText.ERROR);
                filterStates.add(JobStateText.NOT_INITIALIZED);
                filterStates.add(JobStateText.NOT_IN_PERIOD);
                filterStates.add(JobStateText.WAITING_FOR_AGENT);
                filterStates.add(JobStateText.WAITING_FOR_LOCK);
                filterStates.add(JobStateText.WAITING_FOR_PROCESS);
                filterStates.add(JobStateText.WAITING_FOR_TASK);
                break;
            }
        }
        return filterStates;
    }
}
