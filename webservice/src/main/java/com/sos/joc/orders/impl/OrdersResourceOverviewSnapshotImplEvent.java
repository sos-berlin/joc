package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshotEvent;

@Path("orders")
public class OrdersResourceOverviewSnapshotImplEvent extends JOCResourceImpl implements IOrdersResourceOverviewSnapshotEvent {

    private static final String API_CALL = "./orders/overview/snapshot/event";
    private static final String EVENT_ID_PROPKEY = "orders/overview/snapshot/eventId";
    private static final Integer EVENT_TIMEOUT = 0;

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshotEvent(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        try {
            initLogging(API_CALL, jobChainsFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainsFilter.getJobschedulerId(), getPermissons(accessToken).getOrder()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String eventIdStr = System.getProperty(accessToken + ":" + EVENT_ID_PROPKEY);
            OrdersSnapshot entity = new OrdersSnapshot();
            if (eventIdStr != null) {
                JsonObject json = getJsonObject(eventIdStr, accessToken);
                Long newEventId = json.getJsonNumber("eventId").longValue();
                String type = json.getString("TYPE", "Empty");
                switch (type) {
                case "Empty":
                    System.setProperty(accessToken + ":" + EVENT_ID_PROPKEY, newEventId.toString());
                    entity.setOrders(null);
                    entity.setSurveyDate(JobSchedulerDate.getDateFromEventId(newEventId));
                    entity.setDeliveryDate(Date.from(Instant.now()));
                    break;
                case "NonEmpty":
                    boolean isOrderStarted = false;
                    for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
                        if ("OrderStarted".equals(event.getString("TYPE", ""))) {
                            isOrderStarted = true;
                            break;
                        }
                    }
                    if (isOrderStarted) {
                        getJsonObject(newEventId.toString(), accessToken);
                    }
                    entity = Orders.getSnapshot(dbItemInventoryInstance.getUrl(), accessToken, EVENT_ID_PROPKEY, jobChainsFilter);
                    break;
                case "Torn":
                    entity = Orders.getSnapshot(dbItemInventoryInstance.getUrl(), accessToken, EVENT_ID_PROPKEY, jobChainsFilter);
                    break;
                }
            }

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private JsonObject getJsonObject(String eventIdStr, String accessToken) throws JocException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", "/");
        JOCJsonCommand command = new JOCJsonCommand();
        command.setUriBuilderForEvents(dbItemInventoryInstance.getUrl());
        command.addOrderEventQuery(eventIdStr, EVENT_TIMEOUT);
        command.setSocketTimeout((EVENT_TIMEOUT + 2) * 1000);
        return new JOCJsonCommand().getJsonObjectFromPost(builder.build().toString(), accessToken);
    }
}
