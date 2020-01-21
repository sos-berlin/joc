package com.sos.joc.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemReportExecution;
import com.sos.jitl.reporting.db.ReportExecutionsDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.order.History;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.model.order.OrderStepHistory;
import com.sos.joc.model.order.OrderStepHistoryItem;
import com.sos.joc.order.resource.IOrderHistoryResource;
import com.sos.schema.JsonValidator;

@Path("order")
public class OrderHistoryResourceImpl extends JOCResourceImpl implements IOrderHistoryResource {

	private static final String API_CALL = "./order/history";

	@Override
	public JOCDefaultResponse postOrderHistory(String accessToken, byte[] filterBytes) {
		SOSHibernateSession connection = null;
		try {
		    JsonValidator.validateFailFast(filterBytes, OrderHistoryFilter.class);
		    OrderHistoryFilter orderHistoryFilter = Globals.objectMapper.readValue(filterBytes, OrderHistoryFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderHistoryFilter, accessToken,
					orderHistoryFilter.getJobschedulerId(),
					getPermissonsJocCockpit(orderHistoryFilter.getJobschedulerId(), accessToken).getOrder().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			
			checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			Globals.beginTransaction(connection);

			List<OrderStepHistoryItem> listOrderStepHistory = new ArrayList<OrderStepHistoryItem>();

			ReportExecutionsDBLayer reportExecutionsDBLayer = new ReportExecutionsDBLayer(connection);
			reportExecutionsDBLayer.getFilter().setSchedulerId(orderHistoryFilter.getJobschedulerId());
			reportExecutionsDBLayer.getFilter().setOrderHistoryId(orderHistoryFilter.getHistoryId());
			List<DBItemReportExecution> listOfOrderStepHistoryItems = reportExecutionsDBLayer
					.getOrderStepHistoryItems();

			for (DBItemReportExecution orderStepHistoryItem : listOfOrderStepHistoryItems) {

				OrderStepHistoryItem orderStepHistory = new OrderStepHistoryItem();
				orderStepHistory.setEndTime(orderStepHistoryItem.getEndTime());
				Err errorSchema = new Err();
				errorSchema.setCode(orderStepHistoryItem.getErrorCode());
				errorSchema.setMessage(orderStepHistoryItem.getErrorText());
				orderStepHistory.setError(errorSchema);
				orderStepHistory.setJob(orderStepHistoryItem.getName());
				orderStepHistory.setNode(orderStepHistoryItem.getState());
				orderStepHistory.setStartTime(orderStepHistoryItem.getStartTime());
				orderStepHistory.setStep(orderStepHistoryItem.getStep().intValue());
				orderStepHistory.setAgent(orderStepHistoryItem.getAgentUrl());
				// orderStepHistory.setClusterMember(dbItemReportExecution.getClusterMemberId());
				orderStepHistory.setExitCode(orderStepHistoryItem.getExitCode());
				orderStepHistory.setTaskId(orderStepHistoryItem.getHistoryIdAsString());
				listOrderStepHistory.add(orderStepHistory);
			}

			OrderStepHistory entity = new OrderStepHistory();
			History history = new History();
			history.setHistoryId(orderHistoryFilter.getHistoryId());
			history.setSteps(listOrderStepHistory);
			entity.setDeliveryDate(new Date());
			entity.setHistory(history);

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
