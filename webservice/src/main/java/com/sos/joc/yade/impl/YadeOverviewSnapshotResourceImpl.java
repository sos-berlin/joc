package com.sos.joc.yade.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.classes.orders.OrdersPerJobChain;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.db.yade.TransferOrderPath;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.yade.TransfersSummary;
import com.sos.joc.model.yade.YadeSnapshot;
import com.sos.joc.yade.resource.IYadeOverviewSnapshotResource;


@Path("yade")
public class YadeOverviewSnapshotResourceImpl extends JOCResourceImpl implements IYadeOverviewSnapshotResource {

    private static final String API_CALL = "./yade/overview/snapshot";

    @Override
    public JOCDefaultResponse postYadeOverviewSnapshot(String accessToken, JobSchedulerId jobschedulerId) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobschedulerId, accessToken, jobschedulerId.getJobschedulerId(), 
                    getPermissonsJocCockpit(accessToken).getYADE().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
            List<TransferOrderPath> orders = dbLayer.getOrders(dbItemInventoryInstance.getId());
            OrdersFilter ordersBody = new OrdersFilter();
            ordersBody.setOrders(new ArrayList<OrderPath>());
            ordersBody.setCompact(true);
            
            JOCJsonCommand command = new JOCJsonCommand(this);
            command.setUriBuilderForOrders();
            command.addOrderCompactQuery(ordersBody.getCompact());
            
            Map<String, OrderVolatile> listOrders = new HashMap<String, OrderVolatile>();
            List<OrdersVCallable> tasks = new ArrayList<OrdersVCallable>();
            
            Map<String, OrdersPerJobChain> ordersLists = new HashMap<String, OrdersPerJobChain>();
            if (orders != null && !orders.isEmpty()) {
                InventoryJobChainsDBLayer dbJCLayer = new InventoryJobChainsDBLayer(connection);
                List<String> outerJobChains = dbJCLayer.getOuterJobChains(dbItemInventoryInstance.getId());
                for (TransferOrderPath order : orders) {
                    OrderPath orderPath = order.getOrderPath();
                    if (orderPath == null || orderPath.getJobChain() == null || orderPath.getJobChain().isEmpty()) {
                        continue;
                    }
                    ordersBody.getOrders().add(orderPath);
                    OrdersPerJobChain opj;
                    if (ordersLists.containsKey(orderPath.getJobChain())) {
                        opj = ordersLists.get(orderPath.getJobChain());
                        if (opj.containsOrder(orderPath.getOrderId())) {
                            continue;
                        } else {
                            opj.addOrder(orderPath.getOrderId());
                        }
                    } else {
                        opj = new OrdersPerJobChain();
                        opj.setJobChain(orderPath.getJobChain());
                        opj.setIsOuterJobChain(outerJobChains.contains(orderPath.getJobChain()));
                        opj.addOrder(orderPath.getOrderId());
                    }
                    ordersLists.put(orderPath.getJobChain(), opj);
                }
            }
            if (!ordersLists.isEmpty()) {
                for (OrdersPerJobChain opj : ordersLists.values()) {
                    tasks.add(new OrdersVCallable(opj, ordersBody, new JOCJsonCommand(command), accessToken, new ArrayList<String>()));
                }
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
            int running = 0; 
            int setback = 0; 
            int suspended = 0; 
            int waiting = 0; 
            for (OrderVolatile o : listOrders.values()) {
                if (orders.remove(new TransferOrderPath(o.getJobChain(), o.getOrderId(), o.getState()))) {
                    switch (o.getProcessingState().get_text()) {
                    case PENDING:
                    case BLACKLIST:
                        break;
                    case RUNNING:
                        running++;
                        break;
                    case SUSPENDED:
                        suspended++;
                        break;
                    case SETBACK:
                        setback++;
                        break;
                    default:
                        waiting++;
                        break;
                    }
                }
            }
            
            TransfersSummary summary = new TransfersSummary();
            summary.setRunning(running);
            summary.setSetback(setback);
            summary.setSuspended(suspended);
            summary.setWaitingForResource(waiting);
            
            YadeSnapshot snapshot = new YadeSnapshot();
            snapshot.setTransfers(summary);
            snapshot.setSurveyDate(Date.from(Instant.now()));
            snapshot.setDeliveryDate(snapshot.getSurveyDate());
            
            return JOCDefaultResponse.responseStatus200(snapshot);
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
