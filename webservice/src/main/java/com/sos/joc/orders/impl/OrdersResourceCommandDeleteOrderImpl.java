package com.sos.joc.orders.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.orders.post.commands.start.Order;
import com.sos.joc.orders.post.commands.start.OrdersModifyOrderBody;
import com.sos.joc.orders.resource.IOrdersResourceOrderCommandDeleteOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdRemoveOrder;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandDeleteOrderImpl extends JOCResourceImpl implements IOrdersResourceOrderCommandDeleteOrder {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceCommandDeleteOrderImpl.class);
 
    private JOCDefaultResponse executeDeleteOrderCommand(Order order) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdRemoveOrder objRemoveOrder = objFactory.createRemoveOrder();
            objRemoveOrder.setJobChainIfNotEmpty(order.getJobChain());
            objRemoveOrder.setOrderIfNotEmpty(order.getOrderId());
            String xml = objFactory.toXMLString(objRemoveOrder);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order.delete:" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersDelete(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders:Delete");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(ordersModifyOrderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getDelete().isTemporary());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeDeleteOrderCommand(order);
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
