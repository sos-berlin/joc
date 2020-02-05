package com.sos.joc.event.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;
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
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.event.custom.Event;
import com.sos.joc.model.event.custom.Events;
import com.sos.joc.model.event.custom.EventsFilter;
import com.sos.schema.JsonValidator;
import com.sos.xml.XMLBuilder;

@Path("events")
public class CustomEventsResourceImpl extends JOCResourceImpl implements ICustomEventsResource {

    private static final String API_CALL = "./events/custom/";

    private Date utc2LocalTime(Date expiresUtc) {
        String toTimeZoneString = DateTimeZone.getDefault().getID();
        String fromTimeZoneString = "UTC";
        DateTimeZone fromZone = DateTimeZone.forID(fromTimeZoneString);
        Date expiresLocal = UtcTimeHelper.convertTimeZonesToDate(fromTimeZoneString, toTimeZoneString, new DateTime(expiresUtc).withZone(fromZone));
        return expiresLocal;
    }

    @Override
    public JOCDefaultResponse postCustomEvents(String accessToken, byte[] eventFilterBytes) {

        SOSHibernateSession session = null;
        try {
            JsonValidator.validateFailFast(eventFilterBytes, EventsFilter.class);
            EventsFilter eventFilter = Globals.objectMapper.readValue(eventFilterBytes, EventsFilter.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, eventFilter, accessToken, eventFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    eventFilter.getJobschedulerId(), accessToken).getEvent().getView().isStatus());
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
                schedulerEventFilter.setIntervalFromDate(utc2LocalTime(JobSchedulerDate.getDateFrom(eventFilter.getDateFrom(), eventFilter
                        .getTimeZone())));
            }
            if (eventFilter.getDateTo() != null) {
                schedulerEventFilter.setIntervalToDate(utc2LocalTime(JobSchedulerDate.getDateTo(eventFilter.getDateTo(), eventFilter.getTimeZone())));
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
                event.setExpires(item.getExpires());
                event.setJob(item.getJobName());
                event.setJobChain(item.getJobChain());
                event.setJobschedulerId(item.getSchedulerId());
                event.setOrderId(item.getOrderId());
                event.setRemoteJobSchedulerHost(item.getRemoteSchedulerHost());
                event.setRemoteJobSchedulerPort(item.getRemoteSchedulerPort());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                if (item.getParameters() != null && !item.getParameters().isEmpty()) {
                    try {
                        Element elem = XMLBuilder.parse(item.getParameters()).getRootElement();
                        @SuppressWarnings("unchecked")
                        List<Element> paramNodes = elem.elements("param");
                        if (paramNodes != null) {
                            for (Element node : paramNodes) {
                                NameValuePair param = new NameValuePair();
                                param.setName(node.attributeValue("name"));
                                param.setValue(node.attributeValue("value"));
                                params.add(param);
                            }
                        }
                    } catch (Exception e) {
                        throw new DBInvalidDataException(e);
                    }
                }
                if (!params.isEmpty()) {
                    event.setParams(params);
                } else {
                    event.setParams(null);
                }
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
