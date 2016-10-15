package com.sos.joc.orders.impl;

import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandModifyOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdModifyOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandModifyOrder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceCommandModifyOrderImpl.class);

    private String[] getParams(List<NameValuePair> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            NameValuePair param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeModifyOrderCommand(ModifyOrder order, String command) {

        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

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
            listOfErrors = addError(listOfErrors, jocXmlCommand, order.getJobChain());

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing modify order.%s %s:%s", command, e.getCause(), e.getMessage()));
        }
    }

    private JOCDefaultResponse postOrdersCommand(String accessToken, String command, boolean permission, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders: Start");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(accessToken, modifyOrders.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyOrder order : modifyOrders.getOrders()) {
                jocDefaultResponse = executeModifyOrderCommand(order, command);
            }
            if (listOfErrors != null) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders: Start");
        try {
            return postOrdersCommand(accessToken, "start", getPermissons(accessToken).getOrder().isStart(), modifyOrders);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSuspend(String accessToken, ModifyOrders modifyOrders) throws Exception {
        LOGGER.debug("init Orders:Suspend");
        return postOrdersCommand(accessToken, "suspend", getPermissons(accessToken).getOrder().isSuspend(), modifyOrders);
    }

    @Override
    public JOCDefaultResponse postOrdersResume(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders:Resume");
        try {
            return postOrdersCommand(accessToken, "resume", getPermissons(accessToken).getOrder().isResume(), modifyOrders);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersReset(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders: Reset");
        try {
            return postOrdersCommand(accessToken, "reset", getPermissons(accessToken).getOrder().isReset(), modifyOrders);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetState(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders: Set State");
        try {
            return postOrdersCommand(accessToken, "set_state", getPermissons(accessToken).getOrder().isSetState(), modifyOrders);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders: Set Runtime");
        try {
            return postOrdersCommand(accessToken, "set_run_time", getPermissons(accessToken).getOrder().isSetRunTime(), modifyOrders);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

}
