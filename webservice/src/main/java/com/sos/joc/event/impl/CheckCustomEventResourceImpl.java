package com.sos.joc.event.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.eventing.db.SchedulerEventDBLayer;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.ICheckCustomEventResource;
import com.sos.joc.model.event.custom.CheckEvent;
import com.sos.joc.model.event.custom.CheckResult;

@Path("events")
public class CheckCustomEventResourceImpl extends JOCResourceImpl implements ICheckCustomEventResource {

	private static final String API_CALL = "./events/custom/check";
	private String xPath = "";

	@Override
	public JOCDefaultResponse checkEvent(String accessToken, CheckEvent checkEvent) {
		SOSHibernateSession session = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, checkEvent, accessToken,
					checkEvent.getJobschedulerId(), getPermissonsCommands(checkEvent.getJobschedulerId(), accessToken)
							.getJobschedulerMaster().getView().isParameter());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			DBItemInventoryInstance dbItemInventorySupervisorInstance = null;
			session = Globals.createSosHibernateStatelessConnection(API_CALL);
			Long supervisorId = dbItemInventoryInstance.getSupervisorId();
			if (supervisorId != DBLayer.DEFAULT_ID) {
				InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
				dbItemInventorySupervisorInstance = dbLayer.getInventoryInstanceByKey(supervisorId);
			}
			if (dbItemInventorySupervisorInstance == null) {
				dbItemInventorySupervisorInstance = dbItemInventoryInstance;
			}

			CheckResult entity = new CheckResult();
			entity.setCount(0);
			entity.setDeliveryDate(Date.from(Instant.now()));

			SchedulerEventDBLayer schedulerEventDBLayer = new SchedulerEventDBLayer(session);
			Document eventDocument = schedulerEventDBLayer.getEventsAsXml(checkEvent.getJobschedulerId());

			if (checkEvent.getxPathValue() != null && !checkEvent.getxPathValue().isEmpty()) {
				NodeList nl = XPathAPI.selectNodeList(eventDocument, checkEvent.getxPathValue());
				if (nl != null) {
					entity.setCount(nl.getLength());
				}
			} else {
				if (checkEvent.getEventClass() != null && !checkEvent.getEventClass().isEmpty()) {
					buildXPath("event_class", checkEvent.getEventClass());
				}
				if (checkEvent.getEventId() != null && !checkEvent.getEventId().isEmpty()) {
					buildXPath("event_id", checkEvent.getEventId());
				}
				if (checkEvent.getExitCode() != null) {
					buildXPath("exit_code", checkEvent.getExitCode() + "");
				}
				if (!xPath.isEmpty()) {
					xPath = "//events/event[" + xPath + "]";
				} else {
					xPath = "//events/event";
				}
				NodeList nl = XPathAPI.selectNodeList(eventDocument, xPath);
				if (nl != null) {
					entity.setCount(nl.getLength());
				}
			}

			return JOCDefaultResponse.responsePlainStatus200(entity.getCount());
		} catch (Exception e) {
			String errorOutput = e.getClass().getSimpleName() + ": "
					+ ((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
			return JOCDefaultResponse.responsePlainStatus420(errorOutput);
		} finally {
			Globals.disconnect(session);
		}

	}

	private void buildXPath(String attr, String value) {
		String[] values = value.trim().split("\\s+");
		String xPathOr = "";
		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				xPathOr += "@" + attr + "='" + values[i] + "'";
			} else {
				xPathOr += "or @" + attr + "='" + values[i] + "'";
			}
		}
		if (values.length > 1) {
			xPathOr = "(" + xPathOr + ")";
		}
		if (values.length > 0) {
			if (!xPath.isEmpty()) {
				xPathOr = " and " + xPathOr;
			}
			xPath += xPathOr;
		}
	}

}
