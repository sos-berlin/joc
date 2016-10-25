package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.Date;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersHistoricSummary;
import com.sos.joc.model.order.OrdersOverView;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSummary;

@Path("orders")
public class OrdersResourceOverviewSummaryImpl extends JOCResourceImpl implements IOrdersResourceOverviewSummary {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceOverviewSummaryImpl.class);
    private static final String API_CALL = "./orders/overview/summary";
    
    @Override
    public JOCDefaultResponse postOrdersOverviewSummary(String accessToken, OrdersFilter ordersFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(ordersFilter.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // Reading orders from the database tale inventory.orders filtered
            // by
//            ordersFilter.getDateFrom(); // is ISO 8601 or something like
//                                              // 6h 1w 65y
//            ordersFilter.getDateTo(); // same as dateFrom
//            ordersFilter.getTimeZone();
//            ordersFilter.getRegex();
//            ordersFilter.getOrders(); // list of wanted orders.

            // TODO JOC Cockpit Webservice (Inventory_Orders)

            OrdersHistoricSummary orders = new OrdersHistoricSummary();
            orders.setFailed(-1);
            orders.setSuccessful(-1);

            OrdersOverView entity = new OrdersOverView();
            entity.setSurveyDate(Date.from(Instant.now()));
            entity.setOrders(orders);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, ordersFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, ordersFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }

}
