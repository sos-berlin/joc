package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.shiro.session.Session;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.exceptions.ForcedClosingHttpClientException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshotEvent;

@Path("orders")
public class OrdersResourceOverviewSnapshotImplEvent extends JOCResourceImpl implements IOrdersResourceOverviewSnapshotEvent {

    private static final String API_CALL = "./orders/overview/snapshot/event";
    private static final String EVENT_ID_PROPKEY = "orders/overview/snapshot/eventId";
    private static final String HTTP_CLIENT_PROPKEY = "orders/overview/snapshot/httpClient";
    private static final Integer EVENT_TIMEOUT = 60;
    private String eventIdStr = null;

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshotEvent(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        try {
            initLogging(API_CALL, jobChainsFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainsFilter.getJobschedulerId(), getPermissons(accessToken).getOrder()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String eventIdPropKey = EVENT_ID_PROPKEY + ":" + getJsonString(jobChainsFilter);
            // String eventIdStr = System.getProperty(eventIdPropKey);
            Session session = getJobschedulerUser().getSosShiroCurrentUser().getCurrentSubject().getSession();
            CloseableHttpClient httpClient = (CloseableHttpClient) session.getAttribute(HTTP_CLIENT_PROPKEY);
            if (httpClient != null) {
                try {
                    session.removeAttribute(HTTP_CLIENT_PROPKEY);
                    JOCJsonCommand command = new JOCJsonCommand();
                    command.setHttpClient(httpClient);
                    command.forcedClosingHttpClient();
                } catch (Exception e) {
                } 
            }
            eventIdStr = (String) session.getAttribute(eventIdPropKey);
            OrdersSnapshot entity = new OrdersSnapshot();
            JOCJsonCommand command = new JOCJsonCommand(this);
            if (jobChainsFilter.getClose() != null && jobChainsFilter.getClose()) {
                entity.setOrders(null);
                entity.setDeliveryDate(Date.from(Instant.now()));
            } else {
                if (eventIdStr != null) {
                    JsonObject json = getJsonObject(eventIdStr, session, accessToken);
                    Long newEventId = json.getJsonNumber("eventId").longValue();
                    String type = json.getString("TYPE", "Empty");
                    switch (type) {
                    case "Empty":
                        // System.setProperty(eventIdPropKey,
                        // newEventId.toString());
                        session.setAttribute(eventIdPropKey, newEventId.toString());
                        entity.setOrders(null);
                        entity.setSurveyDate(JobSchedulerDate.getDateFromEventId(newEventId));
                        entity.setDeliveryDate(Date.from(Instant.now()));
                        break;
                    case "NonEmpty":
//                        boolean isOrderStarted = false;
//                        for (JsonObject event : json.getJsonArray("eventSnapshots").getValuesAs(JsonObject.class)) {
//                            if ("OrderStarted".equals(event.getString("TYPE", ""))) {
//                                isOrderStarted = true;
//                                break;
//                            }
//                        }
//                        if (isOrderStarted) {
//                            getJsonObject(newEventId.toString(), session, accessToken);
//                        }
                        entity = Orders.getSnapshot(command, session, accessToken, HTTP_CLIENT_PROPKEY, eventIdPropKey,
                                jobChainsFilter);
                        break;
                    case "Torn":
                        entity = Orders.getSnapshot(command, session, accessToken, HTTP_CLIENT_PROPKEY, eventIdPropKey,
                                jobChainsFilter);
                        break;
                    }
                } else {
                    entity = Orders.getSnapshot(command, session, accessToken, HTTP_CLIENT_PROPKEY, eventIdPropKey,
                            jobChainsFilter);
                }
            }

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (ForcedClosingHttpClientException e) {
            OrdersSnapshot entity = new OrdersSnapshot();
            entity.setOrders(null);
            if (eventIdStr != null) {
                entity.setSurveyDate(JobSchedulerDate.getDateFromEventId(new Long(eventIdStr)));
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private JsonObject getJsonObject(String eventIdStr, Session session, String accessToken) throws JocException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("path", "/");
        JOCJsonCommand command = new JOCJsonCommand(this);
        command.setUriBuilderForEvents();
        command.addOrderEventQuery(eventIdStr, EVENT_TIMEOUT);
        command.setSocketTimeout((EVENT_TIMEOUT + 5) * 1000);
        command.createHttpClient();
        session.setAttribute(HTTP_CLIENT_PROPKEY, command.getHttpClient());
        return command.getJsonObjectFromPostWithRetry(builder.build().toString(), accessToken);
    }
}
