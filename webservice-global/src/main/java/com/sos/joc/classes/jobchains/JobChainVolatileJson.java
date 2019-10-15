package com.sos.joc.classes.jobchains;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.jobs.JobVolatileJson;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.ConfigurationStateText;
import com.sos.joc.model.jobChain.FileWatchingNodeFile;
import com.sos.joc.model.jobChain.FileWatchingNodeV;
import com.sos.joc.model.jobChain.JobChainNodeJobChainV;
import com.sos.joc.model.jobChain.JobChainNodeJobV;
import com.sos.joc.model.jobChain.JobChainNodeState;
import com.sos.joc.model.jobChain.JobChainNodeStateText;
import com.sos.joc.model.jobChain.JobChainNodeV;
import com.sos.joc.model.jobChain.JobChainState;
import com.sos.joc.model.jobChain.JobChainStateText;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.order.OrderState;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersSummary;

public class JobChainVolatileJson extends JobChainV {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainVolatileJson.class);
    private final JsonObject jobChain;
    private final JsonObject overview;
    @JsonIgnore
    private Set<String> nestedJobChains = new HashSet<String>();
    @JsonIgnore
    private final Boolean compactView;
    @JsonIgnore
    private final Map<String, String> jobDocumentations;
    @JsonIgnore
    private final Map<String, String> jobChainDocumentations;

    public JobChainVolatileJson() {
        super();
        this.jobChain = null;
        this.overview = null;
        this.compactView = true;
        this.jobDocumentations = null;
        this.jobChainDocumentations = null;
    }

    public JobChainVolatileJson(JsonObject jobChain, Boolean compactView) {
        super();
        this.jobChain = jobChain;
        this.overview = getJobChainOverview();
        this.compactView = compactView;
        this.jobDocumentations = null;
        this.jobChainDocumentations = null;
    }

    public JobChainVolatileJson(JsonObject jobChain, Boolean compactView, Map<String, String> jobDocumentations,
            Map<String, String> jobChainDocumentations) {
        super();
        this.jobChain = jobChain;
        this.overview = getJobChainOverview();
        this.jobDocumentations = jobDocumentations;
        this.jobChainDocumentations = jobChainDocumentations;
        this.compactView = compactView;
    }

    public Set<String> getNestedJobChains() {
        return nestedJobChains;
    }

    public void setPath() {
        if (getPath() == null) {
            setPath(this.overview.getString(WebserviceConstants.PATH, null));
            LOGGER.debug("...processing jobChain: " + getPath());
            if (compactView != Boolean.TRUE) {
                setInitialOrdersSummary();
            }
        }
    }

    public void setState() {
        if (getState() == null) {
            setState(new JobChainState());
            String stateText = this.overview.getString(WebserviceConstants.STATE, "");
            if ("running".equals(stateText)) {
                getState().set_text(JobChainStateText.ACTIVE);
            } else {
                try {
                    getState().set_text(JobChainStateText.fromValue(this.overview.getString(WebserviceConstants.STATE, "").toUpperCase()));
                } catch (Exception e) {
                    getState().set_text(JobChainStateText.ACTIVE);
                }
            }
            setSeverity(getState());
        }
    }

    public boolean hasJobNodes() {
        return !overview.getBoolean("hasJobChainNodes", false);
    }

    public boolean hasJobChainNodes() {
        return overview.getBoolean("hasJobChainNodes", false);
    }

    public boolean hasOrders() {
        return overview.getInt("nonBlacklistedOrderCount", 0) > 0;
    }

    public OrdersSummary setInitialOrdersSummary() {
        OrdersSummary summary = new OrdersSummary();
        summary.setBlacklist(0);
        summary.setPending(0);
        summary.setRunning(0);
        summary.setSetback(0);
        summary.setSuspended(0);
        summary.setWaitingForResource(0);
        setOrdersSummary(summary);
        return summary;
    }

    public Set<String> getJobPaths() {
        JsonArray nodes = this.jobChain.getJsonArray("nodes");
        if (nodes != null) {
            return nodes.stream().filter(i -> {
                JsonObject item = (JsonObject) i;
                if (item.getString("TYPE", "").equals("Job")) {
                    Optional<JsonValue> jobIsMissing = Optional.empty();
                    if (item.getJsonArray("obstacles") != null) {
                        jobIsMissing = item.getJsonArray("obstacles").stream().filter(p -> "MissingJob".equals(((JsonObject) p).getString("TYPE",
                                null))).findFirst();
                    }
                    return !jobIsMissing.isPresent();
                }
                return false;
            }).collect(Collectors.mapping(item -> ((JsonObject) item).getString("jobPath"), Collectors.toSet()));
        }
        return new HashSet<String>();
    }

    private void cleanArrays() {
        setNodes(null);
        setFileOrderSources(null);
        if (getBlacklist() != null && getBlacklist().size() == 0) {
            setBlacklist((List<OrderV>) null);
        }
    }

    public void setDetailedFields(Map<String, JobVolatileJson> listJobs, Collection<OrderVolatile> listOrders, Integer maxOrders)
            throws JocException {
        setFileOrderSources(getFileWatchingNodeVSchema());
        setNodes(new ArrayList<JobChainNodeV>());
        setBlacklist(jobChain.getJsonArray("blacklistedOrders"));
        setOrdersSummary(listOrders);
        JsonArray nodes = jobChain.getJsonArray("nodes");
        if (nodes != null) {
            Map<String, List<OrderV>> ordersGroupedByNode = null;
            if (!overview.getBoolean("hasJobChainNodes", false)) {
                if (listOrders != null) {
                    ordersGroupedByNode = listOrders.stream().filter(o -> o.getState() != null).collect(Collectors.groupingBy(OrderVolatile::getState,
                            Collectors.toList()));
                }
                for (JsonObject nodeItem : nodes.getValuesAs(JsonObject.class)) {
                    if (!"Job".equals(nodeItem.getString("TYPE", null))) {
                        continue;
                    }
                    JobChainNodeV node = new JobChainNodeV();
                    node.setName(nodeItem.getString("nodeId", ""));
                    node.setLevel(node.getName().replaceAll("[^:]", "").length());
                    node.setState(getNodeState(nodeItem));
                    node.setNumOfOrders(nodeItem.getInt("orderCount", 0));
                    List<OrderV> currentOrders = null;
                    if (ordersGroupedByNode != null) {
                        currentOrders = ordersGroupedByNode.get(node.getName());
                    }
                    if (maxOrders != null && currentOrders != null && currentOrders.size() > maxOrders) {
                        node.setOrders(currentOrders.stream().limit(maxOrders).collect(Collectors.toList()));
                    } else {
                        node.setOrders(currentOrders);
                    }
                    JobChainNodeJobV job = new JobChainNodeJobV();
                    job.setPath(nodeItem.getString("jobPath", ""));
                    if (jobDocumentations != null) {
                        job.setDocumentation(jobDocumentations.get(job.getPath()));
                    }

                    Optional<JsonValue> jobIsMissing = Optional.empty();
                    if (nodeItem.getJsonArray("obstacles") != null) {
                        jobIsMissing = nodeItem.getJsonArray("obstacles").stream().filter(p -> "MissingJob".equals(((JsonObject) p).getString("TYPE",
                                ""))).findFirst();
                    }

                    if (jobIsMissing.isPresent()) {
                        // JOC-131: If Job is missing do not throw NPE, rather set state to Severity -> 2, Text -> RESOURCE_IS_MISSING
                        ConfigurationState confStatus = new ConfigurationState();
                        confStatus.setSeverity(2);
                        confStatus.setMessage(ConfigurationStateText.RESOURCE_IS_MISSING.toString());
                        job.setConfigurationStatus(confStatus);
                    } else {
                        JobVolatileJson jobV = listJobs.get(job.getPath());
                        // JOC-89
                        jobV.setState(hasRunningOrWaitingOrder(currentOrders));
                        job.setState(jobV.getState());
                    }
                    node.setJob(job);
                    getNodes().add(node);
                }
            } else {
                if (listOrders != null) {
                    ordersGroupedByNode = listOrders.stream().collect(Collectors.groupingBy(OrderVolatile::getJobChain, Collectors.toList()));
                }
                for (JsonObject nodeItem : nodes.getValuesAs(JsonObject.class)) {
                    if (!"NestedJobChain".equals(nodeItem.getString("TYPE", null))) {
                        continue;
                    }
                    JobChainNodeV node = new JobChainNodeV();
                    JobChainNodeJobChainV jobChainNode = new JobChainNodeJobChainV();
                    jobChainNode.setPath(nodeItem.getString("nestedJobChainPath", null));
                    nestedJobChains.add(jobChainNode.getPath());
                    if (jobChainDocumentations != null) {
                        jobChainNode.setDocumentation(jobChainDocumentations.get(jobChainNode.getPath()));
                    }
                    List<OrderV> currentOrders = null;
                    if (ordersGroupedByNode != null) {
                        currentOrders = ordersGroupedByNode.get(jobChainNode.getPath());
                    }
                    node.setJobChain(jobChainNode);
                    node.setName(nodeItem.getString("nodeId", ""));
                    node.setLevel(0);
                    node.setState(getNodeState(nodeItem));
                    if (maxOrders != null && currentOrders != null && currentOrders.size() > maxOrders) {
                        node.setOrders(currentOrders.stream().limit(maxOrders).collect(Collectors.toList()));
                    } else {
                        node.setOrders(currentOrders);
                    }
                    node.setNumOfOrders(currentOrders == null ? 0 : currentOrders.size());
                    getNodes().add(node);
                }
            }
        }
    }

    private boolean hasRunningOrWaitingOrder(List<OrderV> orders) {
        if (orders == null) {
            return false;
        }
        for (OrderV order : orders) {
            if (order != null && order.getProcessingState() != null) {
                switch (order.getProcessingState().get_text()) {
                case RUNNING:
                case JOB_NOT_IN_PERIOD:
                case NODE_DELAY:
                case WAITING_FOR_LOCK:
                case WAITING_FOR_PROCESS:
                case WAITING_FOR_TASK:
                    return true;
                default:
                    break;
                }
            }
        }
        return false;
    }

    private void setBlacklist(JsonArray blacklist) {
        List<OrderV> orders = new ArrayList<OrderV>();
        if (blacklist != null && !blacklist.isEmpty()) {
            for (JsonObject blacklistedOrder : blacklist.getValuesAs(JsonObject.class)) {
                OrderV order = new OrderV();
                order.set_type(OrderType.FILE_ORDER);
                JsonArray obstacles = blacklistedOrder.getJsonArray("obstacles");
                order.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(obstacles));
                order.setHistoryId(blacklistedOrder.getInt("history_id", 0) + "");
                order.setPath(blacklistedOrder.getString("path"));
                order.setJobChain(blacklistedOrder.getString("jobChainPath"));
                order.setOrderId(order.getPath().substring(order.getJobChain().length() + 1));
                OrderState orderState = new OrderState();
                orderState.set_text(OrderStateText.BLACKLIST);
                orderState.setSeverity(3);
                order.setProcessingState(orderState);
                order.setStartedAt(JobSchedulerDate.getDateFromISO8601String(blacklistedOrder.getString("startedAt", Instant.EPOCH.toString())));
                order.setState(blacklistedOrder.getString("nodeId", null));
                order.setParams(null);
                orders.add(order);
            }
        }
        if (orders.size() == 0) {
            orders = null;
        }
        setBlacklist(orders);
    }

    public void setCompactFields() throws JocException {
        setPath();
        setState();
        setName(Paths.get(getPath()).getFileName().toString());
        setNumOfNodes(overview.getInt("jobOrJobChainNodeCount", 0));
        setNumOfOrders(overview.getInt("nonBlacklistedOrderCount", 0) + overview.getInt("blacklistedOrderCount", 0));
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(overview.getJsonArray("obstacles")));
        cleanArrays();
    }

    private List<FileWatchingNodeV> getFileWatchingNodeVSchema() {
        JsonArray fileOrderSources = this.jobChain.getJsonArray("fileOrderSources");
        if (fileOrderSources == null || fileOrderSources.isEmpty()) {
            return null;
        }
        List<FileWatchingNodeV> fileWatchingNodes = new ArrayList<FileWatchingNodeV>();
        for (JsonObject item : fileOrderSources.getValuesAs(JsonObject.class)) {
            FileWatchingNodeV fileOrderSource = new FileWatchingNodeV();
            fileOrderSource.setAlertWhenDirectoryMissing(item.getBoolean("alertWhenDirectoryMissing", true));
            fileOrderSource.setDelayAfterError(item.getInt("delayAfterError", Integer.MAX_VALUE));
            if (fileOrderSource.getDelayAfterError() >= Integer.MAX_VALUE) {
                fileOrderSource.setDelayAfterError(null);
            }
            fileOrderSource.setDirectory(item.getString("directory", null));
            JsonArray files = item.getJsonArray("files");
            if (files == null) {
                fileOrderSource.setFiles(null);
            } else {
                List<FileWatchingNodeFile> filelist = new ArrayList<FileWatchingNodeFile>();
                for (JsonObject fileItem : files.getValuesAs(JsonObject.class)) {
                    FileWatchingNodeFile file = new FileWatchingNodeFile();
                    file.setModified(JobSchedulerDate.getDateFromISO8601String(fileItem.getString("lastModified", Instant.EPOCH.toString())));
                    file.setPath(fileItem.getString("file", null));
                    filelist.add(file);
                }
                fileOrderSource.setFiles(filelist);
            }
            fileOrderSource.setRegex(item.getString("regex", ".*"));
            fileOrderSource.setRepeat(item.getInt("repeat", 60));
            fileWatchingNodes.add(fileOrderSource);
        }
        return fileWatchingNodes;
    }

    private void setSeverity(JobChainState state) {
        switch (state.get_text()) {
        case ACTIVE:
            state.setSeverity(4);
            break;
        case INITIALIZED:
            state.setSeverity(3);
            break;
        case NOT_INITIALIZED:
        case STOPPED:
        case UNDER_CONSTRUCTION:
            state.setSeverity(2);
            break;
        }
    }

    private JobChainNodeState getNodeState(JsonObject node) {
        JobChainNodeState state = new JobChainNodeState();
        switch (node.getString("action", "")) {
        case "stop":
            state.setSeverity(2);
            state.set_text(JobChainNodeStateText.STOPPED);
            return state;
        case "next_state":
            state.setSeverity(5);
            state.set_text(JobChainNodeStateText.SKIPPED);
            return state;
        default:
            state.setSeverity(4);
            state.set_text(JobChainNodeStateText.ACTIVE);
            return state;
        }
    }

    private void setOrdersSummary(Collection<OrderVolatile> orders) {
        OrdersSummary summary = null;
        if (compactView != Boolean.TRUE) {
            summary = setInitialOrdersSummary();
            if (orders != null) {
                Map<String, Long> sums = orders.stream().filter(o -> o.getProcessingState() != null).collect(Collectors.groupingBy(o -> {
                    OrderVolatile ov = (OrderVolatile) o;
                    OrderStateText oStateText = ov.getProcessingState().get_text();
                    switch (oStateText) {
                    case BLACKLIST:
                        return "blacklist";
                    case JOB_CHAIN_STOPPED:
                    case JOB_NOT_IN_PERIOD:
                    case JOB_STOPPED:
                    case NODE_DELAY:
                    case NODE_STOPPED:
                    case WAITING_FOR_AGENT:
                    case WAITING_FOR_LOCK:
                    case WAITING_FOR_PROCESS:
                    case WAITING_FOR_TASK:
                        return "waitingForResource";
                    case PENDING:
                        return "pending";
                    case RUNNING:
                        return "running";
                    case SETBACK:
                        return "setback";
                    case SUSPENDED:
                        return "suspended";
                    }
                    return "";
                }, Collectors.counting()));
                sums.putIfAbsent("blacklist", 0L);
                sums.putIfAbsent("waitingForResource", 0L);
                sums.putIfAbsent("pending", 0L);
                sums.putIfAbsent("running", 0L);
                sums.putIfAbsent("setback", 0L);
                sums.putIfAbsent("suspended", 0L);
                summary.setBlacklist(sums.get("blacklist").intValue());
                summary.setWaitingForResource(sums.get("waitingForResource").intValue());
                summary.setPending(sums.get("pending").intValue());
                summary.setRunning(sums.get("running").intValue());
                summary.setSetback(sums.get("setback").intValue());
                summary.setSuspended(sums.get("suspended").intValue());
            }
            if (getBlacklist() != null && summary.getBlacklist() == 0) {
                summary.setBlacklist(getBlacklist().size());
            }
        }
        setOrdersSummary(summary);
    }

    private JsonObject getJobChainOverview() {
        return jobChain.containsKey("overview") ? jobChain.getJsonObject("overview") : jobChain;
    }
}
