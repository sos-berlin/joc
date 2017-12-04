package com.sos.joc.yade.impl;

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

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.filter.FilterFolder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.classes.orders.OrdersPerJobChain;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.inventory.jobs.InventoryJobsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersV;
import com.sos.joc.yade.resource.IYadeOrdersResource;

@Path("yade")
public class YadeOrdersResourceImpl extends JOCResourceImpl implements IYadeOrdersResource {

    private static final String API_CALL = "./yade/orders";

    @Override
    public JOCDefaultResponse postOrders(String accessToken, OrdersFilter ordersBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, ordersBody, accessToken, ordersBody.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCJsonCommand command = new JOCJsonCommand(this);
            command.setUriBuilderForOrders();
            command.addOrderCompactQuery(ordersBody.getCompact());

            Map<String, OrderVolatile> listOrders = new HashMap<String, OrderVolatile>();
            List<OrderPath> orders = ordersBody.getOrders();
            List<Folder> folders = addPermittedFolder(ordersBody.getFolders());
            
            List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();

            if (ordersBody.getFolders().size() == 0 && jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
                for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size(); i++) {
                    FilterFolder folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
                    com.sos.joc.model.common.Folder f = new com.sos.joc.model.common.Folder();
                    f.setFolder(folder.getFolder());
                    f.setRecursive(folder.isRecursive());
                    folders.add(f);
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            InventoryJobsDBLayer jobDbLayer = new InventoryJobsDBLayer(connection);
            List<String> yadeJobs = jobDbLayer.getYadeJobs(dbItemInventoryInstance.getId());

            Map<String, OrdersPerJobChain> ordersLists = new HashMap<String, OrdersPerJobChain>();
            if (orders != null && !orders.isEmpty()) {
                InventoryJobChainsDBLayer dbJCLayer = new InventoryJobChainsDBLayer(connection);
                List<String> outerJobChains = dbJCLayer.getOuterJobChains(dbItemInventoryInstance.getId());
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
                        opj.setIsOuterJobChain(outerJobChains.contains(order.getJobChain()));
                        opj.addOrder(order.getOrderId());
                    }
                    ordersLists.put(order.getJobChain(), opj);
                }
            }

            if (!ordersLists.isEmpty()) {
                for (OrdersPerJobChain opj : ordersLists.values()) {
                    tasks.add(new OrdersVCallable(opj, ordersBody, new JOCJsonCommand(command), accessToken, yadeJobs));
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    folder.setFolder(normalizeFolder(folder.getFolder()));
                    tasks.add(new OrdersVCallable(folder, ordersBody, new JOCJsonCommand(command), accessToken, yadeJobs));
                }
            } else {
                Folder rootFolder = new Folder();
                rootFolder.setFolder("/");
                rootFolder.setRecursive(true);
                OrdersVCallable callable = new OrdersVCallable(rootFolder, ordersBody, command, accessToken, yadeJobs);
                listOrders.putAll(callable.call());
            }

            if (tasks != null && !tasks.isEmpty()) {
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                try {
                    for (Future<Map<String, OrderVolatile>> result : executorService.invokeAll(tasks)) {
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
                } finally {
                    executorService.shutdown();
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
        } finally {
            Globals.disconnect(connection);
        }
    }
}
