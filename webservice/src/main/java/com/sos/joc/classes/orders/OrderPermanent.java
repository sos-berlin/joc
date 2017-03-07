package com.sos.joc.classes.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayerReporting;
import com.sos.joc.Globals;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.model.order.OrderP;
import com.sos.joc.model.order.OrderType;


public class OrderPermanent {

    public static List<OrderP> fillOutputOrders(List<DBItemInventoryOrder> ordersFromDB, InventoryOrdersDBLayer dbLayer, Boolean compact)
            throws Exception {
        List<OrderP> listOfOutputOrders = new ArrayList<OrderP>();
        SOSHibernateSession connection = null;

        try {
            connection = Globals.createSosHibernateStatelessConnection("fillOutputOrders");
            DBLayerReporting dbLayerReporting = new DBLayerReporting(connection);
            int limit = Globals.sosShiroProperties.getProperty("limit_for_average_calculation", WebserviceConstants.DEFAULT_LIMIT);

            Globals.beginTransaction(connection);

            for (DBItemInventoryOrder inventoryOrder : ordersFromDB) {
                OrderP order = new OrderP();
                order.setSurveyDate(inventoryOrder.getModified());
                order.setPath(inventoryOrder.getName());
                order.setOrderId(inventoryOrder.getOrderId());
                order.setJobChain(inventoryOrder.getJobChainName());
                Long estimatedDurationInMillis = dbLayerReporting.getOrderEstimatedDuration(inventoryOrder, limit);
                if (estimatedDurationInMillis != null) {
                    order.setEstimatedDuration((estimatedDurationInMillis.intValue() / 1000));
                } else {
                    order.setEstimatedDuration(0);
                }
                if (compact == null || !compact) {
                    Date configDate = dbLayer.getOrderConfigurationDate(inventoryOrder.getId());
                    if (configDate != null) {
                        order.setConfigurationDate(new Date());
                    }
                    order.setEndState(inventoryOrder.getEndState());
                    order.setInitialState(inventoryOrder.getInitialState());
                    order.setTitle(inventoryOrder.getTitle());
                    order.set_type(OrderType.PERMANENT);
                    order.setPriority(inventoryOrder.getPriority());
                }
                listOfOutputOrders.add(order);
            }
            return listOfOutputOrders;
        } finally {
            Globals.disconnect(connection);
        }
    }

}
