package com.sos.joc.order.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.Order200VSchema;
import com.sos.joc.model.order.OrderFilterWithCompactSchema;
import com.sos.joc.order.resource.IOrderResource;

@Path("order")
public class OrderResourceImpl extends JOCResourceImpl implements IOrderResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrder(String accessToken, OrderFilterWithCompactSchema orderBody) throws Exception {
        LOGGER.debug("init order");
        try {
            JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String masterUrl = dbItemInventoryInstance.getUrl();
            JOCJsonCommand command = new JOCJsonCommand(masterUrl);
            command.addCompactQuery(orderBody.getCompact());
            Order200VSchema entity = new Order200VSchema();

            if (checkRequiredParameter("orderId", orderBody.getOrderId()) && checkRequiredParameter("jobChain", orderBody.getJobChain())) {
                OrdersVCallable o = new OrdersVCallable(orderBody, command.getURI());
                entity.setDeliveryDate(new Date());
                entity.setOrder(o.getOrder());
            }

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
}
