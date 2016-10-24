package com.sos.joc.orders.impl;

import java.time.Instant;
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
import com.sos.joc.orders.resource.IOrdersResourceCommandModifyOrder;
import com.sos.scheduler.model.commands.JSCmdModifyOrder;
import com.sos.scheduler.model.objects.JSObjRunTime;

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandModifyOrder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceCommandModifyOrderImpl.class);
    private static String API_CALL = "./jobs/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    
    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "start", getPermissons(accessToken).getOrder().isStart(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"start", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSuspend(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "suspend", getPermissons(accessToken).getOrder().isSuspend(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"suspend", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersResume(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "resume", getPermissons(accessToken).getOrder().isResume(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"resume", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersReset(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "reset", getPermissons(accessToken).getOrder().isReset(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"reset", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetState(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "set_state", getPermissons(accessToken).getOrder().isSetState(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"set_state", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "set_run_time", getPermissons(accessToken).getOrder().isSetRunTime(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"set_run_time", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    @Override
    public JOCDefaultResponse postOrdersRemoveSetBack(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "remove_setback", getPermissons(accessToken).getOrder().isRemoveSetback(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL+"remove_setback", modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private Date executeModifyOrderCommand(ModifyOrder order, String command) {

        try {
            checkRequiredParameter("jobChain", order.getJobChain());
            checkRequiredParameter("orderId", order.getOrderId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdModifyOrder jsCmdModifyOrder = Globals.schedulerObjectFactory.createModifyOrder();
            jsCmdModifyOrder.setJobChain(order.getJobChain());
            jsCmdModifyOrder.setOrder(order.getOrderId());
            
            switch(command) {
            case "start":
                if (order.getAt() == null || "".equals(order.getAt())) {
                    jsCmdModifyOrder.setAt("now");
                } else {
                    jsCmdModifyOrder.setAt(order.getAt());
                }
                if (order.getParams() != null || order.getParams().size() > 0) {
                    jsCmdModifyOrder.setParams(getParams(order.getParams()));
                }
                break;
            case "set_state":
                if (order.getEndState() != null && !"".equals(order.getEndState())) {
                    jsCmdModifyOrder.setEndState(order.getEndState());
                }
                if (order.getState() != null && !"".equals(order.getState())) {
                    jsCmdModifyOrder.setState(order.getState());
                }
                if (order.getRemoveSetback() != null && !"".equals(order.getRemoveSetback())) {
                    jsCmdModifyOrder.setSetback("no");
                }
                //TODO resume order implicitly if new state is an end state
                //Here, asks database.
                if (order.getResume() != null && !"".equals(order.getResume())) {
                    jsCmdModifyOrder.setSuspended("no");
                }
                break;
            case "suspend":
                jsCmdModifyOrder.setSuspended("yes");
                break;
            case "resume":
                jsCmdModifyOrder.setSuspended("no");
                if (order.getParams() != null || order.getParams().size() > 0) {
                    jsCmdModifyOrder.setParams(getParams(order.getParams()));
                }
                break;
            case "reset":
                jsCmdModifyOrder.setAction("reset");
                break;
            case "remove_setback":
                jsCmdModifyOrder.setSetback("no");
                break;
            case "set_run_time":
                if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                    try {
                        //TODO order.getRunTime() is checked against scheduler.xsd
                        JSObjRunTime objRuntime = new JSObjRunTime(Globals.schedulerObjectFactory, order.getRunTime());
                        jsCmdModifyOrder.setRunTime(objRuntime);
                    } catch (Exception e) {
                        throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                    }
                }
            }
            String xml = jsCmdModifyOrder.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml);
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, order));
        }
        return null;
    }

    private JOCDefaultResponse postOrdersCommand(String accessToken, String command, boolean permission, ModifyOrders modifyOrders) {
        API_CALL += command;
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyOrders.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'orders'");
            }
            Date surveyDate = Date.from(Instant.now());
            for (ModifyOrder order : modifyOrders.getOrders()) {
                surveyDate = executeModifyOrderCommand(order, command);
            }
            if (listOfErrors.size() > 0) {
                JocError err = new JocError();
                err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyOrders));
                return JOCDefaultResponse.responseStatus419(listOfErrors, err);
            }
            return JOCDefaultResponse.responseStatusJSOk(surveyDate);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyOrders));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private Map<String,String> getParams(List<NameValuePair> params) {
        Map<String,String> orderParams = new HashMap<String,String>();
        for (NameValuePair param : params) {
            orderParams.put(param.getName(), param.getValue());
        }
        return orderParams;
    }
}
