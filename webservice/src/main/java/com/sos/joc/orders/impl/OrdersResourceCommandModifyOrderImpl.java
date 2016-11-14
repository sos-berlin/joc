package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
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

    private static String API_CALL = "./orders/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "start", modifyOrders);
            return postOrdersCommand(accessToken, "start", getPermissons(accessToken).getOrder().isStart(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSuspend(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "suspend", modifyOrders);
            return postOrdersCommand(accessToken, "suspend", getPermissons(accessToken).getOrder().isSuspend(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersResume(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "resume", modifyOrders);
            return postOrdersCommand(accessToken, "resume", getPermissons(accessToken).getOrder().isResume(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersReset(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "reset", modifyOrders);
            return postOrdersCommand(accessToken, "reset", getPermissons(accessToken).getOrder().isReset(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetState(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "set_state", modifyOrders);
            return postOrdersCommand(accessToken, "set_state", getPermissons(accessToken).getOrder().isSetState(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "set_run_time", modifyOrders);
            return postOrdersCommand(accessToken, "set_run_time", getPermissons(accessToken).getOrder().isSetRunTime(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersRemoveSetBack(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "remove_setback", modifyOrders);
            return postOrdersCommand(accessToken, "remove_setback", getPermissons(accessToken).getOrder().isRemoveSetback(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyOrderCommand(ModifyOrder order, String command) {

        try {
            if (order.getParams() != null && order.getParams().isEmpty()) {
                order.setParams(null);
            }
            logAuditMessage(order);
            
            checkRequiredParameter("jobChain", order.getJobChain());
            checkRequiredParameter("orderId", order.getOrderId());
            
            JSCmdModifyOrder jsCmdModifyOrder = Globals.schedulerObjectFactory.createModifyOrder();
            jsCmdModifyOrder.setJobChain(normalizePath(order.getJobChain()));
            jsCmdModifyOrder.setOrder(order.getOrderId());

            switch (command) {
            case "start":
                if (order.getAt() == null || "".equals(order.getAt())) {
                    jsCmdModifyOrder.setAt("now");
                } else {
                    jsCmdModifyOrder.setAt(order.getAt());
                }
                if (order.getParams() != null && !order.getParams().isEmpty()) {
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
                // TODO resume order implicitly if new state is an end state
                // Here, asks database.
                if (order.getResume() != null && !"".equals(order.getResume())) {
                    jsCmdModifyOrder.setSuspended("no");
                }
                break;
            case "suspend":
                jsCmdModifyOrder.setSuspended("yes");
                break;
            case "resume":
                jsCmdModifyOrder.setSuspended("no");
                if (order.getParams() != null && !order.getParams().isEmpty()) {
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
                        ValidateXML.validateRunTimeAgainstJobSchedulerSchema(order.getRunTime());
                        JSObjRunTime objRuntime = new JSObjRunTime(Globals.schedulerObjectFactory, order.getRunTime());
                        jsCmdModifyOrder.setRunTime(objRuntime);
                    } catch (JocException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                    }
                }
            }
            String xml = jsCmdModifyOrder.toXMLString();
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        }
        return null;
    }

    private JOCDefaultResponse postOrdersCommand(String accessToken, String command, boolean permission, ModifyOrders modifyOrders) throws Exception {
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
            return JOCDefaultResponse.responseStatus419(listOfErrors);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }

    private Map<String, String> getParams(List<NameValuePair> params) {
        Map<String, String> orderParams = new HashMap<String, String>();
        for (NameValuePair param : params) {
            orderParams.put(param.getName(), param.getValue());
        }
        return orderParams;
    }
}
