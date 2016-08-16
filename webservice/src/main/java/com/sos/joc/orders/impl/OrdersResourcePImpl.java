package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.post.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResourceP;
import com.sos.joc.orders.resource.IOrdersResource.OrdersResponse;
import com.sos.joc.response.JocCockpitResponse;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourcePImpl.class);

    @Override
    public OrdersResponseP postOrdersP(String accessToken, OrdersBody ordersBody) throws Exception {

        OrdersResponseP ordersResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return OrdersResponseP.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return OrdersResponseP.responseStatus440(JocCockpitResponse.getError401Schema(accessToken,e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return OrdersResponseP.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

        if (!getPermissons().getOrder().getView().isStatus()) {
            return OrdersResponseP.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (ordersBody.getJobschedulerId() == null) {
            return OrdersResponseP.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(ordersBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return OrdersResponseP.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s", ordersBody.getJobschedulerId(),
                        DBLayer.TABLE_INVENTORY_INSTANCES)));
            }

            OrdersPSchema entity = new OrdersPSchema();

            // TODO JOC Cockpit Webservice
            String actProcessingState = "getValueFromScheduler";

            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());
            List<Order> listOrder = new ArrayList<Order>();
            Order order1 = new Order();
            Order order2 = new Order();

            order1.setSurveyDate(new Date());
            order1.setPath("myPath1");
            order1.setOrderId("myOrderId1");
            order1.setJobChain("myJobChain1");
            if (!ordersBody.getCompact()) {
                order1.setEstimatedDuration(-1);
                order1.setConfigurationDate(new Date());
                order1.setEndState("myEndState");
                order1.setInitialState("myInitialState");
                order1.setTitle("myTitle");
                order1.setType(Order.Type.PERMANENT);
                order1.setPriority(-1);

                List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                NameValuePairsSchema param1 = new NameValuePairsSchema();
                NameValuePairsSchema param2 = new NameValuePairsSchema();
                param1.setName("param1");
                param1.setValue("value1");
                param2.setName("param2");
                param2.setValue("value2");
                parameters.add(param1);
                parameters.add(param1);
                order1.setParams(parameters);

                listOrder.add(order1);

                order2.setSurveyDate(new Date());
                order2.setPath("myPath1");
                order2.setOrderId("myOrderId1");
                order2.setJobChain("myJobChain1");
                if (!ordersBody.getCompact()) {
                    order2.setEstimatedDuration(-1);
                    order2.setConfigurationDate(new Date());
                    order2.setEndState("myEndState");
                    order2.setInitialState("myInitialState");
                    order2.setTitle("myTitle");
                    order2.setType(Order.Type.PERMANENT);
                    order2.setPriority(-1);

                    order2.setParams(parameters);

                    listOrder.add(order2);
                }

                entity.setOrders(listOrder);

            }
            ordersResponse = OrdersResponseP.responseStatus200(entity);
            return ordersResponse;
        } catch (Exception e) {
            return OrdersResponseP.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));

        }
    }
}
