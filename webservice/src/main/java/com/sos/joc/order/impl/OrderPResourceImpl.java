package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.jitl.reporting.db.DBLayerReporting;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderP;
import com.sos.joc.model.order.OrderP200;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.order.resource.IOrderPResource;

@Path("order")
public class OrderPResourceImpl extends JOCResourceImpl implements IOrderPResource {

	private static final String API_CALL = "./order/p";
	private SOSHibernateSession connection = null;

	@Override
	public JOCDefaultResponse postOrderP(String xAccessToken, String accessToken, OrderFilter orderFilter)
			throws Exception {
		return postOrderP(getAccessToken(xAccessToken, accessToken), orderFilter);
	}

	public JOCDefaultResponse postOrderP(String accessToken, OrderFilter orderFilter) throws Exception {

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderFilter, accessToken,
					orderFilter.getJobschedulerId(),
					getPermissonsJocCockpit(orderFilter.getJobschedulerId(), accessToken).getOrder().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			Long instanceId = dbItemInventoryInstance.getId();
			Boolean compact = orderFilter.getCompact();
			OrderP200 entity = new OrderP200();
			InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
			DBItemInventoryOrder dbItemInventoryOrder = dbLayer.getInventoryOrderByOrderId(
					normalizePath(orderFilter.getJobChain()), orderFilter.getOrderId(), instanceId);
			if (dbItemInventoryOrder == null) {
				throw new DBMissingDataException(String.format("no entry found in DB: %1$s,%2$s",
						orderFilter.getJobChain(), orderFilter.getOrderId()));
			}
			DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(connection);
			OrderP order = new OrderP();
			order.setSurveyDate(dbItemInventoryOrder.getModified());
			order.setPath(dbItemInventoryOrder.getName());
			order.setOrderId(dbItemInventoryOrder.getOrderId());
			order.setJobChain(dbItemInventoryOrder.getJobChainName());
			order.setDocumentation(dbDocLayer.getDocumentationPath(orderFilter.getJobschedulerId(), JobSchedulerObjectType.ORDER, dbItemInventoryOrder.getName()));
			Integer estimatedDuration = getEstimatedDurationInSeconds(dbItemInventoryOrder);
			if (estimatedDuration != null) {
				order.setEstimatedDuration(estimatedDuration);
			} else {
				order.setEstimatedDuration(0);
			}
			order.setTitle(dbItemInventoryOrder.getTitle());
			order.set_type(OrderType.PERMANENT);
			if (compact == null || !compact) {
				Date orderFileModified = dbLayer.getOrderConfigurationDate(dbItemInventoryOrder.getId());
				if (orderFileModified != null) {
					order.setConfigurationDate(orderFileModified);
				}
				order.setInitialState(dbItemInventoryOrder.getInitialState());
				order.setEndState(dbItemInventoryOrder.getEndState());
				order.setPriority(dbItemInventoryOrder.getPriority());
			}
			entity.setOrder(order);
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

	private Integer getEstimatedDurationInSeconds(DBItemInventoryOrder order) throws Exception {
		DBLayerReporting dbLayer = new DBLayerReporting(connection);

		Long estimatedDurationInMillis = dbLayer.getOrderEstimatedDuration(order, Globals.sosShiroProperties
				.getProperty("limit_for_average_calculation", WebserviceConstants.DEFAULT_LIMIT));
		if (estimatedDurationInMillis != null) {
			return estimatedDurationInMillis.intValue() / 1000;
		}
		return null;
	}

}