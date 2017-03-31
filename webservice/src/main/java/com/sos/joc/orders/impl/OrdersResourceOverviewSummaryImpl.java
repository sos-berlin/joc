package com.sos.joc.orders.impl;

import java.util.Date;
import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.ReportTriggerDBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersHistoricSummary;
import com.sos.joc.model.order.OrdersOverView;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSummary;

@Path("orders")
public class OrdersResourceOverviewSummaryImpl extends JOCResourceImpl implements IOrdersResourceOverviewSummary {

    private static final String API_CALL = "./orders/overview/summary";

    @Override
    public JOCDefaultResponse postOrdersOverviewSummary(String accessToken, OrdersFilter ordersFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersFilter, accessToken, ordersFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getOrder().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            OrdersHistoricSummary ordersHistoricSummary = new OrdersHistoricSummary();
            Globals.beginTransaction(connection);

            ReportTriggerDBLayer reportTriggerDBLayer = new ReportTriggerDBLayer(connection);
            reportTriggerDBLayer.getFilter().setSchedulerId(ordersFilter.getJobschedulerId());

            if (ordersFilter.getDateFrom() != null) {
                reportTriggerDBLayer.getFilter().setExecutedFrom(JobSchedulerDate.getDateFrom(ordersFilter.getDateFrom(), ordersFilter.getTimeZone()));
            }
            if (ordersFilter.getDateTo() != null) {
                reportTriggerDBLayer.getFilter().setExecutedTo(JobSchedulerDate.getDateTo(ordersFilter.getDateTo(), ordersFilter.getTimeZone()));
            }

            if (ordersFilter.getOrders().size() > 0) {
                for (OrderPath orderPath : ordersFilter.getOrders()) {
                    reportTriggerDBLayer.getFilter().addOrderPath(normalizePath(orderPath.getJobChain()), orderPath.getOrderId());
                }
            }

            OrdersOverView entity = new OrdersOverView();
            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());
            entity.setOrders(ordersHistoricSummary);
            reportTriggerDBLayer.getFilter().setFailed(true);
            ordersHistoricSummary.setFailed(reportTriggerDBLayer.getCountSchedulerOrderHistoryListFromTo().intValue());

            reportTriggerDBLayer.getFilter().setSuccess(true);
            ordersHistoricSummary.setSuccessful(reportTriggerDBLayer.getCountSchedulerOrderHistoryListFromTo().intValue());

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
