package com.sos.joc.order.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryCalendarUsage;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderRunTimeResource;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {

	private static final String API_CALL = "./order/run_time";

	@Override
	public JOCDefaultResponse postOrderRunTime(String xAccessToken, String accessToken, OrderFilter orderFilter)
			throws Exception {
		return postOrderRunTime(getAccessToken(xAccessToken, accessToken), orderFilter);
	}

	public JOCDefaultResponse postOrderRunTime(String accessToken, OrderFilter orderFilter) throws Exception {
		SOSHibernateSession connection = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderFilter, accessToken,
					orderFilter.getJobschedulerId(),
					getPermissonsJocCockpit(orderFilter.getJobschedulerId(), accessToken).getOrder().getView()
							.isStatus());
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
			List<DBItemInventoryCalendarUsage> dbCalendars = calendarUsageDBLayer.getCalendarUsagesOfAnObject(
					dbItemInventoryInstance.getId(), "ORDER", jobChainPath + "," + orderFilter.getOrderId());
			if (dbCalendars != null && !dbCalendars.isEmpty()) {
				List<Calendar> calendars = new ArrayList<Calendar>();
				ObjectMapper objMapper = new ObjectMapper();
				for (DBItemInventoryCalendarUsage dbCalendar : dbCalendars) {
					if (dbCalendar.getConfiguration() != null && !dbCalendar.getConfiguration().isEmpty()) {
						calendars.add(objMapper.readValue(dbCalendar.getConfiguration(), Calendar.class));
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
}
