package com.sos.joc.event.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.jitl.eventing.db.SchedulerEventDBLayer;
import com.sos.jitl.eventing.db.SchedulerEventFilter;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.DeleteEventsAudit;
import com.sos.joc.classes.audit.ModifyEventAudit;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.IModifyCustomEventResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.event.custom.EventIdsFilter;
import com.sos.joc.model.event.custom.ModifyEvent;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

@Path("events")
public class ModifyCustomEventResourceImpl extends JOCResourceImpl implements IModifyCustomEventResource {

    private static final String UTC = "UTC";

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyCustomEventResourceImpl.class);

    private static final String EXPIRATION_CYCLE_DATE_FORMAT = "HH:mm:ss";
    private static final String EXPIRES_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String JOB_CHAIN_EVENT_SERVICE = "/sos/events/scheduler_event_service";
    private static final String REMOVE = "remove";
    private static final String ADD = "add";
    private static final String PROCESS = "process";
    private static final String API_CALL = "./events/custom/";
    private SchedulerEventDBLayer schedulerEventDBLayer;

    @Override
    public JOCDefaultResponse addEvent(String accessToken, byte[] modifyOrdersBytes) {
        return executeModifyEvent(ADD, modifyOrdersBytes, accessToken);
    }

    private void createProcessOrder(String jobChain, ModifyEvent modifyEvent) throws JocException {

        SOSHibernateSession session = null;
        try {
            DBItemInventoryInstance dbItemInventorySupervisorInstance = null;
            session = Globals.createSosHibernateStatelessConnection(API_CALL + PROCESS);
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
                dbItemInventorySupervisorInstance = dbLayer.getInventoryInstanceByKey(supervisorId);
            }
            if (dbItemInventorySupervisorInstance == null) {
                dbItemInventorySupervisorInstance = dbItemInventoryInstance;
            }
            Element xml = XMLBuilder.create("add_order").addAttribute("job_chain", normalizePath(jobChain));
            Element params = xml.addElement("params");
            params.addElement("param").addAttribute("name", "action").addAttribute("value", PROCESS);
            if (modifyEvent != null) {
                if (modifyEvent.getEventClass() != null) {
                    params.addElement("param").addAttribute("name", "event_class").addAttribute("value", modifyEvent.getEventClass());
                }
                if (modifyEvent.getJobChain() != null) {
                    params.addElement("param").addAttribute("name", "job_chain").addAttribute("value", modifyEvent.getJobChain());
                }
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventorySupervisorInstance);
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
        } finally {
            Globals.disconnect(session);
        }
    }

    @Override
    public JOCDefaultResponse removeEvent(String accessToken, byte[] modifyOrdersBytes) {
        return executeModifyEvent(REMOVE, modifyOrdersBytes, accessToken);
    }

    private JOCDefaultResponse executeModifyEvent(String request, byte[] modifyOrdersBytes, String accessToken) {

        SOSHibernateSession session = null;
        try {
            JsonValidator.validateFailFast(modifyOrdersBytes, ModifyOrders.class);
            ModifyOrders modifyOrders = Globals.objectMapper.readValue(modifyOrdersBytes, ModifyOrders.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, modifyOrders, accessToken, modifyOrders.getJobschedulerId(),
                    getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getJobChain().getExecute().isAddOrder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'order'");
            }

            session = Globals.createSosHibernateStatelessConnection(API_CALL + request);
            session.setAutoCommit(false);
            schedulerEventDBLayer = new SchedulerEventDBLayer(session);

            for (ModifyOrder order : modifyOrders.getOrders()) {

                if (order.getParams() != null && order.getParams().isEmpty()) {
                    order.setParams(null);
                }
                if (order.getCalendars() != null && order.getCalendars().isEmpty()) {
                    order.setCalendars(null);
                }
                ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
                logAuditMessage(orderAudit);

                schedulerEventDBLayer.beginTransaction();
                String timeZone = "";
                ModifyEvent modifyEvent = new ModifyEvent();
                if (order.getParams() != null && !order.getParams().isEmpty()) {
                    for (NameValuePair param : order.getParams()) {

                        if ("event_id".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setEventId(param.getValue());
                        }
                        if ("event_class".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setEventClass(param.getValue());
                        }
                        if ("exit_code".equals(param.getName()) && !param.getValue().isEmpty()) {
                            try {
                                modifyEvent.setExitCode(Integer.parseInt(param.getValue()));
                            } catch (NumberFormatException e) {
                                LOGGER.warn("Could not parse exitCode=" + param.getValue() + " to String");
                            }
                        }
                        if ("expires".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setExpires(param.getValue());
                        }

                        if ("expires_timezone".equals(param.getName()) && !param.getValue().isEmpty()) {
                            timeZone = param.getValue();
                        }

                        if ("expiration_cycle".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setExpirationCycle(param.getValue());
                        }

                        if ("expiration_period".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setExpirationPeriod(param.getValue());
                        }
                        if ("job_chain".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setEventjobChain(param.getValue());
                        }
                        if ("job_name".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setJob(param.getValue());
                        }
                        if ("order_id".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setOrderId(param.getValue());
                        }

                        if ("scheduler_id".equals(param.getName()) && !param.getValue().isEmpty()) {
                            modifyEvent.setJobschedulerId(param.getValue());
                        }

                    }

                    if (!"".equals(timeZone) && timeZone != null) {
                        if ((!"".equals(modifyEvent.getExpirationCycle()) && modifyEvent.getExpirationCycle() != null)) {
                            String expirationCycle = modifyEvent.getExpirationCycle() + ":00:00:00";
                            expirationCycle = expirationCycle.substring(0, 8);
                            expirationCycle = UtcTimeHelper.convertTimeZoneToTimeZone(EXPIRATION_CYCLE_DATE_FORMAT, timeZone, "UTC", expirationCycle);
                            modifyEvent.setExpirationCycle(expirationCycle);
                        }
                        if ((!"".equals(modifyEvent.getExpires()) && modifyEvent.getExpires() != null)) {
                            String expires = modifyEvent.getExpires() + ":00:00:00";
                            expires = expires.substring(0, 8);
                            expires = UtcTimeHelper.convertTimeZoneToTimeZone(EXPIRES_DATE_FORMAT, timeZone, "UTC", expires);
                            modifyEvent.setExpires(expires);

                        }
                    }

                    performEventRequest(request, modifyEvent);
                    schedulerEventDBLayer.commit();
                    storeAuditLogEntry(orderAudit);
                    createProcessOrder(order.getJobChain(), modifyEvent);
                }
            }
            notify(schedulerEventDBLayer, accessToken);

            return JOCDefaultResponse.responsePlainStatus200("JobScheduler response: OK");

        } catch (Exception e) {
            String errorOutput = e.getClass().getSimpleName() + ": " + ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
            return JOCDefaultResponse.responsePlainStatus420(errorOutput);
        } finally {
            Globals.disconnect(session);
        }
    }

    private String getParameterAsString(ModifyEvent modifyEvent) {
        if (modifyEvent.getParams() != null && !modifyEvent.getParams().isEmpty()) {
            Element params = XMLBuilder.create("params");
            for (NameValuePair param : modifyEvent.getParams()) {
                params.addElement("param").addAttribute("name", param.getName()).addAttribute("value", param.getValue());
            }
            return params.asXML();
        } else {
            return "";
        }
    }

    private void performEventRequest(String request, ModifyEvent modifyEvent) throws Exception {

        SchedulerEventFilter schedulerEventFilter = new SchedulerEventFilter();
        schedulerEventFilter.setFilterTimezone(UTC);

        schedulerEventFilter.setSchedulerId(modifyEvent.getJobschedulerId());
        schedulerEventFilter.setJobChain(modifyEvent.getEventjobChain());
        schedulerEventFilter.setEventId(modifyEvent.getEventId());
        schedulerEventFilter.setEventClass(modifyEvent.getEventClass());

        String timeZone = modifyEvent.getExpiresTimezone();

        if (!"".equals(timeZone) && timeZone != null) {
            if ((!"".equals(modifyEvent.getExpirationCycle()) && modifyEvent.getExpirationCycle() != null)) {
                String expirationCycle = modifyEvent.getExpirationCycle() + ":00:00:00";
                expirationCycle = expirationCycle.substring(0, 8);
                expirationCycle = UtcTimeHelper.convertTimeZoneToTimeZone(EXPIRATION_CYCLE_DATE_FORMAT, timeZone, "UTC", expirationCycle);
                modifyEvent.setExpirationCycle(expirationCycle);
            }
            if ((!"".equals(modifyEvent.getExpires()) && modifyEvent.getExpires() != null)) {
                String expires = modifyEvent.getExpires();
                expires = UtcTimeHelper.convertTimeZoneToTimeZone(EXPIRES_DATE_FORMAT, timeZone, "UTC", expires);
                modifyEvent.setExpires(expires);
            }
        }

        if (modifyEvent.getExpirationCycle() != null && !modifyEvent.getExpirationCycle().isEmpty()) {
            schedulerEventFilter.setExpirationCycle(modifyEvent.getExpirationCycle());
        } else {
            if (modifyEvent.getExpires() != null) {
                schedulerEventFilter.setExpires(modifyEvent.getExpires());
            } else {
                if (modifyEvent.getExpirationPeriod() != null && !modifyEvent.getExpirationPeriod().isEmpty()) {
                    schedulerEventFilter.setExpirationPeriod(modifyEvent.getExpirationPeriod());
                } else {
                    schedulerEventFilter.setExpirationPeriod("24:00:00");
                }
            }
        }

        schedulerEventFilter.setJobName(modifyEvent.getJob());
        schedulerEventFilter.setOrderId(modifyEvent.getOrderId());
        schedulerEventFilter.setExitCode(modifyEvent.getExitCode());
        schedulerEventFilter.setRemoteSchedulerHost(dbItemInventoryInstance.getHostname());
        schedulerEventFilter.setRemoteSchedulerPort(dbItemInventoryInstance.getPort());
        schedulerEventFilter.setParametersAsString(getParameterAsString(modifyEvent));

        if (ADD.equals(request)) {
            schedulerEventDBLayer.addEvent(schedulerEventFilter);
        }
        if (REMOVE.equals(request)) {
            schedulerEventDBLayer.removeEvent(schedulerEventFilter);
        }
    }

    @Override
    public JOCDefaultResponse addEvent2(String accessToken, byte[] modifyEventBytes) {

        SOSHibernateSession session = null;
        try {
            JsonValidator.validateFailFast(modifyEventBytes, ModifyEvent.class);
            ModifyEvent modifyEvent = Globals.objectMapper.readValue(modifyEventBytes, ModifyEvent.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "add_event", modifyEvent, accessToken, modifyEvent.getJobschedulerId(),
                    getPermissonsJocCockpit(modifyEvent.getJobschedulerId(), accessToken).getEvent().getExecute().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("event_class", modifyEvent.getEventClass());
            checkRequiredParameter("event_id", modifyEvent.getEventId());
            checkRequiredParameter("exit_code", modifyEvent.getExitCode());

            if (modifyEvent.getParams() != null && modifyEvent.getParams().isEmpty()) {
                modifyEvent.setParams(null);
            }

            ModifyEventAudit modifyEventAudit = new ModifyEventAudit(modifyEvent);
            logAuditMessage(modifyEventAudit);

            session = Globals.createSosHibernateStatelessConnection(API_CALL + "add_event");
            session.setAutoCommit(false);
            schedulerEventDBLayer = new SchedulerEventDBLayer(session);
            schedulerEventDBLayer.beginTransaction();
            performEventRequest(ADD, modifyEvent);
            schedulerEventDBLayer.commit();
            createProcessOrder(JOB_CHAIN_EVENT_SERVICE, modifyEvent);
            storeAuditLogEntry(modifyEventAudit);

            notify(schedulerEventDBLayer, accessToken);

            return JOCDefaultResponse.responseStatusJSOk(new Date());

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
        }
    }

    @Override
    public JOCDefaultResponse deleteEvents(String accessToken, byte[] eventIdsFilterBytes) {
        SOSHibernateSession session = null;
        try {
            JsonValidator.validateFailFast(eventIdsFilterBytes, EventIdsFilter.class);
            EventIdsFilter eventIdsFilter = Globals.objectMapper.readValue(eventIdsFilterBytes, EventIdsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "delete_events", eventIdsFilter, accessToken, eventIdsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(eventIdsFilter.getJobschedulerId(), accessToken).getEvent().getExecute().isDelete());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (eventIdsFilter.getIds() == null || eventIdsFilter.getIds().isEmpty()) {
                throw new JocMissingRequiredParameterException("'ids' are undefined or not convertible to a collection of number");
            }

            DeleteEventsAudit modifyEventAudit = new DeleteEventsAudit(eventIdsFilter);
            logAuditMessage(modifyEventAudit);

            session = Globals.createSosHibernateStatelessConnection(API_CALL + "delete_events");

            SchedulerEventDBLayer schedulerEventDBLayer = new SchedulerEventDBLayer(session);
            SchedulerEventFilter schedulerEventFilter = new SchedulerEventFilter();
            if (eventIdsFilter.getIds().size() > 0) {
                schedulerEventFilter.setIds(eventIdsFilter.getIds());
            }
            session.setAutoCommit(false);
            schedulerEventDBLayer.beginTransaction();
            int rows = schedulerEventDBLayer.removeEvent(schedulerEventFilter);
            schedulerEventDBLayer.commit();
            storeAuditLogEntry(modifyEventAudit);
            if (rows > 0) {
                notify(schedulerEventDBLayer, accessToken);
                createProcessOrder(JOB_CHAIN_EVENT_SERVICE, null);
            }

            return JOCDefaultResponse.responseStatusJSOk(new Date());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
        }
    }

    private void notify(SchedulerEventDBLayer schedulerEventDBLayer, String accessToken) throws JocException {
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(schedulerEventDBLayer.getNotifyCommand(), accessToken);

    }
}
