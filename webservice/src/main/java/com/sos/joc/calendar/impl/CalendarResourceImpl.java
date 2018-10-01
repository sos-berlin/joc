package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryClusterCalendar;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendar200;
import com.sos.joc.model.calendar.CalendarId;

@Path("calendar")
public class CalendarResourceImpl extends JOCResourceImpl implements ICalendarResource {

	private static final String API_CALL = "./calendar";

	@Override
	public JOCDefaultResponse postCalendar(String accessToken, CalendarId calendarFilter) throws Exception {
		SOSHibernateSession connection = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken,
					calendarFilter.getJobschedulerId(),
					getPermissonsJocCockpit(calendarFilter.getJobschedulerId(), accessToken).getCalendar().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			if (calendarFilter.getId() == null
					&& (calendarFilter.getPath() == null || calendarFilter.getPath().isEmpty())) {
				throw new JocMissingRequiredParameterException("undefined 'calendar path'");
			}
			// TODO only check path, id will be removed
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
			DBItemInventoryClusterCalendar calendarItem = null;
			if (calendarFilter.getPath() != null) {
				String calendarPath = normalizePath(calendarFilter.getPath());
				calendarItem = dbLayer.getCalendar(dbItemInventoryInstance.getSchedulerId(), calendarPath);
				if (calendarItem == null) {
					throw new DBMissingDataException(String.format("calendar '%1$s' not found", calendarPath));
				}
			} else {
				calendarItem = dbLayer.getCalendar(calendarFilter.getId());
				if (calendarItem == null) {
					throw new DBMissingDataException(
							String.format("calendar with id '%1$d' not found", calendarFilter.getId()));
				}
			}
			
			if (!canAdd(calendarItem.getName(), folderPermissions.getListOfFolders())) {
			    return accessDeniedResponse("Access to folder " + getParent(calendarItem.getName()) + " denied.");
            }

			Calendar calendar = new ObjectMapper().readValue(calendarItem.getConfiguration(), Calendar.class);
			calendar.setId(calendarItem.getId());
			calendar.setPath(calendarItem.getName());
			calendar.setName(calendarItem.getBaseName());
			Calendar200 entity = new Calendar200();
			entity.setCalendar(calendar);
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