package com.sos.joc.orders.impl;

import java.util.Date;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.order.Orders_;
import com.sos.joc.model.order.SummarySchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSummary;

@Path("orders")
public class OrdersResourceOverviewSummaryImpl extends JOCResourceImpl implements IOrdersResourceOverviewSummary {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceOverviewSummaryImpl.class);

    @Override
    public JOCDefaultResponse postOrdersOverviewSummary(String accessToken, OrdersBody ordersBody) throws Exception {
        LOGGER.debug("init Orders");
        JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());

        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {

            // Reading orders from the database tale inventory.orders filtered
            // by
            ordersBody.getDateFrom(); // is ISO 8601 or something like 6h 1w 65y
            ordersBody.getDateTo(); // same as dateFrom
            ordersBody.getTimeZone();
            ordersBody.getRegex();
            ordersBody.getOrders(); // list of wanted orders.

            // TODO JOC Cockpit Webservice (Inventory_Orders)

            SummarySchema entity = new SummarySchema();
            Orders_ orders = new Orders_();

            orders.setFailed(-1);
            orders.setSuccessful(-1);

            entity.setDeliveryDate(new Date());
            entity.setSurveyDate(new Date());
            entity.setOrders(orders);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

}
