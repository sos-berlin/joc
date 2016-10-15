package com.sos.joc.orders.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandDeleteOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdRemoveOrder;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandDeleteOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandDeleteOrder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceCommandDeleteOrderImpl.class);

    private JOCDefaultResponse executeDeleteOrderCommand(ModifyOrder order) {

        try {
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdRemoveOrder objRemoveOrder = objFactory.createRemoveOrder();
            objRemoveOrder.setJobChainIfNotEmpty(order.getJobChain());
            objRemoveOrder.setOrderIfNotEmpty(order.getOrderId());
            String xml = objFactory.toXMLString(objRemoveOrder);
            jocXmlCommand.excutePost(xml);
            listOfErrors = addError(listOfErrors, jocXmlCommand, order.getJobChain());

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(String.format("Error executing delete order %s:%s", e.getCause(), e.getMessage()));
        }
    }

    @Override
    public JOCDefaultResponse postOrdersDelete(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug("init Orders:Delete");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyOrders.getJobschedulerId(), getPermissons(accessToken).getOrder().getDelete().isTemporary());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyOrder order : modifyOrders.getOrders()) {
                jocDefaultResponse = executeDeleteOrderCommand(order);
            }

            if (listOfErrors != null) {
                return JOCDefaultResponse.responseStatus419(listOfErrors);
            }
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return jocDefaultResponse;
        }

        return jocDefaultResponse;

    }

}
