package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.resource.IOrdersResourceP;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourcePImpl.class);

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersFilterSchema ordersFilterSchema) throws Exception {

        LOGGER.debug("init OrdersP");
        JOCDefaultResponse jocDefaultResponse = init(ordersFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            OrdersPSchema entity = new OrdersPSchema();
            entity.setDeliveryDate(new Date());

            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection,ordersFilterSchema.getJobschedulerId());
            List<DBItemInventoryOrder> listOfOrders = dbLayer.getInventoryOrders();
            List<Order> listOrder = new ArrayList<Order>();
            for (DBItemInventoryOrder inventoryOrder : listOfOrders) {
                Order order = new Order();
                order.setSurveyDate(inventoryOrder.getModified());
                order.setPath(inventoryOrder.getName());
                order.setOrderId(inventoryOrder.getOrderId());
                order.setJobChain(inventoryOrder.getJobChain());
                if (!ordersFilterSchema.getCompact()) {
                    order.setEstimatedDuration(-1);
                    order.setConfigurationDate(new Date());
                    order.setEndState("myEndState");
                    order.setInitialState("myInitialState");
                    order.setTitle("myTitle");
                    order.setType(Order.Type.PERMANENT);
                    order.setPriority(-1);

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
