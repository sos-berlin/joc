package com.sos.joc.orders.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersSnapshotCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.order.Orders;
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

            JOCJsonCommand command = new JOCJsonCommand(dbItemInventoryInstance.getUrl());
            command.addOrderStatisticsQuery();
            URI uri = command.getURI();
            
            Map<String, Orders> listOrders = new HashMap<String, Orders>();
            List<Order_> orders = ordersFilterSchema.getOrders();
            List<FoldersSchema> folders = ordersFilterSchema.getFolders();
            List<OrdersSnapshotCallable> tasks = new ArrayList<OrdersSnapshotCallable>();
            
            if (orders.size() > 0) {
                for (Order_ order : orders) {
                    if (order.getJobChain() == null || order.getJobChain().isEmpty()) {
                        throw new JocMissingRequiredParameterException("jobChain");
                    }
                    tasks.add(new OrdersSnapshotCallable(("/"+order.getJobChain().trim()).replaceAll("//+", "/").replaceFirst("/$", ""), uri));
                }
            } else if (folders.size() > 0) {
                for (FoldersSchema folder : folders) {
                    tasks.add(new OrdersSnapshotCallable(getPath(folder), uri));
                }
            } else {
                tasks.add(new OrdersSnapshotCallable("/", uri));
            }
            
            Orders summary = new Orders();
            summary.setBlacklist(0);
            summary.setPending(0);
            summary.setRunning(0);
            summary.setSetback(0);
            summary.setSuspended(0);
            summary.setWaitingForResource(0);
            
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<Map<String, Orders>> result : executorService.invokeAll(tasks)) {
                listOrders.putAll(result.get());
            }
            for (Orders o : listOrders.values()) {
                summary.setBlacklist(summary.getBlacklist() + o.getBlacklist());
                summary.setPending(summary.getPending() + o.getPending());
                summary.setRunning(summary.getRunning() + o.getRunning());
                summary.setSetback(summary.getSetback() + o.getSetback());
                summary.setSuspended(summary.getSuspended() + o.getSuspended());
                summary.setWaitingForResource(summary.getWaitingForResource() + o.getWaitingForResource());
            }
            
            SnapshotSchema entity = new SnapshotSchema();
            entity.setDeliveryDate(new Date());
            entity.setOrders(summary);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }

    }
    
    private String getPath(FoldersSchema folder) {
        String path = folder.getFolder();
        if (path == null) {
            return "/";
        } else {
            path = ("/"+path.trim()+"/").replaceAll("//+", "/");
            if (!folder.getRecursive()) {
                path += "*";
            } 
        }
        return path;
    }
}
