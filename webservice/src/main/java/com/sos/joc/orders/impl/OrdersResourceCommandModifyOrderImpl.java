package com.sos.joc.orders.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateInvalidSessionException;
import com.sos.jitl.dailyplan.db.DailyPlanCalender2DBFilter;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jobscheduler.model.event.CalendarEvent;
import com.sos.jobscheduler.model.event.CalendarObjectType;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.classes.configuration.JSObjectConfiguration;
import com.sos.joc.classes.jobscheduler.ValidateXML;
import com.sos.joc.db.calendars.CalendarUsedByWriter;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandModifyOrder;

@Path("orders")
public class OrdersResourceCommandModifyOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandModifyOrder {

    private static String API_CALL = "./orders/";
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private SOSHibernateSession session = null;
    private InventoryJobChainsDBLayer dbJobChainLayer = null;
    private InventoryOrdersDBLayer dbOrderLayer = null;

    @Override
    public JOCDefaultResponse postOrdersStart(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersStart(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersStart(String accessToken, ModifyOrders modifyOrders) {
        try {
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
    public JOCDefaultResponse postOrdersSuspend(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersSuspend(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersSuspend(String accessToken, ModifyOrders modifyOrders) {
        try {
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
    public JOCDefaultResponse postOrdersResume(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersResume(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersResume(String accessToken, ModifyOrders modifyOrders) {
        try {
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
    public JOCDefaultResponse postOrdersReset(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersReset(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersReset(String accessToken, ModifyOrders modifyOrders) {
        try {
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
    public JOCDefaultResponse postOrdersSetState(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersSetState(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersSetState(String accessToken, ModifyOrders modifyOrders) {
        try {
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
    public JOCDefaultResponse postOrdersSetRunTime(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersSetRunTime(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersSetRunTime(String accessToken, ModifyOrders modifyOrders) {
        try {
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
    public JOCDefaultResponse postOrdersRemoveSetBack(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersRemoveSetBack(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersRemoveSetBack(String accessToken, ModifyOrders modifyOrders) {
        try {
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
            if ("set_run_time".equals(command)) {
                checkRequiredParameter("runTime", order.getRunTime());
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
                    JSObjectConfiguration jocConfiguration = new JSObjectConfiguration();
                    ModifyOrder configuration = jocConfiguration.modifyOrderRuntime(order.getRunTime(), this, jobChainPath, order.getOrderId());

                    if (configuration == null) { // adhoc order
                        ValidateXML.validateAgainstJobSchedulerSchema(order.getRunTime());
                        xml.add(XMLBuilder.parse(order.getRunTime()));
                        jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());

                        updateRunTimeIsTemporary(jobChainPath, order.getOrderId(), true);

                        DailyPlanCalender2DBFilter dailyPlanCalender2DBFilter = new DailyPlanCalender2DBFilter();
                        dailyPlanCalender2DBFilter.setForJobChain(order.getJobChain());
                        dailyPlanCalender2DBFilter.setForOrderId(order.getOrderId());
                        updateDailyPlan(dailyPlanCalender2DBFilter);
                    } else {
                        ValidateXML.validateAgainstJobSchedulerSchema(configuration.getRunTime());
                        XMLBuilder xmlBuilder = new XMLBuilder("modify_hot_folder");
                        Element orderElement = XMLBuilder.parse(configuration.getRunTime());
                        orderElement.addAttribute("job_chain", Paths.get(configuration.getJobChain()).getFileName().toString());
                        orderElement.addAttribute("id", order.getOrderId());

                        xmlBuilder.addAttribute("folder", getParent(jobChainPath)).add(orderElement);
                        jocXmlCommand.executePostWithThrowBadRequest(xmlBuilder.asXML(), getAccessToken());

                        if (session == null) {
                            session = Globals.createSosHibernateStatelessConnection(API_CALL);
                        }
                        CalendarUsedByWriter calendarUsedByWriter = new CalendarUsedByWriter(session, dbItemInventoryInstance.getSchedulerId(),
                                CalendarObjectType.ORDER, configuration.getJobChain() + "," + order.getOrderId(), order.getRunTime(), order
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
                    throw new JobSchedulerInvalidResponseDataException(order.getRunTime());
                }
                break;
            }
            if (!"set_run_time".equals(command)) {
                jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
            }
            storeAuditLogEntry(orderAudit);

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
            if ("set_run_time".equals(command) && session == null) {
                session = Globals.createSosHibernateStatelessConnection(API_CALL);

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

}
