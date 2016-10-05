package com.sos.joc.orders.impl;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersSummaryCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.SnapshotSchema;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceOverviewSnapshotImpl.class);

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, OrdersFilterSchema ordersFilterSchema) throws Exception {
        LOGGER.debug("init orders/overview/summary");
        try {
            JOCDefaultResponse jocDefaultResponse = init(ordersFilterSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO consider OrdersFilterSchema
            String masterUrl = dbItemInventoryInstance.getUrl();
            JOCJsonCommand command = new JOCJsonCommand(masterUrl);
            command.addOrderStatisticsQuery();
            URI uri = command.getURI();
            
            OrdersSummaryCallable osc = new OrdersSummaryCallable(uri);
            SnapshotSchema entity = osc.getOrdersSnapshot(new SnapshotSchema());
            entity.setDeliveryDate(new Date());

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
}
