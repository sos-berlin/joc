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
import com.sos.joc.classes.orders.OrdersPerJobChain;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
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

        try {
            JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO date post body parameters are not yet considered
            String masterUrl = dbItemInventoryInstance.getUrl();
            JOCJsonCommand command = new JOCJsonCommand(masterUrl);
            command.addCompactQuery(ordersBody.getCompact());

            Map<String, OrderQueue> listOrders = new HashMap<String, OrderQueue>();
            List<Order_> orders = ordersBody.getOrders();
            List<FoldersSchema> folders = ordersBody.getFolders();
            List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();

            URI uri = command.getURI();

            Map<String, OrdersPerJobChain> ordersLists = new HashMap<String, OrdersPerJobChain>();
            for (Order_ order : orders) {
                if (order.getJobChain() == null || order.getJobChain().isEmpty()) {
                    throw new JocMissingRequiredParameterException("jobChain");
                }
                OrdersPerJobChain opj;
                if (ordersLists.containsKey(order.getJobChain())) {
                    opj = ordersLists.get(order.getJobChain());
                    if (opj.containsOrder(order.getOrderId())) {
                        continue;
                    } else {
                        opj.addOrder(order.getOrderId());
                    }
                } else {
                    opj = new OrdersPerJobChain();
                    opj.setJobChain(order.getJobChain());
                    opj.addOrder(order.getOrderId());
                }
                ordersLists.put(order.getJobChain(), opj);
            }

            if (!ordersLists.isEmpty()) {
                for (OrdersPerJobChain opj : ordersLists.values()) {
                    tasks.add(new OrdersVCallable(opj, ordersBody.getCompact(), uri));
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
                listOrders.putAll(result.get());
            }

            OrdersVSchema entity = new OrdersVSchema();
            entity.setDeliveryDate(new Date());
            entity.setOrders(new ArrayList<OrderQueue>(listOrders.values()));

            return JOCDefaultResponse.responseStatus200(entity);
            // } catch (JocException e) {
            // return JOCDefaultResponse.responseStatusJSError(e);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
}
