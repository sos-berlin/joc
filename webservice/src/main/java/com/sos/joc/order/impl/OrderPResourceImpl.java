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
import com.sos.joc.model.order.OrderP200;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderP;
import com.sos.joc.order.resource.IOrderPResource;

@Path("order")
public class OrderPResourceImpl extends JOCResourceImpl implements IOrderPResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderP(String accessToken, OrderFilter orderFilter) throws Exception {
        LOGGER.debug("init OrderP");
        try {
            JOCDefaultResponse jocDefaultResponse = init(orderFilter.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection, accessToken);
            DBItemInventoryOrder dbItemInventoryOrder = dbLayer.getInventoryOrderByOrderId(orderFilter.getJobChain(), orderFilter.getOrderId());

            OrderP order = new OrderP();

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
            order.set_type(OrderType.PERMANENT);

            OrderP200 entity = new OrderP200();
            entity.setOrder(order);
            entity.setDeliveryDate(new Date());
            
            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
