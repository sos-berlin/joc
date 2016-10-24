package com.sos.joc.orders.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandAddOrder;
import com.sos.scheduler.model.commands.JSCmdAddOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;

@Path("orders")
public class OrdersResourceCommandAddOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandAddOrder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceCommandAddOrderImpl.class);
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private static final String API_CALL = "./orders/add";

    @Override
    public JOCDefaultResponse postOrdersAdd(String accessToken, ModifyOrders modifyOrders) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyOrders.getJobschedulerId(), getPermissons(accessToken).getJobChain().isAddOrder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'orders'");
            }
            Date surveyDate = new Date();
            for (ModifyOrder order : modifyOrders.getOrders()) {
                surveyDate = executeAddOrderCommand(order);
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
    
    private Date executeAddOrderCommand(ModifyOrder order) {

        try {
            checkRequiredParameter("jobChain", order.getJobChain());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdAddOrder objOrder = Globals.schedulerObjectFactory.createAddOrder();
            objOrder.setJobChain(order.getJobChain());
            if (order.getOrderId() != null && !order.getOrderId().isEmpty()) {
                objOrder.setId(order.getOrderId()); 
            }
            if ((order.getAt() == null || "".equals(order.getAt())) && (order.getRunTime() == null || "".equals(order.getRunTime()))) {
                objOrder.setAt("now");
            }
            if (order.getAt() != null && !"".equals(order.getAt())) {
                objOrder.setAt(order.getAt());
            }
            if (order.getEndState() != null && !"".equals(order.getEndState())) {
                objOrder.setEndState(order.getEndState());
            }
            if (order.getState() != null && !"".equals(order.getState())) {
                objOrder.setState(order.getState());
            }
            if (order.getTitle() != null) {
                objOrder.setTitle(order.getTitle());
            }
            if (order.getPriority() != null && order.getPriority() >= 0) {
                objOrder.setPriority(BigInteger.valueOf(order.getPriority()));
            }
            if (order.getParams() != null || order.getParams().size() > 0) {
                objOrder.setParams(getParams(order.getParams()));
            }
            if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                try {
                    //TODO order.getRunTime() is checked against scheduler.xsd
                    JSObjRunTime objRuntime = new JSObjRunTime(Globals.schedulerObjectFactory, order.getRunTime());
                    objOrder.setRunTime(objRuntime);
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                }
            }
            String xml = objOrder.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml);
            
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, order));
        }
        return null;
    }
    
    private Map<String,String> getParams(List<NameValuePair> params) {
        Map<String,String> orderParams = new HashMap<String,String>();
        for (NameValuePair param : params) {
            orderParams.put(param.getName(), param.getValue());
        }
        return orderParams;
    }
}
