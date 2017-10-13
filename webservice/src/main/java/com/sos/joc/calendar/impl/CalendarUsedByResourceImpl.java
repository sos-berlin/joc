package com.sos.joc.calendar.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.calendar.resource.ICalendarUsedByResource;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarUsage;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.calendar.CalendarId;
import com.sos.joc.model.calendar.UsedBy;
import com.sos.joc.model.calendar.UsedBy200;

@Path("calendar")
public class CalendarUsedByResourceImpl extends JOCResourceImpl implements ICalendarUsedByResource {

    private static final String API_CALL = "./calendar/used";

    @Override
    public JOCDefaultResponse postUsedBy(String accessToken, CalendarId calendarFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, calendarFilter, accessToken, "", getPermissonsJocCockpit(accessToken).getCalendar()
                    .isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            if (calendarFilter.getId() == null && (calendarFilter.getPath() == null || calendarFilter.getPath().isEmpty())) {
                throw new JocMissingRequiredParameterException("undefined 'calendar id'");
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarUsageDBLayer dbCalendarLayer = new CalendarUsageDBLayer(connection);
            List<CalendarUsage> calendarUsages = null;
            if (calendarFilter.getId() != null) {
                calendarUsages = dbCalendarLayer.getCalendarUsages(calendarFilter.getId()); 
            } else {
                calendarUsages = dbCalendarLayer.getCalendarUsages(normalizePath(calendarFilter.getPath()));
            }
            Map<Long, UsedBy> usedByMap = new HashMap<Long, UsedBy>();
            Long instanceId = null;
            if (calendarUsages != null) {
                for (CalendarUsage calendarUsage : calendarUsages) {
                    instanceId = calendarUsage.getInstanceId();
                    if (instanceId != null) {
                        if (usedByMap.containsKey(instanceId)) {
                            UsedBy usedBy = usedByMap.get(instanceId);
                            addJobs(calendarUsage, usedBy);
                            addOrders(calendarUsage, usedBy);
                            addSchedules(calendarUsage, usedBy);
                        } else {
                            usedByMap.put(instanceId, calendarUsage);
                        }
                    }
                }
            }
            UsedBy200 entity = new UsedBy200();
            entity.setJobschedulers(new ArrayList<UsedBy>(usedByMap.values()));
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
    
    private void addJobs(CalendarUsage calendarUsage, UsedBy usedBy) {
        if (calendarUsage.getJobs() != null) {
            if (usedBy.getJobs() == null) {
                usedBy.setJobs(new ArrayList<String>(calendarUsage.getJobs()));
            } else {
                usedBy.getJobs().addAll(calendarUsage.getJobs());
            }
        }
    }
    
    private void addOrders(CalendarUsage calendarUsage, UsedBy usedBy) {
        if (calendarUsage.getOrders() != null) {
            if (usedBy.getOrders() == null) {
                usedBy.setOrders(new ArrayList<String>(calendarUsage.getOrders()));
            } else {
                usedBy.getOrders().addAll(calendarUsage.getOrders());
            }
        }
    }
    
    private void addSchedules(CalendarUsage calendarUsage, UsedBy usedBy) {
        if (calendarUsage.getSchedules() != null) {
            if (usedBy.getSchedules() == null) {
                usedBy.setSchedules(new ArrayList<String>(calendarUsage.getSchedules()));
            } else {
                usedBy.getSchedules().addAll(calendarUsage.getSchedules());
            }
        }
    }

}