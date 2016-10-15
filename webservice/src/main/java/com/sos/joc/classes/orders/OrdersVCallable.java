package com.sos.joc.classes.orders;

import java.net.URI;
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

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersFilter;

public class OrdersVCallable implements Callable<Map<String,OrderV>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersVCallable.class);
    private final String job;
    private final OrdersPerJobChain orders;
    private final Folder folder;
    private final OrdersFilter ordersBody;
    private final Boolean compact;
    private final URI uri;
    
    public OrdersVCallable(String job, Boolean compact, URI uri) {
        this.orders = null;
        this.job = ("/"+job.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.uri = uri;
    }
    
    public OrdersVCallable(OrdersPerJobChain orders, Boolean compact, URI uri) {
        this.orders = orders;
        this.job = null;
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.uri = uri;
    }
    
    public OrdersVCallable(JobChainV jobChain, Boolean compact, URI uri) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(jobChain.getPath());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.uri = uri;
    }
    
    public OrdersVCallable(OrderFilter order, URI uri) {
        OrdersPerJobChain o = new OrdersPerJobChain();
        o.setJobChain(order.getJobChain());
        o.addOrder(order.getOrderId());
        this.orders = o;
        this.job = null;
        this.folder = null;
        this.ordersBody = null;
        this.compact = order.getCompact();
        this.uri = uri;
    }
    
    public OrdersVCallable(Folder folder, OrdersFilter ordersBody, URI uri) {
        this.orders = null;
        this.job = null;
        this.folder = folder;
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.uri = uri;
    }
    
    @Override
    public Map<String,OrderV> call() throws Exception {
        if(orders != null) {
            return getOrders(orders, compact, uri);
        } else if(job != null) { 
            return getOrders(job, compact, uri);
        } else {
            return getOrders(folder, ordersBody, uri);
        }
    }
    
    public OrderV getOrder() throws Exception {
        Map<String,OrderV> orderMap = getOrders(orders, compact, uri);
        if (orderMap == null || orderMap.isEmpty()) {
            throw new JobSchedulerInvalidResponseDataException(String.format("Order doesn't exist: %1$s,%2$s", orders.getJobChain(), orders.getOrders().get(0)));
        }
        return orderMap.values().iterator().next();
    }
    
    public List<OrderV> getOrdersOfJob() throws Exception {
        Map<String,OrderV> orderMap = getOrders(job, compact, uri);
        if (orderMap == null || orderMap.isEmpty()) {
            return null;
        }
        return new ArrayList<OrderV>(orderMap.values());
    }
    
    private Map<String,OrderV> getOrders(OrdersPerJobChain orders, boolean compact, URI uri) throws Exception {
        return getOrders(new JOCJsonCommand().getJsonObjectFromResponse(uri, getServiceBody(orders)), compact);
    }
    
    private Map<String,OrderV> getOrders(String job, boolean compact, URI uri) throws Exception {
        return getOrders(new JOCJsonCommand().getJsonObjectFromResponse(uri, getServiceBody(job)), compact);
    }
    
    private Map<String,OrderV> getOrders(Folder folder, OrdersFilter ordersBody, URI uri) throws JocMissingRequiredParameterException, Exception {
        return getOrders(new JOCJsonCommand().getJsonObjectFromResponse(uri, getServiceBody(folder, ordersBody)), ordersBody.getCompact(), (folder.getRecursive()) ? null : folder.getFolder(), ordersBody.getRegex());
    }
    
    private Map<String,OrderV> getOrders(JsonObject json, boolean compact) throws JobSchedulerInvalidResponseDataException {
        return getOrders(json, compact, null, null);
    }
    
    private Map<String,OrderV> getOrders(JsonObject json, boolean compact, String refFolderIfNotRecursive, String regex) throws JobSchedulerInvalidResponseDataException {
        UsedNodes usedNodes = new UsedNodes();
        UsedJobs usedJobs = new UsedJobs();
        UsedJobChains usedJobChains = new UsedJobChains();
        UsedTasks usedTasks = new UsedTasks();
        usedNodes.addEntries(json.getJsonArray("usedNodes"));
        usedTasks.addEntries(json.getJsonArray("usedTasks"));
        Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
        Map<String,OrderV> listOrderQueue = new HashMap<String,OrderV>();
        
        for (JsonObject ordersItem: json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
            OrderVolatile order = new OrderVolatile(ordersItem);
            order.setPathJobChainAndOrderId();
            if (!FilterAfterResponse.matchReqex(regex, order.getPath())) {
                LOGGER.info("...processing skipped caused by 'regex=" + regex + "'");
                continue; 
            }
            order.setSurveyDate(surveyDate);
            order.setFields(usedNodes, usedTasks, compact);
            if (!order.processingStateIsSet() && order.isWaitingForJob()) {
                usedJobs.addEntries(json.getJsonArray("usedJobs"));
                order.readJobObstacles(usedJobs.get(order.getJob()));
            }
            if (!order.processingStateIsSet()) {
                usedJobChains.addEntries(json.getJsonArray("usedJobChains"));
                order.readJobChainObstacles(usedJobChains.get(order.getJobChain()));
            }
            listOrderQueue.put(order.getPath(), order);
        }
        return listOrderQueue;
    }
    
    private String getServiceBody(OrdersPerJobChain orders) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String jobChain = orders.getJobChain();
        jobChain = ("/"+jobChain.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        builder.add("path", jobChain);
        for (String order : orders.getOrders()) {
            JsonArrayBuilder ordersArray = Json.createArrayBuilder();
            ordersArray.add(order);
            builder.add("orderIds", ordersArray);
        }
        return builder.build().toString();
    }
    
    private String getServiceBody(String job) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("jobPath", job);
        return builder.build().toString();
    }
    
    private String getServiceBody(Folder folder, OrdersFilter ordersBody) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        // add folder path from response body 
        String path = folder.getFolder();
        if (path != null && !path.isEmpty()) {
            path = ("/"+path.trim()+"/").replaceAll("//+", "/");
            if (!folder.getRecursive()) {
                path += "*";
                LOGGER.info(String.format("...consider 'recursive=%1$b' for folder '%2$s'", folder.getRecursive(), folder.getFolder()));
            }
            builder.add("path", path);
        }
        
        // add processingState from response body
        List<OrderStateText> states = ordersBody.getProcessingStates();
        Map<String, Boolean> filterValues = new HashMap<String, Boolean>();
        boolean suspended = false;
        if (states != null && !states.isEmpty()) {
            for (OrderStateText state : states) {
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
                builder.add("orIsSuspended",true); 
            }
        } else {
            if (suspended) {
                builder.add("isSuspended",true); 
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
        
        return builder.build().toString();
    }
}
