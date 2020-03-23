package com.sos.joc.orders.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Path;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.exception.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.joe.common.XmlDeserializer;
import com.sos.joe.common.XmlSerializer;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.dailyplan.db.DailyPlanCalender2DBFilter;
import com.sos.jitl.dailyplan.db.DailyPlanDBItem;
import com.sos.jitl.reporting.db.DBItemAuditLog;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayerReporting;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.calendar.SendEventScheduled;
import com.sos.joc.classes.configuration.JSObjectConfiguration;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandModifyOrder;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

 

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandModifyOrder {

    private static String API_CALL = "./orders/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private SOSHibernateSession session = null;
    private InventoryJobChainsDBLayer dbJobChainLayer = null;
    private InventoryOrdersDBLayer dbOrderLayer = null;
    private DBLayerReporting dbLayerReporting = null;

    @Override
    public JOCDefaultResponse postOrdersStart(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            return postOrdersCommand(accessToken, "start", getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getOrder()
                    .getExecute().isStart(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSuspend(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            return postOrdersCommand(accessToken, "suspend", getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getOrder()
                    .getExecute().isSuspend(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersResume(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            SOSPermissionJocCockpit perm = getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken);
            boolean hasResumeOrderPerm = perm.getOrder().getExecute().isResume() || perm.getYADE().getExecute().isTransferStart();
            return postOrdersCommand(accessToken, "resume", hasResumeOrderPerm, modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersReset(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            return postOrdersCommand(accessToken, "reset", getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getOrder()
                    .getExecute().isReset(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetState(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            return postOrdersCommand(accessToken, "set_state", getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getOrder()
                    .getChange().isState(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            return postOrdersCommand(accessToken, "set_run_time", getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getOrder()
                    .getChange().isRunTime(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse postOrdersRemoveSetBack(String accessToken, byte[] modifyOrdersBytes) {
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);
            
            return postOrdersCommand(accessToken, "remove_setback", getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getOrder()
                    .getExecute().isRemoveSetback(), modifyOrders);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private Date executeModifyOrderCommand(ModifyOrder order, ModifyOrders modifyOrders, String command, List<DBItemInventoryInstance> clusterMembers) {

        try {
            if (order.getParams() != null && order.getParams().isEmpty()) {
                order.setParams(null);
            }
            if (order.getCalendars() != null && order.getCalendars().isEmpty()) {
                order.setCalendars(null);
            }
            ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
            logAuditMessage(orderAudit);

            checkRequiredParameter("jobChain", order.getJobChain());
            checkRequiredParameter("orderId", order.getOrderId());
            XMLBuilder xml = new XMLBuilder("modify_order");
            if ("set_run_time".equals(command) && order.getRunTime() == null) {
                throw new JocMissingRequiredParameterException("undefined 'runTime'");
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            String jobChainPath = normalizePath(order.getJobChain());
            xml.addAttribute("order", order.getOrderId()).addAttribute("job_chain", jobChainPath);
            switch (command) {
            case "start":
                if (order.getAt() == null || order.getAt().isEmpty()) {
                    xml.addAttribute("at", "now");
                } else {
                    if (order.getAt().contains("now")) {
                        xml.addAttribute("at", order.getAt());
                    } else {
                        xml.addAttribute("at", JobSchedulerDate.getAtInJobSchedulerTimezone(order.getAt(), order.getTimeZone(),
                                dbItemInventoryInstance.getTimeZone()));
                    }
                }
                xml.add(getParams(order.getParams()));
                if (order.getEndState() != null && !"".equals(order.getEndState())) {
                    xml.addAttribute("end_state", order.getEndState());
                }
                if (order.getState() != null && !"".equals(order.getState())) {
                    xml.addAttribute("state", order.getState());
                }
                break;
            case "set_state":
                if (order.getEndState() != null && !"".equals(order.getEndState())) {
                    xml.addAttribute("end_state", order.getEndState());
                }
                if (order.getState() != null && !"".equals(order.getState())) {
                    xml.addAttribute("state", order.getState());
                }
                if (order.getRemoveSetback() != null && !order.getRemoveSetback()) {
                    xml.addAttribute("setback", "no");
                }
                if (order.getResume() != null && order.getResume()) {
                    xml.addAttribute("suspended", "no");
                } else {
                    if (isEndNode(jobChainPath, order.getState())) {
                        xml.addAttribute("suspended", "no");
                    }
                }
                xml.add(getParams(order.getParams()));
                break;
            case "suspend":
                xml.addAttribute("suspended", "yes");
                break;
            case "resume":
                xml.addAttribute("suspended", "no");
                xml.add(getParams(order.getParams()));
                if (order.getEndState() != null && !"".equals(order.getEndState())) {
                    xml.addAttribute("end_state", order.getEndState());
                }
                if (order.getState() != null && !"".equals(order.getState())) {
                    xml.addAttribute("state", order.getState());
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
                    RunTime runTime = XmlSerializer.serializeAbstractSchedule(order.getRunTime());
                    order.setRunTimeXml(Globals.xmlMapper.writeValueAsString(runTime));
                    JSObjectConfiguration jocConfiguration = new JSObjectConfiguration();
                    ModifyOrder configuration = jocConfiguration.modifyOrderRuntime(order.getRunTimeXml(), this, jobChainPath, order.getOrderId());

                    if (configuration == null) { // adhoc order
                        Document doc = ValidateXML.validateAgainstJobSchedulerSchema(order.getRunTimeXml());
                        if (doc != null) {
                            xml.add(doc.getRootElement());
                        }
                        jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());

                        updateRunTimeIsTemporary(jobChainPath, order.getOrderId(), true);

                        DailyPlanCalender2DBFilter dailyPlanCalender2DBFilter = new DailyPlanCalender2DBFilter();
                        dailyPlanCalender2DBFilter.setForJobChain(order.getJobChain());
                        dailyPlanCalender2DBFilter.setForOrderId(order.getOrderId());
                        updateDailyPlan(dailyPlanCalender2DBFilter);
                    } else {
                        
                        Document doc = ValidateXML.validateAgainstJobSchedulerSchema(configuration.getRunTimeXml());
                        if (versionIsOlderThan("1.13.1")) {

                            XMLBuilder xmlBuilder = new XMLBuilder("modify_hot_folder");
                            if (doc != null) {
                                Element orderElement = doc.getRootElement();
                                orderElement.addAttribute("job_chain", Paths.get(configuration.getJobChain()).getFileName().toString());
                                orderElement.addAttribute("id", order.getOrderId());
                                xmlBuilder.addAttribute("folder", getParent(jobChainPath)).add(orderElement);
                            }
                            jocXmlCommand.executePostWithThrowBadRequest(xmlBuilder.asXML(), getAccessToken());
                        } else {

                            Order orderPojo = XmlDeserializer.deserialize(configuration.getRunTimeXml(), Order.class);
                            if (orderPojo.getRunTime() != null && order.getCalendars() != null && !order.getCalendars().isEmpty()) {
                                Calendars calendars = new Calendars();
                                calendars.setCalendars(order.getCalendars());
                                orderPojo.getRunTime().setCalendars(Globals.objectMapper.writeValueAsString(calendars));
                            }
                            JOCHotFolder jocHotFolder = new JOCHotFolder(this);
                            jocHotFolder.putFile(jobChainPath + "," + order.getOrderId() + ".order.xml", XmlSerializer.serializeToStringWithHeader(
                                    XmlSerializer.serializeOrder(orderPojo)));
                        }

                        if (session == null) {
                            session = Globals.createSosHibernateStatelessConnection(API_CALL);
                        }
                        CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(session, dbItemInventoryInstance.getSchedulerId(),
                                CalendarObjectType.ORDER, configuration.getJobChain() + "," + order.getOrderId(), order.getRunTimeXml(), order
                                        .getCalendars());
                        calendarUsedByWriter.updateUsedBy();
                        CalendarEvent calEvt = calendarUsedByWriter.getCalendarEvent();
                        if (calEvt != null) {
                            if (clusterMembers != null) {
                                SendCalendarEventsUtil.sendEvent(calEvt, clusterMembers, getAccessToken());
                            } else {
                                SendCalendarEventsUtil.sendEvent(calEvt, dbItemInventoryInstance, getAccessToken());
                            }
                        }
                    }
                } catch (JocException e) {
                    throw e;
                } catch (Exception e) {
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime().toString());
                }
                break;
            }
            
            Instant plannedStart = getPlannedStartOfOrder(orderAudit, command);
            DBItemAuditLog dbItemAuditLog = storeAuditLogEntry(orderAudit);
            try {
                savePlannedStartOfOrder(order, plannedStart, dbItemAuditLog.getId(), command);
            } finally {
                if (!"set_run_time".equals(command)) {
                    jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
                }
            }
            sendOrderStartedEvent(order, plannedStart, command, jocXmlCommand);
            
            
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        }
        return null;
    }

    private JOCDefaultResponse postOrdersCommand(String accessToken, String command, boolean permission, ModifyOrders modifyOrders)
            throws JocException {
        Date surveyDate = Date.from(Instant.now());
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + command, modifyOrders, accessToken, modifyOrders.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(modifyOrders.getAuditLog());
            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'orders'");
            }
            
            List<DBItemInventoryInstance> clusterMembers = null;
            if (session == null && ("set_run_time".equals(command) || "start".equals(command))) {
                session = Globals.createSosHibernateStatelessConnection(API_CALL);
            }
            if ("set_run_time".equals(command)) {
                if ("active".equals(dbItemInventoryInstance.getClusterType())) {
                    InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(session);
                    clusterMembers = instanceLayer.getInventoryInstancesBySchedulerId(modifyOrders.getJobschedulerId());
                }
            }
            
            for (ModifyOrder order : modifyOrders.getOrders()) {
                surveyDate = executeModifyOrderCommand(order, modifyOrders, command, clusterMembers);
            }
        } finally {
            Globals.disconnect(session);
        }
        if (listOfErrors.size() > 0) {
            return JOCDefaultResponse.responseStatus419(listOfErrors);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
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

    private boolean isEndNode(String jobChainPath, String orderState) throws JocException {
        if (session == null) {
            session = Globals.createSosHibernateStatelessConnection(API_CALL);
        }
        if (dbJobChainLayer == null) {
            dbJobChainLayer = new InventoryJobChainsDBLayer(session);
        }
        if (dbJobChainLayer.isEndNode(jobChainPath, orderState, dbItemInventoryInstance.getId())) {
            return true;
        }
        return false;
    }

    private DBItemInventoryOrder getDBItem(String jobChainPath, String orderId) throws JocException {
        if (session == null) {
            session = Globals.createSosHibernateStatelessConnection(API_CALL);
        }
        if (dbOrderLayer == null) {
            dbOrderLayer = new InventoryOrdersDBLayer(session);
        }
        return dbOrderLayer.getInventoryOrderByOrderId(jobChainPath, orderId, dbItemInventoryInstance.getId());
    }

    private void updateRunTimeIsTemporary(String jobChainPath, String orderId, boolean value) throws JocException {
        DBItemInventoryOrder dbItem = getDBItem(jobChainPath, orderId);
        if (dbItem != null) {
            updateRunTimeIsTemporary(dbItem, value);
        }
    }

    private void updateRunTimeIsTemporary(DBItemInventoryOrder dbItem, boolean value) throws JocException {
        dbItem.setRunTimeIsTemporary(value);
        try {
            session.update(dbItem);
        } catch (SOSHibernateInvalidSessionException ex) {
            throw new DBConnectionRefusedException(ex);
        } catch (Exception ex) {
            throw new DBInvalidDataException(ex);
        }
    }
    
    private Instant getPlannedStartOfOrder(ModifyOrderAudit orderAudit, String command) throws JobSchedulerBadRequestException {
        if ("start".equals(command)) {
            String timeZone = orderAudit.getTimeZone();
            if (timeZone == null || timeZone.isEmpty()) {
                timeZone = dbItemInventoryInstance.getTimeZone();
            }
            Instant plannedStart = Instant.parse(JobSchedulerDate.getAtInUTCISO8601(orderAudit.getAt(), timeZone));
            orderAudit.setStartTime(plannedStart);
            return plannedStart;
        }
        return null;
    }
    
    private Instant savePlannedStartOfOrder(ModifyOrder order, Instant plannedStart, Long auditLogId, String command) throws DBConnectionRefusedException,
            DBInvalidDataException {
        if ("start".equals(command)) {
            try {
                if (dbLayerReporting == null) {
                    dbLayerReporting = new DBLayerReporting(session);
                }
                Long duration = 30L;
                try {
                    duration = dbLayerReporting.getOrderEstimatedDuration(order.getJobChain(), order.getOrderId(), 30);
                } catch (Exception e) {
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
                dailyPlanDbItem.setOrderId(order.getOrderId());
                dailyPlanDbItem.setPlannedStart(Date.from(plannedStart));
                dailyPlanDbItem.setExpectedEnd(new Date(dailyPlanDbItem.getPlannedStart().getTime() + duration));
                dailyPlanDbItem.setSchedulerId(dbItemInventoryInstance.getSchedulerId());
                dailyPlanDbItem.setStartStart(false);
                dailyPlanDbItem.setState("PLANNED");
                session.save(dailyPlanDbItem);
            } catch (SOSHibernateInvalidSessionException ex) {
                throw new DBConnectionRefusedException(ex);
            } catch (SOSHibernateException ex) {
                if (ex.getCause() != null && ex.getCause() instanceof ConstraintViolationException) {
                    //
                } else {
                    throw new DBInvalidDataException(ex);
                }
            } catch (Exception ex) {
                throw new DBInvalidDataException(ex);
            }
        }
        return plannedStart;
    }
    
    private void sendOrderStartedEvent(ModifyOrder order, Instant plannedStart, String command, JOCXmlCommand jocXmlCommand)
            throws JsonProcessingException, JocException {
        if ("start".equals(command) || "resume".equals(command)) {
            long seconds = 0L;
            if (plannedStart != null) {
                seconds = plannedStart.getEpochSecond() - Instant.now().getEpochSecond();
                if (seconds < 0) {
                    seconds = 0L;
                }
            }
            if (seconds < 60 * 6) { // event api needs max. 6 minutes for next call
                SendEventScheduled evt = new SendEventScheduled(order.getJobChain() + "," + order.getOrderId(), jocXmlCommand, getAccessToken());
                if (seconds == 0L) {
                    evt.run();
                } else {
                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                    executor.schedule(evt, seconds, TimeUnit.SECONDS);
                    executor.shutdown();
                }
            }
        }
    }

}
