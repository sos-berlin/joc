package com.sos.joc.order.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderP200;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.model.order.OrderP;
import com.sos.joc.order.resource.IOrderPResource;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBLayer;

@Path("order")
public class OrderPResourceImpl extends JOCResourceImpl implements IOrderPResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPResourceImpl.class);

    @Override
    public JOCDefaultResponse postOrderP(String accessToken, OrderFilter orderFilterWithCompactSchema) throws Exception {
        LOGGER.debug("init OrderP");
        try {
            JOCDefaultResponse jocDefaultResponse =
                    init(orderFilterWithCompactSchema.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            /** FILTER: compact */
            Boolean compact = orderFilterWithCompactSchema.getCompact();
            OrderP200 entity = new OrderP200();
            InventoryOrdersDBLayer dbLayer =
                    new InventoryOrdersDBLayer(Globals.sosHibernateConnection, orderFilterWithCompactSchema.getJobschedulerId());
            DBItemInventoryOrder dbItemInventoryOrder =
                    dbLayer.getInventoryOrderByOrderId(orderFilterWithCompactSchema.getJobChain(), orderFilterWithCompactSchema.getOrderId());
            entity.setDeliveryDate(new Date());
            OrderP order = new OrderP();
            // Required fields
            order.setSurveyDate(dbItemInventoryOrder.getModified());
            order.setPath(dbItemInventoryOrder.getName());
            order.setOrderId(dbItemInventoryOrder.getOrderId());
            order.setJobChain(dbItemInventoryOrder.getJobChainName());
            Integer estimatedDuration = getEstimatedDurationInSeconds(dbItemInventoryOrder);
            if(estimatedDuration != null) {
                order.setEstimatedDuration(estimatedDuration);
            } else {
                order.setEstimatedDuration(0);
            }
            order.setTitle(dbItemInventoryOrder.getTitle());
            order.set_type(OrderType.PERMANENT);
            if(compact == null || !compact) {
                Date orderFileModified = dbLayer.getOrderConfigurationDate(dbItemInventoryOrder.getId());
                if (orderFileModified != null) {
                    order.setConfigurationDate(orderFileModified);
                }
                order.setInitialState(dbItemInventoryOrder.getInitialState());
                order.setEndState(dbItemInventoryOrder.getEndState());
                order.setPriority(dbItemInventoryOrder.getPriority());
            }
            entity.setOrder(order);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }

    private Integer getEstimatedDurationInSeconds(DBItemInventoryOrder order) throws Exception {
        JobSchedulerOrderHistoryDBLayer dbLayer = new JobSchedulerOrderHistoryDBLayer(Globals.sosHibernateConnection);
        Long estimatedDurationInMillis = dbLayer.getOrderEstimatedDuration(order.getOrderId());
        if (estimatedDurationInMillis != null) {
            return estimatedDurationInMillis.intValue()/1000;
        }
        return null;
    }

}