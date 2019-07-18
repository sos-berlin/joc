package com.sos.joc.classes.orders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.jobchains.JobChainVolatile;
import com.sos.joc.classes.jobchains.JobChainVolatileJson;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderStateFilter;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersFilter;

public class OrdersVCallable implements Callable<Map<String, OrderVolatile>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersVCallable.class);
    private final String job;
    private final OrdersPerJobChain orders;
    private final Folder folder;
    private final Path folderPath;
    private final OrdersFilter ordersBody;
    private final Boolean compact;
    private final JOCJsonCommand jocJsonCommand;
    private final String accessToken;
    private final String clusterMemberId;
    private final Boolean suppressJobSchedulerObjectNotExistException;
    private final Boolean checkOrderPathIsInFolder;
    private final Map<String, String> orderDocumentations;
    private List<String> yadeJobs;
    private SOSHibernateSession connection = null;

    public OrdersVCallable(String job, Boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.orders = null;
        this.job = ("/" + job.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        this.folder = null;
        this.folderPath = null;
        this.ordersBody = null;
        this.compact = compact;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = false;
        this.yadeJobs = null;
        this.orderDocumentations = null;
    }

    public OrdersVCallable(OrdersPerJobChain orders, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.orders = orders;
        this.job = null;
        this.folder = null;
        this.folderPath = Paths.get(orders.getJobChain()).getParent();
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = null;
        this.yadeJobs = null;
        this.orderDocumentations = null;
    }

    public OrdersVCallable(OrdersPerJobChain orders, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand, String accessToken, List<String> yadeJobs) {
        this.orders = orders;
        this.job = null;
        this.folder = null;
        this.folderPath = Paths.get(orders.getJobChain()).getParent();
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = null;
        this.yadeJobs = yadeJobs;
        this.orderDocumentations = null;
    }

    public OrdersVCallable(JobChainVolatile jobChain, JOCJsonCommand jocJsonCommand, Map<String, String> orderDocumentations, String accessToken) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(jobChain.getPath());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.folderPath = Paths.get(jobChain.getPath()).getParent();
        this.ordersBody = null;
        this.compact = false;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = jobChain.hasJobChainNodes();
        this.yadeJobs = null;
        this.orderDocumentations = orderDocumentations;
    }
    
    public OrdersVCallable(JobChainVolatileJson jobChain, JOCJsonCommand jocJsonCommand, Map<String, String> orderDocumentations, String accessToken) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(jobChain.getPath());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.folderPath = Paths.get(jobChain.getPath()).getParent();
        this.ordersBody = null;
        this.compact = false;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = jobChain.hasJobChainNodes();
        this.yadeJobs = null;
        this.orderDocumentations = orderDocumentations;
    }

    public OrdersVCallable(OrderFilter order, JOCJsonCommand jocJsonCommand, String accessToken) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(order.getJobChain());
        o.addOrder(order.getOrderId());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.folderPath = Paths.get(order.getJobChain()).getParent();
        this.ordersBody = null;
        this.compact = order.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = order.getSuppressNotExistException();
        this.checkOrderPathIsInFolder = null;
        this.yadeJobs = null;
        this.orderDocumentations = null;
    }
    
    public OrdersVCallable(OrderVolatile order, Boolean compact, JOCJsonCommand jocJsonCommand, String accessToken) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(order.getJobChain());
        o.addOrder(order.getOrderId());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.folderPath = Paths.get(order.getJobChain()).getParent();
        this.ordersBody = null;
        this.compact = compact;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = false;
        this.checkOrderPathIsInFolder = null;
        this.yadeJobs = null;
        this.orderDocumentations = null;
    }
    
    public OrdersVCallable(Folder folder, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand, String accessToken) {
        this.orders = null;
        this.job = null;
        this.folder = folder;
        this.folderPath = Paths.get(folder.getFolder());
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = !("/".equals(folder.getFolder()));
        this.yadeJobs = null;
        this.orderDocumentations = null;
    }

    public OrdersVCallable(Folder folder, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand, String accessToken, List<String> yadeJobs) {
        this.orders = null;
        this.job = null;
        this.folder = folder;
        this.folderPath = Paths.get(folder.getFolder());
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = !("/".equals(folder.getFolder()));
        this.yadeJobs = yadeJobs;
        this.orderDocumentations = null;
    }
    
    public OrdersVCallable(Folder folder, JOCJsonCommand jocJsonCommand, Map<String, String> orderDocumentations, String accessToken) {
        this.orders = null;
        this.job = null;
        this.folder = folder;
        this.folderPath = Paths.get(folder.getFolder());
        OrdersFilter ordersBody = new OrdersFilter();
        ordersBody.setCompact(false);
        ordersBody.setProcessingStates(null);
        ordersBody.setRegex(null);
        ordersBody.setRunTimeIsTemporary(false);
        ordersBody.setTypes(null);
        this.ordersBody = ordersBody;
        this.compact = false;
        this.jocJsonCommand = jocJsonCommand;
        this.accessToken = accessToken;
        this.clusterMemberId = jocJsonCommand.getClusterMemberId();
        this.suppressJobSchedulerObjectNotExistException = true;
        this.checkOrderPathIsInFolder = !("/".equals(folder.getFolder()));
        this.yadeJobs = null;
        this.orderDocumentations = orderDocumentations;
    }

    @Override
    public Map<String, OrderVolatile> call() throws JocException {
        try {
            if (orders != null && ordersBody == null) {
                return getOrders(orders, compact, jocJsonCommand);
            } else if (orders != null && ordersBody != null) {
                return getOrders(orders, ordersBody, jocJsonCommand);
            } else if (job != null) {
                return getOrders(job, compact, jocJsonCommand);
            } else {
                return getOrders(folder, ordersBody, jocJsonCommand);
            }
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return new HashMap<String, OrderVolatile>();
            }
            throw e;
        }
    }

    public OrderV getOrder() throws JocException {
        Map<String, OrderVolatile> orderMap;
        OrderV o = new OrderV();
        o.setParams(null);
        try {
            orderMap = getOrders(orders, compact, jocJsonCommand);
            if (orderMap == null || orderMap.isEmpty()) {
                return o;
            }
            return orderMap.values().iterator().next();
        } catch (JobSchedulerObjectNotExistException e) {
            if (suppressJobSchedulerObjectNotExistException) {
                return o;
            }
            throw e;
        }
    }
    
    public OrderVolatile getOrderVolatile() {
        try {
            Map<String, OrderVolatile> orderMap = getOrders(orders, compact, jocJsonCommand);
            if (orderMap == null || orderMap.isEmpty()) {
                return null;
            }
            return orderMap.values().iterator().next();
        } catch (Exception e) {
            return null;
        }
    }

    public List<OrderV> getOrdersOfJob() throws JocException {
        Map<String, OrderVolatile> orderMap = getOrders(job, compact, jocJsonCommand);
        if (orderMap == null || orderMap.isEmpty()) {
            return null;
        }
        return new ArrayList<OrderV>(orderMap.values());
    }

    private Map<String, OrderVolatile> getOrders(OrdersPerJobChain orders, boolean compact, JOCJsonCommand jocJsonCommand) throws JocException {
        JsonObject json = null;
        Boolean chckOrderPathIsInFolder = false;
        try {
            json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(orders, null), accessToken);
        } catch (JobSchedulerObjectNotExistException e) {
            // fix for nested job chains
            if (orders.getOrders().size() > 0) {
                chckOrderPathIsInFolder = true;
                OrdersPerJobChain onlyJobChain = new OrdersPerJobChain();
                onlyJobChain.setJobChain(orders.getJobChain());
                json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(onlyJobChain, null), accessToken);
            } else {
                throw e;
            }
        }
        if (checkOrderPathIsInFolder != null) {
            chckOrderPathIsInFolder = checkOrderPathIsInFolder;
        }
        return getOrders(json, compact, null, null, null, chckOrderPathIsInFolder);
    }

    private Map<String, OrderVolatile> getOrders(OrdersPerJobChain orders, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand) throws JocException {
        if (orders.getOrders().size() > 0) {
            ordersBody.setRegex(null);
            ordersBody.setProcessingStates(null);
            ordersBody.setRunTimeIsTemporary(null);
        }
        JsonObject json = null;
        Boolean chckOrderPathIsInFolder = orders.isOuterJobChain();
        try {
            json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(orders, null), accessToken);
        } catch (JobSchedulerObjectNotExistException e) {
            // fix for nested job chains
            if (orders.getOrders().size() > 0) {
                chckOrderPathIsInFolder = true;
                OrdersPerJobChain onlyJobChain = new OrdersPerJobChain();
                onlyJobChain.setJobChain(orders.getJobChain());
                json = jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(onlyJobChain, null), accessToken);
            } else {
                throw e;
            }
        }
        if (checkOrderPathIsInFolder != null) {
            chckOrderPathIsInFolder = checkOrderPathIsInFolder;
        }
        return getOrders(json, ordersBody.getCompact(), ordersBody.getRegex(), ordersBody.getProcessingStates(), ordersBody.getRunTimeIsTemporary(), chckOrderPathIsInFolder);
    }

    private Map<String, OrderVolatile> getOrders(String job, boolean compact, JOCJsonCommand jocJsonCommand) throws JocException {
        return getOrders(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(job), accessToken), compact, null, null, null,
                checkOrderPathIsInFolder);
    }

    private Map<String, OrderVolatile> getOrders(Folder folder, OrdersFilter ordersBody, JOCJsonCommand jocJsonCommand) throws JocException {
        return getOrders(jocJsonCommand.getJsonObjectFromPostWithRetry(getServiceBody(folder, ordersBody), accessToken), ordersBody.getCompact(),
                ordersBody.getRegex(), ordersBody.getProcessingStates(), ordersBody.getRunTimeIsTemporary(), checkOrderPathIsInFolder);
    }

    private Map<String, OrderVolatile> getOrders(JsonObject json, boolean compact, String regex, List<OrderStateFilter> processingStates,
            Boolean filterByRunTimeIsTemporary, Boolean checkOrderPathIsInFolder) throws JobSchedulerInvalidResponseDataException {
        UsedNodes usedNodes = new UsedNodes();
        UsedJobs usedJobs = new UsedJobs();
        UsedJobChains usedJobChains = new UsedJobChains();
        UsedTasks usedTasks = new UsedTasks();
        usedNodes.addEntries(json.getJsonArray("usedNodes"));
        usedTasks.addEntries(json.getJsonArray("usedTasks"));
        usedJobs.addEntries(json.getJsonArray("usedJobs"));
        Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
        String origJobChain = orders == null ? null : orders.getJobChain();
        Map<String, OrderVolatile> listOrderQueue = new HashMap<String, OrderVolatile>();

        for (JsonObject ordersItem : json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
            OrderVolatile order = new OrderVolatile(ordersItem, origJobChain);
            order.setPathJobChainOrderIdAndClusterMember();
            
            if (orders != null && !orders.getOrders().isEmpty() && !orders.containsOrder(order.getOrderId())) {
                continue;
            }
            // necessary for nested job chains but only for outer job chain and
            // for folder filter
            if (checkOrderPathIsInFolder && !checkOrderPathIsInFolder(order.getPath())) {
                continue;
            }
            if (!FilterAfterResponse.matchRegex(regex, order.getPath())) {
                LOGGER.debug("...processing skipped caused by 'regex=" + regex + "'");
                continue;
            }
            order.setRunTimeIsTemporary(false);
//            if (filterByRunTimeIsTemporary != null && order.getRunTimeIsTemporary() != filterByRunTimeIsTemporary) {
//                LOGGER.debug("...processing skipped caused by 'runTimeIsTemporary=" + filterByRunTimeIsTemporary + "'");
//                continue; 
//            }
            if (order.getProcessedBy() != null && clusterMemberId != null && !clusterMemberId.equals(order.getProcessedBy())) {
                try {
                    if (connection == null) {
                        connection = Globals.createSosHibernateStatelessConnection("OrdersVCallable-" + order.getPath());
                        InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
                        DBItemInventoryInstance inventoryInstance = dbLayer.getInventoryInstanceByClusterMemberId(order.getProcessedBy());
                        JOCJsonCommand jocJsonCommand2 = new JOCJsonCommand(inventoryInstance);
                        jocJsonCommand2.setUriBuilderForOrders();
                        jocJsonCommand2.addOrderCompactQuery(compact);
                        OrderVolatile o = new OrdersVCallable(order, compact, jocJsonCommand2, accessToken).getOrderVolatile();
                        o.setProcessedBy(order.getProcessedBy());
                        listOrderQueue.put(o.getPath(), o);
                        continue;
                    }
                } catch (Exception e) {
                } finally {
                    Globals.disconnect(connection);
                    connection = null;
                }
            }
            order.setSurveyDate(surveyDate);
            order.setFields(usedNodes, usedTasks, compact);
            if (orderDocumentations != null) {
                order.setDocumentation(orderDocumentations.get(order.getPath()));
            }
            if (yadeJobs != null && !yadeJobs.contains(order.getJob())) {
                continue;
            }
            if (!order.processingStateIsSet() && order.isWaitingForJob()) {
                order.readJobObstacles(usedJobs.get(order.getJob()));
            }
            if (!order.processingStateIsSet()) {
                usedJobChains.addEntries(json.getJsonArray("usedJobChains"));
                order.readJobChainObstacles(usedJobChains.get(order.getJobChain()));
            }
            if (!order.processingStateIsSet() && order.getTaskId() != null) {
                // it's very special situation of nested order while changing the job chain
                order.setSeverity(OrderStateText.RUNNING);
            }
            if (checkSuspendedOrdersWithFilter(order.getProcessingFilterState(), processingStates)) {
                listOrderQueue.put(order.getPath(), order);
            }
        }
        return listOrderQueue;
    }

    private boolean checkOrderPathIsInFolder(String orderPath) {
        try {
            return Paths.get(orderPath).startsWith(folderPath);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean checkSuspendedOrdersWithFilter(OrderStateFilter processingState, List<OrderStateFilter> processingStates) {
        if (processingState == null || processingStates == null) {
            return true;
        }
        return FilterAfterResponse.filterStateHasState(processingStates, processingState);
    }

    private String getServiceBody(OrdersPerJobChain orders, OrdersFilter ordersBody) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = null;
        if (orders.getOrders().size() > 0) {
            builder = Json.createObjectBuilder();
            JsonArrayBuilder ordersArray = Json.createArrayBuilder();
            for (String order : orders.getOrders()) {
                ordersArray.add(order);
            }
            if (suppressJobSchedulerObjectNotExistException && orders.getOrders().size() == 1) {
                ordersArray.add(""); //hack for distributed orders, otherwise SCHEDULER-161 There is no Order 'xxx'
            }
            builder.add("orderIds", ordersArray);
        } else {
            builder = filterProcessingStateAndType(ordersBody);
        }
        String jobChain = orders.getJobChain();
        jobChain = ("/" + jobChain.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        builder.add("path", jobChain);

        return builder.build().toString();
    }

    private String getServiceBody(String job) throws JocMissingRequiredParameterException {
        JsonObjectBuilder oBuilder = Json.createObjectBuilder();
        JsonArrayBuilder aBuilder = Json.createArrayBuilder();
        aBuilder.add(job);
        oBuilder.add("jobPaths", aBuilder);
        return oBuilder.build().toString();
    }

    private String getServiceBody(Folder folder, OrdersFilter ordersBody) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = filterProcessingStateAndType(ordersBody);

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

    private JsonObjectBuilder filterProcessingStateAndType(OrdersFilter ordersBody) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // add processingState from response body
        if (ordersBody == null) {
            return builder;
        }
        List<OrderStateFilter> states = ordersBody.getProcessingStates();
        Map<String, Boolean> filterValues = new HashMap<String, Boolean>();
        boolean suspended = false;
        if (states != null && !states.isEmpty()) {
            for (OrderStateFilter state : states) {
                switch (state) {
                case PENDING:
                    filterValues.put("Planned", true);
                    filterValues.put("NotPlanned", true);
                    break;
                case RUNNING:
                    filterValues.put("InTaskProcess", true);
                    filterValues.put("OccupiedByClusterMember", true);
                    break;
                case SUSPENDED:
                    suspended = true;
                    break;
                case BLACKLIST:
                    filterValues.put("Blacklisted", true);
                    break;
                case SETBACK:
                    filterValues.put("Setback", true);
                    break;
                default:
                    filterValues.put("Due", true);
                    filterValues.put("WaitingInTask", true);
                    filterValues.put("WaitingForResource", true);
                    break;
                }
            }
        }
        if (!filterValues.isEmpty()) {
            JsonArrayBuilder processingStates = Json.createArrayBuilder();
            for (String key : filterValues.keySet()) {
                processingStates.add(key);
            }
            builder.add("isOrderProcessingState", processingStates);
            if (suspended) {
                builder.add("orIsSuspended", true);
            }
        } else {
            if (suspended) {
                builder.add("isSuspended", true);
            }
        }

        // add type from response body
        List<OrderType> types = ordersBody.getTypes();
        filterValues.clear();
        if (types != null && !types.isEmpty()) {
            for (OrderType type : types) {
                switch (type) {
                case AD_HOC:
                    filterValues.put("AdHoc", true);
                    break;
                case PERMANENT:
                    filterValues.put("Permanent", true);
                    break;
                case FILE_ORDER:
                    filterValues.put("FileOrder", true);
                    break;
                }
            }
        }
        if (!filterValues.isEmpty()) {
            JsonArrayBuilder sourceTypes = Json.createArrayBuilder();
            for (String key : filterValues.keySet()) {
                sourceTypes.add(key);
            }
            builder.add("isOrderSourceType", sourceTypes);
        }
        return builder;
    }
}
