package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemJocStartedOrders;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.audit.StartedOrdersDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.AddedOrders;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.model.order.OrderPath200;
import com.sos.joc.orders.resource.IOrdersResourceCommandAddOrder;

@Path("orders")
public class OrdersResourceCommandAddOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandAddOrder {

    private static final String API_CALL = "./orders/add";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private List<OrderPath200> orderPaths = new ArrayList<OrderPath200>();
    private SOSHibernateSession session = null;
    private StartedOrdersDBLayer startedOrdersDbLayer = null;

    @Override
    public JOCDefaultResponse postOrdersAdd(String xAccessToken, String accessToken, ModifyOrders modifyOrders) throws Exception {
        return postOrdersAdd(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersAdd(String accessToken, ModifyOrders modifyOrders) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, modifyOrders, accessToken, modifyOrders.getJobschedulerId(),
                    getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getJobChain().getExecute().isAddOrder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(modifyOrders.getAuditLog());
            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'orders'");
            }
            AddedOrders entity = new AddedOrders();
            for (ModifyOrder order : modifyOrders.getOrders()) {
                executeAddOrderCommand(order, modifyOrders);
            }
            entity.setOrders(orderPaths);
            if (orderPaths.isEmpty()) {
                entity.setOrders(null);
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            if (listOfErrors.size() > 0) {
                entity.setErrors(listOfErrors);
                entity.setOk(null);
                return JOCDefaultResponse.responseStatus419(entity);
            } else {
                entity.setErrors(null);
                entity.setOk(true);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
        }
    }

    private void executeAddOrderCommand(ModifyOrder order, ModifyOrders modifyOrders) {

        try {
            String plannedStart = null;
            if (order.getParams() != null && order.getParams().isEmpty()) {
                order.setParams(null);
            }
            if (order.getCalendars() != null && order.getCalendars().isEmpty()) {
                order.setCalendars(null);
            }
            ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
            logAuditMessage(orderAudit);

            checkRequiredParameter("jobChain", order.getJobChain());
            XMLBuilder xml = new XMLBuilder("add_order");
            xml.addAttribute("job_chain", normalizePath(order.getJobChain()));

            if (order.getOrderId() != null && !order.getOrderId().isEmpty()) {
                xml.addAttribute("id", order.getOrderId());
            }
            if ((order.getAt() == null || "".equals(order.getAt())) && (order.getRunTime() == null || "".equals(order.getRunTime()))) {
                xml.addAttribute("at", "now");
            }
            if (order.getAt() != null && !"".equals(order.getAt())) {
                if (order.getAt().contains("now")) {
                    xml.addAttribute("at", order.getAt());
                } else {
                    xml.addAttribute("at", JobSchedulerDate.getAtInJobSchedulerTimezone(order.getAt(), order.getTimeZone(), dbItemInventoryInstance
                            .getTimeZone()));
                }
                try {
                    plannedStart = JobSchedulerDate.getAtInUTCISO8601(order.getAt(), order.getTimeZone());
                } catch (Exception e) {
                }
            }
            if (order.getEndState() != null && !"".equals(order.getEndState())) {
                xml.addAttribute("end_state", order.getEndState());
            }
            if (order.getState() != null && !"".equals(order.getState())) {
                xml.addAttribute("state", order.getState());
            }
            if (order.getTitle() != null) {
                xml.addAttribute("title", order.getTitle());
            }
            if (order.getPriority() != null && order.getPriority() >= 0) {
                xml.addAttribute("priority", order.getPriority().toString());
            }
            xml.add(getParams(order.getParams()));
            if (order.getRunTime() != null && !order.getRunTime().isEmpty()) {
                try {
                    ValidateXML.validateRunTimeAgainstJobSchedulerSchema(order.getRunTime());
                    xml.add(XMLBuilder.parse(order.getRunTime()));
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                }
            }
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());

            OrderPath200 orderPath = new OrderPath200();
            orderPath.setSurveyDate(jocXmlCommand.getSurveyDate());
            orderPath.setJobChain(order.getJobChain());

            if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                try {
                    String orderId = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/ok/order/@id");
                    if (orderId != null && !orderId.isEmpty()) {
                        orderAudit.setOrderId(orderId);
                    }
                    orderPath.setOrderId(orderId);
                } catch (Exception e) {
                    orderPath.setOrderId(null);
                }
            }

            savePlannedStartOfOrder(orderAudit, plannedStart);
            storeAuditLogEntry(orderAudit);
            orderPaths.add(orderPath);
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        }
    }

    private Element getParams(List<NameValuePair> params) throws SessionNotExistException {
        Element paramsElem = XMLBuilder.create("params");
        if (params != null) {
            for (NameValuePair param : params) {
                if (!"SCHEDULER_JOC_USER_ACCOUNT".equalsIgnoreCase(param.getName())) {
                    paramsElem.addElement("param").addAttribute("name", param.getName()).addAttribute("value", param.getValue());
                }
            }
        }
        paramsElem.addElement("param").addAttribute("name", "SCHEDULER_JOC_USER_ACCOUNT").addAttribute("value", getJobschedulerUser()
                .getSosShiroCurrentUser().getUsername());
        return paramsElem;
    }

    private void savePlannedStartOfOrder(ModifyOrder order, String plannedStart) throws JobSchedulerBadRequestException,
            DBConnectionRefusedException, DBInvalidDataException, JocConfigurationException, DBOpenSessionException {
        if (plannedStart != null && order.getOrderId() != null && !order.getOrderId().isEmpty()) {
            DBItemJocStartedOrders startedOrdersDbItem = new DBItemJocStartedOrders();
            startedOrdersDbItem.setId(null);
            startedOrdersDbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
            startedOrdersDbItem.setJobChain(order.getJobChain());
            startedOrdersDbItem.setOrderId(order.getOrderId());
            startedOrdersDbItem.setPlannedStart(Date.from(Instant.parse(plannedStart)));
            if (session == null) {
                session = Globals.createSosHibernateStatelessConnection(API_CALL);
                startedOrdersDbLayer = new StartedOrdersDBLayer(session);
            }
            startedOrdersDbLayer.save(startedOrdersDbItem);
        }
    }
}
