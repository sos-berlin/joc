package com.sos.joc.order.impl;

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
import com.sos.joc.model.order.Order200PSchema;
import com.sos.joc.order.post.OrderBody;
import com.sos.joc.order.resource.IOrderPResource;

@Path("order")
public class OrderPResourceImpl extends JOCResourceImpl implements IOrderPResource {
    private static final Logger LOGGER = Logger.getLogger(OrderPResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderP(String accessToken, OrderBody orderBody) throws Exception {
        LOGGER.debug("init OrderP");
        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            Order200PSchema entity = new Order200PSchema();

            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(jobschedulerUser.getSosShiroCurrentUser().getSosHibernateConnection(), accessToken);
            DBItemInventoryOrder dbItemInventoryOrder = dbLayer.getInventoryOrderByOrderId(orderBody.getJobChain(), orderBody.getOrderId());

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

            List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
            NameValuePairsSchema param1 = new NameValuePairsSchema();
            NameValuePairsSchema param2 = new NameValuePairsSchema();
            param1.setName("param1");
            param1.setValue("value1");
            param2.setName("param2");
            param2.setValue("value2");
            parameters.add(param1);
            parameters.add(param2);
            order.setParams(parameters);

            order.setPriority(-1);
            order.setTitle(dbItemInventoryOrder.getTitle());
            order.setType(Order.Type.permanent);

            entity.setOrder(order);

            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
