package com.sos.joc.order.impl;

import java.io.StringReader;
import java.net.URI;
import java.util.Date;

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
import com.sos.joc.classes.orders.UsedNodes;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.order.Order200VSchema;
import com.sos.joc.model.order.OrderFilterWithCompactSchema;
import com.sos.joc.order.resource.IOrderResource;


@Path("order")
public class OrderResourceImpl extends JOCResourceImpl implements IOrderResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postOrder(String accessToken, OrderFilterWithCompactSchema orderBody) throws Exception {
        LOGGER.debug("init Order");
        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
 
            //TODO URL "http://localhost:40410" has to read from database
            JsonObject json = getJsonObjectFromResponse(orderBody, "http://localhost:40410");
            LOGGER.info(json.toString());
                       
            UsedNodes usedNodes = new UsedNodes();
            usedNodes.addEntries(json.getJsonArray("usedNodes"));
            
            OrderV order = new OrderV((JsonObject) json.getJsonArray("orders").stream().findFirst().get());
            order.setSurveyDate(getDateFromTimestamp(json.getJsonNumber("eventId").longValue()));
            boolean needMoreInfos = false;
            if (orderBody.getCompact()) {
                needMoreInfos = order.setCompactFields(usedNodes);
            } else {
                needMoreInfos = order.setDetailedFields(usedNodes);
            }
//            if (needMoreInfos) {
//                UsedJobChains usedJobChains = new UsedJobChains();
//                usedJobChains.addEntries(json.getJsonArray("UsedJobChains"));
//                usedJobChains.isStopped(order.getJobChain());
//            }
            
            Order200VSchema entity = new Order200VSchema();
            entity.setDeliveryDate(new Date());
            entity.setOrder(order);
            LOGGER.info(entity.getOrder().toString());
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage();
            LOGGER.error(errorMsg, e);
            return JOCDefaultResponse.responseStatusJSError(((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage());
        }

    }

    private URI getServiceURI(OrderFilterWithCompactSchema orderBody, String masterUrl) {
        StringBuilder s = new StringBuilder();
        s.append(masterUrl).append(WebserviceConstants.ORDER_API_PATH);
        String returnQuery = (orderBody.getCompact()) ? WebserviceConstants.ORDER_OVERVIEW : WebserviceConstants.ORDER_DETAILED;
        URI uri = UriBuilder.fromPath(s.toString()).queryParam("return", returnQuery).build();
        LOGGER.info("call " + uri.toString());
        return uri;
    }
    
    private String getServiceBody(OrderFilterWithCompactSchema orderBody) throws JocMissingRequiredParameterException {
        String jobChain = orderBody.getJobChain();
        String orderId = orderBody.getOrderId();
        if (jobChain == null || jobChain.isEmpty() || orderId == null || orderId.isEmpty()) {
            throw new JocMissingRequiredParameterException("undefined 'jobChain'");
        }
        if (orderId == null || orderId.isEmpty()) {
            throw new JocMissingRequiredParameterException("undefined 'orderId'");
        }
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", jobChain);
        builder.add("orderId", orderId);
        return builder.build().toString();
    }
    
    private JsonObject getJsonObjectFromResponse(OrderFilterWithCompactSchema orderBody, String masterUrl) throws Exception {
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
