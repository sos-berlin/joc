package com.sos.joc.schedule.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.schedule.ScheduleVolatile;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.schedule.ScheduleFilter;
import com.sos.joc.model.schedule.ScheduleV200;
import com.sos.joc.schedule.resource.IScheduleResource;

@Path("schedule")
public class ScheduleResourceImpl extends JOCResourceImpl implements IScheduleResource {

	private static final String API_CALL = "./schedule";

	@Override
	public JOCDefaultResponse postSchedule(String xAccessToken, String accessToken, ScheduleFilter scheduleFilter)
			throws Exception {
		return postSchedule(getAccessToken(xAccessToken, accessToken), scheduleFilter);
	}

	public JOCDefaultResponse postSchedule(String accessToken, ScheduleFilter scheduleFilter) throws Exception {

		SOSHibernateSession connection = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, scheduleFilter, accessToken,
					scheduleFilter.getJobschedulerId(),
					getPermissonsJocCockpit(scheduleFilter.getJobschedulerId(), accessToken).getSchedule().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			this.checkRequiredParameter("schedule", scheduleFilter.getSchedule());

			String schedulePath = normalizePath(scheduleFilter.getSchedule());
			String scheduleParent = getParent(schedulePath);

			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
			String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders no_subfolders",
					scheduleParent);
			jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

			String xPath = String.format("/spooler/answer//schedules/schedule[@path='%1$s']", schedulePath);
			Element scheduleElement = (Element) jocXmlCommand.getSosxml().selectSingleNode(xPath);

			if (scheduleElement == null) {
				throw new JobSchedulerBadRequestException(String.format("Schedule '%1$s' doesn't exit.", schedulePath));
			}
			InventorySchedulesDBLayer dbLayer = null;
			try {
				connection = Globals.createSosHibernateStatelessConnection(API_CALL);
				Globals.beginTransaction(connection);
				dbLayer = new InventorySchedulesDBLayer(connection);
			} catch (Exception e) {
			}

			ScheduleV200 entity = new ScheduleV200();
			entity.setSchedule(new ScheduleVolatile(jocXmlCommand.getSurveyDate(), scheduleElement, dbLayer,
					dbItemInventoryInstance));
			entity.setDeliveryDate(Date.from(Instant.now()));

			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}
}