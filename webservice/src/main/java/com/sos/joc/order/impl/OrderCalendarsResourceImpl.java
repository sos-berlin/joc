package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.CalendarsOfAnObject;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderCalendarsResource;
import com.sos.schema.JsonValidator;

@Path("order")
public class OrderCalendarsResourceImpl extends JOCResourceImpl implements IOrderCalendarsResource {

    private static final String API_CALL = "./order/calendars";

    @Override
    public JOCDefaultResponse postOrderCalendars(String accessToken, byte[] orderFilterBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(orderFilterBytes, OrderFilter.class);
            OrderFilter orderFilter = Globals.objectMapper.readValue(orderFilterBytes, OrderFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderFilter, accessToken, orderFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    orderFilter.getJobschedulerId(), accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("orderId", orderFilter.getOrderId());
            checkRequiredParameter("jobChain", orderFilter.getJobChain());
            String jobChainPath = normalizePath(orderFilter.getJobChain());
            checkFolderPermissions(jobChainPath);
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            Calendars entity = CalendarsOfAnObject.get(connection, dbItemInventoryInstance.getSchedulerId(), CalendarType.ORDER, jobChainPath + ","
                    + orderFilter.getOrderId(), orderFilter.getCompact());

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
