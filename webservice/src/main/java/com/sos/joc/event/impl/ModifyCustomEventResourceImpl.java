package com.sos.joc.event.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.dom4j.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.jitl.eventing.db.SchedulerEventDBItem;
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
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.event.custom.EventIdsFilter;
import com.sos.joc.model.event.custom.ModifyEvent;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

@Path("events")
public class ModifyCustomEventResourceImpl extends JOCResourceImpl implements IModifyCustomEventResource {

	private static final String EXPIRES_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String SOS_EVENTS_SCHEDULER_EVENT_SERVICE = "/sos/events/scheduler_event_service";
	private static final String API_CALL = "./events/custom/";

	@Override
	public JOCDefaultResponse addEvent(String accessToken, ModifyOrders modifyEvent) {
		return executeModifyEvent("add", modifyEvent, accessToken);
	}

	@Override
	public JOCDefaultResponse removeEvent(String accessToken, ModifyOrders modifyEvent) {
		return executeModifyEvent("remove", modifyEvent, accessToken);
	}

	private JOCDefaultResponse executeModifyEvent(String request, ModifyOrders modifyEvent, String accessToken) {

		SOSHibernateSession connection = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, modifyEvent, accessToken,
					modifyEvent.getJobschedulerId(),
					getPermissonsJocCockpit(modifyEvent.getJobschedulerId(), accessToken).getJobChain().getExecute()
							.isAddOrder());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			DBItemInventoryInstance dbItemInventorySupervisorInstance = null;
			connection = Globals.createSosHibernateStatelessConnection(API_CALL + request);
			Long supervisorId = dbItemInventoryInstance.getSupervisorId();
			if (supervisorId != DBLayer.DEFAULT_ID) {
				InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
				dbItemInventorySupervisorInstance = dbLayer.getInventoryInstanceByKey(supervisorId);
			}
			if (dbItemInventorySupervisorInstance == null) {
				dbItemInventorySupervisorInstance = dbItemInventoryInstance;
			}

			if (modifyEvent.getOrders().size() == 0) {
				throw new JocMissingRequiredParameterException("undefined 'order'");
			}

			Map<String, Boolean> requiredParams = new HashMap<String, Boolean>();
			if ("add".equals(request)) {
				requiredParams.put("event_class", Boolean.FALSE);
				requiredParams.put("event_id", Boolean.FALSE);
				requiredParams.put("exit_code", Boolean.FALSE);
			}
			for (ModifyOrder order : modifyEvent.getOrders()) {

				checkRequiredParameter("jobChain", order.getJobChain());

				XMLBuilder xml = new XMLBuilder("add_order");
				xml.addAttribute("job_chain", normalizePath(order.getJobChain()));
				if (order.getOrderId() != null && !order.getOrderId().isEmpty()) {
					xml.addAttribute("id", order.getOrderId());
				}
				boolean createdFound = false;
				if (order.getParams() != null && !order.getParams().isEmpty()) {
					Element params = XMLBuilder.create("params");
					for (NameValuePair param : order.getParams()) {
						if ("add".equals(request) && requiredParams.containsKey(param.getName())
								&& !param.getValue().isEmpty()) {
							requiredParams.put(param.getName(), Boolean.TRUE);
						}
						if ("created".equals(param.getName()) && !param.getValue().isEmpty()) {
							createdFound = true;
						}
						if (param.getValue() != null && param.getName() != null) {
							params.addElement("param").addAttribute("name", param.getName()).addAttribute("value",
									param.getValue());
						}
					}
					params.addElement("param").addAttribute("name", "action").addAttribute("value", request);
					if (!createdFound) {
						params.addElement("param").addAttribute("name", "created").addAttribute("value",
								JobSchedulerDate.getNowInISO());
					}
					xml.add(params);
				}

				for (Map.Entry<String, Boolean> entry : requiredParams.entrySet()) {
					if (!entry.getValue()) {
						throw new JocMissingRequiredParameterException("undefined " + entry.getKey());
					}
				}

				JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventorySupervisorInstance);
				jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
			}
			return JOCDefaultResponse.responsePlainStatus200("JobScheduler response: OK");
		} catch (JobSchedulerBadRequestException e) {
			String errorOutput = "JobScheduler reports error: " + e.getError().getMessage();
			return JOCDefaultResponse.responsePlainStatus420(errorOutput);
		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responsePlainStatus420(errorOutput);
		} finally {
			Globals.disconnect(connection);
		}
	}

	private NameValuePair newParam(String name, String value) {
		NameValuePair param = new NameValuePair();
		param.setName(name);
		param.setValue(value);
		return param;
	}

	private String getLocalTimeAsString(String fromTimeZoneString, String dateIso) {
		String toTimeZoneString = DateTimeZone.getDefault().getID();

		Date expiresUtc = JobSchedulerDate.getDateFromISO8601String(dateIso);
		DateTimeZone fromZone = DateTimeZone.forID(fromTimeZoneString);
		String expiresLocal = UtcTimeHelper.convertTimeZonesToString(EXPIRES_DATE_FORMAT, fromTimeZoneString,
				toTimeZoneString, new DateTime(expiresUtc).withZone(fromZone));
		return expiresLocal;
	}

	@Override
	public JOCDefaultResponse addEvent(String accessToken, ModifyEvent modifyEvent) throws Exception {
		SOSHibernateSession session = null;
		String request = "add";
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, modifyEvent, accessToken,
					modifyEvent.getJobschedulerId(),
					getPermissonsJocCockpit(modifyEvent.getJobschedulerId(), accessToken).getEvent().getExecute()
							.isAdd());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			List<ModifyOrder> listOfOrders = new ArrayList<ModifyOrder>();

			ModifyOrders modifyOrders = new ModifyOrders();
			modifyOrders.setJobschedulerId(modifyEvent.getJobschedulerId());

			ModifyOrder modifyOrder = new ModifyOrder();
			if (modifyEvent.getEventjobChain() != null && !modifyEvent.getEventjobChain().isEmpty()) {
				modifyOrder.setJobChain(modifyEvent.getEventjobChain());
			} else {
				modifyOrder.setJobChain(SOS_EVENTS_SCHEDULER_EVENT_SERVICE);
			}
			String orderId = request + ":";
			if (modifyEvent.getExitCode() != null) {
				orderId = orderId + modifyEvent.getEventClass() + ":" + modifyEvent.getEventId() + ":"
						+ modifyEvent.getExitCode();
			} else {
				orderId = orderId + modifyEvent.getEventClass() + ":" + modifyEvent.getEventId();
			}
			modifyOrder.setOrderId(orderId);
			modifyOrder.setTitle("Add event: " + modifyEvent.getEventClass() + ":" + modifyEvent.getEventId());

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(newParam("event_id", modifyEvent.getEventId()));
			params.add(newParam("event_class", modifyEvent.getEventClass()));
			params.add(newParam("exit_code", String.valueOf(modifyEvent.getExitCode())));

			if (modifyEvent.getExpires() != null) {
				String expiresLocal = getLocalTimeAsString("UTC", modifyEvent.getExpires());
				params.add(newParam("expires", expiresLocal));
			}

			if (modifyEvent.getExpirationCycle() != null && !modifyEvent.getExpirationCycle().isEmpty()) {
				params.add(newParam("expiration_cycle", modifyEvent.getExpirationCycle()));
			}
			if (modifyEvent.getExpirationPeriod() != null && !modifyEvent.getExpirationPeriod().isEmpty()) {
				params.add(newParam("expiration_period", modifyEvent.getExpirationPeriod()));
			} else {
				params.add(newParam("expiration_period", "24:00:00"));
			}
			params.add(newParam("job_chain", modifyEvent.getJobChain()));
			params.add(newParam("job_name", modifyEvent.getJob()));
			params.add(newParam("order_id", modifyEvent.getOrderId()));
			params.add(newParam("scheduler_id", modifyEvent.getJobschedulerId()));
			params.add(newParam("remote_scheduler_host", dbItemInventoryInstance.getHostname()));
			params.add(newParam("remote_scheduler_port", String.valueOf(dbItemInventoryInstance.getPort())));

			modifyOrder.setParams(params);
			listOfOrders.add(modifyOrder);

			modifyOrders.setOrders(listOfOrders);

			JOCDefaultResponse response = addEvent(accessToken, modifyOrders);
			if (response.getStatus() == 200) {
				return JOCDefaultResponse.responseStatusJSOk(new Date());
			} else {
				return JOCDefaultResponse.responseStatusJSError((String) response.getEntity());
			}

		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responseStatusJSError(errorOutput);
		} finally {
			Globals.disconnect(session);
		}
	}

	@Override
	public JOCDefaultResponse deleteEvent(String accessToken, EventIdsFilter eventIdsFilter) throws Exception {
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
			Integer limit = 0;

			schedulerEventDBLayer.setFilter(schedulerEventFilter);
			List<SchedulerEventDBItem> listOfEvents = schedulerEventDBLayer.getSchedulerEventList(limit);
			List<ModifyOrder> listOfOrders = new ArrayList<ModifyOrder>();

			ModifyOrders modifyOrders = new ModifyOrders();
			modifyOrders.setJobschedulerId(eventIdsFilter.getJobschedulerId());

			for (SchedulerEventDBItem item : listOfEvents) {
				ModifyOrder modifyOrder = new ModifyOrder();
				String orderId = request + ":";
				if (item.getExitCode() != null) {
					orderId = orderId + item.getEventClass() + ":" + item.getEventId() + ":" + item.getExitCode() + ":"
							+ item.getId();
				} else {
					orderId = orderId + item.getEventClass() + ":" + item.getEventId() + "::" + item.getId();
				}
				modifyOrder.setJobChain(SOS_EVENTS_SCHEDULER_EVENT_SERVICE);
				modifyOrder.setOrderId(orderId);
				modifyOrder.setTitle("Delete event: " + item.getEventClass() + ":" + item.getEventId());

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(newParam("event_id", item.getEventId()));
				params.add(newParam("event_class", item.getEventClass()));
				if (!"0".equals(item.getExitCodeAsString())) {
					params.add(newParam("exit_code", item.getExitCodeAsString()));
				}
				params.add(newParam("expires", item.getExpiresAsString()));
				params.add(newParam("job_chain", item.getJobChain()));
				params.add(newParam("job_name", item.getJobName()));
				params.add(newParam("order_id", item.getOrderId()));
				params.add(newParam("remote_scheduler_host", item.getRemoteSchedulerHost()));
				params.add(newParam("remote_scheduler_port", item.getRemoteSchedulerPortAsString()));
				params.add(newParam("scheduler_id", item.getSchedulerId()));

				modifyOrder.setParams(params);
				listOfOrders.add(modifyOrder);
			}

			modifyOrders.setOrders(listOfOrders);
			JOCDefaultResponse response = removeEvent(accessToken, modifyOrders);
			if (response.getStatus() == 200) {
				return JOCDefaultResponse.responseStatusJSOk(new Date());
			} else {
				return JOCDefaultResponse.responseStatusJSError((String) response.getEntity());
			}

		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responseStatusJSError(errorOutput);
		} finally {
			Globals.disconnect(session);
		}
	}
}
