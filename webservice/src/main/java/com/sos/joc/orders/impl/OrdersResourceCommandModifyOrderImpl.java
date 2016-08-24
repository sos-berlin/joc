package com.sos.joc.orders.impl;

import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.orders.post.commands.start.Order;
import com.sos.joc.orders.post.commands.start.OrdersModifyOrderBody;
import com.sos.joc.orders.post.commands.start.Param;
import com.sos.joc.orders.resource.IOrdersResourceOrderCommandModifyOrder;
import com.sos.joc.response.JOCDefaultResponse;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdAddOrder;
import com.sos.scheduler.model.commands.JSCmdModifyOrder;
import com.sos.scheduler.model.commands.JSCmdRemoveOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceOrderCommandModifyOrder {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceCommandModifyOrderImpl.class);

    private String[] getParams(List<Param> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            Param param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeModifyOrderCommand(Order order, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdModifyOrder objOrder = objFactory.createModifyOrder();
            objOrder.setJobChainIfNotEmpty(order.getJobChain());
            objOrder.setOrderIfNotEmpty(order.getOrderId());

            if ("start".equals(command)) {
                if (order.getAt() == null || "".equals(order.getAt())) {
                    order.setAt("now");
                }
            } else {
                objOrder.setAtIfNotEmpty(order.getAt());
            }

            objOrder.setEndStateIfNotEmpty(order.getEndState());
            objOrder.setPriorityIfNotEmpty("");
            if ("set_state".equals(command)) {
                objOrder.setStateIfNotEmpty(order.getState());
            }
            objOrder.setSetbackIfNotEmpty("");
            if ("suspend".equals(command)) {
                objOrder.setSuspendedIfNotEmpty("yes");
            }
            if ("resume".equals(command)) {
                objOrder.setSuspendedIfNotEmpty("no");
            }
            if ("reset".equals(command)) {
                objOrder.setAction("reset");
            }
            objOrder.setTitleIfNotEmpty("");
            String[] jobParams = getParams(order.getParams());
            if (jobParams != null) {
                objOrder.setParams(jobParams);
            }
            if ("set_run_time".equals(command)) {
                if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                    JSObjRunTime objRuntime = new JSObjRunTime(objFactory, order.getRunTime());
                    objOrder.setRunTime(objRuntime);
                }
            }

            String xml = objFactory.toXMLString(objOrder);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError("Error executing order." + command + ":" + e.getCause() + ":" + e.getMessage());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Start");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(ordersModifyOrderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, "start");
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersSuspend(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) throws Exception {
        LOGGER.debug("init Orders:Suspend");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(ordersModifyOrderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isSuspend());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, "suspend");
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersResume(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders:Resume");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(ordersModifyOrderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isResume());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, "resume");
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersReset(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Reset");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(accessToken, ordersModifyOrderBody.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, "reset");
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersSetState(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Set State");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken, ordersModifyOrderBody.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, "set_state");
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, OrdersModifyOrderBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Set Runtime");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(accessToken, ordersModifyOrderBody.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, "set_run_time");
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
