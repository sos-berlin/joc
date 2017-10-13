package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemCalendar;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderCalendarsResource;

@Path("order")
public class OrderCalendarsResourceImpl extends JOCResourceImpl implements IOrderCalendarsResource {

    private static final String API_CALL = "./order/calendars";

    @Override
    public JOCDefaultResponse postOrderCalendars(String accessToken, OrderFilter orderFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderFilter, accessToken, orderFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("orderId", orderFilter.getOrderId());
            checkRequiredParameter("jobChain", orderFilter.getJobChain());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarsDBLayer dbCalendarLayer = new CalendarsDBLayer(connection);

            List<DBItemCalendar> dbCalendars = dbCalendarLayer.getCalendarsOfAnObject(dbItemInventoryInstance.getId(), "ORDER", normalizePath(
                    orderFilter.getJobChain()) + "," + orderFilter.getOrderId());

            List<Calendar> calendarList = new ArrayList<Calendar>();
            if (dbCalendars != null) {
                ObjectMapper om = new ObjectMapper();
                for (DBItemCalendar dbCalendar : dbCalendars) {
                    Calendar calendar = om.readValue(dbCalendar.getConfiguration(), Calendar.class);
                    calendar.setId(dbCalendar.getId());
                    calendar.setPath(dbCalendar.getName());
                    calendar.setName(dbCalendar.getBaseName());
                    //calendar.setIncludes(null);
                    //calendar.setExcludes(null);
                    calendarList.add(calendar);
                }
            }
            Calendars entity = new Calendars();
            entity.setCalendars(calendarList);
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
