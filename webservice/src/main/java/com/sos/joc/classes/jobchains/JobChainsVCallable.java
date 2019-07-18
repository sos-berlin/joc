package com.sos.joc.classes.jobchains;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.jobs.JobVolatileJson;
import com.sos.joc.classes.jobs.JobsVCallable;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainsFilter;

public class JobChainsVCallable implements Callable<Map<String, JobChainVolatileJson>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsVCallable.class);

    private final String jobChain;
    private final Folder folder;
    private final Boolean compact;
    private final Boolean compactView;
    private final Boolean withOrders;
    private final Integer maxOrders;
    private final JobChainsFilter jobChainsFilter;

    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;

    private final Map<String, String> jobDocumentations;
    private final Map<String, String> jobChainDocumentations;
    private final Map<String, String> orderDocumentations;
    private final Map<String, List<OrderVolatile>> groupedOrders;

    private final Boolean suppressJobSchedulerObjectNotExistException;
    // private Set<String> nestedJobChains = new HashSet<String>();

    public JobChainsVCallable(JobChainFilter filter, JOCJsonCommand jocJsonCommand, String accessToken, Map<String, String> jobDocumentations,
            Map<String, String> jobChainDocumentations, Map<String, String> orderDocumentations) {
        this.jobChain = filter.getJobChain();
        this.folder = null;
        this.compact = filter.getCompact();
        this.compactView = filter.getCompactView();
        this.withOrders = compact != Boolean.TRUE;
        this.maxOrders = filter.getMaxOrders();
        this.jobChainsFilter = new JobChainsFilter();
        ;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.jobDocumentations = jobDocumentations;
        this.jobChainDocumentations = jobChainDocumentations;
        this.orderDocumentations = withOrders ? orderDocumentations : null;
        this.groupedOrders = null;
        this.suppressJobSchedulerObjectNotExistException = false;
    }

    public JobChainsVCallable(String jobChain, JobChainsFilter filter, JOCJsonCommand jocJsonCommand, String accessToken,
            Map<String, String> jobDocumentations, Map<String, String> jobChainDocumentations, Map<String, List<OrderVolatile>> groupedOrders, Map<String, String> orderDocumentations) {
        this.jobChain = jobChain;
        this.folder = null;
        this.compact = filter.getCompact();
        this.compactView = filter.getCompactView();
        this.withOrders = compact != Boolean.TRUE;
        this.maxOrders = filter.getMaxOrders();
        this.jobChainsFilter = filter;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.jobDocumentations = jobDocumentations;
        this.jobChainDocumentations = jobChainDocumentations;
        this.orderDocumentations = withOrders ? orderDocumentations : null;
        this.groupedOrders = null;
        this.suppressJobSchedulerObjectNotExistException = true;
    }

    public JobChainsVCallable(String jobChain, JobChainsFilter filter, JOCJsonCommand jocJsonCommand, String accessToken,
            Map<String, String> jobDocumentations, Map<String, String> jobChainDocumentations, Map<String, List<OrderVolatile>> groupedOrders) {
        this.jobChain = jobChain;
        this.folder = null;
        this.compact = filter.getCompact();
        this.compactView = filter.getCompactView();
        this.withOrders = Boolean.FALSE;
        this.maxOrders = filter.getMaxOrders();
        this.jobChainsFilter = filter;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.jobDocumentations = jobDocumentations;
        this.jobChainDocumentations = jobChainDocumentations;
        this.orderDocumentations = null;
        this.groupedOrders = groupedOrders;
        this.suppressJobSchedulerObjectNotExistException = true;
    }

    public JobChainsVCallable(Folder folder, JobChainsFilter filter, JOCJsonCommand jocJsonCommand, String accessToken,
            Map<String, String> jobDocumentations, Map<String, String> jobChainDocumentations, Map<String, List<OrderVolatile>> groupedOrders) {
        this.jobChain = null;
        this.folder = folder;
        this.compact = filter.getCompact();
        this.compactView = filter.getCompactView();
        this.withOrders = Boolean.FALSE;
        this.maxOrders = filter.getMaxOrders();
        this.jobChainsFilter = filter;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.jobDocumentations = jobDocumentations;
        this.jobChainDocumentations = jobChainDocumentations;
        this.orderDocumentations = null;
        this.groupedOrders = groupedOrders;
        this.suppressJobSchedulerObjectNotExistException = true;
    }

    @Override
    public Map<String, JobChainVolatileJson> call() throws Exception {
        try {
            if (jobChain != null) {
                return getJobChains(getServiceBody(jobChain));
            } else {
                return getJobChains(getServiceBody(folder));
            }
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return new HashMap<String, JobChainVolatileJson>();
            }
            throw e;
        }
    }

    public JobChainVolatileJson getJobChain() throws Exception {
        jocJsonCommand.setUriBuilderForJobChains();
        jocJsonCommand.addJobChainCompactQuery(compact);
        JsonObject json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(jobChain), accessToken);
        return getJobChain(json);
    }

    private JobChainVolatileJson getJobChain(JsonObject json) throws Exception {
        JobChainVolatileJson jobChainV = new JobChainVolatileJson(json, compactView, jobDocumentations, jobChainDocumentations);
        jobChainV.setSurveyDate(JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue()));
        return getJobChain(jobChainV);
    }

    private JobChainVolatileJson getJobChain(JobChainVolatileJson jobChainV) throws Exception {
        jobChainV.setCompactFields();
        Collection<OrderVolatile> orders = null;
        if (jobChainV.hasOrders()) {
            if (withOrders) {
                OrdersVCallable ordersVCallable = new OrdersVCallable(jobChainV, setUriForOrdersJsonCommand(), orderDocumentations, accessToken);
                orders = ordersVCallable.call().values();
            } else {
                orders = groupedOrders.get(jobChainV.getPath());
            }
        }
        if (compact != Boolean.TRUE) {
            if (jobChainV.hasJobNodes()) {
                jobChainV.setDetailedFields(getJobsOfJobChain(jobChainV), orders, maxOrders);
            } else {
                jobChainV.setDetailedFields(null, orders, maxOrders);
            }
        }
        return jobChainV;
    }

    private Map<String, JobChainVolatileJson> getJobChains(String body) throws Exception {
        jocJsonCommand.setUriBuilderForJobChains();
        if (jobChainsFilter.getJob() != null) {
            jocJsonCommand.addJobChainCompactQuery(false);
        } else {
            jocJsonCommand.addJobChainCompactQuery(compact);
        }
        return getJobChains((JsonObject) jocJsonCommand.getJsonObjectFromPostWithRetry(body, accessToken));
    }

    private Map<String, JobChainVolatileJson> getJobChains(JsonObject json) throws Exception {
        Map<String, JobChainVolatileJson> listJobChainQueue = new HashMap<String, JobChainVolatileJson>();
        JsonArray elements = json.getJsonArray("elements");
        if (elements == null) {
            JobChainVolatileJson jobChain = getJobChain(json);
            listJobChainQueue.put(jobChain.getPath(), getJobChain(json));
        } else {
            Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
            Pattern regex = null;
            Pattern jobRegex = null;
            if (jobChainsFilter.getRegex() != null && !jobChainsFilter.getRegex().isEmpty()) {
                regex = Pattern.compile(jobChainsFilter.getRegex());
            }
            if (jobChainsFilter.getJob() != null && jobChainsFilter.getJob().getRegex() != null && !jobChainsFilter.getJob().getRegex().isEmpty()) {
                jobRegex = Pattern.compile(jobChainsFilter.getJob().getRegex());
            }

            for (JsonObject jobChainsItem : json.getJsonArray("elements").getValuesAs(JsonObject.class)) {

                JobChainVolatileJson jobChain = new JobChainVolatileJson(jobChainsItem, compactView, jobDocumentations, jobChainDocumentations);
                jobChain.setPath();
                if (!FilterAfterResponse.matchRegex(regex, jobChain.getPath())) {
                    LOGGER.debug("...processing skipped caused by 'regex=" + jobChainsFilter.getRegex() + "'");
                    continue;
                }

                jobChain.setState();
                if (!FilterAfterResponse.filterStateHasState(jobChainsFilter.getStates(), jobChain.getState().get_text())) {
                    LOGGER.debug(String.format("...processing skipped because job's state '%1$s' doesn't contain in state filter '%2$s'", jobChain
                            .getState().get_text().name(), jobChainsFilter.getStates().toString()));
                    continue;
                }

                if (jobChainsFilter.getJob() != null) {
                    if (jobChain.getJobPaths() == null || jobChain.getJobPaths().isEmpty()) {
                        continue;
                    }
                    if (!FilterAfterResponse.matchRegex(jobRegex, jobChain.getJobPaths())) {
                        LOGGER.debug("...processing skipped caused by 'jobRegex=" + jobChainsFilter.getJob().getRegex() + "'");
                        continue;
                    }
                    if (jobChainsFilter.getJob().getFolders() != null && !jobChainsFilter.getJob().getFolders().isEmpty()) {
                        boolean folderFound = false;
                        for (String jobPathStr : jobChain.getJobPaths()) {
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
                jobChain.setSurveyDate(surveyDate);
                listJobChainQueue.put(jobChain.getPath(), getJobChain(jobChain));
            }
        }
        return listJobChainQueue;
    }

    private String getServiceBody(String job) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", job);

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

    private Map<String, JobVolatileJson> getJobsOfJobChain(JobChainVolatileJson jobChainV) throws Exception {
        JOCJsonCommand command = new JOCJsonCommand(jocJsonCommand);
        command.setUriBuilderForJobs();
        command.addJobCompactQuery(true);
        List<JobsVCallable> tasks = new ArrayList<JobsVCallable>();
        Map<String, JobVolatileJson> listJobs = new HashMap<String, JobVolatileJson>();
        for (String jobPath : jobChainV.getJobPaths()) {
            tasks.add(new JobsVCallable(jobPath, new JOCJsonCommand(command), accessToken));
        }

        if (tasks != null && !tasks.isEmpty()) {
            if (tasks.size() == 1) {
                listJobs = tasks.get(0).call();
            } else {
                ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, tasks.size()));
                try {
                    for (Future<Map<String, JobVolatileJson>> result : executorService.invokeAll(tasks)) {
                        try {
                            listJobs.putAll(result.get());
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
        return listJobs;
    }

    private JOCJsonCommand setUriForOrdersJsonCommand() {
        JOCJsonCommand jsonOrdersCommand = new JOCJsonCommand(jocJsonCommand);
        jsonOrdersCommand.setUriBuilderForOrders();
        jsonOrdersCommand.addOrderCompactQuery(false);
        return jsonOrdersCommand;
    }

}
