package com.sos.joc.order.impl;

import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.Order;
import com.sos.joc.model.order.Order200PSchema;
import com.sos.joc.model.order.OrderFilterWithCompactSchema;
import com.sos.joc.order.resource.IOrderPResource;

@Path("order")
public class OrderPResourceImpl extends JOCResourceImpl implements IOrderPResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderP(String accessToken, OrderFilterWithCompactSchema orderFilterWithCompactSchema) throws Exception {
        LOGGER.debug("init OrderP");
        try {
            JOCDefaultResponse jocDefaultResponse = init(orderFilterWithCompactSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Order200PSchema entity = new Order200PSchema();

            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection, accessToken);
            DBItemInventoryOrder dbItemInventoryOrder = dbLayer.getInventoryOrderByOrderId(orderFilterWithCompactSchema.getJobChain(), orderFilterWithCompactSchema.getOrderId());

            entity.setDeliveryDate(new Date());
            Order order = new Order();

            entity.setDeliveryDate(new Date());
            order.setSurveyDate(dbItemInventoryOrder.getModified());
            order.setPath(dbItemInventoryOrder.getName());
            order.setJobChain(dbItemInventoryOrder.getJobChain());
            order.setOrderId(dbItemInventoryOrder.getOrderId());

            order.setConfigurationDate(new Date());
            order.setEstimatedDuration(-1);
            order.setInitialState("myInitialState");
            order.setEndState("myEndState");

            order.setPriority(-1);
            order.setTitle(dbItemInventoryOrder.getTitle());
            order.setType(Order.Type.PERMANENT);

            entity.setOrder(order);

            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
