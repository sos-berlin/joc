package com.sos.joc.order.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.configuration.CalendarUsageConfiguration;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderRunTimeResource;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {

    private static final String API_CALL = "./order/run_time";

    // old school with JOC-730
    @Override
    public JOCDefaultResponse postOrderRunTimeWithXML(String accessToken, OrderFilter orderFilter) {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "_xml", orderFilter, accessToken, orderFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(orderFilter.getJobschedulerId(), accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            RunTime200 runTimeAnswer = new RunTime200();
            checkRequiredParameter("orderId", orderFilter.getOrderId());
            checkRequiredParameter("jobChain", orderFilter.getJobChain());

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            String jobChainPath = normalizePath(orderFilter.getJobChain());
            String orderCommand = jocXmlCommand.getShowOrderCommand(jobChainPath, orderFilter.getOrderId(), "run_time");
            runTimeAnswer = RunTime.set(jobChainPath, jocXmlCommand, orderCommand, "//order/run_time", accessToken, true);

            connection = Globals.createSosHibernateStatelessConnection(API_CALL + "_xml");
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(connection);
            List<CalendarUsageConfiguration> dbCalendars = calendarUsageDBLayer.getConfigurationsOfAnObject(dbItemInventoryInstance.getSchedulerId(),
                    "ORDER", jobChainPath + "," + orderFilter.getOrderId());
            if (dbCalendars != null && !dbCalendars.isEmpty()) {
                List<Calendar> calendars = new ArrayList<Calendar>();
                for (CalendarUsageConfiguration dbCalendar : dbCalendars) {
                    if (dbCalendar.getCalendar() != null) {
                        calendars.add(dbCalendar.getCalendar());
                    }
                }
                runTimeAnswer.getRunTime().setCalendars(calendars);
            }
            
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderFilter orderFilter) {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderFilter, accessToken, orderFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    orderFilter.getJobschedulerId(), accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            RunTime200 runTimeAnswer = new RunTime200();
            checkRequiredParameter("orderId", orderFilter.getOrderId());
            checkRequiredParameter("jobChain", orderFilter.getJobChain());

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            String jobChainPath = normalizePath(orderFilter.getJobChain());
            String orderCommand = jocXmlCommand.getShowOrderCommand(jobChainPath, orderFilter.getOrderId(), "run_time");
            runTimeAnswer = RunTime.set(jobChainPath, jocXmlCommand, orderCommand, "//order/run_time", accessToken);

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(connection);
            List<CalendarUsageConfiguration> dbCalendars = calendarUsageDBLayer.getConfigurationsOfAnObject(dbItemInventoryInstance.getSchedulerId(),
                    "ORDER", jobChainPath + "," + orderFilter.getOrderId());
            if (dbCalendars != null && !dbCalendars.isEmpty()) {
                List<Calendar> calendars = new ArrayList<Calendar>();
                for (CalendarUsageConfiguration dbCalendar : dbCalendars) {
                    if (dbCalendar.getCalendar() != null) {
                        calendars.add(dbCalendar.getCalendar());
                    }
                }
                runTimeAnswer.getRunTime().setCalendars(calendars);
            }
            
            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(runTimeAnswer);
            StreamingOutput streamOut = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    try {
                        output.write(bytes);
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                    }
                }
            };
            return JOCDefaultResponse.responseStatus200(streamOut, MediaType.APPLICATION_JSON);
            //return JOCDefaultResponse.responseStatus200(runTimeAnswer);
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
