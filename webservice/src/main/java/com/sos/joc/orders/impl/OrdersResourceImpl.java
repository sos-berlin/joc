package com.sos.joc.orders.impl;

import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.orders.OrderV;
import com.sos.joc.classes.orders.UsedJobChains;
import com.sos.joc.classes.orders.UsedJobs;
import com.sos.joc.classes.orders.UsedNodes;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.model.order.ProcessingState;
import com.sos.joc.orders.resource.IOrdersResource;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postOrders(String accessToken, OrdersFilterSchema ordersBody) throws Exception {
        LOGGER.debug("init Orders");
        JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

 
        try {
            
            //TODO URL "http://localhost:40410" has to read from database
            JsonObject json = getJsonObjectFromResponse(ordersBody, "http://localhost:40410");
            LOGGER.info(json.toString());
            
            UsedNodes usedNodes = new UsedNodes();
            UsedJobs usedJobs = new UsedJobs();
            UsedJobChains usedJobChains = new UsedJobChains();
            usedNodes.addEntries(json.getJsonArray("usedNodes"));
            Date surveyDate = getDateFromTimestamp(json.getJsonNumber("eventId").longValue());
            
            List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();
            
            for (JsonObject ordersItem: json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
                OrderV order = new OrderV(ordersItem);
                order.setSurveyDate(surveyDate);
                boolean needMoreInfos = false;
                if (ordersBody.getCompact()) {
                    needMoreInfos = order.setCompactFields(usedNodes);
                } else {
                    needMoreInfos = order.setDetailedFields(usedNodes);
                }
                if (needMoreInfos) {
                    usedJobs.addEntries(json.getJsonArray("usedJobs"));
                    needMoreInfos = order.hasJobObstacles(usedJobs.get(order.getJob()));
                }
                if (needMoreInfos) {
                    usedJobChains.addEntries(json.getJsonArray("usedJobChains"));
                    order.hasJobChainObstacles(usedJobChains.get(order.getJobChain()));
                }
                listOrderQueue.add(order);
            }
            
 
            OrdersVSchema entity = new OrdersVSchema();
            entity.setDeliveryDate(new Date());
            entity.setOrders(listOrderQueue);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private URI getServiceURI(OrdersFilterSchema orderBody, String masterUrl) {
        
        StringBuilder s = new StringBuilder();
        s.append(masterUrl).append(WebserviceConstants.ORDER_API_PATH);
        UriBuilder uriBuilder = UriBuilder.fromPath(s.toString());
        String returnQuery = (orderBody.getCompact()) ? WebserviceConstants.ORDER_OVERVIEW : WebserviceConstants.ORDER_DETAILED;
        uriBuilder = uriBuilder.queryParam("return", returnQuery);
        
        List<ProcessingState> states = orderBody.getProcessingState();
        if (states != null) {
            for (ProcessingState state : states) {
                switch(state) {
                case PENDING:
                    //TODO not yet implemented
                    break;
                case RUNNING:
                    //TODO not yet implemented
                    break;
                case SUSPENDED:
                    uriBuilder = uriBuilder.queryParam("isSuspended", true);
                    break;
                case BLACKLIST:
                    uriBuilder = uriBuilder.queryParam("isBlacklisted", true);
                    break;
                case SETBACK:
                    uriBuilder = uriBuilder.queryParam("isSetBack", true);
                    break;
                case WAITINGFORRESOURCE:
                    //TODO not yet implemented
                    break;
                }
            }
        }
        URI uri = uriBuilder.build();
        LOGGER.info("call " + uri.toString());
        return uri;
    }
    
    private String getServiceBody(OrdersFilterSchema orderBody) throws JocMissingRequiredParameterException {
        //TODO set body
        List<Order_> orders = orderBody.getOrders();
        List<FoldersSchema> folders = orderBody.getFolders();
        String path = "/";
        if (orders != null && !orders.isEmpty()) {
            
        } else if (folders != null && !folders.isEmpty()) {
            path = getGreatestCommonPath(folders);
        }        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", path);
        //builder.add("orderId", orderId);
        String postBody = builder.build().toString();
        LOGGER.info("with POST body: " + postBody);
        return postBody;
    }
    
    private JsonObject getJsonObjectFromResponse(OrdersFilterSchema orderBody, String masterUrl) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        String response = client.executeRestServiceCommand("post", getServiceURI(orderBody, masterUrl).toURL(), getServiceBody(orderBody));
        int httpReplyCode = client.statusCode();
        
        if (httpReplyCode == 200) {
            //TODO check Content-Type. Must be application/json
            JsonReader rdr = Json.createReader(new StringReader(response));
            return rdr.readObject();
        } else if (httpReplyCode == 400) {
            //TODO check Content-Type
            //Now the exception is plain/text instead of JSON
            throw new JobSchedulerBadRequestException(response);
        } else {
            throw new JobSchedulerBadRequestException(httpReplyCode+ " " +client.getHttpResponse().getStatusLine().getReasonPhrase());
        }
    }
    
    private String getGreatestCommonPath(List<FoldersSchema> folders) {
        java.nio.file.Path refFolder = Paths.get(folders.get(0).getFolder().replaceAll("//+", "/").replaceFirst("^/",""));
        boolean start = true;
        for (FoldersSchema folder : folders) {
            if (start) {
                start = false;
                continue;
            }
            java.nio.file.Path refFolder2 = Paths.get(folder.getFolder().replaceAll("//+", "/").replaceFirst("^/",""));
            int min = Math.min(refFolder.getNameCount(), refFolder2.getNameCount());
            for (int i = 1; i <= min; i++) {
                if (!refFolder2.startsWith(refFolder.subpath(0, i))) {
                    if (i == 1) {
                        refFolder = Paths.get("");
                    } else {
                        refFolder = Paths.get(refFolder.subpath(0, i-1).toString());
                    }
                    break;
                }
            }
            if (refFolder.getNameCount() == 0) {
                break;
            }
        }
        return ("/"+refFolder.toString()+"/").replaceAll("\\\\", "/").replaceAll("//+", "/"); 
    }


}
