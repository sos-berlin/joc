package com.sos.joc.orders.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderPermanent;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;
import com.sos.joc.orders.resource.IOrdersResourceP;

@Path("orders")
public class OrdersResourcePImpl extends JOCResourceImpl implements IOrdersResourceP {

    private static final String API_CALL = "./orders/p";
    private String regex = null;
    private List<OrderPath> orderPaths = null;
    private List<Folder> foldersFilter = null;
    private Boolean compact = null;

    @Override
    public JOCDefaultResponse postOrdersP(String accessToken, OrdersFilter ordersFilter) throws Exception {
        try {
            initLogging(API_CALL, ordersFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, ordersFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getOrder()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            OrdersP entity = new OrdersP();
            Long instanceId = dbItemInventoryInstance.getId();
            InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection);
            regex = ordersFilter.getRegex();
            orderPaths = ordersFilter.getOrders();
            foldersFilter = ordersFilter.getFolders();
            compact = ordersFilter.getCompact();
            List<DBItemInventoryOrder> ordersFromDB = new ArrayList<DBItemInventoryOrder>();
            if (orderPaths != null && !orderPaths.isEmpty()) {
                for (OrderPath order : orderPaths) {
                    List<DBItemInventoryOrder> filteredOrders = dbLayer.getInventoryOrdersFilteredByOrders(normalizePath(order.getJobChain()), order
                            .getOrderId(), instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (foldersFilter != null && !foldersFilter.isEmpty()) {
                for (Folder folder : foldersFilter) {
                    List<DBItemInventoryOrder> filteredOrders = null;
                    filteredOrders = dbLayer.getInventoryOrdersFilteredByFolders(normalizeFolder(folder.getFolder()), folder.getRecursive(),
                            instanceId);
                    if (filteredOrders != null && !filteredOrders.isEmpty()) {
                        ordersFromDB.addAll(filteredOrders);
                    }
                }
            } else if (regex != null && !regex.isEmpty()) {
                List<DBItemInventoryOrder> unfilteredOrders = dbLayer.getInventoryOrders(instanceId);
                for (DBItemInventoryOrder unfilteredOrder : unfilteredOrders) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(unfilteredOrder.getName());
                    if (regExMatcher.find()) {
                        ordersFromDB.add(unfilteredOrder);
                    }
                }
            } else {
                ordersFromDB = dbLayer.getInventoryOrders(instanceId);
            }
            entity.setOrders(OrderPermanent.fillOutputOrders(ordersFromDB, dbLayer, compact));
            entity.setDeliveryDate(Date.from(Instant.now()));
            dbLayer.closeSession();
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}