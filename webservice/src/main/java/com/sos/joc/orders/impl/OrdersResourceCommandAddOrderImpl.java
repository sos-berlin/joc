package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Path;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.exception.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.joe.common.XmlSerializer;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBLayerReporting;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.calendar.SendEventScheduled;
import com.sos.joc.classes.jobscheduler.ValidateXML;
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
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joc.model.order.AddedOrders;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.model.order.OrderPath200;
import com.sos.joc.orders.resource.IOrdersResourceCommandAddOrder;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;


@Path("orders")
public class OrdersResourceCommandAddOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandAddOrder {

    private static final String API_CALL = "./orders/add";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private List<OrderPath200> orderPaths = new ArrayList<OrderPath200>();
    private SOSHibernateSession session = null;
    private DBLayerReporting dbLayerReporting = null;

    @Override
    public JOCDefaultResponse postOrdersAdd(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
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
            Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
            for (ModifyOrder order : modifyOrders.getOrders()) {
                executeAddOrderCommand(order, modifyOrders, permittedFolders);
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

    private void executeAddOrderCommand(ModifyOrder order, ModifyOrders modifyOrders, Set<Folder> permittedFolders) {

        try {
            Instant plannedStart = null;
            if (order.getParams() != null && order.getParams().isEmpty()) {
                order.setParams(null);
            }
            if (order.getCalendars() != null && order.getCalendars().isEmpty()) {
                order.setCalendars(null);
            }
            ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
            logAuditMessage(orderAudit);

            checkRequiredParameter("jobChain", order.getJobChain());
            checkFolderPermissions(order.getJobChain(), permittedFolders);
            
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
                    plannedStart = getPlannedStartOfOrder(order);
                    orderAudit.setStartTime(plannedStart);
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
            if (order.getRunTime() != null) {
                try {
                    RunTime runTime = XmlSerializer.serializeAbstractSchedule(order.getRunTime());
                    order.setRunTimeXml(Globals.xmlMapper.writeValueAsString(runTime));
                    Document doc = ValidateXML.validateRunTimeAgainstJobSchedulerSchema(order.getRunTimeXml());
                    if (doc != null) {
                        xml.add(doc.getRootElement());                        
                    }
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime().toString());
                }
            }
            
            DBItemAuditLog auditLogDbItem = storeAuditLogEntry(orderAudit);
            DailyPlanDBItem dailyPlanDBItem = null;
            try {
                dailyPlanDBItem = savePlannedStartOfOrder(order, plannedStart, auditLogDbItem.getId());
            } finally {
                
                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
                jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                
                OrderPath200 orderPath = new OrderPath200();
                orderPath.setSurveyDate(jocXmlCommand.getSurveyDate());
                orderPath.setJobChain(order.getJobChain());

                if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                    try {
                        order.setOrderId(jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/ok/order/@id"));
                        if (order.getOrderId() != null && !order.getOrderId().isEmpty()) {
                            updatePlannedStartOfOrder(order.getOrderId(), auditLogDbItem, dailyPlanDBItem);
                        }
                        orderPath.setOrderId(order.getOrderId());
                    } catch (Exception e) {
                        orderPath.setOrderId(null);
                    }
                }
                sendOrderStartedEvent(order, plannedStart, jocXmlCommand);
                orderPaths.add(orderPath);
            }
            
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

    private DailyPlanDBItem savePlannedStartOfOrder(ModifyOrder order, Instant plannedStart, Long auditLogId) throws JobSchedulerBadRequestException,
            DBConnectionRefusedException, DBInvalidDataException, JocConfigurationException, DBOpenSessionException {
        if (plannedStart != null) {
            String orderId = order.getOrderId();
            if (orderId == null || orderId.isEmpty()) {
                orderId = ".";
            }
            try {
                if (session == null) {
                    session = Globals.createSosHibernateStatelessConnection(API_CALL);
                }
                if (dbLayerReporting == null) {
                    dbLayerReporting = new DBLayerReporting(session);
                }
                Long duration = 30L;
                if (!orderId.equals(".")) {
                    try {
                        duration = dbLayerReporting.getOrderEstimatedDuration(order.getJobChain(), order.getOrderId(), 30);
                    } catch (Exception e) {
                    }
                }
                DailyPlanDBItem dailyPlanDbItem = new DailyPlanDBItem();
                dailyPlanDbItem.setId(null);
                dailyPlanDbItem.setAuditLogId(auditLogId);
                dailyPlanDbItem.setIsAssigned(false);
                dailyPlanDbItem.setIsLate(false);
                dailyPlanDbItem.setJob("..");
                dailyPlanDbItem.setJobChain(order.getJobChain());
                dailyPlanDbItem.setModified(new Date());
                dailyPlanDbItem.setCreated(dailyPlanDbItem.getModified());
                dailyPlanDbItem.setOrderId(orderId);
                dailyPlanDbItem.setPlannedStart(Date.from(plannedStart));
                dailyPlanDbItem.setExpectedEnd(new Date(dailyPlanDbItem.getPlannedStart().getTime() + duration));
                dailyPlanDbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
                dailyPlanDbItem.setStartStart(false);
                dailyPlanDbItem.setState("PLANNED");
                if (session == null) {
                    session = Globals.createSosHibernateStatelessConnection(API_CALL);
                }
                session.save(dailyPlanDbItem);
                return dailyPlanDbItem;
            } catch (SOSHibernateInvalidSessionException ex) {
                throw new DBConnectionRefusedException(ex);
            }  catch (SOSHibernateException ex) {
                if (ex.getCause() != null && ex.getCause() instanceof ConstraintViolationException) {
                    //
                } else {
                    throw new DBInvalidDataException(ex);
                }
            } catch (Exception ex) {
                throw new DBInvalidDataException(ex);
            }
        }
        return null;
    }
    
    private void updatePlannedStartOfOrder(String orderId, DBItemAuditLog auditLogDbItem, DailyPlanDBItem dailyPlanDBItem)
            throws DBConnectionRefusedException, DBInvalidDataException {
        try {
            if (session == null) {
                session = Globals.createSosHibernateStatelessConnection(API_CALL);
            }
            if (dailyPlanDBItem != null) {
                dailyPlanDBItem.setOrderId(orderId);
                session.update(dailyPlanDBItem);
            }
            if (auditLogDbItem != null) {
                auditLogDbItem.setOrderId(orderId);
                session.update(auditLogDbItem);
            }
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }

    private void sendOrderStartedEvent(ModifyOrder order, Instant plannedStart, JOCXmlCommand jocXmlCommand)
            throws JsonProcessingException, JocException {
            long seconds = 0L;
            if (plannedStart != null) {
                seconds = plannedStart.getEpochSecond() - Instant.now().getEpochSecond();
            }
            if (seconds < 60 * 6 && seconds > 0L) { // event api needs max. 6 minutes for next call
                SendEventScheduled evt = new SendEventScheduled(order.getJobChain() + "," + order.getOrderId(), jocXmlCommand, getAccessToken());
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.schedule(evt, seconds, TimeUnit.SECONDS);
                executor.shutdown();
            }
    }
    
    private Instant getPlannedStartOfOrder(ModifyOrder order) throws JobSchedulerBadRequestException {
        String timeZone = order.getTimeZone();
        if (timeZone == null || timeZone.isEmpty()) {
            timeZone = dbItemInventoryInstance.getTimeZone();
        }
        return Instant.parse(JobSchedulerDate.getAtInUTCISO8601(order.getAt(), timeZone));
    }
}
