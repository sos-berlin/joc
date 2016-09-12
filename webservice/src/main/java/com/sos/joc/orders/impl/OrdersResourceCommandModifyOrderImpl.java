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
import com.sos.joc.orders.resource.IOrdersResourceCommandModifyOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifyOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandModifyOrder {
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

            SchedulerObjectFactory schedulerObjectFactory = new SchedulerObjectFactory();
            schedulerObjectFactory.initMarshaller(Spooler.class);
            JSCmdModifyOrder jsCmdModifyOrder = schedulerObjectFactory.createModifyOrder();
            jsCmdModifyOrder.setJobChainIfNotEmpty(order.getJobChain());
            jsCmdModifyOrder.setOrderIfNotEmpty(order.getOrderId());

            if ("start".equals(command)) {
                if (order.getAt() == null || "".equals(order.getAt())) {
                    order.setAt("now");
                }
            } else {
                jsCmdModifyOrder.setAtIfNotEmpty(order.getAt());
            }

            jsCmdModifyOrder.setEndStateIfNotEmpty(order.getEndState());
            jsCmdModifyOrder.setPriorityIfNotEmpty("");
            if ("set_state".equals(command)) {
                jsCmdModifyOrder.setStateIfNotEmpty(order.getState());
            }
            jsCmdModifyOrder.setSetbackIfNotEmpty("");
            if ("suspend".equals(command)) {
                jsCmdModifyOrder.setSuspendedIfNotEmpty("yes");
            }
            if ("resume".equals(command)) {
                jsCmdModifyOrder.setSuspendedIfNotEmpty("no");
            }
            if ("reset".equals(command)) {
                jsCmdModifyOrder.setAction("reset");
            }
            jsCmdModifyOrder.setTitleIfNotEmpty("");
            String[] jobParams = getParams(order.getParams());
            if (jobParams != null) {
                jsCmdModifyOrder.setParams(jobParams);
            }
            if ("set_run_time".equals(command)) {
                if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                    JSObjRunTime objRuntime = new JSObjRunTime(schedulerObjectFactory, order.getRunTime());
                    jsCmdModifyOrder.setRunTime(objRuntime);
                }
            }

            String xml = schedulerObjectFactory.toXMLString(jsCmdModifyOrder);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing order.%s %s:%s",command, e.getCause(), e.getMessage()));
        }
    }

    private JOCDefaultResponse postOrdersCommand(String accessToken, String command, boolean permission, ModifyOrdersBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Start");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken, ordersModifyOrderBody.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (Order order : ordersModifyOrderBody.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, command);
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrdersBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Start");
        return postOrdersCommand(accessToken, "start", getPermissons(accessToken).getOrder().isStart(), ordersModifyOrderBody);
    }

    @Override
    public JOCDefaultResponse postOrdersSuspend(String accessToken, ModifyOrdersBody ordersModifyOrderBody) throws Exception {
        LOGGER.debug("init Orders:Suspend");
        return postOrdersCommand(accessToken, "suspend", getPermissons(accessToken).getOrder().isSuspend(), ordersModifyOrderBody);
    }

    @Override
    public JOCDefaultResponse postOrdersResume(String accessToken, ModifyOrdersBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders:Resume");
        return postOrdersCommand(accessToken, "resume", getPermissons(accessToken).getOrder().isResume(), ordersModifyOrderBody);
    }

    @Override
    public JOCDefaultResponse postOrdersReset(String accessToken, ModifyOrdersBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Reset");
        return postOrdersCommand(accessToken, "reset", getPermissons(accessToken).getOrder().isReset(), ordersModifyOrderBody);
    }

    @Override
    public JOCDefaultResponse postOrdersSetState(String accessToken, ModifyOrdersBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Set State");
        return postOrdersCommand(accessToken, "set_state", getPermissons(accessToken).getOrder().isSetState(), ordersModifyOrderBody);
    }

    @Override
    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, ModifyOrdersBody ordersModifyOrderBody) {
        LOGGER.debug("init Orders: Set Runtime");
        return postOrdersCommand(accessToken, "set_run_time", getPermissons(accessToken).getOrder().isSetRunTime(), ordersModifyOrderBody);
    }

}
