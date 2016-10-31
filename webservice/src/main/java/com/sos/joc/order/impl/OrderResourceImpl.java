package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderV200;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderResource;

@Path("order")
public class OrderResourceImpl extends JOCResourceImpl implements IOrderResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResourceImpl.class);
    private static final String API_CALL = "./order";

    @Override
    public JOCDefaultResponse postOrder(String accessToken, OrderFilter orderBody) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, orderBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand();
            command.setUriBuilderForOrders(dbItemInventoryInstance.getUrl());
            command.addOrderCompactQuery(orderBody.getCompact());
            OrderV200 entity = new OrderV200();

            if (checkRequiredParameter("orderId", orderBody.getOrderId()) && checkRequiredParameter("jobChain", orderBody.getJobChain())) {
                OrdersVCallable o = new OrdersVCallable(orderBody, command.getURI(), accessToken);
                entity.setOrder(o.getOrder());
                entity.setDeliveryDate(Date.from(Instant.now()));
            }

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, orderBody));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, orderBody));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }

    }
}
