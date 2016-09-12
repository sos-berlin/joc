package com.sos.joc.orders.impl;

import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.orders.post.commands.modify.ModifyOrdersBody;
import com.sos.joc.orders.post.commands.modify.Order;
import com.sos.joc.orders.post.commands.modify.Param;
import com.sos.joc.orders.resource.IOrdersResourceCommandAddOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdAddOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandAddOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandAddOrder {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceCommandAddOrderImpl.class);

    private String[] getParams(List<Param> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            Param param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeAddOrderCommand(Order order) {
        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdAddOrder objOrder = objFactory.createAddOrder();
            objOrder.setJobChainIfNotEmpty(order.getJobChain());
            objOrder.setIdIfNotEmpty(order.getOrderId());
            objOrder.setAtIfNotEmpty(order.getAt());
            objOrder.setEndState(order.getEndState());
            objOrder.setPriorityIfNotEmpty("");
            objOrder.setStateIfNotEmpty(order.getState());
            objOrder.setTitleIfNotEmpty("");

            String[] orderParams = getParams(order.getParams());
            if (orderParams != null) {
                objOrder.setParams(orderParams);
            }
            if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                JSObjRunTime objRuntime = new JSObjRunTime(objFactory, order.getRunTime());
                objOrder.setRunTime(objRuntime);
            }
            String xml = objFactory.toXMLString(objOrder);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing order.add %s:%s", e.getCause(), e.getMessage()));
        }
    }

    @Override
    public JOCDefaultResponse postOrdersAdd(String accessToken, ModifyOrdersBody ordersModifyOrderBody) throws Exception {
        LOGGER.debug("init Orders: Add");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(ordersModifyOrderBody.getJobschedulerId(), getPermissons(accessToken).getJobChain().isAddOrder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeAddOrderCommand(order);
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
