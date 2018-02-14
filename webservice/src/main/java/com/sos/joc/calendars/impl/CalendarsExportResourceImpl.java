package com.sos.joc.calendars.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.Globals;
import com.sos.joc.calendars.resource.ICalendarsExportResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.calendar.CalendarsFilter;
import com.sos.joc.model.calendar.UsedBy;

@Path("calendars")
public class CalendarsExportResourceImpl extends JOCResourceImpl implements ICalendarsExportResource {

	private static final String API_CALL = "./calendars/export";

	@Override
	public JOCDefaultResponse exportCalendars(String accessToken, CalendarsFilter calendarsFilter) throws Exception {
		SOSHibernateSession connection = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarsFilter, accessToken,
					calendarsFilter.getJobschedulerId(),
					getPermissonsJocCockpit(calendarsFilter.getJobschedulerId(), accessToken).getCalendar().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			if (calendarsFilter.getCalendars() == null) {
				calendarsFilter.setCalendars(new ArrayList<String>());
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			CalendarsDBLayer dbLayer = new CalendarsDBLayer(connection);
			CalendarUsageDBLayer usageDBLayer = new CalendarUsageDBLayer(connection);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"));

			List<Calendar> calendarList = new ArrayList<Calendar>();
			List<DBItemCalendar> calendarsFromDb = dbLayer.getCalendarsFromPaths(dbItemInventoryInstance.getId(),
					new HashSet<String>(calendarsFilter.getCalendars()));
			if (calendarsFromDb != null && !calendarsFromDb.isEmpty()) {
				for (DBItemCalendar dbCalendar : calendarsFromDb) {
					if (dbCalendar.getConfiguration() != null) {
						Calendar calendar = objectMapper.readValue(dbCalendar.getConfiguration(), Calendar.class);
						calendar.setPath(dbCalendar.getName());
						calendarList.add(calendar);
						SortedSet<String> orders = new TreeSet<String>();
						SortedSet<String> jobs = new TreeSet<String>();
						SortedSet<String> schedules = new TreeSet<String>();
						List<DBItemInventoryCalendarUsage> dbUsages = usageDBLayer
								.getCalendarUsages(dbCalendar.getId());
						UsedBy usedBy = new UsedBy();
						if (dbUsages != null && !dbUsages.isEmpty()) {
							for (DBItemInventoryCalendarUsage dbUsage : dbUsages) {
								if (dbUsage.getObjectType() == null) {
									continue;
								}
								Calendar usage = null;
								if (dbUsage.getConfiguration() != null) {
									usage = objectMapper.readValue(dbUsage.getConfiguration(), Calendar.class);
								}
								switch (dbUsage.getObjectType().toUpperCase()) {
								case "ORDER":
									orders.add(dbUsage.getPath());
									if (usage != null) {
										usage.setType(CalendarType.ORDER);
									}
									break;
								case "JOB":
									jobs.add(dbUsage.getPath());
									if (usage != null) {
										usage.setType(CalendarType.JOB);
									}
									break;
								case "SCHEDULE":
									schedules.add(dbUsage.getPath());
									if (usage != null) {
										usage.setType(CalendarType.SCHEDULE);
									}
									break;
								}
								if (usage != null) {
									usage.setPath(dbUsage.getPath());
									calendarList.add(usage);
								}
							}
						}
						usedBy.setJobs(new ArrayList<String>(jobs));
						usedBy.setOrders(new ArrayList<String>(orders));
						usedBy.setSchedules(new ArrayList<String>(schedules));
						calendar.setUsedBy(usedBy);
					}
				}
			}
			if (calendarList.isEmpty()) {
				throw new DBMissingDataException("no calendars found.");
			}
			Calendars entity = new Calendars();
			entity.setCalendars(calendarList);
			entity.setDeliveryDate(Date.from(Instant.now()));

			return JOCDefaultResponse.responseOctetStreamDownloadStatus200(objectMapper.writeValueAsString(entity),
					"calendars.json");
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