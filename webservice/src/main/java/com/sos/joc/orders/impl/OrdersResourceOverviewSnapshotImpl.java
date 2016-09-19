package com.sos.joc.orders.impl;

import java.io.StringReader;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.jitl.restclient.JobSchedulerRestClient;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.order.Orders;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.SnapshotSchema;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceOverviewSnapshotImpl.class);

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, OrdersFilterSchema ordersFilterSchema) throws Exception {
        LOGGER.debug("init Orders");
        JOCDefaultResponse jocDefaultResponse = init( ordersFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        
        try {

            // Reading orders from the JobScheduler Webservice
            JobSchedulerRestClient.headers.put("Authorization", "Basic U09TMDE6c29zMDE=");
            JobSchedulerRestClient.headers.put("Content-Type", "application/json");

            String response = JobSchedulerRestClient.executeRestServiceCommand("get", "http://localhost:4404/jobscheduler/master/api/order/?return=OrderOverview");

            JsonReader rdr = Json.createReader(new StringReader(response));
            JsonObject obj = rdr.readObject();

            ordersFilterSchema.getRegex();
            ordersFilterSchema.getOrders(); // list of wanted orders.

            // TODO JOC Cockpit Webservice (Inventory_Orders)

            SnapshotSchema entity = new SnapshotSchema();
            Orders orders = new Orders();

            orders.setBlacklist(-1);
            orders.setPending(-1);
            orders.setRunning(-1);
            orders.setSetback(-1);
            orders.setSuspended(-1);
            orders.setWaitingForResource(-1);

            entity.setSurveyDate(getDateFromTimestamp(obj.getJsonNumber("eventId").longValue()));
            entity.setDeliveryDate(new Date());
            entity.setOrders(orders);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            System.out.println(e.getCause() + ":" + e.getMessage());
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
