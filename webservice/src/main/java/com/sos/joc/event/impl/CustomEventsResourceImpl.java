package com.sos.joc.event.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.Path;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.jitl.eventing.db.SchedulerEventDBItem;
import com.sos.jitl.eventing.db.SchedulerEventDBLayer;
import com.sos.jitl.eventing.db.SchedulerEventFilter;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.event.resource.ICustomEventsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.event.custom.Event;
import com.sos.joc.model.event.custom.Events;
import com.sos.joc.model.event.custom.EventsFilter;

@Path("events")
public class CustomEventsResourceImpl extends JOCResourceImpl implements ICustomEventsResource {

	private static final String API_CALL = "./events/custom/";
	private static final String EXPIRES_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private Date utc2LocalTime(Date expiresUtc) {
		String toTimeZoneString = DateTimeZone.getDefault().getID();
		String fromTimeZoneString = "UTC";
		DateTimeZone fromZone = DateTimeZone.forID(fromTimeZoneString);
		Date expiresLocal = UtcTimeHelper.convertTimeZonesToDate(fromTimeZoneString, toTimeZoneString,
				new DateTime(expiresUtc).withZone(fromZone));
		return expiresLocal;
	}

	private Date getUtcExpiredTime(Date dateFrom) throws ParseException {
		SimpleDateFormat isoFormat = new SimpleDateFormat(EXPIRES_DATE_FORMAT);
		isoFormat.setTimeZone(TimeZone.getTimeZone(DateTimeZone.getDefault().getID()));
		DateFormat df = new SimpleDateFormat(EXPIRES_DATE_FORMAT);
		String dateFromAsString = df.format(dateFrom);
		Date date = isoFormat.parse(dateFromAsString);
		SimpleDateFormat sdfUtc = new SimpleDateFormat(EXPIRES_DATE_FORMAT);
		TimeZone tzUtc = TimeZone.getTimeZone("UTC");
		sdfUtc.setTimeZone(tzUtc);
		String sDateUtc = sdfUtc.format(date); // Convert to String first
		return sdfUtc.parse(sDateUtc); // Create a new Date object
	}

	@Override
	public JOCDefaultResponse postCustomEvents(String accessToken, EventsFilter eventFilter) {

		SOSHibernateSession session = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, eventFilter, accessToken,
					eventFilter.getJobschedulerId(),
					getPermissonsJocCockpit(eventFilter.getJobschedulerId(), accessToken).getEvent().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			session = Globals.createSosHibernateStatelessConnection(API_CALL);

			SchedulerEventDBLayer schedulerEventDBLayer = new SchedulerEventDBLayer(session);
			SchedulerEventFilter schedulerEventFilter = new SchedulerEventFilter();
			if (eventFilter.getEventClasses().size() > 0) {
				schedulerEventFilter.setEventClasses(eventFilter.getEventClasses());
			}
			if (eventFilter.getExitCodes().size() > 0) {
				schedulerEventFilter.setExitCodes(eventFilter.getExitCodes());
			}
			if (eventFilter.getEventIds().size() > 0) {
				schedulerEventFilter.setEventIds(eventFilter.getEventIds());
			}
			if (eventFilter.getOrders().size() > 0) {
				schedulerEventFilter.setOrders(eventFilter.getOrders());
			}
			if (eventFilter.getJobs().size() > 0) {
				schedulerEventFilter.setJobs(eventFilter.getJobs());
			}
			if (eventFilter.getDateFrom() != null) {
				schedulerEventFilter.setIntervalFromDate(utc2LocalTime(
						JobSchedulerDate.getDateFrom(eventFilter.getDateFrom(), eventFilter.getTimeZone())));
			}
			if (eventFilter.getDateTo() != null) {
				schedulerEventFilter.setIntervalToDate(
						utc2LocalTime(JobSchedulerDate.getDateTo(eventFilter.getDateTo(), eventFilter.getTimeZone())));
			}

			Integer limit = 0;
			if (eventFilter.getLimit() != null) {
				limit = eventFilter.getLimit();
			}
			schedulerEventFilter.setLimit(limit);			
			List<SchedulerEventDBItem> listOfEvents = schedulerEventDBLayer.getSchedulerEventList(schedulerEventFilter);
			List<Event> events = new ArrayList<Event>();

			for (SchedulerEventDBItem item : listOfEvents) {
				Event event = new Event();
				event.setId(item.getId());
				event.setCreated(item.getCreated());
				event.setEventClass(item.getEventClass());
				event.setEventId(item.getEventId());
				event.setExitCode(item.getExitCode());
				event.setExpires(getUtcExpiredTime(item.getExpires()));
				event.setJob(item.getJobName());
				event.setJobChain(item.getJobChain());
				event.setJobschedulerId(item.getSchedulerId());
				event.setOrderId(item.getOrderId());
				event.setRemoteJobSchedulerHost(item.getRemoteSchedulerHost());
				event.setRemoteJobSchedulerPort(item.getRemoteSchedulerPort());
				events.add(event);
			}

			Events entity = new Events();
			entity.setEvents(events);
			entity.setDeliveryDate(Date.from(Instant.now()));
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

}
