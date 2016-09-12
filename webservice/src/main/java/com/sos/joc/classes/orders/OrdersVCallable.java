package com.sos.joc.classes.orders;

import java.io.StringReader;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.OrderFilterWithCompactSchema;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.order.OrdersFilterSchema;

public class OrdersVCallable implements Callable<Map<String,OrderQueue>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersVCallable.class);
    private final Order_ order;
    private final FoldersSchema folder;
    private final OrdersFilterSchema ordersBody;
    private final Boolean compact;
    private final URI uri;
    
    public OrdersVCallable(Order_ order, Boolean compact, URI uri) {
        this.order = order;
        this.folder = null;
        this.ordersBody = null;
        this.compact = compact;
        this.uri = uri;
    }
    
    public OrdersVCallable(OrderFilterWithCompactSchema order, URI uri) {
        Order_ o = new Order_();
        o.setJobChain(order.getJobChain());
        o.setOrderId(order.getOrderId());
        this.order = o;
        this.folder = null;
        this.ordersBody = null;
        this.compact = order.getCompact();
        this.uri = uri;
    }
    
    public OrdersVCallable(FoldersSchema folder, OrdersFilterSchema ordersBody, URI uri) {
        this.order = null;
        this.folder = folder;
        this.ordersBody = ordersBody;
        this.compact = ordersBody.getCompact();
        this.uri = uri;
    }
    
    @Override
    public Map<String,OrderQueue> call() throws Exception {
        if(order != null) {
            return getOrders(order, compact, uri);
        } else {
            return getOrders(folder, ordersBody, uri);
        }
    }
    
    public OrderQueue getOrder() throws Exception {
        Map<String,OrderQueue> orderMap = getOrders(order, compact, uri);
        if (orderMap == null || orderMap.isEmpty()) {
            throw new JobSchedulerInvalidResponseDataException(String.format("Order doesn't exist: %1$s,%2$s", order.getJobChain(), order.getOrderId()));
        }
        return orderMap.values().iterator().next();
    }
    
    private Map<String,OrderQueue> getOrders(Order_ order, boolean compact, URI uri) throws Exception {
        return getOrders(getJsonObjectFromResponse(uri, getServiceBody(order)), compact);
    }
    
    private Map<String,OrderQueue> getOrders(FoldersSchema folder, OrdersFilterSchema ordersBody, URI uri) throws JocMissingRequiredParameterException, Exception {
        return getOrders(getJsonObjectFromResponse(uri, getServiceBody(folder)), ordersBody.getCompact(), (folder.getRecursive()) ? null : folder.getFolder(), ordersBody.getRegex());
    }
    
    private Map<String,OrderQueue> getOrders(JsonObject json, boolean compact) throws JobSchedulerInvalidResponseDataException {
        return getOrders(json, compact, null, null);
    }
    
    private Map<String,OrderQueue> getOrders(JsonObject json, boolean compact, String refFolderIfNotRecursive, String regex) throws JobSchedulerInvalidResponseDataException {
        UsedNodes usedNodes = new UsedNodes();
        UsedJobs usedJobs = new UsedJobs();
        UsedJobChains usedJobChains = new UsedJobChains();
        usedNodes.addEntries(json.getJsonArray("usedNodes"));
        Date surveyDate = JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue());
        Map<String,OrderQueue> listOrderQueue = new HashMap<String,OrderQueue>();
        
        for (JsonObject ordersItem: json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
            OrderV order = new OrderV(ordersItem);
            order.setPathJobChainAndOrderId();
            if (FilterAfterResponse.isLocatedInSubFolder(refFolderIfNotRecursive, order.getJobChain())) {
                LOGGER.info("...processing skipped caused by 'recursive=false'");
               continue; 
            }
            if (!FilterAfterResponse.matchReqex(regex, order.getPath())) {
                LOGGER.info("...processing skipped caused by 'regex=" + regex + "'");
                continue; 
            }
            order.setSurveyDate(surveyDate);
            boolean needMoreInfos = order.setFields(usedNodes, compact);
            if (needMoreInfos) {
                usedJobs.addEntries(json.getJsonArray("usedJobs"));
                needMoreInfos = order.hasJobObstacles(usedJobs.get(order.getJob()));
            }
            if (needMoreInfos) {
                usedJobChains.addEntries(json.getJsonArray("usedJobChains"));
                order.hasJobChainObstacles(usedJobChains.get(order.getJobChain()));
            }
            listOrderQueue.put(order.getPath(), order);
        }
        return listOrderQueue;
    }
    
    private JsonObject getJsonObjectFromResponse(URI uri, String postBody) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        LOGGER.info("call " + uri.toString());
        if (postBody != null) {
            LOGGER.info("with POST body: " + postBody); 
        }
        String response = client.executeRestServiceCommand("post", uri.toURL(), postBody);
        int httpReplyCode = client.statusCode();
        String contentType = client.getResponseHeader("Content-Type");
        
        if (httpReplyCode == 200) {
            if (contentType.contains("application/json")) {
                JsonReader rdr = Json.createReader(new StringReader(response));
                JsonObject json = rdr.readObject();
                LOGGER.info(json.toString());
                return json;
            } else {
                throw new JobSchedulerInvalidResponseDataException("Unexpected content type '" + contentType + "'. Response: " + response);
            }
        } else if (httpReplyCode == 400) {
            //TODO check Content-Type
            //Now the exception is plain/text instead of JSON
            throw new JobSchedulerBadRequestException(response);
        } else {
            throw new JobSchedulerBadRequestException(httpReplyCode+ " " +client.getHttpResponse().getStatusLine().getReasonPhrase());
        }
    }
    
    private String getServiceBody(Order_ order) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String jobChain = order.getJobChain();
        if (jobChain == null || jobChain.isEmpty()) {
            throw new JocMissingRequiredParameterException("jobChain");
        }
        jobChain = ("/"+jobChain.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
        builder.add("path", jobChain);
        if (order.getOrderId() != null && !order.getOrderId().isEmpty()) {
            builder.add("orderId", order.getOrderId());
        }
        String postBody = builder.build().toString();
        return postBody;
    }
    
    private String getServiceBody(FoldersSchema folder) throws JocMissingRequiredParameterException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        String path = folder.getFolder();
        if (path != null && !path.isEmpty()) {
            path = ("/"+path.trim()+"/").replaceAll("//+", "/");
        }
        builder.add("path", path);
        String postBody = builder.build().toString();
        return postBody;
    }
}
