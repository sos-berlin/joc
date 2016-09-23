package com.sos.joc.orders.impl;

import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.order.ModifyOrderSchema;
import com.sos.joc.model.order.ModifyOrdersSchema;
import com.sos.joc.orders.resource.IOrdersResourceCommandAddOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdAddOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandAddOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandAddOrder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceCommandAddOrderImpl.class);

    private String[] getParams(List<NameValuePairsSchema> list) {
        String[] orderParams = new String[list.size() * 2];

        for (int i = 0; i < list.size(); i = i + 2) {
            NameValuePairsSchema param = list.get(i);
            orderParams[i] = param.getName();
            orderParams[i + 1] = param.getValue();
        }

        return orderParams;
    }

    private JOCDefaultResponse executeAddOrderCommand(ModifyOrderSchema order) {

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
            listOfErrors = addError(listOfErrors, jocXmlCommand, order.getJobChain());

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing delete order %s:%s", e.getCause(), e.getMessage()));
        }
    }

    @Override
    public JOCDefaultResponse postOrdersAdd(String accessToken, ModifyOrdersSchema modifyOrdersSchema) throws Exception {
        LOGGER.debug("init Orders: Add");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());
        try {
            jocDefaultResponse = init(modifyOrdersSchema.getJobschedulerId(), getPermissons(accessToken).getJobChain().isAddOrder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyOrderSchema order : modifyOrdersSchema.getOrders()) {
                jocDefaultResponse = executeAddOrderCommand(order);
            }
            if (listOfErrors != null) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
