package com.sos.joc.order.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.db.audit.AuditLogDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrderV200;
import com.sos.joc.order.resource.IOrderResource;
import com.sos.schema.JsonValidator;

@Path("order")
public class OrderResourceImpl extends JOCResourceImpl implements IOrderResource {

    private static final String API_CALL = "./order";

    @Override
    public JOCDefaultResponse postOrder(String accessToken, byte[] orderFilterBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(orderFilterBytes, OrderFilter.class);
            OrderFilter orderBody = Globals.objectMapper.readValue(orderFilterBytes, OrderFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderBody, accessToken, orderBody.getJobschedulerId(), getPermissonsJocCockpit(
                    orderBody.getJobschedulerId(), accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand(this);
            command.setUriBuilderForOrders();
            command.addOrderCompactQuery(orderBody.getCompact());
            OrderV200 entity = new OrderV200();

            checkRequiredParameter("orderId", orderBody.getOrderId());
            checkRequiredParameter("jobChain", orderBody.getJobChain());
            String jobChainPath = normalizePath(orderBody.getJobChain());
            checkFolderPermissions(jobChainPath);

            orderBody.setJobChain(jobChainPath);
            OrderV o = new OrdersVCallable(orderBody, command, accessToken).getOrder();

            // JOC-678
            if (o != null && o.getProcessingState() != null) {
                OrderStateText orderState = o.getProcessingState().get_text();
                if (orderState == OrderStateText.SUSPENDED || orderState == OrderStateText.JOB_STOPPED) {
                    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                    AuditLogDBLayer dbLayer = new AuditLogDBLayer(connection);
                    switch (orderState) {
                    case SUSPENDED:
                        o.getProcessingState().setManually(dbLayer.isManuallySuspended(orderBody.getJobschedulerId(), o.getJobChain(), o.getOrderId()));
                        break;
                    case JOB_STOPPED:
                        o.getProcessingState().setManually(dbLayer.isManuallyStopped(orderBody.getJobschedulerId(), o.getJob()));
                        break;
                    default:
                        break;
                    }
                }
            }
            entity.setOrder(o);
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
}
