package com.sos.joc.orders.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.order.ModifyOrderSchema;
import com.sos.joc.model.order.ModifyOrdersSchema;
import com.sos.joc.orders.resource.IOrdersResourceCommandDeleteOrder;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.commands.JSCmdRemoveOrder;
import com.sos.scheduler.model.objects.Spooler;

@Path("orders")
public class OrdersResourceCommandDeleteOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandDeleteOrder {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceCommandDeleteOrderImpl.class);

    private JOCDefaultResponse executeDeleteOrderCommand(ModifyOrderSchema order) {
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

        try {
            SchedulerObjectFactory objFactory = new SchedulerObjectFactory();
            objFactory.initMarshaller(Spooler.class);
            JSCmdRemoveOrder objRemoveOrder = objFactory.createRemoveOrder();
            objRemoveOrder.setJobChainIfNotEmpty(order.getJobChain());
            objRemoveOrder.setOrderIfNotEmpty(order.getOrderId());
            String xml = objFactory.toXMLString(objRemoveOrder);
            jocXmlCommand.excutePost(xml);

            return JOCDefaultResponse.responseStatusJSOk(jocXmlCommand.getSurveyDate());
        } catch (Exception e) {
            addError(listOfErrors, jocXmlCommand, order.getJobChain(), e.getMessage());
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersDelete(String accessToken, ModifyOrdersSchema modifyOrdersSchema) {
        LOGGER.debug("init Orders:Delete");
        JOCDefaultResponse jocDefaultResponse = JOCDefaultResponse.responseStatusJSOk(new Date());

        try {
            jocDefaultResponse = init(modifyOrdersSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getDelete().isTemporary());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            for (ModifyOrderSchema order : modifyOrdersSchema.getOrders()) {
                jocDefaultResponse = executeDeleteOrderCommand(order);
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
