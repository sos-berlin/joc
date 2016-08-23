package com.sos.joc.orders.impl;

import java.util.List;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.orders.post.commands.start.Order;
import com.sos.joc.orders.post.commands.start.OrdersStartBody;
import com.sos.joc.orders.post.commands.start.Param;
import com.sos.joc.orders.resource.IOrdersResourceOrderCommands;
import com.sos.joc.response.JOCCockpitResponse;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifyOrder;
import com.sos.scheduler.model.commands.JSCmdRemoveOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandsImpl extends JOCResourceImpl implements IOrdersResourceOrderCommands {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceCommandsImpl.class);

    private String[] getParams(List<Param> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            Param param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCCockpitResponse executeDeleteOrderCommand(Order order) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdRemoveOrder objRemoveOrder = objFactory.createRemoveOrder();
            objRemoveOrder.setJobChainIfNotEmpty(order.getJobChain());
            objRemoveOrder.setOrderIfNotEmpty(order.getOrderId());
            String xml = objFactory.toXMLString(objRemoveOrder);
            jocXmlCommand.excutePost(xml);

            return JOCCockpitResponse.responseStatus200(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCCockpitResponse.responseStatus420(e.getMessage());
        }
    }

    private JOCCockpitResponse executeModifyOrderCommand(Order order, String command) {
        try {

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdModifyOrder objOrder = objFactory.createModifyOrder();
            objOrder.setJobChainIfNotEmpty(order.getJobChain());
            objOrder.setOrderIfNotEmpty(order.getOrderId());
            objOrder.setActionIfNotEmpty(command);

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

            return JOCCockpitResponse.responseStatus200(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCCockpitResponse.responseStatus420(e.getMessage());
        }
    }

    @Override
    public JOCCockpitResponse postOrdersStart(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders: Start");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeModifyOrderCommand(order, "start");
        }

        return jocCockpitResponse;
    }

    @Override
    public JOCCockpitResponse postOrdersAdd(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JOCCockpitResponse postOrdersSuspend(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders:Suspend");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeModifyOrderCommand(order, "suspend");
        }

        return jocCockpitResponse;
    }

    @Override
    public JOCCockpitResponse postOrdersResume(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders:Resume");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeModifyOrderCommand(order, "resume");
        }

        return jocCockpitResponse;
    }

    @Override
    public JOCCockpitResponse postOrdersReset(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders: Reset");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeModifyOrderCommand(order, "reset");
        }

        return jocCockpitResponse;
    }

    @Override
    public JOCCockpitResponse postOrdersDelete(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders:Delete");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeDeleteOrderCommand(order);
        }

        return jocCockpitResponse;
    }

    @Override
    public JOCCockpitResponse postOrdersStartSetState(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeModifyOrderCommand(order, "set_state");
        }

        return jocCockpitResponse;
    }

    @Override
    public JOCCockpitResponse postOrdersStartSetRunTime(String accessToken, OrdersStartBody ordersStartBody) throws Exception {
        LOGGER.debug("init Orders");
        init(ordersStartBody.getJobschedulerId(), getPermissons(accessToken).getOrder().isStart());
        JOCCockpitResponse jocCockpitResponse = null;
        for (Order order : ordersStartBody.getOrders()) {
            jocCockpitResponse = executeModifyOrderCommand(order, "set_run_time");
        }

        return jocCockpitResponse;
    }

}
