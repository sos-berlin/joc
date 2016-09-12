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
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.orders.resource.IOrdersResource;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postOrders(String accessToken, OrdersFilterSchema ordersBody) throws Exception {
        LOGGER.debug("init orders");
        JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            // TODO date post body parameters are not yet considered
            // TODO URL "http://localhost:40410" has to read from database
            String masterUrl = "http://localhost:40410";
            JOCJsonCommand command = new JOCJsonCommand(masterUrl);
            command.addCompactQuery(ordersBody.getCompact());

            Map<String, OrderQueue> listOrderQueue = new HashMap<String, OrderQueue>();
            List<Order_> orders = ordersBody.getOrders();
            List<FoldersSchema> folders = ordersBody.getFolders();
            List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();

            if (orders == null || orders.isEmpty()) {
                command.addOrderProcessingStateAndTypeQuery(ordersBody);
            }
            URI uri = command.getURI();

            if (orders != null && !orders.isEmpty()) {
                for (Order_ order : orders) {
                    tasks.add(new OrdersVCallable(order, ordersBody.getCompact(), uri));
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (FoldersSchema folder : folders) {
                    tasks.add(new OrdersVCallable(folder, ordersBody, uri));
                }
            } else {
                FoldersSchema rootFolder = new FoldersSchema();
                rootFolder.setFolder("/");
                rootFolder.setRecursive(true);
                tasks.add(new OrdersVCallable(rootFolder, ordersBody, uri));
            }

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (Future<Map<String, OrderQueue>> result : executorService.invokeAll(tasks)) {
                listOrderQueue.putAll(result.get());
            }

            OrdersVSchema entity = new OrdersVSchema();
            entity.setDeliveryDate(new Date());
            entity.setOrders(new ArrayList<OrderQueue>(listOrderQueue.values()));

            return JOCDefaultResponse.responseStatus200(entity);
            // } catch (JocException e) {
            // return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
}
