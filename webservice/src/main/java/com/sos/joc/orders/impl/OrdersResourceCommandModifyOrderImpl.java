package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandModifyOrder;

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandModifyOrder {

    private static String API_CALL = "./orders/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrders modifyOrders) {
        try {
            initLogging(API_CALL + "start", modifyOrders);
            return postOrdersCommand(accessToken, "start", getPermissonsJocCockpit(accessToken).getOrder().isStart(), modifyOrders);
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
            return postOrdersCommand(accessToken, "suspend", getPermissonsJocCockpit(accessToken).getOrder().isSuspend(), modifyOrders);
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
            return postOrdersCommand(accessToken, "resume", getPermissonsJocCockpit(accessToken).getOrder().isResume(), modifyOrders);
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
            return postOrdersCommand(accessToken, "reset", getPermissonsJocCockpit(accessToken).getOrder().isReset(), modifyOrders);
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
            return postOrdersCommand(accessToken, "set_state", getPermissonsJocCockpit(accessToken).getOrder().isSetState(), modifyOrders);
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
            return postOrdersCommand(accessToken, "set_run_time", getPermissonsJocCockpit(accessToken).getOrder().isSetRunTime(), modifyOrders);
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
            return postOrdersCommand(accessToken, "remove_setback", getPermissonsJocCockpit(accessToken).getOrder().isRemoveSetback(), modifyOrders);
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
            if ("set_run_time".equals(command)) {
                checkRequiredParameter("runTime", order.getRunTime()); 
            }
            
            XMLBuilder xml = new XMLBuilder("modify_order");
            xml.addAttribute("order", order.getOrderId()).addAttribute("job_chain", normalizePath(order.getJobChain()));
            switch (command) {
            case "start":
                if (order.getAt() == null || "".equals(order.getAt())) {
                    xml.addAttribute("at", "now");
                } else {
                    xml.addAttribute("at", order.getAt());
                }
                if (order.getParams() != null && !order.getParams().isEmpty()) {
                    xml.add(getParams(order.getParams()));
                }
                break;
            case "set_state":
                if (order.getEndState() != null && !"".equals(order.getEndState())) {
                    xml.addAttribute("end_state", order.getEndState());
                }
                if (order.getState() != null && !"".equals(order.getState())) {
                    xml.addAttribute("state", order.getState());
                }
                if (order.getRemoveSetback() != null && !"".equals(order.getRemoveSetback())) {
                    xml.addAttribute("setback", "no");
                }
                if (order.getResume() != null && order.getResume()) {
                    xml.addAttribute("suspended", "no");
                } else {
                    InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(Globals.sosHibernateConnection);
                    if (dbLayer.isEndNode(normalizePath(order.getJobChain()), order.getState(), dbItemInventoryInstance.getId())) {
                        xml.addAttribute("suspended", "no");
                    }
                }
                break;
            case "suspend":
                xml.addAttribute("suspended", "yes");
                break;
            case "resume":
                xml.addAttribute("suspended", "no");
                if (order.getParams() != null && !order.getParams().isEmpty()) {
                    xml.add(getParams(order.getParams()));
                }
                break;
            case "reset":
                xml.addAttribute("action", "reset");
                break;
            case "remove_setback":
                xml.addAttribute("setback", "no");
                break;
            case "set_run_time":
                try {
                    ValidateXML.validateRunTimeAgainstJobSchedulerSchema(order.getRunTime());
                    xml.add(XMLBuilder.parse(order.getRunTime()));
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                }
                break;
            }
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
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
        try {
            if ("set_state".equals(command)) {
                Globals.beginTransaction();
            }
            for (ModifyOrder order : modifyOrders.getOrders()) {
                surveyDate = executeModifyOrderCommand(order, command);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if ("set_state".equals(command)) {
                Globals.rollback();;
            }
        }
        if (listOfErrors.size() > 0) {
            return JOCDefaultResponse.responseStatus419(listOfErrors);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }

    private Element getParams(List<NameValuePair> params) {
        Element paramsElem = XMLBuilder.create("params");
        for (NameValuePair param : params) {
            paramsElem.addElement("param").addAttribute("name", param.getName()).addAttribute("value", param.getValue());
        }
        return paramsElem;
    }
}
