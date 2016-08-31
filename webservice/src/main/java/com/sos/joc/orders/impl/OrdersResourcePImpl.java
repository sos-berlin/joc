package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResourceP;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourcePImpl.class);

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersBody ordersBody) throws Exception {

        LOGGER.debug("init OrdersP");
        JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            OrdersPSchema entity = new OrdersPSchema();
            entity.setDeliveryDate(new Date());

            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(jobschedulerUser.getSosShiroCurrentUser().getSosHibernateConnection(),ordersBody.getJobschedulerId());
            List<DBItemInventoryOrder> listOfOrders = dbLayer.getInventoryOrders();
            List<Order> listOrder = new ArrayList<Order>();
            for (DBItemInventoryOrder inventoryOrder : listOfOrders) {
                Order order = new Order();
                order.setSurveyDate(inventoryOrder.getModified());
                order.setPath(inventoryOrder.getName());
                order.setOrderId(inventoryOrder.getOrderId());
                order.setJobChain(inventoryOrder.getJobChain());
                if (!ordersBody.getCompact()) {
                    order.setEstimatedDuration(-1);
                    order.setConfigurationDate(new Date());
                    order.setEndState("myEndState");
                    order.setInitialState("myInitialState");
                    order.setTitle("myTitle");
                    order.setType(Order.Type.permanent);
                    order.setPriority(-1);

                    List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                    NameValuePairsSchema param1 = new NameValuePairsSchema();
                    NameValuePairsSchema param2 = new NameValuePairsSchema();
                    param1.setName("param1");
                    param1.setValue("value1");
                    param2.setName("param2");
                    param2.setValue("value2");
                    parameters.add(param1);
                    parameters.add(param1);
                    order.setParams(parameters);

                    listOrder.add(order);
                }
                entity.setOrders(listOrder);

            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());

        }
    }

}
