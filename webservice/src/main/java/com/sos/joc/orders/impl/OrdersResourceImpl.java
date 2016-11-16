package com.sos.joc.orders.impl;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrdersPerJobChain;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersV;
import com.sos.joc.orders.resource.IOrdersResource;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {

    private static final String API_CALL = "./orders";

    @Override
    public JOCDefaultResponse postOrders(String accessToken, OrdersFilter ordersBody) throws Exception {

        try {
            initLogging(API_CALL, ordersBody);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, ordersBody.getJobschedulerId(), getPermissons(accessToken).getOrder().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO date post body parameters are not yet considered
            JOCJsonCommand command = new JOCJsonCommand();
            command.setUriBuilderForOrders(dbItemInventoryInstance.getUrl());
            command.addOrderCompactQuery(ordersBody.getCompact());

            Map<String, OrderV> listOrders = new HashMap<String, OrderV>();
            List<OrderPath> orders = ordersBody.getOrders();
            List<Folder> folders = ordersBody.getFolders();
            List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();

            URI uri = command.getURI();

            Map<String, OrdersPerJobChain> ordersLists = new HashMap<String, OrdersPerJobChain>();
            if (orders != null && !orders.isEmpty()) {
                for (OrderPath order : orders) {
                    if (order.getJobChain() == null || order.getJobChain().isEmpty()) {
                        throw new JocMissingRequiredParameterException("jobChain");
                    } else {
                        order.setJobChain(normalizePath(order.getJobChain()));
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
            }

            if (!ordersLists.isEmpty()) {
                for (OrdersPerJobChain opj : ordersLists.values()) {
                    tasks.add(new OrdersVCallable(opj, ordersBody.getCompact(), uri, accessToken));
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    folder.setFolder(normalizePath(folder.getFolder()));
                    tasks.add(new OrdersVCallable(folder, ordersBody, uri, accessToken));
                }
            } else {
                Folder rootFolder = new Folder();
                rootFolder.setFolder("/");
                rootFolder.setRecursive(true);
                OrdersVCallable callable = new OrdersVCallable(rootFolder, ordersBody, uri, accessToken);
                listOrders.putAll(callable.call());
            }
            
            if(tasks != null && !tasks.isEmpty()) {
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                for (Future<Map<String, OrderV>> result : executorService.invokeAll(tasks)) {
                    try {
                        listOrders.putAll(result.get());
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof JocException) {
                            throw (JocException) e.getCause();
                        } else {
                            throw (Exception) e.getCause();
                        }
                    }
                }
            }

            OrdersV entity = new OrdersV();
            entity.setOrders(new ArrayList<OrderV>(listOrders.values()));
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
