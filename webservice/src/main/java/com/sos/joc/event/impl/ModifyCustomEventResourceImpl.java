package com.sos.joc.event.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.IModifyCustomEventResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.event.custom.EventIdsFilter;
import com.sos.joc.model.event.custom.ModifyEvent;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

@Path("events")
public class ModifyCustomEventResourceImpl extends JOCResourceImpl implements IModifyCustomEventResource {

	private static final String UTC = "UTC";

	private static final Logger LOGGER = LoggerFactory.getLogger(ModifyCustomEventResourceImpl.class);

	private static final String JOB_CHAIN_EVENT_SERVICE = "/sos/events/scheduler_event_service";
	private static final String REMOVE = "remove";
	private static final String ADD = "add";
	private static final String PROCESS = "process";
	private static final String API_CALL = "./events/custom/";
	private SchedulerEventDBLayer schedulerEventDBLayer;

	@Override
	public JOCDefaultResponse addEvent(String accessToken, ModifyOrders modifyOrders) throws JocException {
		return executeModifyEvent(ADD, modifyOrders, accessToken);
	}

	private void createProcessOrder(String jobChain) throws JocException {

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

			XMLBuilder xml = new XMLBuilder("add_order");
			xml.addAttribute("job_chain", normalizePath(jobChain));
			Element params = XMLBuilder.create("params");
			params.addElement("param").addAttribute("name", "action").addAttribute("value", PROCESS);
			xml.add(params);

			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventorySupervisorInstance);
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
		} finally {
			Globals.disconnect(session);
		}
	}

	@Override
	public JOCDefaultResponse removeEvent(String accessToken, ModifyOrders modifyOrders) throws JocException {
		return executeModifyEvent(REMOVE, modifyOrders, accessToken);
	}

	private JOCDefaultResponse executeModifyEvent(String request, ModifyOrders modifyOrders, String accessToken)
			throws JocException {

		SOSHibernateSession session = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, modifyOrders, accessToken,
					modifyOrders.getJobschedulerId(),
					getPermissonsJocCockpit(modifyOrders.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isAddOrder());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			if (modifyOrders.getOrders().size() == 0) {
				throw new JocMissingRequiredParameterException("undefined 'order'");
			}

			session = Globals.createSosHibernateStatelessConnection(API_CALL + ADD);
			session.setAutoCommit(false);
			schedulerEventDBLayer = new SchedulerEventDBLayer(session);

			for (ModifyOrder order : modifyOrders.getOrders()) {
				schedulerEventDBLayer.beginTransaction();
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
					performEventRequest(request, modifyEvent);
					schedulerEventDBLayer.commit();
					createProcessOrder(order.getJobChain());
				}
			}
			notify(schedulerEventDBLayer, accessToken);

			return JOCDefaultResponse.responsePlainStatus200("JobScheduler response: OK");

		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responsePlainStatus420(errorOutput);
		} finally {
			Globals.disconnect(session);
		}
	}

	private String getParameterAsString(ModifyEvent modifyEvent) {
		String s = "<params>";
		if (modifyEvent.getParams().size() > 0) {
			for (NameValuePair nameValuePair : modifyEvent.getParams()) {
				s = s + "<param name=" + nameValuePair.getName() + " value=\"" + nameValuePair.getValue() + "\"/>";
			}
			return s + "</params>";
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

		if (modifyEvent.getExpirationCycle() != null && !modifyEvent.getExpirationCycle().isEmpty()) {
			schedulerEventFilter.setExpirationCycle(modifyEvent.getExpirationCycle());
		}

		if (modifyEvent.getExpirationPeriod() != null && !modifyEvent.getExpirationPeriod().isEmpty()) {
			schedulerEventFilter.setExpirationPeriod(modifyEvent.getExpirationPeriod());
		} else {
			schedulerEventFilter.setExpirationPeriod("24:00:00");
		}

		if (modifyEvent.getExpires() != null) {
			Date expiresUtc = JobSchedulerDate.getDateFromISO8601String(modifyEvent.getExpires());
			schedulerEventFilter.setExpires(expiresUtc);
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
	public JOCDefaultResponse addEvent(String accessToken, ModifyEvent modifyEvent) throws Exception {

		String request = ADD;
		SOSHibernateSession session = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, modifyEvent, accessToken,
					modifyEvent.getJobschedulerId(),
					getPermissonsJocCockpit(modifyEvent.getJobschedulerId(), accessToken).getEvent().getExecute()
							.isAdd());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			checkRequiredParameter("event_class", modifyEvent.getEventClass());
			checkRequiredParameter("event_id", modifyEvent.getEventId());
			checkRequiredParameter("exit_code", modifyEvent.getExitCode());

			session = Globals.createSosHibernateStatelessConnection(API_CALL + ADD);
			session.setAutoCommit(false);
			schedulerEventDBLayer = new SchedulerEventDBLayer(session);
			schedulerEventDBLayer.beginTransaction();
			performEventRequest(request, modifyEvent);
			schedulerEventDBLayer.commit();
			notify(schedulerEventDBLayer, accessToken);

			return JOCDefaultResponse.responseStatusJSOk(new Date());

		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responseStatusJSError(errorOutput);
		} finally {
			Globals.disconnect(session);
		}
	}

	@Override
	public JOCDefaultResponse deleteEvents(String accessToken, EventIdsFilter eventIdsFilter) throws Exception {
		SOSHibernateSession session = null;
		String request = "delete";
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, eventIdsFilter, accessToken,
					eventIdsFilter.getJobschedulerId(),
					getPermissonsJocCockpit(eventIdsFilter.getJobschedulerId(), accessToken).getEvent().getExecute()
							.isDelete());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			session = Globals.createSosHibernateStatelessConnection(API_CALL + request);

			SchedulerEventDBLayer schedulerEventDBLayer = new SchedulerEventDBLayer(session);
			SchedulerEventFilter schedulerEventFilter = new SchedulerEventFilter();
			if (eventIdsFilter.getIds().size() > 0) {
				schedulerEventFilter.setIds(eventIdsFilter.getIds());
			}
			session.setAutoCommit(false);
			schedulerEventDBLayer.beginTransaction();
			schedulerEventDBLayer.removeEvent(schedulerEventFilter);
			schedulerEventDBLayer.commit();
			notify(schedulerEventDBLayer, accessToken);
			createProcessOrder(JOB_CHAIN_EVENT_SERVICE);

			return JOCDefaultResponse.responseStatusJSOk(new Date());

		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responseStatusJSError(errorOutput);
		} finally {
			Globals.disconnect(session);
		}
	}

	private void notify(SchedulerEventDBLayer schedulerEventDBLayer, String accessToken) throws JocException {
		com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(
				dbItemInventoryInstance);
		jocXmlCommand.executePost(schedulerEventDBLayer.getNotifyCommand(), accessToken);

	}
}
