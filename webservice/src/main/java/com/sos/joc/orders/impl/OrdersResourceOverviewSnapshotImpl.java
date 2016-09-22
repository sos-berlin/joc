package com.sos.joc.orders.impl;

import java.io.StringReader;
import java.net.URI;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.restclient.JobSchedulerRestApiClient;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.Orders;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.SnapshotSchema;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceOverviewSnapshotImpl.class);

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, OrdersFilterSchema ordersFilterSchema) throws Exception {
        LOGGER.debug("init orders/overview/summary");
        JOCDefaultResponse jocDefaultResponse = init( ordersFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        
        try {

            //TODO consider OrdersFilterSchema
            // TODO URL "http://localhost:40410" has to read from database
            String masterUrl = "http://localhost:40410";
            JOCJsonCommand command = new JOCJsonCommand(masterUrl);
            command.getUriBuilder().path("/");
            command.addOrderStatisticsQuery();
            URI uri = command.getURI();
            LOGGER.info("call " + uri.toString());
            
            JsonObject json = getJsonObjectFromResponse(uri);
            
            // {"inProcess":0,"notPlanned":14,"running":8,"setback":0,"inTask":0,"suspended":5,"total":25,"eventId":1474328805429000,"permanent":17,"planned":2,"blacklisted":0,"fileOrder":1,"pending":1}
            Orders orders = new Orders();
            orders.setBlacklist(json.getInt("blacklisted", 0));
            orders.setPending(json.getInt("notPlanned", 0) + json.getInt("planned", 0));
            orders.setRunning(json.getInt("inProcess", 0));
            orders.setSetback(json.getInt("setback", 0));
            orders.setSuspended(json.getInt("suspended", 0));
            orders.setWaitingForResource(json.getInt("total", 0) - orders.getBlacklist() - orders.getPending() - orders.getRunning()
                    - orders.getSetback() - orders.getSuspended());

            SnapshotSchema entity = new SnapshotSchema();
            entity.setOrders(orders);
            entity.setSurveyDate(JobSchedulerDate.getDateFromEventId(json.getJsonNumber("eventId").longValue()));
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
    
    private JsonObject getJsonObjectFromResponse(URI uri) throws Exception {
        JobSchedulerRestApiClient client = new JobSchedulerRestApiClient();
        client.addHeader("Accept", "application/json");
        String response = client.executeRestServiceCommand("get", uri.toURL());
        
        int httpReplyCode = client.statusCode();
        String contentType = client.getResponseHeader("Content-Type");
        
        switch (httpReplyCode) {
        case 200:
            if (contentType.contains("application/json")) {
                JsonReader rdr = Json.createReader(new StringReader(response));
                JsonObject json = rdr.readObject();
                LOGGER.info(json.toString());
                return json;
            } else {
                throw new JobSchedulerInvalidResponseDataException("Unexpected content type '" + contentType + "'. Response: " + response);
            }
        case 400:
            // TODO check Content-Type
            // Now the exception is plain/text instead of JSON
            throw new JobSchedulerBadRequestException(response);
        default:
            throw new JobSchedulerBadRequestException(httpReplyCode + " " + client.getHttpResponse().getStatusLine().getReasonPhrase());
        }
    }

}
