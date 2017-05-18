package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
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
    private SOSHibernateSession connection = null;
    private InventoryOrdersDBLayer dbOrderLayer = null;
    private InventoryJobChainsDBLayer dbJobChainLayer = null;

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "start", getPermissonsJocCockpit(accessToken).getOrder().getExecute().isStart(), modifyOrders);
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
            return postOrdersCommand(accessToken, "suspend", getPermissonsJocCockpit(accessToken).getOrder().getExecute().isSuspend(), modifyOrders);
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
            return postOrdersCommand(accessToken, "resume", getPermissonsJocCockpit(accessToken).getOrder().getExecute().isResume(), modifyOrders);
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
            return postOrdersCommand(accessToken, "reset", getPermissonsJocCockpit(accessToken).getOrder().getExecute().isReset(), modifyOrders);
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
            return postOrdersCommand(accessToken, "set_state", getPermissonsJocCockpit(accessToken).getOrder().getChange().isState(), modifyOrders);
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
            return postOrdersCommand(accessToken, "set_run_time", getPermissonsJocCockpit(accessToken).getOrder().getChange().isRunTime(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
    
    @Override
    public JOCDefaultResponse postOrdersResetRunTime(String accessToken, ModifyOrders modifyOrders) {
        try {
            return postOrdersCommand(accessToken, "reset_run_time", getPermissonsJocCockpit(accessToken).getOrder().getChange().isRunTime(), modifyOrders);
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
            return postOrdersCommand(accessToken, "remove_setback", getPermissonsJocCockpit(accessToken).getOrder().getExecute().isRemoveSetback(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyOrderCommand(ModifyOrder order, ModifyOrders modifyOrders, String command) {

        try {
            if (order.getParams() != null && order.getParams().isEmpty()) {
                order.setParams(null);
            }
            ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
            logAuditMessage(orderAudit);
            
            checkRequiredParameter("jobChain", order.getJobChain());
            checkRequiredParameter("orderId", order.getOrderId());
            if ("set_run_time".equals(command)) {
                checkRequiredParameter("runTime", order.getRunTime()); 
            }
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            XMLBuilder xml = new XMLBuilder("modify_order");
            String jobChainPath = normalizePath(order.getJobChain());
            xml.addAttribute("order", order.getOrderId()).addAttribute("job_chain", jobChainPath);
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
                    if (isEndNode(jobChainPath, order.getState())) {
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
            case "reset_run_time":
                try {
                    DBItemInventoryOrder dbItem = getDBItem(jobChainPath, order.getOrderId());
                    if (dbItem.getRunTimeIsTemporary() == null) {
                        dbItem.setRunTimeIsTemporary(false); 
                    }
                    if (dbItem.getRunTimeIsTemporary()) {
                        String runTimeCommand = jocXmlCommand.getShowOrderCommand(jobChainPath, order.getOrderId(), "source");
                        String runTime = RunTime.getRuntimeXmlString(jobChainPath, jocXmlCommand, runTimeCommand, "//source/order/run_time", getAccessToken());
                        xml.add(XMLBuilder.parse(runTime));
                        jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                        updateRunTimeIsTemporary(dbItem, false);
                        storeAuditLogEntry(orderAudit);
                    } else {
                        //nothing to do
                    }
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                }
                break;
            }
            
            if (!"reset_run_time".equals(command)) {
                jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                if ("set_run_time".equals(command)) {
                    updateRunTimeIsTemporary(jobChainPath, order.getOrderId(), true);
                }
                storeAuditLogEntry(orderAudit);
            }
            
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        }
        return null;
    }

    private JOCDefaultResponse postOrdersCommand(String accessToken, String command, boolean permission, ModifyOrders modifyOrders) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, modifyOrders, accessToken, modifyOrders.getJobschedulerId(), permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        checkRequiredComment(modifyOrders.getAuditLog());
        if (modifyOrders.getOrders().size() == 0) {
            throw new JocMissingRequiredParameterException("undefined 'orders'");
        }
        Date surveyDate = Date.from(Instant.now());

        for (ModifyOrder order : modifyOrders.getOrders()) {
            surveyDate = executeModifyOrderCommand(order, modifyOrders, command);
        }
        Globals.disconnect(connection);
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
    
    private DBItemInventoryOrder getDBItem(String jobChainPath, String orderId) throws JocException {
        if (connection == null) {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
        }
        if (dbOrderLayer == null) {
            dbOrderLayer = new InventoryOrdersDBLayer(connection);
        }
        return dbOrderLayer.getInventoryOrderByOrderId(jobChainPath, orderId, dbItemInventoryInstance.getId());
    }
    
    private boolean isEndNode(String jobChainPath, String orderState) throws JocException {
        if (connection == null) {
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
        }
        if (dbJobChainLayer == null) {
            dbJobChainLayer = new InventoryJobChainsDBLayer(connection);
        }
        if (dbJobChainLayer.isEndNode(jobChainPath, orderState, dbItemInventoryInstance.getId())) {
            return true;
        }
        return false;
    }
    
    private void updateRunTimeIsTemporary(String jobChainPath, String orderId, boolean value) throws JocException {
        updateRunTimeIsTemporary(getDBItem(jobChainPath, orderId), value);
    }
    
    private void updateRunTimeIsTemporary(DBItemInventoryOrder dbItem, boolean value) throws JocException {
        dbItem.setRunTimeIsTemporary(value);
        try {
            connection.update(dbItem);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
}
