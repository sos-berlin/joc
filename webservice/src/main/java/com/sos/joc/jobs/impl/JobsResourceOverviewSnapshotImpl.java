package com.sos.joc.jobs.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobs.resource.IJobsResourceOverviewSnapshot;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobsSnapshot;
import com.sos.joc.model.job.JobsSummary;

@Path("jobs")
public class JobsResourceOverviewSnapshotImpl extends JOCResourceImpl implements IJobsResourceOverviewSnapshot {

    private static final String API_CALL = "./jobs/overview/snapshot";
    public class SummaryStatistics {

        private int count = 0;
        private int countRunningTasks = 0;

        public void accept(JsonValue jv) {
            ++count;
            countRunningTasks += ((JsonObject) jv).getInt("usedTaskCount", 0);
        }
        
        public SummaryStatistics combine(SummaryStatistics other) {
            this.count += other.count;
            this.countRunningTasks += other.countRunningTasks;
            return this;
        }
        
        public final int getCount() {
            return count;
        }
        
        public final int getCountRunningTasks() {
            return countRunningTasks;
        }
    }

    @Override
    public JOCDefaultResponse postJobsOverviewSnapshot(String accessToken, JobSchedulerId jobScheduler) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobScheduler, accessToken, jobScheduler.getJobschedulerId(),
                    getPermissonsJocCockpit(jobScheduler.getJobschedulerId(), accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobsSnapshot entity = new JobsSnapshot();
            JobsSummary jobs = new JobsSummary();

            if (versionIsOlderThan("1.12.6")) {
                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
                String command = jocXmlCommand.getShowStateCommand("job folder", "folders", "", 0, 0);
                jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
                Integer countPending = 0;
                Integer countRunning = 0;
                Integer countStopped = 0;
                Integer countWaitingForResource = 0;
                Integer countRunningTasks = 0;
                Integer countQueuedTasks = 0;
                Integer curRunningTasks = 0;
                Integer curQueuedTasks = 0;
                NodeList jobStatistics = jocXmlCommand.getSosxml().selectNodeList(
                        "//job[not(@path='/scheduler_file_order_sink') and not(@path='/scheduler_service_forwarder') and @enabled='yes']");
                Set<Folder> folders = folderPermissions.getListOfFolders();
                for (int i = 0; i < jobStatistics.getLength(); i++) {
                    Element job = (Element) jobStatistics.item(i);
                    if (!jobIsInFolder(job.getAttribute("path"), folders)) {
                        continue;
                    }
                    curRunningTasks = getNumOfRunningTasks(jocXmlCommand, job);
                    curQueuedTasks = getNumOfQueuedTasks(jocXmlCommand, job);
                    countRunningTasks = countRunningTasks + curRunningTasks;
                    countQueuedTasks = countQueuedTasks + curQueuedTasks;
                    JobStateText jobStateText = setState(jocXmlCommand, job, curRunningTasks, curQueuedTasks);
                    switch (jobStateText) {
                    case PENDING:
                        countPending++;
                        break;
                    case STOPPED:
                        countStopped++;
                        break;
                    case STOPPING:
                    case RUNNING:
                        countRunning++;
                        break;
                    case ERROR:
                    case NOT_IN_PERIOD:
                    case NOT_INITIALIZED:
                    case WAITING_FOR_AGENT:
                    case WAITING_FOR_LOCK:
                    case WAITING_FOR_PROCESS:
                    case WAITING_FOR_TASK:
                        countWaitingForResource++;
                        break;
                    case UNKNOWN:
                    case LOADED:
                    case INITIALIZED:
                    case DISABLED:
                        break;
                    }
                }
                jobs.setPending(countPending);
                jobs.setRunning(countRunning);
                jobs.setStopped(countStopped);
                jobs.setWaitingForResource(countWaitingForResource);
                jobs.setTasks(countRunningTasks);

                entity.setSurveyDate(jocXmlCommand.getSurveyDate());
            } else {
                JOCJsonCommand command = new JOCJsonCommand(this);
                command.setUriBuilderForJobs();
                command.addJobCompactQuery(true);
                
                JsonObject json = command.getJsonObjectFromPostWithRetry("{\"path\":\"/\"}", accessToken);
                entity.setSurveyDate(JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue()));
                JsonArray elements = json.getJsonArray("elements");
                
                Map<String, SummaryStatistics> summary = elements.stream().filter(jobIsEnabled).collect(Collectors.groupingBy(p -> {
                    JsonObject overview = ((JsonObject) p);
                    JsonArray obstacles = overview.getJsonArray("obstacles");
                    String stateText = overview.getString("state", "UNKNOWN").toUpperCase();
                    switch (stateText) {
                    case "STOPPED":
                        return "stopped";
                    case "ERROR":
                    case "NOT_INITIALIZED":
                        return "waitingForResource";
                    case "UNKNOWN":
                    case "LOADED":
                    case "INITIALIZED":
                        return "unknown";
                    }
                    Optional<JsonValue> obstacleWaitingTypeObject = obstacles.stream().filter(o -> ((JsonObject) o).getString("TYPE", "").startsWith("WaitingFor")).findFirst();
                    if (obstacleWaitingTypeObject.isPresent()) {
                        String obstacleWaitingType = ((JsonObject) obstacleWaitingTypeObject.get()).getString("TYPE");
                        if (obstacleWaitingType.equals("WaitingForLocks")) {
                            return "waitingForResource";
                        } else if (obstacleWaitingType.equals("WaitingForProcessClass")) {
                            return "waitingForResource";
                        }
                    } else if (overview.getBoolean("isOrderJob", false) && "PENDING".equals(stateText) && !overview.getBoolean("isInPeriod", true)) {
                        return "waitingForResource";
                    } else if (isWaitingForAgent(overview.getJsonObject("taskObstacles"))) {
                        return "waitingForResource";
                    }
                    switch (stateText) {
                    case "PENDING":
                        return "pending";
                    case "STOPPING":
                    case "RUNNING":
                        return "running";
                    }
                    return "unknown";
                }, Collector.of(SummaryStatistics::new, SummaryStatistics::accept, SummaryStatistics::combine)));
                
                summary.putIfAbsent("pending", new SummaryStatistics());
                summary.putIfAbsent("stopped", new SummaryStatistics());
                summary.putIfAbsent("running", new SummaryStatistics());
                summary.putIfAbsent("waitingForResource", new SummaryStatistics());
                
                jobs.setPending(summary.get("pending").getCount());
                jobs.setRunning(summary.get("running").getCount());
                jobs.setStopped(summary.get("stopped").getCount());
                jobs.setWaitingForResource(summary.get("waitingForResource").getCount());
                jobs.setTasks(summary.get("running").getCountRunningTasks() + summary.get("waitingForResource").getCountRunningTasks());
            }
            entity.setJobs(jobs);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JobSchedulerConnectionResetException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatus434JSError(e);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }
    
    public static Predicate<JsonValue> jobIsEnabled = new Predicate<JsonValue>() {

        @Override
        public boolean test(JsonValue p) {
            try {
                JsonObject overview = ((JsonObject) p);
                String path = overview.getString("path", "");
                if (path.isEmpty() || path.equals("/scheduler_file_order_sink") || path.equals("/scheduler_service_forwarder") || !overview.getBoolean("enabled", true)) {
                    return false;
                } else {
                    ConfigurationState confState = ConfigurationStatus.getConfigurationStatus(overview.getJsonArray("obstacles"));
                    if (confState != null && confState.getSeverity() == 2) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        
    };
    
    public static boolean isWaitingForAgent(JsonObject taskObstacles) {
        if (taskObstacles != null) {
            for (JsonValue j : taskObstacles.values()) {
                if (j.getValueType() == ValueType.ARRAY) {
                    if (((JsonArray) j).stream().filter(p -> ((JsonObject) p).getString("TYPE", "").equals("WaitingForAgent")).findFirst().isPresent()) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    private JobStateText setState(JOCXmlCommand jocXmlCommand, Element job, Integer runningTasks, Integer queuedTasks) throws Exception {
        JobStateText jobStateText = JobStateText.UNKNOWN;
        ConfigurationState confState = ConfigurationStatus.getConfigurationStatus(job);
        if (!jocXmlCommand.getBoolValue(job.getAttribute("enabled"), true)) {
            jobStateText = JobStateText.DISABLED;
        } else if (confState != null && confState.getSeverity() == 2) {
            jobStateText = JobStateText.ERROR;
        } else {
            try {
                jobStateText = JobStateText.fromValue(job.getAttribute(WebserviceConstants.STATE).toUpperCase());
            } catch (Exception e) {
                jobStateText = JobStateText.fromValue("UNKNOWN");
            }
            jocXmlCommand.getSosxml().selectNodeList(job, "tasks/task[@waiting_for_remote_scheduler='true']").getLength();
            if (jocXmlCommand.getSosxml().selectNodeList(job, "tasks/task[@waiting_for_remote_scheduler='true']").getLength() > 0) {
                jobStateText = JobStateText.WAITING_FOR_AGENT;
            } else if (jocXmlCommand.getBoolValue(job.getAttribute(WebserviceConstants.WAITING_FOR_PROCESS), false)) {
                jobStateText = JobStateText.WAITING_FOR_PROCESS;
            } else if (jocXmlCommand.getSosxml().selectNodeList(job, "lock.requestor/lock.use[@is_available='no']").getLength() > 0) {
                jobStateText = JobStateText.WAITING_FOR_LOCK;
                // } else if (runningTasks == Integer.parseInt(getAttributeValue(job, "tasks",
                // "1")) && queuedTasks > 0) {
                // TODO: WaitingForTask has to be improved
                // Look into queue items where start_time in the past
                // it could be that a task is queued caused of a delayed
                // start instead of max tasks is reached
                // jobStateText = JobStateText.WAITING_FOR_TASK;
            } else if (isOrderJob(job) && jobStateText == JobStateText.PENDING && !jocXmlCommand.getBoolValue(job.getAttribute(
                    WebserviceConstants.IN_PERIOD), true)) {
                jobStateText = JobStateText.NOT_IN_PERIOD;
            }
        }
        return jobStateText;
    }

    private String getAttributeValue(Element job, String attributeName, String default_) {
        String val = job.getAttribute(attributeName);
        if (val == null || val.isEmpty()) {
            val = default_;
        }
        return val;
    }

    private Integer getNumOfRunningTasks(JOCXmlCommand jocXmlCommand, Element job) {
        try {
            return Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "tasks/@count", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    private Integer getNumOfQueuedTasks(JOCXmlCommand jocXmlCommand, Element job) {
        try {
            return Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(job, "queued_tasks/@length", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    private Boolean isOrderJob(Element job) {
        return "yes".equals(getAttributeValue(job, "order", "no"));
    }

    private boolean jobIsInFolder(String path, Set<Folder> folders) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return canAdd(path, folders);
    }
}
