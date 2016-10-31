package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.BulkError;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandDeleteOrder;
import com.sos.scheduler.model.commands.JSCmdAddOrder;

@Path("orders")
public class OrdersResourceCommandDeleteOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandDeleteOrder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceCommandDeleteOrderImpl.class);
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private static final String API_CALL = "./orders/delete";
    
    @Override
    public JOCDefaultResponse postOrdersDelete(String accessToken, ModifyOrders modifyOrders) {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyOrders.getJobschedulerId(), getPermissons(accessToken).getOrder().getDelete().isTemporary());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'orders'");
            }
            Date surveyDate = new Date();
            for (ModifyOrder order : modifyOrders.getOrders()) {
                surveyDate = executeDeleteOrderCommand(order);
            }
            if (listOfErrors.size() > 0) {
                JocError err = new JocError();
                err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyOrders));
                return JOCDefaultResponse.responseStatus419(listOfErrors, err);
            }
            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }
    
    private Date executeDeleteOrderCommand(ModifyOrder order) {
        try {
            checkRequiredParameter("jobChain", order.getJobChain());
            checkRequiredParameter("orderId", order.getOrderId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdAddOrder objOrder = Globals.schedulerObjectFactory.createAddOrder();
            objOrder.setJobChain(order.getJobChain());
            objOrder.setId(order.getOrderId());
            String xml = objOrder.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
            
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, order));
        }
        return null;
    }
}
