package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderV200;
import com.sos.joc.order.resource.IOrderResource;

@Path("order")
public class OrderResourceImpl extends JOCResourceImpl implements IOrderResource {

    private static final String API_CALL = "./order";

    @Override
    public JOCDefaultResponse postOrder(String xAccessToken, String accessToken, OrderFilter orderBody) throws Exception {
        return postOrder(getAccessToken(xAccessToken, accessToken), orderBody);
    }

    public JOCDefaultResponse postOrder(String accessToken, OrderFilter orderBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderBody, accessToken, orderBody.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand(this);
            command.setUriBuilderForOrders();
            command.addOrderCompactQuery(orderBody.getCompact());
            OrderV200 entity = new OrderV200();

            if (checkRequiredParameter("orderId", orderBody.getOrderId()) && checkRequiredParameter("jobChain", orderBody.getJobChain())) {
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
                String jobChainPath = normalizePath(orderBody.getJobChain());
                List<String> ordersWithTempRunTime = dbLayer.getOrdersWithTemporaryRuntime(dbItemInventoryInstance.getId(), jobChainPath, orderBody
                        .getOrderId());
                orderBody.setJobChain(jobChainPath);
                OrdersVCallable o = new OrdersVCallable(orderBody, command, accessToken, ordersWithTempRunTime);
                entity.setOrder(o.getOrder());
                entity.setDeliveryDate(Date.from(Instant.now()));
            }

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
