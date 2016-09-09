package com.sos.joc.orders.impl;

import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.orders.OrderV;
import com.sos.joc.classes.orders.UsedNodes;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResource;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postOrders(String accessToken, OrdersBody ordersBody) throws Exception {
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
            usedNodes.addEntries(json.getJsonArray("usedNodes"));
            Date surveyDate = getDateFromTimestamp(json.getJsonNumber("eventId").longValue());
            
            List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();
            
            for (JsonObject ordersItem: json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
                OrderV order = new OrderV(ordersItem);
                order.setSurveyDate(surveyDate);
                if (ordersBody.getCompact()) {
                    order.setCompactFields(usedNodes);
                } else {
                    order.setDetailedFields(usedNodes);
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
            return JOCDefaultResponse.responseStatusJSError(((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage());
        }

    }

    private URI getServiceURI(OrdersBody orderBody, String masterUrl) {
        StringBuilder s = new StringBuilder();
        s.append(masterUrl).append(WebserviceConstants.ORDER_API_PATH);
        String returnQuery = (orderBody.getCompact()) ? WebserviceConstants.ORDER_OVERVIEW : WebserviceConstants.ORDER_DETAILED;
        URI uri = UriBuilder.fromPath(s.toString()).queryParam("return", returnQuery).build();
        LOGGER.info("call " + uri.toString());
        return uri;
    }
    
    private String getServiceBody(OrdersBody orderBody) throws JocMissingRequiredParameterException {
        //TODO set body
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", "/");
        //builder.add("orderId", orderId);
        return builder.build().toString();
    }
    
    private JsonObject getJsonObjectFromResponse(OrdersBody orderBody, String masterUrl) throws Exception {
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


}
